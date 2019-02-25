package net.evatunadailyquest.salkcoding.GUI;

import net.evatunadailyquest.salkcoding.Constants;
import net.evatunadailyquest.salkcoding.quest.QuestEvent;
import net.evatunadailyquest.salkcoding.script.Script;
import net.evatunadailyquest.salkcoding.script.ScriptManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GUICreator {

    public static void createGUI(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, Constants.GUI_Name);

        ItemStack decoration = new ItemStack(Material.PINK_STAINED_GLASS_PANE);
        ItemMeta meta = decoration.getItemMeta();
        meta.setDisplayName(" ");
        decoration.setItemMeta(meta);
        for (int i = 0; i < 27; i++) {
            inv.setItem(i, decoration);
        }
        ItemStack reset = new ItemStack(Material.FEATHER);
        ItemMeta resetMeta = reset.getItemMeta();
        resetMeta.setDisplayName(ChatColor.WHITE + "[" + ChatColor.YELLOW + "퀘스트 리셋" + ChatColor.WHITE + "]");
        List<String> resetLore = new ArrayList<>();
        if (!ScriptManager.didPlayerReset(player) && !ScriptManager.hasClearData(player)) {
            resetLore.add(ChatColor.GRAY + "- " + ChatColor.RED + "하루에 한번 일일퀘스트를 다시 받을 수 있습니다.");
            resetLore.add(ChatColor.GRAY + "- " + ChatColor.RED + "퀘스트를 1개라도 달성하면 초기화를 할 수  없습니다.");
            resetLore.add(ChatColor.GRAY + "- " + ChatColor.RED + "한번 초기화하면 되돌릴 수 없습니다.");
        } else {
            resetLore.add(ChatColor.GRAY + "- " + ChatColor.RED + "이미 초기화를 하셨습니다.");
        }
        resetMeta.setLore(resetLore);
        reset.setItemMeta(resetMeta);
        inv.setItem(26, reset);
        int i = 10;
        for (Script script : ScriptManager.getPlayerDailyQuests(player)) {
            ItemStack quest = new ItemStack(Material.PAPER);
            ItemMeta questMeta = quest.getItemMeta();
            questMeta.setDisplayName(script.getQuestName());
            if (script.isClear()) {
                questMeta.addEnchant(Enchantment.LURE, 0, true);
                questMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            QuestEvent event = script.getQuestEvent();
            int condition = event.getCondition();
            double progress = event.getProgress();
            List<String> lore = script.getGUILore();
            lore.add(ChatColor.WHITE + "[ " + ChatColor.GREEN + "!" + ChatColor.WHITE + " ] " + ChatColor.GRAY + "진행률 : " + String.format("%.1f", progress) + "/" + condition + "(" + String.format("%.1f", progress / (double) condition * 100f) + "%)");//임시추가
            questMeta.setLore(lore);
            quest.setItemMeta(questMeta);
            inv.setItem(i, quest);
            lore.remove(lore.size() - 1);//삭제
            i += 2;
        }
        player.openInventory(inv);
    }

}
