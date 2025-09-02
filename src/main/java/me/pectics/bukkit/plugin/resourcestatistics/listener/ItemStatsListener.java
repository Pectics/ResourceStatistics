package me.pectics.bukkit.plugin.resourcestatistics.listener;

import io.papermc.paper.event.player.PlayerTradeEvent;
import lombok.val;
import me.pectics.bukkit.plugin.resourcestatistics.ResourceStatistics;
import me.pectics.bukkit.plugin.resourcestatistics.StatsRecorder;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.EnchantingInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static me.pectics.bukkit.plugin.resourcestatistics.StatsRecorder.StatType.ITEM;

public class ItemStatsListener implements Listener {

    private final ResourceStatistics plugin;

    public ItemStatsListener(@NotNull ResourceStatistics plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    // Natural related

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDespawnItem(@NotNull ItemDespawnEvent event) {
        val stack = event.getEntity().getItemStack();
        StatsRecorder.record(ITEM, stack.getType() + ".cost.despawn", stack.getAmount());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockDropItem(@NotNull BlockDropItemEvent event) {
        if (event.getPlayer().isOp()) return;
        event.getItems().forEach(i -> {
            val stack = i.getItemStack();
            StatsRecorder.record(ITEM, stack.getType() + ".prod.block", stack.getAmount());
        });
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDropItem(@NotNull EntityDropItemEvent event) {
        val stack = event.getItemDrop().getItemStack();
        StatsRecorder.record(ITEM, stack.getType() + ".prod.entity", stack.getAmount());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onLootGenerateItem(@NotNull LootGenerateEvent event) {
        event.getLoot().forEach(s ->
                StatsRecorder.record(ITEM, s.getType() + ".prod.loot", s.getAmount()));
    }

    // Inventory related

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSmeltItem(@NotNull FurnaceSmeltEvent event) {
        val source = event.getSource();
        val result = event.getResult();
        StatsRecorder.record(ITEM, source.getType()+ ".cost.smelt", source.getAmount());
        StatsRecorder.record(ITEM, result.getType()+ ".prod.smelt", result.getAmount());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBrewItem(@NotNull BrewEvent event) {
        val source = event.getContents().getIngredient();
        if (source != null) StatsRecorder.record(ITEM, source.getType()+ ".cost.brew", source.getAmount());
        event.getResults().forEach(s -> StatsRecorder.record(ITEM, s.getType()+ ".prod.brew", s.getAmount()));
    }

    // Player related

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSmithItem(@NotNull SmithItemEvent event) {
        if (event.getWhoClicked().isOp()) return;
        val source1 = event.getInventory().getInputMineral();
        val source2 = event.getInventory().getInputTemplate();
        if (source1 != null) StatsRecorder.record(ITEM, source1.getType()+ ".cost.smith", source1.getAmount());
        if (source2 != null) StatsRecorder.record(ITEM, source2.getType()+ ".cost.smith", source2.getAmount());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCraftItem(@NotNull CraftItemEvent event) {
        if (event.getWhoClicked().isOp()) return;
        for (@Nullable val stack : event.getInventory().getMatrix()) {
            if (stack == null) continue;
            StatsRecorder.record(ITEM, stack.getType()+ ".cost.craft", stack.getAmount());
        }
        val result = event.getRecipe().getResult();
        StatsRecorder.record(ITEM, result.getType()+ ".prod.craft", result.getAmount());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onConsumeItem(@NotNull PlayerItemConsumeEvent event) {
        if (event.getPlayer().isOp()) return;
        val stack = event.getItem();
        StatsRecorder.record(ITEM, stack.getType() + ".cost.consume", stack.getAmount());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onFishItem(@NotNull PlayerFishEvent event) {
        if (event.getPlayer().isOp()) return;
        val caught = event.getCaught();
        if (caught == null) return;
        if (caught instanceof Item item) {
            val stack = item.getItemStack();
            StatsRecorder.record(ITEM, stack.getType() + ".prod.fish", stack.getAmount());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onTradeItem(@NotNull PlayerTradeEvent event) {
        if (event.getPlayer().isOp()) return;
        event.getTrade().getIngredients().forEach(s ->
                StatsRecorder.record(ITEM, s.getType()+ ".cost.trade", s.getAmount()));
        val result = event.getTrade().getResult();
        StatsRecorder.record(ITEM, result.getType()+ ".prod.trade", result.getAmount());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEnchantItem(@NotNull EnchantItemEvent event) {
        if (event.getEnchanter().isOp()) return;
        val inv = event.getInventory();
        if (inv instanceof EnchantingInventory einv) {
            val stack = einv.getSecondary();
            if (stack == null) return;
            StatsRecorder.record(ITEM, stack.getType() + ".cost.enchant", stack.getAmount());
        }
    }

}
