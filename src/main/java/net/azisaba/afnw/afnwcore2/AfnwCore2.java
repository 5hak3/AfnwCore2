package net.azisaba.afnw.afnwcore2;

import net.azisaba.afnw.afnwcore2.commands.*;
import net.azisaba.afnw.afnwcore2.listener.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public final class AfnwCore2 extends JavaPlugin {

    /**
     * コマンドの実行結果時に表示するprefix
     */
    public static String commandSenderPrefix = "[AfnwCore]";

    @Override
    public void onEnable() {
        getLogger().info("起動を開始します。");

        // コマンド登録
        Objects.requireNonNull(getCommand("ticket")).setExecutor(new TicketCommand(this));
        Objects.requireNonNull(getCommand("ticket#give")).setExecutor(new TicketCommand(this));
        Objects.requireNonNull(getCommand("afnw")).setExecutor(new TicketCommand(this));
        Objects.requireNonNull(getCommand("lobby")).setExecutor(new LobbyCommand());
        Objects.requireNonNull(getCommand("setvoteurlblock")).setExecutor(new LobbyCommand());
        Objects.requireNonNull(getCommand("vote#site")).setExecutor(new TicketCommand(this));
        getLogger().warning("コマンドを登録しました。");

        // イベント登録
        getServer().getPluginManager().registerEvents(new PlayerJoinQuitEvent(this), this);
        getServer().getPluginManager().registerEvents(new AFKTeleporter(), this);
        getServer().getPluginManager().registerEvents(new Lobby(), this);

        // configの設定
        saveDefaultConfig();
        getLogger().warning("Configを設定しました。");


        getLogger().info("起動が完了しました。");
    }

    @Override
    public void onDisable() { getLogger().info("終了しました。"); }
}
