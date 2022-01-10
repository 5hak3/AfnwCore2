package net.azisaba.afnw.afnwcore2;

import net.azisaba.afnw.afnwcore2.commands.*;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class AfnwCore2 extends JavaPlugin {

    /**
     * コマンドの実行結果時に表示するprefix
     */
    public static String commandSenderPrefix = "[AfnwCore]";
    public static ChatColor colorYellow = ChatColor.YELLOW;
    public static ChatColor colorRed = ChatColor.RED;

    @Override
    public void onEnable() {
        getLogger().info("起動を開始します。");

        // コマンド登録
        Objects.requireNonNull(getCommand("ticket")).setExecutor(new TicketCommand(this));
        Objects.requireNonNull(getCommand("ticket#give")).setExecutor(new TicketCommand(this));
        Objects.requireNonNull(getCommand("afnw")).setExecutor(new TicketCommand(this));
        Objects.requireNonNull(getCommand("vote")).setExecutor(new TicketCommand(this));
        getLogger().warning("コマンドを登録しました。");

        // configの設定
        saveDefaultConfig();
        getLogger().warning("Configを設定しました。");


        getLogger().info("起動が完了しました。");
    }

    @Override
    public void onDisable() { getLogger().info("正常に終了しました。"); }
}
