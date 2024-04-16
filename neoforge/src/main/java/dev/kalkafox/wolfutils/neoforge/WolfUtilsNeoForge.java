package dev.kalkafox.wolfutils.neoforge;

import dev.kalkafox.wolfutils.WolfUtils;
import net.neoforged.fml.common.Mod;

@Mod(WolfUtils.MOD_ID)
public class WolfUtilsNeoForge {
    public WolfUtilsNeoForge() {
        WolfUtils.init();
    }
}