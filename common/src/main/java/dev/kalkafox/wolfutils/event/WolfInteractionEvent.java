package dev.kalkafox.wolfutils.event;

import com.mojang.logging.LogUtils;
import dev.architectury.networking.NetworkManager;
import dev.kalkafox.wolfutils.WolfUtils;
import dev.kalkafox.wolfutils.mixin.PlayerAccessor;
import dev.kalkafox.wolfutils.packet.Packets;
import io.netty.buffer.Unpooled;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

public class WolfInteractionEvent {
    private final Player player;
    private final Vec3 wolfPos;
    private final Vec3 wolfLookPos;
    private final Wolf wolfy;

    private int interactTicks;
    private int postInteractTicks;

    private int ambientSoundTime;

    private boolean isInteracting;
    private static final Logger log = LogUtils.getLogger();

    public boolean finished;


    public WolfInteractionEvent(Player player, Wolf wolfy) {
        log.debug("Initialized " + this + " in memory location " + Integer.toHexString(hashCode()));

        this.player = player;
        this.wolfy = wolfy;
        // Save the position and direction of the wolf to be used later
        this.wolfPos = wolfy.position();
        this.wolfLookPos = wolfy.getLookAngle();
        this.finished = false;


        WolfUtils.wolvesInteractingWithPlayers.put(player, this);
        WolfUtils.sleepingWolves.add(wolfy);

        wolfy.setIsInterested(true);

        log.info("Mapping has length! " + WolfUtils.wolvesInteractingWithPlayers.size());



        maybeTeleportWolf();
    }


    public void tick() {

        interactTicks++;


        wolfy.getLookControl().setLookAt(this.player);
        if (interactTicks >= 120) {
            interactWolf();
        }
    }
    
    public void stop() {

        wolfy.playSound(SoundEvents.WOLF_WHINE, 0.5f, 0.5f);
        

        // Set wolfy's interested status to false
        wolfy.setIsInterested(false);

        wolfy.getLookControl().setLookAt(getWolfLookPos());
        wolfy.moveTo(getWolfPos());

        wolfy.setInSittingPose(true);

        wolfy.getNavigation().moveTo(getWolfPos().x, getWolfPos().y, getWolfPos().z, 1);

        wolfy.hasImpulse = false;

        updateClientWolfState(false);

        removeMapping();
    }

    public Wolf getWolf() { return wolfy; }


    public Player getPlayer() {
        return player;
    }

    public Vec3 getWolfPos() {
        return wolfPos;
    }

    public Vec3 getWolfLookPos() {
        return wolfLookPos;
    }

    private void removeMapping() {
        // Remove the mapping from the HashMap
        WolfUtils.sleepingWolves.remove(wolfy);
        WolfUtils.wolvesInteractingWithPlayers.remove(getPlayer());
        log.info("Mapping has length! " + WolfUtils.wolvesInteractingWithPlayers.size());
    }


    private void interactWolf() {
        if (postInteractTicks > 240) {
            finished = true;
            WolfUtils.sleepingWolves.remove(wolfy);
        }

        if (wolfy.distanceToSqr(player) >= 2) {
            wolfy.setInSittingPose(false);
            wolfy.getNavigation().moveTo(player, 0.8);

            Direction dir = player.getBedOrientation();

            if (dir == null) {
                return;
            }

            if (wolfy.distanceToSqr(player) < 3) {
                wolfy.moveTo(player.getX()-dir.getStepX(), player.getY(), player.getZ()-dir.getStepZ());
            }

        } else {

            if (!isInteracting) {
                updateClientWolfState(true);
            }

            isInteracting = true;

            wolfy.setInSittingPose(true);

            wolfy.hasImpulse = true;

            postInteractTicks++;

            if (wolfy.getRandom().nextInt(1000) < ambientSoundTime++) {
                float pitchStart = 0.5f;
                float pitchEnd = 0.8f;

                float pitchResult = pitchStart + wolfy.getRandom().nextFloat() * (pitchEnd - pitchStart);

                wolfy.playSound(SoundEvents.WOLF_PANT, 0.5f, pitchResult);
                //wolfy.level().playSound(wolfy, wolfy.blockPosition(), SoundEvents.WOLF_PANT, SoundSource.NEUTRAL, 0.5f, 0.1f);
                ambientSoundTime -= wolfy.getAmbientSoundInterval();
            }
        }
    }

    private void teleportWolf() {
        Direction dir = player.getBedOrientation();

        if (dir == null) {
            return;
        }

        int offsetX = 0;
        int offsetZ = 0;

        int toOffset = wolfy.getRandom().nextInt(7) - 3;

        switch (dir) {
            case EAST, WEST:
                offsetX = toOffset;
            case NORTH, SOUTH:
                offsetZ = toOffset;
        }

        wolfy.moveTo(player.getX()-dir.getStepX()+offsetX, player.getY(), player.getZ()-dir.getStepZ()+offsetZ);
    }

    private void maybeTeleportWolf() {

        LivingEntity player = wolfy.getOwner();
        if (player == null) {

            return;
        }

        if (player instanceof Player) {
            //Vec3 playerLookVec = player.getViewVector(1.0f).normalize();

            Direction dir = player.getBedOrientation();

            if (dir == null) return;

            Vec3 lookVec = Vec3.atLowerCornerOf(player.getBedOrientation().getNormal());


            Vec3 wolfVec = wolfy.position().subtract(player.position()).normalize();

            double dotProduct = lookVec.dot(wolfVec);

            double angle = Math.toDegrees(Math.acos(dotProduct));

            if (angle <= 120) {
                teleportWolf();
            }
        }
    }

    private void updateClientWolfState(boolean state) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeBoolean(state);
        NetworkManager.sendToPlayer((ServerPlayer)player, Packets.UPDATE_WOLF_STATE, buf);
    }

    public boolean isInteracting() {
        return isInteracting;
    }

    public int getPostInteractTicks() {
        return postInteractTicks;
    }
}
