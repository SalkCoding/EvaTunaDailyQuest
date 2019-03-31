package net.evatunadailyquest.salkcoding.questevent;

import net.evatunadailyquest.salkcoding.script.Script;
import net.evatunadailyquest.salkcoding.script.ScriptManager;
import net.evatunadailyquest.salkcoding.script.specificscript.WalkScript;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.List;

public class PlayerWalk implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWalk(PlayerMoveEvent event) {
        if (event.isCancelled())
            return;
        Player player = event.getPlayer();
        if (ScriptManager.getPlayerDailyQuests(player) == null)
            return;
        List<Script> list = ScriptManager.getPlayerDailyQuests(player);
        for (Script script : list) {
            if (script.isClear())
                continue;
            if (script instanceof WalkScript) {
                double added = event.getFrom().distance(event.getTo());
                script.addProgress(player, added);
                return;
            }
        }
    }

}
