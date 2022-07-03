package couk.mullak99.duoconnect.core;

import couk.mullak99.duoconnect.DuoConnect;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConnectScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;

public class Connection {
    public static void doConnection(Screen screen, ServerInfo serverOne, ServerInfo serverTwo) {
        if (MinecraftClient.getInstance().isMultiplayerEnabled()) {
            new Thread(() -> {
                // First connection
                ConnectScreen.connect(screen, MinecraftClient.getInstance(), ServerAddress.parse(serverOne.address), serverOne);

                // This doesn't really work how planned and needs reworking.
                // Using start launches a new thread, which does not work with switching screens.
                // Shouldn't matter anyway, a delay isn't actually necessary from my testing
                if (DuoConnectConfig.waitBetweenConnections()) {
                    long waitTime = DuoConnectConfig.waitTimeMs();
                    DuoConnect.LOGGER.info("Waiting " + waitTime + "ms before doing the second connection...");
                    try {
                        if (waitTime > DuoConnect.MAX_WAIT_TIME_MS)
                        {
                            DuoConnect.LOGGER.warn("Specified wait time is too long! Limiting to " + DuoConnect.MAX_WAIT_TIME_MS + "ms.");
                            waitTime = DuoConnect.MAX_WAIT_TIME_MS;
                        }
                        Thread.sleep(waitTime);
                    } catch (InterruptedException e) {
                        DuoConnect.LOGGER.error(e.getMessage());
                    }
                }

                // Second connection
                ConnectScreen.connect(screen, MinecraftClient.getInstance(), ServerAddress.parse(serverTwo.address), serverTwo);
            }).run();
        }
        else {
            DuoConnect.LOGGER.warn("Multiplayer is not enabled! Unable to connect.");
        }
    }
}
