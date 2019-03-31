package net.evatunadailyquest.salkcoding.script.specificscript;

import net.evatunadailyquest.salkcoding.quest.QuestEvent;
import net.evatunadailyquest.salkcoding.script.Script;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.List;

public class KillScript extends Script {

    private HashSet<EntityType> entityTypeSet;

    public KillScript(String name, List<String> GUILore, List<String> clearMessages, HashSet<EntityType> entityTypeSet, QuestEvent event, List<String> commands, List<ItemStack> rewards) {
        super(name, GUILore, clearMessages, event, commands, rewards);
        this.entityTypeSet = entityTypeSet;
    }

    public boolean isContainInSet(EntityType type){
        return entityTypeSet.contains(type);
    }

}
