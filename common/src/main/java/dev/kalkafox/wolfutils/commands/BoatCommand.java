package dev.kalkafox.wolfutils.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.logging.LogUtils;
import dev.kalkafox.wolfutils.WolfUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

public class BoatCommand {

    private static final Logger log = LogUtils.getLogger();


    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal(WolfUtils.MOD_ID)
                .requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                .then(Commands.literal("boat").executes(context -> {

                    ServerPlayer player = context.getSource().getPlayer();
                    ServerLevel serverLevel = context.getSource().getLevel();

                    if (player == null) return -1;

                    Vec3 startPos = player.position().subtract(0, 1, 0);
                    BlockPos playerBlockPos = player.blockPosition();

                    int y = 0;

//                    while (y < 320) {
//                        // Calculate position vector for boat
//                        Vec3 boatPos = startPos.add(x++, y++, z++);
//
//                        System.out.println(boatPos.x);
//
//                        // Spawn boat entity
//                        Boat boat = new Boat(serverLevel, boatPos.x, boatPos.y, boatPos.z);
//                        serverLevel.addFreshEntity(boat);
//                    }

                    int offsetX = 0;

                    for (int x = 0; x < 12; x++) {
                        offsetX++;
                        for (int z = 0; z < 64; z+=2) {
                            // Calculate position vector for boat
                            Vec3 boatPos = startPos.add(x+offsetX, y++, z);

                            System.out.println(boatPos.x);

                            // Spawn boat entity
                            Boat boat = new Boat(serverLevel, boatPos.x, boatPos.y, boatPos.z);

                            serverLevel.addFreshEntity(boat);

                            BlockPos slimeBlockPos = new BlockPos(x+offsetX, 10, z);

                            //serverLevel.setBlock(slimeBlockPos, Blocks.SLIME_BLOCK)

                        }
                    }

                    offsetX = 0;

                    for (int x = 0; x < 12; x++) {
                        offsetX++;
                        for (int z = 0; z < 64; z+= 2) {
                            BlockPos slimeBlockPos = new BlockPos(x+offsetX, 10, z);

                            serverLevel.setBlockAndUpdate(slimeBlockPos, Blocks.SLIME_BLOCK.defaultBlockState());
                        }
                    }

                    //serverLevel.setBlockAndUpdate(playerBlockPos, Blocks.SLIME_BLOCK.defaultBlockState());

                    log.info("Added a total of {} boats", y);



                    return 1;
                }))
        );
    }


}
