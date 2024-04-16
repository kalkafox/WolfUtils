package dev.kalkafox.wolfutils.forge.mixin.client;

import dev.kalkafox.wolfutils.client.event.ClientEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ForgeGui.class)
public abstract class GuiMixin extends Gui {

    public GuiMixin(Minecraft minecraft, ItemRenderer itemRenderer) {
        super(minecraft, itemRenderer);
    }

    @Inject(method = "renderSleepFade", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;fill(Lnet/minecraft/client/renderer/RenderType;IIIII)V"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void doRenderSleepFade(int width, int height, GuiGraphics guiGraphics, CallbackInfo ci, int sleepTime, float opacity, int color) {
        ClientEventHandler.onSleepFade(guiGraphics, width, height, sleepTime, opacity, color, ci);
    }

}
