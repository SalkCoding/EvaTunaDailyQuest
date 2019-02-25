package net.evatunadailyquest.salkcoding.questevent;

import net.evatunadailyquest.salkcoding.Constants;
import net.evatunadailyquest.salkcoding.quest.QuestEvent;
import net.evatunadailyquest.salkcoding.quest.QuestType;
import net.evatunadailyquest.salkcoding.script.Script;
import net.evatunadailyquest.salkcoding.script.ScriptManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PickupItem implements Listener {

    @EventHandler
    public void onPick(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;
        Player player = (Player) event.getEntity();
        if (ScriptManager.getPlayerDailyQuests(player) == null)
            return;
        List<Script> list = ScriptManager.getPlayerDailyQuests(player);
        for (Script script : list) {
            if (script.isClear())
                continue;
            ItemStack item = event.getItem().getItemStack();
            QuestEvent questEvent = script.getQuestEvent();
            if (questEvent.getType() == QuestType.PICKUP_ITEM && questEvent.getObjectiveTypes().contains(item.getType())) {
                if (ScriptManager.getDrop(player.getUniqueId(), item) < 0 || Constants.getEmptySlot(player, item) <= 0)
                    return;
                int added = ScriptManager.getDrop(player.getUniqueId(), item);
                double progress = questEvent.getProgress() + added;
                if (questEvent.getCondition() <= progress) script.clear(player);
                else questEvent.setProgress(progress);
                Constants.sendPercentage(player, script, questEvent, added);
                ScriptManager.removeDrop(player.getUniqueId(), item);
                return;
            }
        }
    }

}
