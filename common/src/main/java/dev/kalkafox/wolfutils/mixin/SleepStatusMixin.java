package dev.kalkafox.wolfutils.mixin;

import dev.kalkafox.wolfutils.WolfUtils;
import net.minecraft.server.players.SleepStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SleepStatus.class)
public class SleepStatusMixin {


    @Inject(method = "areEnoughSleeping", at = @At("HEAD"), cancellable = true)
    private void onAreEnoughSleeping(int requiredSleepPercentage, CallbackInfoReturnable<Boolean> cir) {
        if (WolfUtils.areWolvesInteracting()) {
            cir.setReturnValue(false);
        }
    }

}
