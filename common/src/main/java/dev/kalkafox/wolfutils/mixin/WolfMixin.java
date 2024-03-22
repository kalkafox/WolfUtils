package dev.kalkafox.wolfutils.mixin;

import dev.kalkafox.wolfutils.WolfUtils;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.animal.Wolf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Wolf.class)
public class WolfMixin {

    @Inject(method = "getAmbientSound", at = @At("HEAD"), cancellable = true)
    private void onGetAmbientSound(CallbackInfoReturnable<SoundEvent> cir) {
        for (Wolf wolfy : WolfUtils.sleepingWolves) {
            if (wolfy.getId() == ((Wolf)(Object)this).getId()) {
                cir.setReturnValue(null);
            }
        }
    }

}
