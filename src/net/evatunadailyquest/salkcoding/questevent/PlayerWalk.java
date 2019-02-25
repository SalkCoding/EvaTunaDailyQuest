package net.evatunadailyquest.salkcoding.questevent;

import net.evatunadailyquest.salkcoding.Constants;
import net.evatunadailyquest.salkcoding.quest.QuestEvent;
import net.evatunadailyquest.salkcoding.quest.QuestType;
import net.evatunadailyquest.salkcoding.script.Script;
import net.evatunadailyquest.salkcoding.script.ScriptManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.List;

public class PlayerWalk implements Listener {

    @EventHandler
    public void onWalk(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (ScriptManager.getPlayerDailyQuests(player) == null)
            return;
        List<Script> list = ScriptManager.getPlayerDailyQuests(player);
        for (Script script : list) {
            if (script.isClear())
                continue;
            QuestEvent questEvent = script.getQuestEvent();
            if (questEvent.getType() == QuestType.WALK_ON_BLOCK) {
                double added = event.getFrom().distance(event.getTo());
                double progress = questEvent.getProgress() + added;
                if (questEvent.getCondition() <= progress) script.clear(player);
                else questEvent.setProgress(progress);
                Constants.sendPercentage(player, script, questEvent, (int) added);
                return;
            }
        }
    }

}
