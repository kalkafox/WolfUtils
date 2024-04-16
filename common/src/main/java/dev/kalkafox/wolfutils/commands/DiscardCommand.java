package dev.kalkafox.wolfutils.commands;

// Discards an entity instead of killing it. (Mainly because I feel bad for killing debug entities)

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;

import java.util.Collection;

public class DiscardCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder) Commands.literal("discard").requires(commandSourceStack -> commandSourceStack.hasPermission(2))).executes(commandContext -> discard((CommandSourceStack)commandContext.getSource(), ImmutableList.of(((CommandSourceStack)commandContext.getSource()).getEntityOrException())))).then(Commands.argument("targets", EntityArgument.entities()).executes(commandContext -> discard((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntities(commandContext, "targets")))));
    }

    private static int discard(CommandSourceStack source, Collection<? extends Entity> targets) {
        for (Entity entity : targets) {
            entity.discard();
        }
        if (targets.size() == 1) {
            source.sendSuccess(() -> Component.translatable("commands.discard.success.single", (targets.iterator().next()).getDisplayName()), true);
        } else {
            source.sendSuccess(() -> Component.translatable("commands.discard.success.multiple", targets.size()), true);
        }
        return targets.size();
    }
}


