package net.evatunadailyquest.salkcoding.questevent;

import net.evatunadailyquest.salkcoding.quest.QuestEvent;
import net.evatunadailyquest.salkcoding.quest.QuestType;
import net.evatunadailyquest.salkcoding.script.Script;
import net.evatunadailyquest.salkcoding.script.ScriptManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;

import java.util.List;
import java.util.Random;

public class PlayerShear implements Listener {

    @EventHandler
    public void onShear(PlayerShearEntityEvent event) {
        Random random = new Random(System.currentTimeMillis());
        Sheep sheep = (Sheep) event.getEntity();
        sheep.setSheared(true);

        Wool wool = new Wool();
        wool.setColor(sheep.getColor());
        ItemStack drop = wool.toItemStack(random.nextInt(3) + 1);

        Location location = sheep.getLocation();
        location.add(0, 1, 0);//Make item drop naturally such as shearing
        EntityDropItemEvent entityDropItemEvent = new EntityDropItemEvent(sheep, sheep.getWorld().dropItemNaturally(location, drop));
        Bukkit.getPluginManager().callEvent(entityDropItemEvent);
        event.setCancelled(true);

        Player player = event.getPlayer();
        if (ScriptManager.getPlayerDailyQuests(player) == null)
            return;
        List<Script> list = ScriptManager.getPlayerDailyQuests(player);
        for (Script script : list) {
            if (script.isClear())
                continue;
            QuestEvent questEvent = script.getQuestEvent();
            if (questEvent.getType() == QuestType.PICKUP_ITEM) {
                ScriptManager.addDrop(player.getUniqueId(), drop);
            }
        }
    }

}
