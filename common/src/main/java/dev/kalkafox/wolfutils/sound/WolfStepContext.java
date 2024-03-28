package dev.kalkafox.wolfutils.sound;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.animal.Wolf;

public abstract class WolfStepContext {

    private static final float pitchStart = 0.9f;
    private static final float pitchEnd = 1.3f;
    private static final float volumeStart = 0.15f;
    private static final float volumeEnd = 0.25f;

    public static void step(Wolf wolfy) {
        float pitchResult = pitchStart + wolfy.getRandom().nextFloat() * (pitchEnd - pitchStart);
        float volumeResult = volumeStart + wolfy.getRandom().nextFloat() * (volumeEnd - volumeStart);



        wolfy.playSound(SoundEvents.WOLF_STEP, volumeResult, pitchResult);

        wolfy.playSound(wolfy.getFeetBlockState().getSoundType().getStepSound(), volumeResult-(volumeResult/2), pitchResult);

    }

}
