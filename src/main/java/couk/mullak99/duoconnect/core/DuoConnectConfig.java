package couk.mullak99.duoconnect.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import couk.mullak99.duoconnect.DuoConnect;
import net.fabricmc.loader.api.FabricLoader;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DuoConnectConfig {
    private static final Path CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("DuoConnect.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static DuoConnectConfig INSTANCE;
    private boolean waitBetweenConnections = false;
    private long waitTimeMs = 100;

    private static DuoConnectConfig getInstance() {
        if (INSTANCE == null) {
            loadConfig();
        }

        return INSTANCE;
    }

    public static void loadConfig() {
        DuoConnect.LOGGER.info(INSTANCE == null ? "Loading config..." : "Reloading config...");

        INSTANCE = readFile();

        if (INSTANCE == null) {
            INSTANCE = new DuoConnectConfig();
        }

        writeFile(INSTANCE);
    }

    public static void saveConfig() {
        if (INSTANCE != null) {
            writeFile(INSTANCE);
        }
    }

    @Nullable
    private static DuoConnectConfig readFile() {
        if (!Files.isRegularFile(CONFIG_FILE))
            return null;

        try (BufferedReader reader = Files.newBufferedReader(CONFIG_FILE)) {
            return GSON.fromJson(reader, DuoConnectConfig.class);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static void writeFile(DuoConnectConfig instance) {
        try (BufferedWriter writer = Files.newBufferedWriter(CONFIG_FILE)) {
            GSON.toJson(instance, writer);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static boolean waitBetweenConnections() {
        return getInstance().waitBetweenConnections;
    }

    public static long waitTimeMs() {
        return getInstance().waitTimeMs;
    }

}
