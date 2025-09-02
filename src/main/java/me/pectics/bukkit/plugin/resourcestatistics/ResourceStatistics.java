package me.pectics.bukkit.plugin.resourcestatistics;

import me.pectics.bukkit.plugin.resourcestatistics.listener.BlockStatsListener;
import me.pectics.bukkit.plugin.resourcestatistics.listener.EntityStatsListener;
import me.pectics.bukkit.plugin.resourcestatistics.listener.ItemStatsListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class ResourceStatistics extends JavaPlugin {

    private static final long SAVE_INTERVAL_TICKS = 6000L; // 5 minutes

    @Override
    public void onEnable() {
        StatsRecorder.initialize(this);
        new BlockStatsListener(this);
        new ItemStatsListener(this);
        new EntityStatsListener(this);
        new CommandExecutor(this);
        getServer().getScheduler().runTaskTimerAsynchronously(
                this, StatsRecorder::save, SAVE_INTERVAL_TICKS, SAVE_INTERVAL_TICKS);
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        StatsRecorder.save();
        getLogger().info("Stats files saved successfully.");
    }

}
