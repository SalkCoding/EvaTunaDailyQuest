package net.evatunadailyquest.salkcoding.script.specificscript;

import net.evatunadailyquest.salkcoding.quest.QuestEvent;
import net.evatunadailyquest.salkcoding.script.Script;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.List;

public class PickupScript extends Script {

    private HashSet<Material> itemSet;

    public PickupScript(String name, List<String> GUILore, List<String> clearMessages, HashSet<Material> itemSet, QuestEvent event, List<String> commands, List<ItemStack> rewards) {
        super(name, GUILore, clearMessages, event, commands, rewards);
        this.itemSet = itemSet;
    }

    public boolean isContainInSet(Material type) {
        return itemSet.contains(type);
    }

}
