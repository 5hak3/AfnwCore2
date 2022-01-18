package net.azisaba.afnw.afnwcore2.listener;

import net.ess3.api.events.AfkStatusChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.Objects;

/**
 * EssentialsXでAFKになったら所定の座標に飛ばすやつ
 */
public class AFKTeleporter implements Listener {
    /**
     * AfkStatusChangeEventはなんかいい感じにアレ
     * @param event Essentialsのやつ
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAFK (AfkStatusChangeEvent event) {
        World lobby = Bukkit.getWorld("lobby");
        if (Objects.isNull(lobby)) return;
        // Config作るのめんどいからハードコードしてもいいよね？
        Location point = new Location(lobby, 44, 61, 5);

        // adminはテレポートしない
        if (event.getAffected().getBase().hasPermission("afnwcore2.lobby.op"))
            return;

        // どうやらChangeEventの前の状態を参照してるっぽい
        if (!event.getAffected().isAfk()) {
            event.getAffected().getBase().sendMessage(ChatColor.AQUA + "[AfnwCore2] AFKになったためテレポートしました．");
            event.getAffected().getBase().teleport(point);
        }
    }
}
