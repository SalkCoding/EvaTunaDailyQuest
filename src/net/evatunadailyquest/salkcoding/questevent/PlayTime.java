package net.evatunadailyquest.salkcoding.questevent;

import net.evatunadailyquest.salkcoding.Constants;
import net.evatunadailyquest.salkcoding.main.Main;
import net.evatunadailyquest.salkcoding.quest.QuestType;
import net.evatunadailyquest.salkcoding.script.Script;
import net.evatunadailyquest.salkcoding.script.ScriptManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.List;

public class PlayTime implements Listener {

    private static final HashMap<String, BukkitTask> timeMap = new HashMap<>();

    public boolean addPlayer(Player player, Script script) {
        if (script.isClear() || script.getQuestEvent().getType() != QuestType.PLAY_TIME)
            return false;
        if (!timeMap.containsKey(player.getName())) {
            timeMap.put(player.getName(), Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getInstance(), () -> {
                if (script.getQuestEvent().getCondition() > script.getQuestEvent().getProgress()) {
                    int added = 20;
                    script.getQuestEvent().setProgress(script.getQuestEvent().getProgress() + added);
                    Constants.sendPercentage(player, script, script.getQuestEvent(), added);
                } else {
                    Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                        script.clear(player);
                    });
                    timeMap.get(player.getName()).cancel();
                    timeMap.remove(player.getName());
                }
            }, 20, 20));
            return true;
        }
        return false;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (timeMap.containsKey(player.getName())) {
            timeMap.get(player.getName()).cancel();
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (timeMap.containsKey(player.getName())) {
            List<Script> list = ScriptManager.getPlayerDailyQuests(player);
            if (list == null)
                return;
            for (Script script : list) addPlayer(player, script);
        }
    }

}
