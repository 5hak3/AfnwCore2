package net.azisaba.afnw.afnwcore2.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

/**
 * 便利なコマンドたち
 */
public class UtilCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // Player判定
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "このコマンドはプレイヤー専用です．");
            return true;
        }

        // コマンドを判断していい感じにする
        Player player = (Player)sender;
        switch (command.getName()) {
            case "trash":
                player.openInventory(
                        Bukkit.createInventory(
                            null, 9*6,
                            ChatColor.AQUA + "ゴミ箱 " + ChatColor.RED + "アイテムの消滅に注意")
                );
                break;

            case "ec":
            case "pc":
                player.openInventory(
                        player.getEnderChest()
                );
                break;

            default: return false;
        }
        return true;
    }
}
