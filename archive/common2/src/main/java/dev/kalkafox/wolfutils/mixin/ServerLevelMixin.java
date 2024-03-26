package dev.kalkafox.wolfutils.mixin;

import dev.kalkafox.wolfutils.event.EventHandler;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin {

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void onPreServerLevelTick(BooleanSupplier hasTimeLeft, CallbackInfo ci) {

        EventHandler.onPreServerLevelTick((ServerLevel)(Object)this, hasTimeLeft);

    }

    @Inject(method = "tick", at = @At(value = "TAIL"))
    private void onPostServerLevelTick(BooleanSupplier hasTimeLeft, CallbackInfo ci) {

        EventHandler.onPostServerLevelTick((ServerLevel)(Object)this, hasTimeLeft);

    }

}
