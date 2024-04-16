package dev.kalkafox.wolfutils.mixin;


import com.mojang.brigadier.CommandDispatcher;
import dev.kalkafox.wolfutils.commands.BoatCommand;
import dev.kalkafox.wolfutils.commands.DiscardCommand;
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
public class CommandMixin {

    @Shadow @Final private CommandDispatcher<CommandSourceStack> dispatcher;

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void commandInit(Commands.CommandSelection selection, CommandBuildContext context, CallbackInfo ci) {
        //BoatCommand.register(this.dispatcher);
        DiscardCommand.register(this.dispatcher);
    }

}
