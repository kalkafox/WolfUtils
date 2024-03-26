package dev.kalkafox.wolfutils.event;

import dev.kalkafox.wolfutils.WolfUtils;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class WolfInteractionEvent {
    private final Player player;
    private final Vec3 wolfPos;
    private final Vec3 wolfLookPos;
    private final Wolf wolfy;

    public int interactTicks;
    public int ambientSoundTime;

    public boolean isInteracting;

    public WolfInteractionEvent(Player player, Wolf wolfy, Vec3 wolfPos, Vec3 wolfLookPos) {
        this.player = player;
        this.wolfy = wolfy;
        this.wolfPos = wolfPos;
        this.wolfLookPos = wolfLookPos;

        boolean shouldTeleport = canPlayerSeeWolf(wolfy);

        if (shouldTeleport) {

            Direction dir = player.getBedOrientation();

            if (dir == null) {
                return;
            }

            int offsetX = 0;
            int offsetZ = 0;

            int toOffset = 3;

            switch (dir) {
                case EAST, WEST:
                    offsetX = toOffset;
                case NORTH, SOUTH:
                    offsetZ = toOffset;
            }

            wolfy.moveTo(player.getX()-dir.getStepX(), player.getY(), player.getZ()-dir.getStepZ());
        }

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


    public void tick() {

        interactTicks++;


        wolfy.getLookControl().setLookAt(this.player);
        if (interactTicks >= 120) {
            interactWolf();
        }
    }


    private void interactWolf() {
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

            isInteracting = true;

            wolfy.setInSittingPose(true);

            if (!WolfUtils.sleepingWolves.contains(wolfy)) {
                WolfUtils.sleepingWolves.add(wolfy);
            }

            if (wolfy.getRandom().nextInt(1000) < ambientSoundTime++) {
                float pitch = 0.2f + (wolfy.getRandom().nextFloat() * 0.2f);
                wolfy.playSound(SoundEvents.WOLF_PANT, 0.5f, pitch);
                ambientSoundTime -= wolfy.getAmbientSoundInterval();
            }
        }
    }

    private boolean canPlayerSeeWolf(Wolf wolfy) {

        LivingEntity player = wolfy.getOwner();
        if (player == null) {

            return false;
        }

        if (player instanceof Player) {
            //Vec3 playerLookVec = player.getViewVector(1.0f).normalize();

            Direction dir = player.getBedOrientation();

            if (dir == null) return false;

            Vec3 lookVec = Vec3.atLowerCornerOf(player.getBedOrientation().getNormal());


            Vec3 wolfVec = wolfy.position().subtract(player.position()).normalize();

            double dotProduct = lookVec.dot(wolfVec);

            double angle = Math.toDegrees(Math.acos(dotProduct));

            return angle <= 120;
        }

        return false;
    }
}
