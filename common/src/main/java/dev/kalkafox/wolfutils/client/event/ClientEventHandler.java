package dev.kalkafox.wolfutils.client.event;

import com.mojang.logging.LogUtils;
import dev.kalkafox.wolfutils.client.WolfUtilsClient;
import dev.kalkafox.wolfutils.mixin.PlayerAccessor;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public final class ClientEventHandler {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static void onSleepFade(GuiGraphics guiGraphics, int width, int height, int sleepTime, float opacity, int color, CallbackInfo ci) {

        //LOGGER.info("width: " + width + ", height: " + height + ", sleeptime: " + sleepTime + ", opacity: " + opacity + ", color: " + color);

        if (!WolfUtilsClient.postWolfTick) return;

        if (WolfUtilsClient.shouldModifySleepFade) {
            ci.cancel();

            color = (int) (250.0f * opacity) << 24 | 0x050505; //0x101010;

            guiGraphics.fill(RenderType.guiOverlay(), 0, 0, width, height, color);
        }

    }

    public static void onPrePlayerTick(Player player) {
        //System.out.println("client player tick!");

        if (!WolfUtilsClient.postWolfTick && WolfUtilsClient.shouldModifySleepFade) {
            ((PlayerAccessor)player).setSleepCounter(0);
        }
    }


}
