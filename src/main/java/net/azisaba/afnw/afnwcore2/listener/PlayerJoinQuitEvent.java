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

import com.viaversion.viaversion.api.Via;

public class PlayerJoinQuitEvent implements Listener {

    private final JavaPlugin plugin;

    /**
     * 入室退出処理
     * Development by @meru
     */
    public PlayerJoinQuitEvent(JavaPlugin plugin){
        this.plugin = plugin;
    }

    ChatColor colorPurple = ChatColor.LIGHT_PURPLE;
    ChatColor colorRed = ChatColor.RED;
    ChatColor colorYellow = ChatColor.YELLOW;
    ChatColor colorWhite = ChatColor.WHITE;
    Class<AfnwCore2> mainJava = AfnwCore2.class;

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        // 備考: 参加時にLobbyに飛ばす処理はLobby#onJoin()で対応

        Player p = e.getPlayer();
        String pName = e.getPlayer().getName();

        FileConfiguration config = plugin.getConfig();
        String notice = config.getString("notice");

        // 全員共通

        e.setJoinMessage(colorYellow + pName + colorWhite + "がログインしました。");
        new BukkitRunnable() {
            @Override
            public void run() {
                p.sendMessage(colorYellow + "◆ A Fall New Worldへようこそ ◆");
                p.sendMessage("オープンベータ実施中。予期せぬ不具合等が発生する可能性があり、ベータ終了後はプレイヤーデータは削除されます。"); //TODO: オープンベータ終了後削除
                p.sendMessage(colorYellow + "バージョン: " + colorWhite + plugin.getDescription().getVersion());
                p.sendMessage(colorYellow + "お知らせ: " + colorWhite + notice);
            }
        }.runTaskLater(JavaPlugin.getPlugin(mainJava), 20*10);
        p.sendTitle(colorPurple + "Afnw", "オープンベータ", 3, 60, 1);

        // viaverでバージョン確認して1.17(775)未満だったら警告
        if (Via.getAPI().getPlayerVersion(p.getUniqueId()) < 775) {
            p.sendMessage(colorRed + "A Fall New Worldの推奨バージョンは1.17以上です。");
            p.sendMessage(colorRed + "推奨バージョン未満で接続していることにより生じた問題はサポート対象外です。");
        }

        // BEユーザーのみ

        if(!(pName.startsWith("."))) return;
        /*
         * 他のプラグインのメッセージに警告が流されてしまうので "全員共通" のように12tick遅く送信させる
         * 備考: https://github.com/AfnwTeam/AfnwCore2/issues/2
         */
        new BukkitRunnable() {
            @Override
            public void run() {
                p.sendMessage(colorRed + "【警告】\nMinecraft Bedrock Edition(統合版)クライアントで接続していることを検知しました。\nAfnwではBedrock Editionでのプレイをサポートしていません。(非推奨)\nプレイする際はJava Editionを使用することをお勧めします。");
            }
        }.runTaskLater(JavaPlugin.getPlugin(mainJava), 20*12);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        String pName = e.getPlayer().getName();
        e.setQuitMessage(colorYellow + pName + colorWhite + "がログアウトしました。");
    }
}
