package dev.kalkafox.wolfutils;

import com.google.common.base.Suppliers;
import com.mojang.logging.LogUtils;
import dev.architectury.registry.registries.RegistrarManager;
import dev.kalkafox.wolfutils.event.WolfInteractionEvent;
import dev.kalkafox.wolfutils.sound.Sounds;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class WolfUtils
{
	public static final String MOD_ID = "wolfutils";

	public static final Supplier<RegistrarManager> MANAGER = Suppliers.memoize(() -> RegistrarManager.get(MOD_ID));

	public static final ArrayList<Wolf> sleepingWolves = new ArrayList<>();

	public static Logger getLogger() {
		return LOGGER;
	}

	//public static final DeferredRegister<Trigger> ADVANCEMENTS = DeferredRegister.create(MOD_ID, Registries.TRIGGER_TYPE)

	public static void init() {

		LOGGER.info("We in the game, yo.");

		Sounds.register();

	}

	public static ExecutorService getExecutor() {
		return executor;
	}

	public static final HashMap<Player, WolfInteractionEvent> wolvesInteractingWithPlayers = new HashMap<>();

	public static WolfInteractionEvent getInteractionEvent(Player player) {
//		for (Map.Entry<Wolf, WolfInteractionData> entry : wolvesInteractingWithPlayers.entrySet()) {
//			if (entry.getValue().getPlayer().is(player)) {
//				return entry;
//			}
//		}
//        return null;

		return wolvesInteractingWithPlayers.get(player);
    }

	public static Collection<WolfInteractionEvent> getWolfInteractionEvents() {
		return wolvesInteractingWithPlayers.values();
	}

	public static boolean areWolvesInteracting() {
		return !wolvesInteractingWithPlayers.isEmpty() && !sleepingWolves.isEmpty();
	}

	private static final Logger LOGGER = LogUtils.getLogger();

	private static final int THREAD_POOL_SIZE = 12; // Number of threads in the pool
	private static final ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
}
