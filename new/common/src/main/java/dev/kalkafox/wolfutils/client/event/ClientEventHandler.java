package dev.kalkafox.wolfutils.client.event;

import com.mojang.logging.LogUtils;
import dev.kalkafox.wolfutils.WolfUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;

public class ClientEventHandler {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static void onSleepFade(GuiGraphics guiGraphics, int width, int height, int sleepTime, float opacity, int color) {

        //LOGGER.info("width: " + width + ", height: " + height + ", sleeptime: " + sleepTime + ", opacity: " + opacity + ", color: " + color);

        Player player = Minecraft.getInstance().player;

        if (WolfUtils.getInteractionData(player) == null) return;

        color = (int)(255.0f * opacity) << 24 | 0x101010;

        guiGraphics.fill(RenderType.guiOverlay(), 0, 0, width, height, color);

    }


}
