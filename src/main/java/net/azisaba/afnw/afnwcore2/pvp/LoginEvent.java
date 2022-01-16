package net.azisaba.afnw.afnwcore2.pvp;

import net.azisaba.afnw.afnwcore2.AfnwCore2;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.Map;
import java.util.UUID;

import static org.bukkit.GameMode.SURVIVAL;

public class LoginEvent implements Listener {

    ChatColor colorAquaBule = ChatColor.AQUA;
    Map<UUID, Integer> playerData = AfnwCore2.pMap;

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        Player p = e.getPlayer();
        UUID pUUID = e.getPlayer().getUniqueId();

        // データが保存されていないなら、保存
        if(playerData.containsKey(pUUID)) {
            p.sendMessage(colorAquaBule + "プレイヤーデータが存在していなかったため、作成しました。");
            playerData.put(pUUID, 5);
        } else {
            p.sendMessage(colorAquaBule + "プレイヤーデータを確認しました。");
        }

        // 非OPでサバイバルモードじゃなかったら強制変更
        if(!(p.hasPermission("afnwcore2.gamemode.change"))) return;
        if(!(p.getGameMode() == SURVIVAL)) return;
        p.setGameMode(SURVIVAL);
    }
}
