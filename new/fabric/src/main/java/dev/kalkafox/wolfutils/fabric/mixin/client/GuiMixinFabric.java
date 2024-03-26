package dev.kalkafox.wolfutils.fabric.mixin.client;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Gui.class)
public class GuiMixinFabric {

    @Shadow private int screenWidth;
    @Shadow private int screenHeight;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;fill(Lnet/minecraft/client/renderer/RenderType;IIIII)V"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void onRender(GuiGraphics guiGraphics, float partialTick, CallbackInfo ci, Window window, Font font, float f, float h, float j, int k) {
        System.out.println(k);
        ci.cancel();

        k = (int)(255.0f * j) << 24 | 0x101010;

        guiGraphics.fill(RenderType.guiOverlay(), 0, 0, screenWidth, screenHeight, k);
    }

}
