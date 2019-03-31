package net.evatunadailyquest.salkcoding.script.specificscript;

import net.evatunadailyquest.salkcoding.quest.QuestEvent;
import net.evatunadailyquest.salkcoding.script.Script;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PlayScript extends Script {

    private long playTick;

    public PlayScript(String name, List<String> GUILore, List<String> clearMessages, QuestEvent event, List<String> commands, List<ItemStack> rewards) {
        super(name, GUILore, clearMessages, event, commands, rewards);
        playTick = 0L;
    }

    public void addPlayTick(long tick) {
        this.playTick += tick;
    }

    public long getPlayTick() {
        return playTick;
    }
}
