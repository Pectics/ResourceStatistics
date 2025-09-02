package me.pectics.bukkit.plugin.resourcestatistics;

import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CommandExecutor implements TabExecutor {

    private final ResourceStatistics plugin;

    public CommandExecutor(@NotNull ResourceStatistics plugin) {
        this.plugin = plugin;
        val cmd = plugin.getCommand("stats");
        if (cmd == null) return;
        cmd.setExecutor(this);
        cmd.setTabCompleter(this);
    }

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String @NotNull [] args
    ) {
        if (args.length < 1) return false;
        switch (args[0].toLowerCase()) {
            case "save" -> {
                StatsRecorder.save();
                plugin.getLogger().info("Stats files saved successfully.");
                sender.sendMessage("Statistics saved.");
                return true;
            }
            case "reload" -> {
                Bukkit.getServer().getPluginManager().disablePlugin(plugin);
                Bukkit.getServer().getPluginManager().enablePlugin(plugin);
                plugin.getLogger().info("Stats plugin reloaded successfully.");
                sender.sendMessage("Statistics reloaded.");
                return true;
            }
            default -> {
                sender.sendMessage("Unknown subcommand.");
                return false;
            }
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1) return startsWith(args[0], List.of("save", "reload"));
        return List.of();
    }

    private static List<String> startsWith(String p, @NotNull List<String> all) {
        String s = p == null ? "" : p.toLowerCase();
        return all.stream().filter(x -> x.toLowerCase().startsWith(s)).toList();
    }

}
