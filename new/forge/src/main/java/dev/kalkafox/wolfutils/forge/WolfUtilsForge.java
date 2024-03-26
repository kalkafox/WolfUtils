package dev.kalkafox.wolfutils.forge;

import dev.kalkafox.wolfutils.WolfUtils;
import dev.kalkafox.wolfutils.client.WolfUtilsClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(WolfUtils.MOD_ID)
public class WolfUtilsForge {
    public WolfUtilsForge() {

        WolfUtils.init();

        if (FMLEnvironment.dist == Dist.CLIENT) {
            WolfUtilsClient.clientInit();
        }

    }
}