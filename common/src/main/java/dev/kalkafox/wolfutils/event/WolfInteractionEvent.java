package dev.kalkafox.wolfutils.event;

import com.mojang.logging.LogUtils;
import dev.kalkafox.wolfutils.WolfUtils;
import dev.kalkafox.wolfutils.sound.Sounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
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
    private int jingleSoundTime;

    private boolean isInteracting;
    private static final Logger log = LogUtils.getLogger();

    public boolean finished;


    public WolfInteractionEvent(Player player, Wolf wolfy) {

        this.player = player;
        this.wolfy = wolfy;
        // Save the position and direction of the wolf to be used later
        this.wolfPos = wolfy.position();
        this.wolfLookPos = wolfy.getLookAngle();
        this.finished = false;


        WolfUtils.wolvesInteractingWithPlayers.put(player, this);
        WolfUtils.sleepingWolves.add(wolfy);

        wolfy.setIsInterested(true);

        shouldModifySleepFade(true);

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

        if (finished) {
            wolfy.playSound(SoundEvents.WOLF_WHINE, 0.5f, 0.5f);
        }
        

        // Set wolfy's interested status to false
        wolfy.setIsInterested(false);

        wolfy.getLookControl().setLookAt(getWolfLookPos());
        wolfy.moveTo(getWolfPos());

        wolfy.setInSittingPose(true);

        wolfy.getNavigation().moveTo(getWolfPos().x, getWolfPos().y, getWolfPos().z, 1);

        wolfy.hasImpulse = false;

        removeMapping();
    }

    public void updateClientWolfState(boolean state) {
        EventHandler.onUpdateClientWolfState((ServerPlayer) player, state);
    }

    public void shouldModifySleepFade(boolean state) {
        EventHandler.onShouldModifySleepFade((ServerPlayer) player, state);
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

                wolfy.setInSittingPose(true);

                wolfy.hasImpulse = true;
            }

            isInteracting = true;

            postInteractTicks++;

            if (wolfy.getRandom().nextInt(1000) < ambientSoundTime++) {
                float pitchStart = 0.5f;
                float pitchEnd = 0.8f;

                float pitchResult = pitchStart + wolfy.getRandom().nextFloat() * (pitchEnd - pitchStart);

                wolfy.playSound(SoundEvents.WOLF_PANT, 0.5f, pitchResult);
                //wolfy.level().playSound(wolfy, wolfy.blockPosition(), SoundEvents.WOLF_PANT, SoundSource.NEUTRAL, 0.5f, 0.1f);
                ambientSoundTime -= wolfy.getAmbientSoundInterval();
            }

            if (wolfy.getRandom().nextInt(500) < jingleSoundTime++) {
                float pitchStart = 0.8f;
                float pitchEnd = 1.2f;

                float pitchResult = pitchStart + wolfy.getRandom().nextFloat() * (pitchEnd - pitchStart);

                wolfy.playSound(Sounds.WOLF_JINGLE.get(), 0.2f, pitchResult);
                jingleSoundTime -= wolfy.getAmbientSoundInterval();
            }
        }
    }

    private void teleportWolf() {
        Direction dir = player.getBedOrientation();

        if (dir == null) {
            return;
        }

        BlockPos pos = player.blockPosition();

        boolean isSuitable = true;

        Level level = player.level();

        BlockPos chosenPos = null;

        for (int i = 2; i < 5; i++) {
            if (!level.getBlockState(pos.relative(dir, -i)).isAir()) {
                isSuitable = false;
                break;
            }
            chosenPos = pos.relative(dir, -i);
        }

        if (!isSuitable) {
            log.error("Could not find suitable location to place wolf, discarding sleep event");
            updateClientWolfState(false);
            shouldModifySleepFade(false);

            stop();
            return;
        }

        wolfy.moveTo(chosenPos.getX(), chosenPos.getY(), chosenPos.getZ());
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

            Vec3 lookVec = Vec3.atLowerCornerOf(dir.getNormal());


            Vec3 wolfVec = wolfy.position().subtract(player.position()).normalize();

            double dotProduct = lookVec.dot(wolfVec);

            double angle = Math.toDegrees(Math.acos(dotProduct));

            if (angle <= 120) {
                teleportWolf();
            }

            //shouldModifySleepFade(true);
        }
    }

    public boolean isInteracting() {
        return isInteracting;
    }

    public int getPostInteractTicks() {
        return postInteractTicks;
    }
}
