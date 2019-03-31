package net.evatunadailyquest.salkcoding.script.specificscript;

import net.evatunadailyquest.salkcoding.quest.QuestEvent;
import net.evatunadailyquest.salkcoding.script.Script;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.List;

public class PlaceScript extends Script {

    private HashSet<Material> blockSet;

    public PlaceScript(String name, List<String> GUILore, List<String> clearMessages, HashSet<Material> blockSet, QuestEvent event, List<String> commands, List<ItemStack> rewards) {
        super(name, GUILore, clearMessages, event, commands, rewards);
        this.blockSet = blockSet;
    }

    public boolean isContainInSet(Material type) {
        return blockSet.contains(type);
    }

}
