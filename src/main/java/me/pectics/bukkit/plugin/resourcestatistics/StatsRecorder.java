package me.pectics.bukkit.plugin.resourcestatistics;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.logging.Level;

public class StatsRecorder {

    private StatsRecorder() {
        // Private constructor to prevent instantiation
    }

    public enum StatType {
        BLOCK,
        ENTITY,
        ITEM
    }

    private static ResourceStatistics plugin;

    private static final String BLOCK_STATS_FILE = "block-stats.yml";
    private static final String ENTITY_STATS_FILE = "entity-stats.yml";
    private static final String ITEM_STATS_FILE = "item-stats.yml";

    private static File blockFile;
    private static File entityFile;
    private static File itemFile;

    private static FileConfiguration blockConfig;
    private static FileConfiguration entityConfig;
    private static FileConfiguration itemConfig;

    public static void initialize(@NotNull ResourceStatistics plugin) {
        StatsRecorder.plugin = plugin;
        blockFile = new File(plugin.getDataFolder(), BLOCK_STATS_FILE);
        entityFile = new File(plugin.getDataFolder(), ENTITY_STATS_FILE);
        itemFile = new File(plugin.getDataFolder(), ITEM_STATS_FILE);
        if (!blockFile.exists() || !blockFile.isFile()) plugin.saveResource(BLOCK_STATS_FILE, false);
        if (!entityFile.exists() || !entityFile.isFile()) plugin.saveResource(ENTITY_STATS_FILE, false);
        if (!itemFile.exists() || !itemFile.isFile()) plugin.saveResource(ITEM_STATS_FILE, false);
        blockConfig = YamlConfiguration.loadConfiguration(blockFile);
        entityConfig = YamlConfiguration.loadConfiguration(entityFile);
        itemConfig = YamlConfiguration.loadConfiguration(itemFile);
    }

    public static void record(@NotNull StatType type, String key, int delta) {
        switch (type) {
            case BLOCK -> blockConfig.set(key, blockConfig.getInt(key, 0) + delta);
            case ENTITY -> entityConfig.set(key, entityConfig.getInt(key, 0) + delta);
            case ITEM -> itemConfig.set(key, itemConfig.getInt(key, 0) + delta);
        }
    }

    public static void save() {
        try {
            blockConfig.save(blockFile);
            entityConfig.save(entityFile);
            itemConfig.save(itemFile);
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save stats files: " + e.getMessage(), e);
        }
    }

}
