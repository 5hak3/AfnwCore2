package net.azisaba.afnw.afnwcore2.commands;

import net.azisaba.afnw.afnwcore2.AfnwCore2;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class ConfigReloadCommand implements CommandExecutor {

    private final JavaPlugin plugin;

    /**
     * configのリロード機構
     * Development by @meru
     */
    public ConfigReloadCommand(JavaPlugin plugin) { this.plugin = plugin; }

    ChatColor colorAqua = ChatColor.AQUA;
    ChatColor colorYellow = ChatColor.YELLOW;
    ChatColor colorRed = ChatColor.RED;
    Logger log = Bukkit.getLogger();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equals("reload#config")) {
            String prefix = AfnwCore2.commandSenderPrefix;
            sender.sendMessage(prefix + colorAqua + "設定ファイルを再読込します。");
            try {
                plugin.reloadConfig();
                sender.sendMessage(prefix + colorYellow + "正常に完了しました。");
                log.info(colorYellow + sender.getName() + "による設定ファイル再読込完了");
            } catch (Error e) {
                sender.sendMessage(prefix + colorRed + "Unexpected error reported from \"ConfigReloadCommand\".");
                sender.sendMessage(prefix + colorRed + "再読込に失敗しました。開発者である\"める\"にこのことを説明してください。");
                log.warning(colorRed + sender.getName() + "による設定ファイル再読込失敗");}
        }
        return true;
    }
}
