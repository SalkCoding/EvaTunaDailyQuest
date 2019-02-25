package net.evatunadailyquest.salkcoding.script;

import net.evatunadailyquest.salkcoding.Constants;
import net.evatunadailyquest.salkcoding.quest.QuestEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Script implements Cloneable {

    private String name;
    private List<String> GUILore;
    private List<String> clearMessages;
    private QuestEvent event;
    private List<String> commands;
    private List<ItemStack> rewards;
    private boolean clear = false;

    public Script(String name, List<String> GUILore, List<String> clearMessages, QuestEvent event, List<String> commands, List<ItemStack> rewards) {
        this.name = name;
        this.GUILore = GUILore;
        this.clearMessages = clearMessages;
        this.event = event;
        this.commands = commands;
        this.rewards = rewards;
    }

    public String getQuestName() {
        return name;
    }

    public List<String> getGUILore() {
        return GUILore;
    }

    public List<String> getClearMessages() {
        return clearMessages;
    }

    public QuestEvent getQuestEvent() {
        return event;
    }

    public List<String> getCommands() {
        return commands;
    }

    public List<ItemStack> getRewards() {
        return rewards;
    }

    public boolean isClear() {
        return clear;
    }

    public void clear(Player player) {
        for (String message : getClearMessages())
            player.sendMessage(Constants.Info_Format + message);
        for (String command : getCommands())
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("<name>", player.getName()));
        for (ItemStack item : getRewards()) player.getInventory().addItem(item);
        QuestEvent event = getQuestEvent();
        event.setProgress(event.getCondition());
        clear = true;
    }

    public void clear() {
        QuestEvent event = getQuestEvent();
        event.setProgress(event.getCondition());
        clear = true;
    }

    public void setClear(boolean b) {
        clear = false;
    }

    @Override
    public Script clone() throws CloneNotSupportedException {
        Script clone = (Script) super.clone();
        clone.event = new QuestEvent(clone.getQuestEvent().getType(), clone.getQuestEvent().getObjectiveTypes(), clone.getQuestEvent().getCondition());
        return clone;
    }

}
