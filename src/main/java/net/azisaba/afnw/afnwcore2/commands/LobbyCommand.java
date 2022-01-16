package net.azisaba.afnw.afnwcore2.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

/**
 * ロビー関連のコマンド。
 * Development by @5hak_3
 */
public class LobbyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        final String prefix = "[AfnwCore2] ";

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + prefix + "このコマンドはプレイヤー専用です．");
            return true;
        }

        Player player = (Player)sender;
        World lobby = Objects.requireNonNull(Bukkit.getWorld("lobby"));
        Location point = lobby.getSpawnLocation();

        if (Objects.requireNonNull(player.getWorld()) == lobby) {
            player.sendMessage(ChatColor.RED + prefix + "あなたは既にロビーに接続しています．");
            return true;
        }

        if (player.hasPermission("afnwcore2.lobby.op")) {
            player.sendTitle(ChatColor.YELLOW + "オペレータテレポート","制限を回避してロビーに移動しました．", 3, 60, 1);
            player.sendMessage(ChatColor.YELLOW + prefix + "オペレータテレポートにより待機時間を回避しました．");
            player.teleport(point);
        }
        else {
            int waitSec = 5;
            player.sendMessage(ChatColor.AQUA + prefix + "ロビーへ移動します......" + waitSec + "秒待機してください．");
            Bukkit.getScheduler().runTaskLater(
                    Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("AfnwCore2")),
                    () -> {
                        player.sendTitle(ChatColor.YELLOW + "Screaaam!!!", "ロビーへ移動しました．", 3, 60, 1);
                        player.sendMessage(ChatColor.YELLOW + prefix + "ロビーへ移動しました．");
                        player.teleport(point);
                    },
                    20 * waitSec
            );
        }
        return true;
    }
}
