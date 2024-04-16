package dev.kalkafox.wolfutils.mixin;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.commands.KillCommand;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(KillCommand.class)
public class KillCommandMixin {

    @Inject(method = "kill", at = @At("HEAD"), cancellable = true)
    private static void onKill(CommandSourceStack pSource, Collection<? extends Entity> pTargets, CallbackInfoReturnable<Integer> cir) {
        cir.cancel();
        // Send to Entity Heaven instead of killing them (I hate feeling bad for killing my debug wolves)
        for (Entity entity : pTargets) {
            entity.discard();
        }

        if (pTargets.size() == 1) {
            pSource.sendSuccess(() -> {
                return Component.translatable("commands.discard.success.single", pTargets.iterator().next().getDisplayName());
            }, true);
        } else {
            pSource.sendSuccess(() -> {
                return Component.translatable("commands.discard.success.multiple", pTargets.size());
            }, true);
        }

        cir.setReturnValue(pTargets.size());
    }
}

