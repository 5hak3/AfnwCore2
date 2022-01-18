package net.azisaba.afnw.afnwcore2.commands;

import net.azisaba.afnw.afnwcore2.AfnwCore2;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.logging.Logger;

public class TicketCommand implements CommandExecutor {

    private final JavaPlugin plugin;


    /**
     * 投票周りのシステムを制御する。チケット配布・チケットを使っての配布
     * Development by @meru
     */
    public TicketCommand(JavaPlugin plugin){
        this.plugin = plugin;
    }

    static ChatColor colorYellow = ChatColor.YELLOW;
    static ChatColor colorRed = ChatColor.RED;

    // チケットを作成する。
    private static final ItemStack itemStack = new ItemStack(Material.PAPER, 1);
    static {
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null; // nullの場合は返す
        itemMeta.setDisplayName(colorYellow + "Afnwチケット");
        List<String> Lores = new ArrayList<>();
        Lores.add("投票特典と交換できるアイテムです。インベントリに入れた状態で /afnw を実行すると交換できます。");
        itemMeta.setLore(Lores);
        itemStack.setItemMeta(itemMeta);
    }

    private static final Logger Log = Bukkit.getLogger();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        FileConfiguration config = plugin.getConfig();
        int voteTicketSize = config.getInt("vote.ticket_give_size"); /* /vote_ticket で配布するチケット数 */
        int voteAfnwItemSize = config.getInt("vote.afnw_item_size"); /* /afnw で配布するアイテム数 */
        int voteAfnwScaffoldSize = config.getInt("vote.afnw_scaffold_size"); /* /afnw で配布する足場ブロック数 */

