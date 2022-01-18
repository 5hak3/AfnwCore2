package net.azisaba.afnw.afnwcore2.commands;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * ロビー関連のコマンド。
 * Development by @5hak_3
 */
public class LobbyCommand implements CommandExecutor {
    private final JavaPlugin plugin;

    public LobbyCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        final String prefix = "[AfnwCore2] ";

        // これらのコマンドはプレイヤー用なのでそれ以外は弾く
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + prefix + "このコマンドはプレイヤー専用です．");
            return true;
        }

        Player player = (Player)sender;

        // setvoteurlblockは投票URL表示ブロックを設置する
        // Lobbyでこれを置くとMetaDataを設定するようにする
        if (command.getName().equalsIgnoreCase("setvoteurlblock")) {
            this.setVoteUrlBlock();
            return true;
        }
        else if (!command.getName().equalsIgnoreCase("lobby")) return true;

        // lobbyコマンドはロビー以外にいるプレイヤーをロビーに飛ばす
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
            // admin以外は待機時間を取る（5秒にしてある）
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

    public void setVoteUrlBlock () {
        World lobby = Objects.requireNonNull(Bukkit.getWorld("lobby"));
        // 地獄みたいな文 (気が向いたら直して)
        List<Integer> locarr = Arrays.stream(Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(this.plugin.getConfig().getConfigurationSection("lobby")).getConfigurationSection("voteblock")).getString("location")).split(",", 3)).map(Integer::parseInt).collect(Collectors.toList());
        Location ploc = new Location(lobby, locarr.get(0), locarr.get(1), locarr.get(2));

        lobby.getBlockAt(ploc).setType(Material.EMERALD_BLOCK);
        lobby.getBlockAt(ploc).setMetadata("management", new FixedMetadataValue(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("AfnwCore2")), "getvoteurl"));
    }
}
