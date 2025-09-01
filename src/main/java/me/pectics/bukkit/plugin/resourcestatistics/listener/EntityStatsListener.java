package me.pectics.bukkit.plugin.resourcestatistics.listener;

import lombok.val;
import me.pectics.bukkit.plugin.resourcestatistics.ResourceStatistics;
import me.pectics.bukkit.plugin.resourcestatistics.StatsRecorder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.jetbrains.annotations.NotNull;

import static me.pectics.bukkit.plugin.resourcestatistics.StatsRecorder.StatType.ENTITY;

public class EntityStatsListener implements Listener {

    private final ResourceStatistics plugin;

    public EntityStatsListener(@NotNull ResourceStatistics plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDeath(@NotNull EntityDeathEvent event) {
        val cause = event.getDamageSource().getCausingEntity();
        if (cause == null) return;
        if (cause instanceof Player player) {
            if (player.isOp()) return;
            StatsRecorder.record(ENTITY, event.getEntityType() + ".death.player", 1);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntitySpawn(@NotNull EntitySpawnEvent event) {
        StatsRecorder.record(ENTITY, event.getEntityType() + ".spawn", 1);
    }

}
