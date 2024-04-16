package dev.kalkafox.wolfutils.sound;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.kalkafox.wolfutils.WolfUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public final class Sounds {

    public static final String WOLF_JINGLE_KEY = "entity_wolf_jingle";


    private static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(WolfUtils.MOD_ID, Registries.SOUND_EVENT);

    public static final RegistrySupplier<SoundEvent> WOLF_JINGLE = register("entity_wolf_jingle");

    private static RegistrySupplier<SoundEvent> register(String name) {
        return SOUNDS.register(name, () -> SoundEvent.createFixedRangeEvent(new ResourceLocation(WolfUtils.MOD_ID, name.replaceAll("_", ".")), 8.0f));
    }

    public static void register() {
        SOUNDS.register();
    }


}
