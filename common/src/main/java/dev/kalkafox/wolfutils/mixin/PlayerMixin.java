package dev.kalkafox.wolfutils.mixin;

import dev.kalkafox.wolfutils.event.EventHandler;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void onPrePlayerTick(CallbackInfo ci) {
        EventHandler.onPrePlayerTick((Player)(Object)this);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void onPostPlayerTick(CallbackInfo ci) {
        EventHandler.onPostPlayerTick((Player)(Object)this);
    }

}
