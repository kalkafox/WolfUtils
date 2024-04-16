package dev.kalkafox.wolfutils.sound;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.SoundType;

public abstract class EntityStepContext {

    private static final float pitchStart = 0.95f;
    private static final float pitchEnd = 1.15f;

    private static final RandomSource rand = RandomSource.create();


    public static void step(Entity entity, SoundType soundType) {
        float pitchResult = pitchStart + rand.nextFloat() * (pitchEnd - pitchStart);

        entity.playSound(soundType.getStepSound(), soundType.getVolume() * 0.15f, pitchResult);
    }

}
