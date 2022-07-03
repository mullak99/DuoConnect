package couk.mullak99.duoconnect;

import couk.mullak99.duoconnect.core.DuoConnectConfig;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DuoConnect implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("duoconnect");
	public static final long MAX_WAIT_TIME_MS = 3000;

	@Override
	public void onInitialize() {
		LOGGER.info("Hello there.");
		DuoConnectConfig.loadConfig();
	}
}
