package net.slayer.rustle;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Rustle implements ModInitializer {
	public static final String MOD_ID = "rustle";
	public static final Logger LOGGER = LoggerFactory.getLogger("rustle");

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");
	}
}