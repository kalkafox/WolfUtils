package dev.kalkafox.wolfutils.event;

import dev.kalkafox.wolfutils.WolfInteractionData;
import dev.kalkafox.wolfutils.WolfUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BooleanSupplier;

public class EventHandler {

    private static final Logger log = WolfUtils.getLogger();

    private static final HashMap<Wolf, WolfInteractionData> wolvesInteractingWithPlayers = new HashMap<>();

    public static boolean areWolvesInteracting() {
        return !wolvesInteractingWithPlayers.isEmpty();
    }

    public static void onSleep(BlockPos pos, LivingEntity entity) {
        if (!(entity instanceof Player) || entity.level().isClientSide) {
            return;
        }
        log.info("Starting sleep at" + pos);

        List<Wolf> wolves = entity.level().getNearbyEntities(Wolf.class, TargetingConditions.DEFAULT, entity, entity.getBoundingBox().inflate(5));

        if (wolves.isEmpty()) {
            return;
        }

        Wolf closestWolf = null;

        double closestDistanceSq = Double.MAX_VALUE;

        for (Wolf wolf : wolves) {
            double distanceSq = entity.distanceToSqr(wolf);
            if (distanceSq < closestDistanceSq) {
                closestDistanceSq = distanceSq;
                closestWolf = wolf;
            }
        }
        
        if (closestWolf == null || closestWolf.getOwner() == null) return;

        // Save the position and direction of the wolf to be used later
        Vec3 wolfPos = closestWolf.position();
        Vec3 wolfLookPos = closestWolf.getLookAngle();

        wolvesInteractingWithPlayers.put(closestWolf, new WolfInteractionData((Player) entity, wolfPos, wolfLookPos));

        closestWolf.setIsInterested(true);

        log.info("Mapping has length! " + wolvesInteractingWithPlayers.size());

    }

    private static double getAngleDegrees(LivingEntity entity, Vec3 wolfPos) {
        Vec3 wolfToPlayerVec = new Vec3(entity.getX() - wolfPos.x(), entity.getY() - wolfPos.y(), entity.getZ() - wolfPos.z());
        Vec3 playerLookVec = entity.getLookAngle();

        // Normalize vectors
        wolfToPlayerVec.normalize();
        playerLookVec.normalize();

        // Calculate dot product
        double dotProduct = wolfToPlayerVec.dot(playerLookVec);

        // Calculate angle in radians
        double angle = Math.acos(dotProduct);

        // Convert angle to degrees
        return Math.toDegrees(angle);
    }

    public static void onStopSleep(LivingEntity entity) {
        if (!(entity instanceof Player) || entity.level().isClientSide) {
            return;
        }

        for (Map.Entry<Wolf, WolfInteractionData> entry : wolvesInteractingWithPlayers.entrySet()) {
            WolfInteractionData data = entry.getValue();

            // Check if the value (Player) matches the livingEntity
            if (data.getPlayer().is(entity)) {
                Wolf closestWolf = entry.getKey();
                // Set closestWolf's interested status to false
                closestWolf.setIsInterested(false);

                closestWolf.getLookControl().setLookAt(data.getWolfLookPos());
                closestWolf.moveTo(data.getWolfPos());
                closestWolf.setInSittingPose(true);

                closestWolf.getNavigation().moveTo(data.getWolfPos().x, data.getWolfPos().y, data.getWolfPos().z, 1);


                // Remove the mapping from the HashMap
                WolfUtils.sleepingWolves.remove(closestWolf);
                wolvesInteractingWithPlayers.remove(closestWolf);
                break; // Exit loop once the mapping is removed
            }
        }
    }

    public static void onPreServerLevelTick(ServerLevel serverLevel, BooleanSupplier hasTimeLeft) {
    }

    public static void onPostServerLevelTick(ServerLevel serverLevel, BooleanSupplier hasTimeLeft) {
//        for (Map.Entry<Wolf, Player> entry : wolvesInteractingWithPlayers.entrySet()) {
//            if (entry.getValue())
//        }
    }

    public static void onPrePlayerTick(Player player) {
    }


    private static void interactWolf(WolfInteractionData data, Wolf wolfy, Player player) {

        if (data.getPlayer().is(player)) {
            if (wolfy.distanceToSqr(player) >= 2) {
                wolfy.setInSittingPose(false);
                wolfy.getNavigation().moveTo(player, 1);
            } else {
                wolfy.setInSittingPose(true);

                if (!WolfUtils.sleepingWolves.contains(wolfy)) {
                    WolfUtils.sleepingWolves.add(wolfy);
                }

                if (data.interactTicks % wolfy.getRandom().nextInt(50, 100) == 0) {
                    float pitch = 0.2f + (wolfy.getRandom().nextFloat() * 0.2f);
                    wolfy.playSound(SoundEvents.WOLF_PANT, 0.5f, pitch);
                }

                data.interactTicks++;
            }
        }
    }


    public static void onPostPlayerTick(Player player) {
        for (Map.Entry<Wolf, WolfInteractionData> entry : wolvesInteractingWithPlayers.entrySet()) {
            Wolf wolfy = entry.getKey();
            wolfy.getLookControl().setLookAt(player);
            if (player.getSleepTimer() >= 50) {
                interactWolf(entry.getValue(), wolfy, player);
            }
        }
    }
}
