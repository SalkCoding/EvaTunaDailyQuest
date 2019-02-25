package net.evatunadailyquest.salkcoding.questevent;

import net.evatunadailyquest.salkcoding.Constants;
import net.evatunadailyquest.salkcoding.main.Main;
import net.evatunadailyquest.salkcoding.quest.QuestEvent;
import net.evatunadailyquest.salkcoding.quest.QuestType;
import net.evatunadailyquest.salkcoding.script.Script;
import net.evatunadailyquest.salkcoding.script.ScriptManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class KillEntity implements Listener {

    @EventHandler
    public void onKill(EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null)
            return;
        Player player = event.getEntity().getKiller();
        if (ScriptManager.getPlayerDailyQuests(player) == null)
            return;
        List<Script> list = ScriptManager.getPlayerDailyQuests(player);
        for (Script script : list) {
            if (script.isClear())
                continue;
            LivingEntity entity = event.getEntity();
            QuestEvent questEvent = script.getQuestEvent();

            if (script.getQuestEvent().getType() == QuestType.PICKUP_ITEM) {
                boolean added = false;
                for (ItemStack item : event.getDrops()) {
                    if (questEvent.getObjectiveTypes().contains(item.getType())) {
                        ScriptManager.addDrop(player.getUniqueId(), item);
                        added = true;
                    }
                }
                if (added) Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getInstance(), () -> {
                    for (ItemStack item : event.getDrops())
                        if (ScriptManager.getDrop(player.getUniqueId(), item) < 0)
                            ScriptManager.removeDrop(player.getUniqueId(), item);
                }, 6000);
            }

            if (questEvent.getType() == QuestType.KILL_ENTITY && questEvent.getObjectiveTypes().contains(entity.getType())) {
                int added = 1;
                double progress = questEvent.getProgress() + added;
                if (questEvent.getCondition() <= progress) script.clear(player);
                else questEvent.setProgress(progress);
                Constants.sendPercentage(player, script, questEvent, added);
                return;
            }
        }
    }

}
