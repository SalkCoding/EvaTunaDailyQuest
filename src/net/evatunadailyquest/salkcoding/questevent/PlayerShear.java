package net.evatunadailyquest.salkcoding.questevent;

import net.evatunadailyquest.salkcoding.quest.QuestEvent;
import net.evatunadailyquest.salkcoding.quest.QuestType;
import net.evatunadailyquest.salkcoding.script.Script;
import net.evatunadailyquest.salkcoding.script.ScriptManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

public class PlayerShear implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onShear(PlayerShearEntityEvent event) {
        if(event.isCancelled())
            return;
        Random random = new Random(System.currentTimeMillis());
        Sheep sheep = (Sheep) event.getEntity();
        sheep.setSheared(true);

        ItemStack drop = new ItemStack(Material.valueOf(sheep.getColor().toString() + "_WOOL"), random.nextInt(3) + 1);

        Location location = sheep.getLocation();
        location.add(0, 1, 0);//Make item drop naturally such as shearing
        location.getWorld().playSound(location, Sound.ENTITY_SHEEP_SHEAR, 5, 5);
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
            if (questEvent.getType() == QuestType.PICKUP_ITEM && questEvent.getObjectiveTypes().contains(drop.getType())) {
                ScriptManager.addDrop(player.getUniqueId(), drop);
                return;
            }
        }
    }

}
