package net.evatunadailyquest.salkcoding.event;

import net.evatunadailyquest.salkcoding.script.ScriptManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if(ScriptManager.getPlayerDailyQuests(player) == null){
            ScriptManager.addPlayerDailyQuests(player);
        }
    }

}
