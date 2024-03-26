package dev.kalkafox.wolfutils.event;

import dev.kalkafox.wolfutils.WolfUtils;
import dev.kalkafox.wolfutils.mixin.PlayerAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.function.BooleanSupplier;

public abstract class EventHandler {

    private static final Logger log = WolfUtils.getLogger();

    public static void onSleep(BlockPos pos, LivingEntity entity) {
        if (!(entity instanceof Player) || entity.level().isClientSide) {
            return;
        }
        log.info("Starting sleep at" + pos);

        List<Wolf> wolves = entity.level().getNearbyEntities(Wolf.class, TargetingConditions.DEFAULT, entity, entity.getBoundingBox().inflate(10));

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
        
        if (closestWolf == null || closestWolf.getOwner() == null || !closestWolf.isInSittingPose()) return;

        Player player = (Player) closestWolf.getOwner();

        if ((entity).getUUID() != player.getUUID()) {
            return;
        }

        // Save the position and direction of the wolf to be used later
        Vec3 wolfPos = closestWolf.position();
        Vec3 wolfLookPos = closestWolf.getLookAngle();

        //WolfUtils.wolvesInteractingWithPlayers.put(closestWolf, new WolfInteractionData((Player) entity, wolfPos, wolfLookPos));
        WolfInteractionEvent data = new WolfInteractionEvent(player, closestWolf, wolfPos, wolfLookPos);

        WolfUtils.wolvesInteractingWithPlayers.put(player, data);

        closestWolf.setIsInterested(true);

        log.info("Mapping has length! " + WolfUtils.wolvesInteractingWithPlayers.size());

    }

    public static void onStopSleep(LivingEntity entity) {

        if (!(entity instanceof Player) || entity.level().isClientSide) {
            return;
        }

        //Map.Entry<Wolf, WolfInteractionData> entry = WolfUtils.getInteractionData((Player) entity);

        WolfInteractionEvent data = WolfUtils.getInteractionData((Player) entity);

        if (data == null) {
            return;
        }

        Wolf closestWolf = data.getWolf();

        // Set closestWolf's interested status to false
        closestWolf.setIsInterested(false);

        closestWolf.getLookControl().setLookAt(data.getWolfLookPos());
        closestWolf.moveTo(data.getWolfPos());

        closestWolf.setInSittingPose(true);

        closestWolf.getNavigation().moveTo(data.getWolfPos().x, data.getWolfPos().y, data.getWolfPos().z, 1);


        // Remove the mapping from the HashMap
        WolfUtils.sleepingWolves.remove(closestWolf);
        WolfUtils.wolvesInteractingWithPlayers.remove((Player) entity);
    }

    public static void onPreServerLevelTick(ServerLevel serverLevel, BooleanSupplier hasTimeLeft) {
    }

    public static void onPostServerLevelTick(ServerLevel serverLevel, BooleanSupplier hasTimeLeft) {

        if (WolfUtils.wolvesInteractingWithPlayers.isEmpty()) return;

        for (Map.Entry<Player, WolfInteractionEvent> entry : WolfUtils.wolvesInteractingWithPlayers.entrySet()) {
            WolfInteractionEvent interactEvent = entry.getValue();

            interactEvent.tick();
        }
    }

    public static void onPrePlayerTick(Player player) {

    }

    public static void onPrePlayerTick(Player player, int sleepCounter) {

    }

    public static void playWolfStepSound(Wolf wolfy) {
        float start = 0.7f;
        float end = 1.3f;

        float result = start + wolfy.getRandom().nextFloat() * (end - start);

        wolfy.playSound(SoundEvents.WOLF_STEP, 0.15f, result);
    }


    public static void onPostPlayerTick(Player player) {
        WolfInteractionEvent data = WolfUtils.getInteractionData(player);

        if (data == null) {
            return;
        }

        if (!data.isInteracting) {
            ((PlayerAccessor)player).setSleepCounter(0);
        }
    }
}
