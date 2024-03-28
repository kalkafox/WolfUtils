package dev.kalkafox.wolfutils.event;

import dev.kalkafox.wolfutils.WolfUtils;
import dev.kalkafox.wolfutils.mixin.PlayerAccessor;
import dev.kalkafox.wolfutils.sound.EntityStepContext;
import dev.kalkafox.wolfutils.sound.WolfStepContext;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.SoundType;
import org.slf4j.Logger;

import java.util.List;
import java.util.function.BooleanSupplier;

public abstract class EventHandler {

    private static final Logger log = WolfUtils.getLogger();

    public static void onSleep(BlockPos pos, LivingEntity entity) {
        System.out.println("yo");
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

        if (entity.getUUID() != player.getUUID()) {
            return;
        }

        //WolfUtils.wolvesInteractingWithPlayers.put(closestWolf, new WolfInteractionData((Player) entity, wolfPos, wolfLookPos));
        new WolfInteractionEvent(player, closestWolf);
    }

    public static void onStopSleep(LivingEntity entity) {

        if (!(entity instanceof Player) || entity.level().isClientSide) {
            return;
        }

        //Map.Entry<Wolf, WolfInteractionData> entry = WolfUtils.getInteractionData((Player) entity);

        WolfInteractionEvent wolfInteractionEvent = WolfUtils.getInteractionEvent((Player) entity);

        if (wolfInteractionEvent == null) {
            return;
        }

        if (wolfInteractionEvent.finished) {
            ((Player)entity).displayClientMessage(Component.translatable("text.wolfutils.after_sleep").withStyle(ChatFormatting.ITALIC), true);
        }

        wolfInteractionEvent.stop();
    }

    public static void onPreServerLevelTick(ServerLevel serverLevel, BooleanSupplier hasTimeLeft) {
    }

    public static void onPostServerLevelTick(ServerLevel serverLevel, BooleanSupplier hasTimeLeft) {
    }

    public static void onPrePlayerTick(Player player) {
        if (!player.isSleeping()) return;

        if (!player.level().isClientSide) {
            WolfInteractionEvent data = WolfUtils.getInteractionEvent(player);

            if (data == null) {
                return;
            }

            if (!data.isInteracting() && data.getPostInteractTicks() < 120) {
                ((PlayerAccessor)player).setSleepCounter(0);
            }
        }
    }

    public static void onPrePlayerTick(Player player, int sleepCounter) {

    }

    public static void playWolfStepSound(Wolf wolfy) {

        WolfStepContext.step(wolfy);

    }

    public static void playStepSound(Entity entity, SoundType soundType) {
        EntityStepContext.step(entity, soundType);
    }


    public static void onPostPlayerTick(Player player) {
        if (player.level().isClientSide) return;


        if (!player.isSleeping()) return;

        WolfInteractionEvent data = WolfUtils.getInteractionEvent(player);

        if (data == null) {
            return;
        }

        data.tick();
    }
}
