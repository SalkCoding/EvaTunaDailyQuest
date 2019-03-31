package net.evatunadailyquest.salkcoding.script.specificscript;

import net.evatunadailyquest.salkcoding.quest.QuestEvent;
import net.evatunadailyquest.salkcoding.script.Script;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.List;

public class MythicMobKillScript extends Script {

    private HashSet<String> entityTypeSet;

    public MythicMobKillScript(String name, List<String> GUILore, List<String> clearMessages, HashSet<String> entityTypeSet, QuestEvent event, List<String> commands, List<ItemStack> rewards) {
        super(name, GUILore, clearMessages, event, commands, rewards);
        this.entityTypeSet = entityTypeSet;
    }

    public boolean isContainInSet(String type){
        return entityTypeSet.contains(type);
    }

}
