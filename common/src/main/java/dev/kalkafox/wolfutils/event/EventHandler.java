package dev.kalkafox.wolfutils.event;

import dev.architectury.networking.NetworkManager;
import dev.kalkafox.wolfutils.WolfUtils;
import dev.kalkafox.wolfutils.mixin.PlayerAccessor;
import dev.kalkafox.wolfutils.packet.Packets;
import dev.kalkafox.wolfutils.sound.EntityStepContext;
import dev.kalkafox.wolfutils.sound.WolfStepContext;
import io.netty.buffer.Unpooled;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
        if (!(entity instanceof Player) || entity.level().isClientSide) {
            return;
        }

        List<Wolf> wolves = entity.level().getNearbyEntities(Wolf.class, TargetingConditions.DEFAULT, entity, entity.getBoundingBox().inflate(10));

        if (wolves.isEmpty()) {
            log.error("Could not find any wolves nearby.");
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

        if (closestWolf == null) {
            log.error("closestWolf is null");
            return;
        }

        if (closestWolf.getOwner() == null) {
            log.error("owner is null");
            return;
        }

        if (!closestWolf.isInSittingPose()) {
            log.error("wolf not in sitting pose");
        }

        Player player = (Player) closestWolf.getOwner();

        if (entity.getUUID() != player.getUUID()) {
            log.error("Sleeping player and wolf owner UUID does not exist!");
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
            log.error("WolfInteractionEvent for {} not found.", entity.getDisplayName().getString());
            return;
        }

        if (wolfInteractionEvent.finished) {
            ((Player)entity).displayClientMessage(Component.translatable("text.wolfutils.after_sleep").withStyle(ChatFormatting.ITALIC), true);
        }

        wolfInteractionEvent.updateClientWolfState(false);

        wolfInteractionEvent.shouldModifySleepFade(false);

        wolfInteractionEvent.stop();
    }

    public static void onPreServerLevelTick(ServerLevel serverLevel, BooleanSupplier hasTimeLeft) {
    }

    public static void onPostServerLevelTick(ServerLevel serverLevel, BooleanSupplier hasTimeLeft) {

        WolfUtils.getWolfInteractionEvents().forEach(WolfInteractionEvent::tick);

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
//        if (player.level().isClientSide) return;
//
//
//        if (!player.isSleeping()) return;
//
//        WolfInteractionEvent data = WolfUtils.getInteractionEvent(player);
//
//        if (data == null) {
//            return;
//        }
//
//        data.tick();
    }


    public static void onUpdateClientWolfState(ServerPlayer player, boolean state) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeBoolean(state);
        NetworkManager.sendToPlayer(player, Packets.UPDATE_WOLF_STATE, buf);
    }

    public static void onShouldModifySleepFade(ServerPlayer player, boolean state) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeBoolean(state);
        NetworkManager.sendToPlayer(player, Packets.MODIFY_SLEEP_FADE, buf);
    }
}
