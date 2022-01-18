package net.azisaba.afnw.afnwcore2.listener;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Objects;

/**
 * Lobbyに関連するListenerを集めたもの
 */
public class Lobby implements Listener {
    /**
     * Message等に付与するPrefix
     */
    private static final String prefix = "[Afnw Core2] ";

    /**
     * プレイヤーがログインしたら権限を確認してロビーに飛ばす
     * @param event PJoinEv
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogin (PlayerJoinEvent event) {
        // ロビーのワールドを取得してスポーン地点を取得しておく，"lobby"がなければおわおわり
        World lobby = Bukkit.getWorld("lobby");
        if (Objects.isNull(lobby)) return;
        Location point = lobby.getSpawnLocation();

        // Adminじゃなかったらロビーのスポーンに飛ばす
        Player player = event.getPlayer();
        if (player.hasPermission("afnwcore2.lobby.op"))
            Bukkit.getScheduler().runTaskLater(
                    Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("AfnwCore2")),
                    () -> {
                        player.sendTitle(ChatColor.YELLOW + "オペレータリスタート","オペレータのため，前回と同じ地点からスタートになります．", 3, 60, 1);
                        player.sendMessage(ChatColor.YELLOW + prefix + "オペレータリスタート機能が発動しました．オペレータのため，前回と同じ地点からスタートになります．");
                    }
            , 20 * 6);
        else
            player.teleport(point);
    }

    /**
     * ロビーでブロック破壊を阻害する
     * @param event BlockBEv
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak (BlockBreakEvent event) {
        // admin判定
        if (event.getPlayer().hasPermission("afnwcore2.lobby.op")) return;

        // ワールド判定
        World lobby = Bukkit.getWorld("lobby");
        if (Objects.isNull(lobby)) return;
        if (event.getPlayer().getWorld() != lobby) return;

        event.setCancelled(true);
        event.getPlayer().sendMessage(ChatColor.RED + prefix + "ロビー内ではブロックの破壊はできません．");
    }

    /**
     * ロビーでブロック設置を阻害する
     * @param event BlockPEv
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace (BlockPlaceEvent event) {
        // admin判定
        if (event.getPlayer().hasPermission("afnwcore2.lobby.op")) return;

        // ワールド判定
        World lobby = Bukkit.getWorld("lobby");
        if (Objects.isNull(lobby)) return;
        if (event.getPlayer().getWorld() != lobby) return;

        event.setCancelled(true);
        event.getPlayer().sendMessage(ChatColor.RED + prefix + "ロビー内ではブロックの設置はできません．");
    }

    /**
     * ロビーでバケツを空にするのを阻害する
     * @param event PBucketEEv
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBucketEmpty (PlayerBucketEmptyEvent event) {
        // admin判定
        if (event.getPlayer().hasPermission("afnwcore2.lobby.op")) return;

        // ワールド判定
        World lobby = Bukkit.getWorld("lobby");
        if (Objects.isNull(lobby)) return;
        if (event.getPlayer().getWorld() != lobby) return;

        event.setCancelled(true);
        event.getPlayer().sendMessage(ChatColor.RED + prefix + "ロビー内ではバケツの操作はできません．");
    }

    /**
     * ロビーで右クリックで物を使ったりするのを阻害する
     * @param event PInterEv
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRightClick (PlayerInteractEvent event) {
        // admin判定
        if (event.getPlayer().hasPermission("afnwcore2.lobby.op")) return;

        // ワールド判定
        World lobby = Bukkit.getWorld("lobby");
        if (Objects.isNull(lobby)) return;
        if (event.getPlayer().getWorld() != lobby) return;

        // InteractしたアイテムがNULLかAIRの時終了
        if (Objects.isNull(event.getItem()) || event.getItem().getType().isAir()) return;

        event.setCancelled(true);
        event.getPlayer().sendMessage(ChatColor.RED + prefix + "ロビー内ではその操作はできません．");
    }

    /**
     * ロビーでMetadataに特定の値を持つブロックを破壊し始めたときの処理
     * @param event BDamageEv
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockDamage (BlockDamageEvent event) {
        // Interact対象のブロックが投票URL表示ブロックならvote#siteコマンドを実行させる
        Block clickedBlock = event.getBlock();

        // クリックされたブロックがエメラルドでmanagementにgetvoteurlを持つなら"vote#site"を実行させる
        if (clickedBlock.getType() == Material.EMERALD_BLOCK &&
            ((String)(clickedBlock.getMetadata("management").get(0).value())).equalsIgnoreCase("getvoteurl")) {
            event.getPlayer().performCommand("vote#site");
            return;
        }
    }

    /**
     * ロビーで物を捨てることを阻害する
     * @param event PDropItemEv
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDrop (PlayerDropItemEvent event) {
        // admin判定
        if (event.getPlayer().hasPermission("afnwcore2.lobby.op")) return;

        // ワールド判定
        World lobby = Bukkit.getWorld("lobby");
        if (Objects.isNull(lobby)) return;
        if (event.getPlayer().getWorld() != lobby) return;

        event.setCancelled(true);
        event.getPlayer().sendMessage(ChatColor.RED + prefix + "ロビー内ではアイテムを落とせません．");
    }

    /**
     * ロビーで物を拾うことを阻害する
     * @param event EntityPickItemEv
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPick (EntityPickupItemEvent event) {
        // 対象がPlayerじゃない時Return
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player)(event.getEntity());

        // admin判定
        if (player.hasPermission("afnwcore2.lobby.op")) return;

        // ワールド判定
        World lobby = Bukkit.getWorld("lobby");
        if (Objects.isNull(lobby)) return;
        if (player.getWorld() != lobby) return;

        event.setCancelled(true);
        player.sendMessage(ChatColor.RED + prefix + "ロビー内ではアイテムを拾えません．");
    }

    /**
     * ロビーでアイテムフレームから物を外せなくする
     * @param event
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHanging (EntityDamageByEntityEvent event) {
        // DamageがPlayerによるものか確かめる
        if (!(event.getDamager() instanceof Player)) return;
        Player damager = (Player)(event.getDamager());

        // Damageを受けたEntityがPaintingかItemFrameかを確かめる
        Entity entity = event.getEntity();
        if (!(entity instanceof Painting) &&
            !(entity instanceof ItemFrame)) return;

        // admin判定
        if (damager.hasPermission("afnwcore2.lobby.op")) return;

        // ワールド判定
        World lobby = Bukkit.getWorld("lobby");
        if (Objects.isNull(lobby)) return;
        if (damager.getWorld() != lobby) return;

        event.setCancelled(true);
        damager.sendMessage(ChatColor.RED + prefix + "ロビー内ではこの操作はできません．");
    }
}
