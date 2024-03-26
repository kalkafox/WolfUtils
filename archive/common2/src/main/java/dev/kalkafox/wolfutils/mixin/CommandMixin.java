package dev.kalkafox.wolfutils.mixin;


import com.mojang.brigadier.CommandDispatcher;
import dev.kalkafox.wolfutils.commands.CountCommand;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(Commands.class)
public abstract class CommandMixin {

    @Shadow
    @Final
    private CommandDispatcher<CommandSourceStack> dispatcher;

    @Inject(at = @At(value = "TAIL", target = "Lcom/mojang/brigadier/CommandDispatcher;setConsumer(Lcom/mojang/brigadier/ResultConsumer;)V"), method = "<init>")
    private void onDispatch(Commands.CommandSelection environment, CommandBuildContext registryAccess, CallbackInfo ci) {

        CountCommand.register(this.dispatcher);
    }

}
