package dev.kalkafox.wolfutils;

import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.animal.Wolf;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WolfUtils
{
	public static final String MOD_ID = "wolfutils";

	private static final Logger LOGGER = LogUtils.getLogger();

	private static final int THREAD_POOL_SIZE = 12; // Number of threads in the pool
	private static final ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

	public static final ArrayList<Wolf> sleepingWolves = new ArrayList<>();

	public static Logger getLogger() {
		return LOGGER;
	}

	public static void init() {



		LOGGER.info("We in the game, yo.");
	}

	public static ExecutorService getExecutor() {
		return executor;
	}
}
