package me.pectics.bukkit.plugin.resourcestatistics.listener;

import me.pectics.bukkit.plugin.resourcestatistics.ResourceStatistics;
import me.pectics.bukkit.plugin.resourcestatistics.StatsRecorder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;

import static me.pectics.bukkit.plugin.resourcestatistics.StatsRecorder.StatType.BLOCK;

public class BlockStatsListener implements Listener {

    private final ResourceStatistics plugin;

    public BlockStatsListener(@NotNull ResourceStatistics plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBreakBlock(@NotNull BlockBreakEvent event) {
        if (event.getPlayer().isOp()) return;
        StatsRecorder.record(BLOCK, event.getBlock().getType() + ".break", 1);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlaceBlock(@NotNull BlockPlaceEvent event) {
        if (event.getPlayer().isOp()) return;
        StatsRecorder.record(BLOCK, event.getBlock().getType() + ".place", 1);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onExplodeBlock(@NotNull BlockExplodeEvent event) {
        StatsRecorder.record(BLOCK, event.getExplodedBlockState().getType() + ".explode", 1);
    }

}
