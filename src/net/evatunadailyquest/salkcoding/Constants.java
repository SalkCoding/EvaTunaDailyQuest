package net.evatunadailyquest.salkcoding;

import net.evatunadailyquest.salkcoding.main.Main;
import net.evatunadailyquest.salkcoding.quest.QuestEvent;
import net.evatunadailyquest.salkcoding.script.Script;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public final class Constants {

    private Constants() {
    }

    public static final String Console_Format = "[EvaTunaDailyQuest] ";
    public static final String GUI_Name = "Daily quest";
    public static final String Info_Format = ChatColor.RESET + "" + ChatColor.WHITE + ChatColor.ITALIC + "[ " + ChatColor.GREEN + ChatColor.ITALIC + "!" + ChatColor.WHITE + ChatColor.ITALIC + " ] " + ChatColor.GRAY + ChatColor.ITALIC;
    public static final String Warn_Format = ChatColor.RESET + "" + ChatColor.WHITE + ChatColor.ITALIC + "[ " + ChatColor.YELLOW + ChatColor.ITALIC + "!" + ChatColor.WHITE + ChatColor.ITALIC + " ] " + ChatColor.GRAY + ChatColor.ITALIC;
    public static final String Error_Format = ChatColor.RESET + "" + ChatColor.WHITE + ChatColor.ITALIC + "[ " + ChatColor.RED + ChatColor.ITALIC + "!" + ChatColor.WHITE + ChatColor.ITALIC + " ] " + ChatColor.GRAY + ChatColor.ITALIC;

    public static final File script_dir = new File(Main.getInstance().getDataFolder(), "Script");
    public static final File timemap_dir = new File(Main.getInstance().getDataFolder(), "PlayerData");

    public static void sendPercentage(Player player, Script script, QuestEvent event, int added) {
        double percentage = (event.getProgress() / event.getCondition() * 100f);
        double printPercentage = (event.getCondition() * 0.1f);
        if (percentage >= 100)
            return;
        if (printPercentage <= added)
            player.sendMessage(Constants.Info_Format + script.getQuestName() + ChatColor.GRAY + ChatColor.ITALIC + "의 현재 진행률 : " + String.format("%.1f%%", percentage));
    }

    public static int getEmptySlot(Player p, ItemStack picked) {
        ItemStack[] cont = p.getInventory().getContents();
        int i = 0;
        for (ItemStack item : cont) {
            if (item == null)
                continue;
            if (item.getType() != Material.AIR && (item.getType() != picked.getType() || (item.getAmount() + picked.getAmount()) > 64))
                i++;
        }
        //p.sendMessage(36 - i + "");
        return 36 - i;
    }

}
