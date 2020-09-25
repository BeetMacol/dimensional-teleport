package com.beetmacol.dimensionalteleport;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DimensionalTeleport implements ModInitializer {

	Logger logger = LogManager.getLogger(DimensionalTeleport.class);

	@Override
	public void onInitialize() {
		logger.info("Dimensional Teleport mod initialized.");
	}
}
