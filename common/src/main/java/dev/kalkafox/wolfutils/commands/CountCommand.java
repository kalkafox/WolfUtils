package dev.kalkafox.wolfutils.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.logging.LogUtils;
import dev.kalkafox.wolfutils.WolfUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import org.slf4j.Logger;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class CountCommand {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal(WolfUtils.MOD_ID)
                .requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                .then(Commands.literal("count")
                        .then(Commands.literal("cube")
                                .then(Commands.argument("radius", IntegerArgumentType.integer(1))
                                        .executes(context -> countBlocksAsync(context, BlockShape.CUBE))))
                        .then(Commands.literal("sphere")
                                .then(Commands.argument("radius", IntegerArgumentType.integer(1))
                                        .executes(context -> countBlocksAsync(context, BlockShape.SPHERE))))
                        .then(Commands.literal("cylinder")
                                .then(Commands.argument("radius", IntegerArgumentType.integer(1))
                                        .executes(context -> countBlocksAsync(context, BlockShape.CYLINDER))))
                )
        );
    }

    private static final AtomicInteger blockCount = new AtomicInteger();


    private static int countBlocks(CommandContext<CommandSourceStack> context, BlockShape blockShape) throws CommandSyntaxException {
        int radius = IntegerArgumentType.getInteger(context, "radius");
        LOGGER.info("Counting the amount of blocks in {} with a radius of {}...", blockShape, radius);
        ServerLevel world = context.getSource().getLevel();
        BlockPos playerPos = context.getSource().getPlayerOrException().blockPosition();

        Instant start = Instant.now();

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos scanPos = playerPos.offset(x, y, z);
                    if (isWithinShape(scanPos, playerPos, radius, blockShape)) {
                        if (!world.getBlockState(scanPos).isAir()) {
                            blockCount.updateAndGet(i -> ++i);
                        }
                    }
                }
            }
        }

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);

        String formattedTime = formatTime(timeElapsed);

        context.getSource().sendSystemMessage(
                Component.literal(String.format("Found %s blocks in %s with a radius of %s. Time taken: %s.",
                        blockCount.get(),
                        blockShape,
                        radius,
                        formattedTime
                ))
        );

        return -1;
    }

    private static int countBlocksAsync(CommandContext<CommandSourceStack> context, BlockShape blockShape) throws CommandSyntaxException {

        ExecutorService executor = WolfUtils.getExecutor();

        Future<Integer> future = executor.submit(() -> countBlocks(context, blockShape));

        try {
            return future.get();
        } catch (Exception e) {
            // Handle exceptions here
            return -1; // Or any error code to indicate failure
        }
    }


    private static String formatTime(Duration timeElapsed) {
        long millis = timeElapsed.toMillis();

        String formattedTime;

        if (millis < 1000) {
            formattedTime = millis + " milliseconds";
        } else if (millis < 60000) {
            formattedTime = millis / 1000 + " seconds";
        } else if (millis < 3600000) {
            formattedTime = String.format("%d minutes, %d seconds",
                    millis / 60000,
                    (millis % 60000) / 1000);
        } else {
            formattedTime = String.format("%d hours, %d minutes, %d seconds",
                    millis / 3600000,
                    (millis % 3600000) / 60000,
                    (millis % 60000) / 1000);
        }
        return formattedTime;
    }

    private static boolean isWithinShape(BlockPos pos, BlockPos center, int radius, BlockShape blockShape) {
        return switch (blockShape) {
            case CUBE -> true;
            case SPHERE -> center.distSqr(pos) <= radius * radius;
            case CYLINDER -> Math.abs(center.getX() - pos.getX()) <= radius &&
                    Math.abs(center.getZ() - pos.getZ()) <= radius &&
                    Math.abs(center.getY() - pos.getY()) <= radius;
        };
    }

    private enum BlockShape {
        CUBE,
        SPHERE,
        CYLINDER
    }

}
