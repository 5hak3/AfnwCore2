package net.azisaba.afnw.afnwcore2.listener;

import net.azisaba.afnw.afnwcore2.AfnwCore2;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerJoinQuitEvent implements Listener {

    private final JavaPlugin plugin;
    public PlayerJoinQuitEvent(JavaPlugin plugin){
        this.plugin = plugin;
    }

    ChatColor colorPurple = ChatColor.LIGHT_PURPLE;
    ChatColor colorRed = ChatColor.RED;
    ChatColor colorYellow = ChatColor.YELLOW;
    ChatColor colorWhite = ChatColor.WHITE;

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        String pName = e.getPlayer().getName();

        FileConfiguration config = plugin.getConfig();
        String notice = config.getString("notice");

        // 全員共通

        e.setJoinMessage(colorYellow + "[+] " + colorYellow + pName + colorWhite + "がログインしました。");
        new BukkitRunnable() {
            @Override
            public void run() {
                p.sendMessage(colorYellow + "◆ A Fall New Worldへようこそ ◆");
                p.sendMessage("オープンベータ実施中。予期せぬ不具合等が発生する可能性があり、ベータ終了後はプレイヤーデータは削除されます。"); //TODO: オープンベータ終了後削除
                p.sendMessage(colorYellow + "バージョン: " + colorWhite + plugin.getDescription().getVersion());
                p.sendMessage(colorYellow + "お知らせ: " + colorWhite + notice);
            }
        }.runTaskLater(JavaPlugin.getPlugin(AfnwCore2.class), 20*10);
        p.sendTitle(colorPurple + "Afnw", "オープンベータ", 3, 60, 1);

        // BEユーザーのみ

        if(!(pName.startsWith("."))) return;
        p.sendMessage(colorRed + "Minecraft Bedrock Edition(統合版)クライアントで接続していることを検知しました。\nAfnwではBedrock Editionでのプレイをサポートしていません。(非推奨)\nプレイする際はJava Editionからどうぞ。");
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        String pName = e.getPlayer().getName();
        e.setQuitMessage(colorYellow + "- " + colorYellow + pName + colorWhite + "がログインしました。");
    }
}