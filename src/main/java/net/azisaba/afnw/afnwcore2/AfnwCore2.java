package net.azisaba.afnw.afnwcore2;

import net.azisaba.afnw.afnwcore2.commands.*;
import net.azisaba.afnw.afnwcore2.listener.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class AfnwCore2 extends JavaPlugin {

    /**
     * コマンドの実行結果時に表示するprefix
     */
    public static String commandSenderPrefix = "[AfnwCore]";
    public static Map<UUID, Integer> pMap = new HashMap<>();
    public static Map<String, Boolean> stateMap = new HashMap<>();

    @Override
    public void onEnable() {
        getLogger().info("起動を開始します。");

        // コマンド登録
        Objects.requireNonNull(getCommand("ticket")).setExecutor(new TicketCommand(this));
        Objects.requireNonNull(getCommand("ticket#give")).setExecutor(new TicketCommand(this));
        Objects.requireNonNull(getCommand("afnw")).setExecutor(new TicketCommand(this));
        Objects.requireNonNull(getCommand("lobby")).setExecutor(new LobbyCommand());
        Objects.requireNonNull(getCommand("vote#site")).setExecutor(new TicketCommand(this));
        Objects.requireNonNull(getCommand("reload#config")).setExecutor(new ConfigReloadCommand(this));
        getLogger().warning("コマンドを登録しました。");

        // イベント登録
        getServer().getPluginManager().registerEvents(new PlayerJoinQuitEvent(this), this);
        getServer().getPluginManager().registerEvents(new AFKTeleporter(), this);
        getServer().getPluginManager().registerEvents(new Lobby(), this);

        // 状態登録
        if(stateMap.containsValue(true) || stateMap.containsValue(null)) {
            stateMap.put("戦闘状態", false);
        }

        // configの設定
        saveDefaultConfig();
        getLogger().warning("Configを設定しました。");


        getLogger().info("起動が完了しました。");
    }

    @Override
    public void onDisable() { getLogger().info("終了しました。"); }
}
