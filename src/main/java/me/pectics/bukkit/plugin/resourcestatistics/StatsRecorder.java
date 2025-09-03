package me.pectics.bukkit.plugin.resourcestatistics;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private static final String BLOCK_STATS_FILE = "block-stats.yml";
    private static final String ENTITY_STATS_FILE = "entity-stats.yml";
    private static final String ITEM_STATS_FILE = "item-stats.yml";

    private static ResourceStatistics plugin;

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

    public static void record(@NotNull StatType type, String key, long delta) {
        timeEmbedRecord(switch (type) {
            case BLOCK -> blockConfig;
            case ENTITY -> entityConfig;
            case ITEM -> itemConfig;
        }, key, delta);
    }

    private static void timeEmbedRecord(@NotNull FileConfiguration config, String key, long delta) {
        // Update total
        String totalKey = key + ".total";
        config.set(totalKey, config.getLong(totalKey, 0L) + delta);
        // Update daily
        String date = DATE_FORMAT.format(new Date());
        String timedKey = key + "." + date;
        config.set(timedKey, config.getLong(timedKey, 0L) + delta);
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