        int i;
        switch (command.getName()) {
            case "ticket": { /* /ticket : チケット配布処理 */
                Player p = Bukkit.getPlayer(args[0]);
                if(p == null) return true;
                Inventory pInv = p.getInventory();

                for (i = 0; i < voteTicketSize; i++) {
                    pInv.addItem(itemStack);
                }

                p.sendMessage("=================");
                p.sendMessage("");
                p.sendMessage(colorYellow + "投票特典として以下のアイテムが付与されました。");
                p.sendMessage("[+] Afnwチケット ×" + voteTicketSize);
                p.sendMessage("");
                p.sendMessage(colorYellow + "この特典とアイテムを交換するには /afnw を実行してください。");
                p.sendMessage("");
                p.sendMessage("=================");
                Log.info("[AfnwCore2] " + p.getName() + "に投票特典が配布されました。");
                break;
            }
            case "ticket#give": {
                if(args.length == 0) {
                    sender.sendMessage(AfnwCore2.commandSenderPrefix + colorRed + " コマンドの構文が正しくありません。\n/ticket#give <target> <size>");
                } else if(args.length == 1) {
                    sender.sendMessage(AfnwCore2.commandSenderPrefix + colorRed + " 配布する対象プレイヤーを指定してください。ただし、Afnwにいるプレイヤーのみを指定できます。");
                } else if(args.length == 2) {
                    sender.sendMessage(AfnwCore2.commandSenderPrefix + colorRed + " 配布する数を指定してください。");
                }

                Player giveP = Bukkit.getPlayer(args[0]);
                int giveSize = Integer.parseInt(args[1]);

                if(giveP == null) {
                    sender.sendMessage(AfnwCore2.commandSenderPrefix + colorRed + " プレイヤーが存在しません。");
                    return true;
                }
                if(giveSize == 0) {
                    sender.sendMessage(AfnwCore2.commandSenderPrefix + colorRed + " 0は指定できません。");
                    return true;
                }

                Inventory giveTarget = giveP.getInventory();
                sender.sendMessage(AfnwCore2.commandSenderPrefix + " " + giveP.getName() + "に" + giveSize + "枚のチケットを配布します。");
                for(i = 0; i < giveSize; i++) {
                    giveTarget.addItem(itemStack);
                }
                sender.sendMessage(AfnwCore2.commandSenderPrefix + colorYellow + " チケットの配布に成功しました。");
                giveP.sendMessage("=================");
                giveP.sendMessage("");
                giveP.sendMessage(colorYellow + "投票特典が補填されました。");
                giveP.sendMessage("[+] Afnwチケット×" + giveSize);
                giveP.sendMessage("");
                giveP.sendMessage(colorYellow + "この特典とアイテムを交換するには /afnw を実行してください。");
                giveP.sendMessage("");
                giveP.sendMessage("=================");
                Log.info("[AfnwCore2] " + sender.getName() + "が" + giveP.getName() + "に" + giveSize + "枚のチケットを配布しました。");
                break;
            }
            case "afnw": {
                Player p = (Player) sender;
                Inventory pInv = p.getInventory();

                if(Arrays.stream(pInv.getContents()).noneMatch(item -> item != null && item.hasItemMeta() && (colorYellow + "Afnwチケット").equals(Objects.requireNonNull(Objects.requireNonNull(item.getItemMeta()).getDisplayName())))) {
                    p.sendMessage(AfnwCore2.commandSenderPrefix + colorRed + " チケットが見つからないため、特典と交換できませんでした。特典と交換するには最低1枚のAfnwチケットが必要です。");
                    Log.warning("[AfnwCore2]" + p.getName() + "がアイテムの交換を受けようとしましたが、チケットがぬるぽのため交換失敗しました。 *");
                    return true;
                }
                if(pInv.firstEmpty() == -1) {
                    p.sendMessage(AfnwCore2.commandSenderPrefix + colorRed + " インベントリがいっぱいのため、配布ができません。整理してから再実行してください。(チケットの消費はされていません。)");
                    return true;
                }

                pInv.removeItem(itemStack);

                // アイテムの配布
                List<Material> materialList = new ArrayList<>(Arrays.asList(Material.values()));
                Collections.shuffle(materialList);
                materialList.removeIf(type -> !isAllowed(type));
                ItemStack itemStack = new ItemStack(materialList.get(0), voteAfnwItemSize);
                ItemStack itemStack1 = new ItemStack(Material.SCAFFOLDING, voteAfnwScaffoldSize);
                pInv.addItem(itemStack, itemStack1);
                p.sendMessage("=================");
                p.sendMessage("");
                p.sendMessage(colorYellow + "Afnwチケットと交換しました。");
                p.sendMessage("[-] Afnwチケット ×1");
                p.sendMessage("[+] 足場ブロック(確定) ×" + voteAfnwItemSize);
                p.sendMessage("[+] " + itemStack.getType() + " ×" + voteAfnwScaffoldSize);
                p.sendMessage("");
                p.sendMessage(colorYellow + "このままチケットの交換を行う際は /afnw を実行してください。");
                p.sendMessage(colorYellow + "ヒント: Java版では矢印キー\"↑\"を押すことで補完することができます。");
                p.sendMessage("");
                p.sendMessage("=================");
                Log.info("[AfnwCore2 / Afnwチケットの交換] " + p.getName() + "がアイテムの交換を行いました。(足場 + " + itemStack.getType() + ")");
                break;
            }
            case "vote#site": {
                sender.sendMessage("=================");
                sender.sendMessage("");
                sender.sendMessage(colorYellow + "◆ Japan Minecraft Servers: ◆");
                sender.sendMessage("https://minecraft.jp/servers/azisaba.net/vote");
                sender.sendMessage(colorYellow + "◆ MonoCraft: ◆");
                sender.sendMessage("https://monocraft.net/servers/xWBVrf1nqB2P0LxlMm2v");
                sender.sendMessage("");
                sender.sendMessage("=================");
            }
        }
        return true;
    }

    private static boolean isAllowed(Material type) {
        if(!type.isItem()) return false;
        switch (type) {
            case BEDROCK:
            case STRUCTURE_BLOCK:
            case STRUCTURE_VOID:
            case COMMAND_BLOCK:
            case CHAIN_COMMAND_BLOCK:
            case COMMAND_BLOCK_MINECART:
            case REPEATING_COMMAND_BLOCK:
            case BARRIER:
            case LIGHT:
            case JIGSAW:
            case END_PORTAL:
            case KNOWLEDGE_BOOK:
            case DEBUG_STICK:
                return false;
            default:
                return true;
        }
    }
}
