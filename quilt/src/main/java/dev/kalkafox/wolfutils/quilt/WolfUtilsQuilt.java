package dev.kalkafox.wolfutils.quilt;


import dev.kalkafox.wolfutils.WolfUtils;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public class WolfUtilsQuilt implements ModInitializer {
    @Override
    public void onInitialize(ModContainer mod) {
        WolfUtils.init();
    }
}
