package net.azisaba.afnw.afnwcore2.listener;

import org.bukkit.ChatColor;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;

/**
 * 壊さないようにするかんじのやつら
 */
public class AntiBreakEvent implements Listener {
    /**
     * 成長しきっていない農作物と農作物の植えられたブロックの破壊を阻害する．
     * 成長しきっていれば阻害しない．
     *
     * @param event BlockBreak
     */
    @EventHandler
    public void onCropBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block target = event.getBlock();
        Block targetOn = target.getLocation().add(0, 1, 0).getBlock();
        String msg = "[AfnwCore2] ";

        if (Tag.CROPS.isTagged(target.getType())) {
            msg += "一度植えた成長しきっていない農作物は破壊できません。";
        }
        else if (Tag.CROPS.isTagged(targetOn.getType())) {
            msg += "農作物が植えられている耕地は破壊できません。";
            target = targetOn;
        }
        else return;

        Ageable crop = (Ageable) target.getBlockData();
        if (crop.getAge() == crop.getMaximumAge()) return;

        player.sendMessage(ChatColor.RED + msg);

        event.setCancelled(true);
    }

    /**
     * 成長しきっていない農作物に対し何かしらが流れ込むのを阻害する．
     * 成長しきっていれば阻害しない．
     *
     * @param event BlockFromTo
     */
    @EventHandler
    public void onFlowing2Crop(BlockFromToEvent event) {
        Block target = event.getToBlock();

        if (!(Tag.CROPS.isTagged(target.getType()) &&
              (target.getBlockData() instanceof Ageable))) return;

        Ageable crop = (Ageable) target.getBlockData();
        if (crop.getAge() == crop.getMaximumAge()) return;

        event.setCancelled(true);
    }
}
