package net.evatunadailyquest.salkcoding.questevent;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import net.evatunadailyquest.salkcoding.script.Script;
import net.evatunadailyquest.salkcoding.script.ScriptManager;
import net.evatunadailyquest.salkcoding.script.specificscript.KillScript;
import net.evatunadailyquest.salkcoding.script.specificscript.MythicMobKillScript;
import net.evatunadailyquest.salkcoding.script.specificscript.PickupScript;
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
        BukkitAPIHelper helper = MythicMobs.inst().getAPIHelper();
        if(helper.isMythicMob(event.getEntity()))
            return;
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

            if (script instanceof PickupScript) {
                PickupScript pickupScript = (PickupScript) script;
                for (ItemStack item : event.getDrops()) {
                    if (item == null) continue;
                    if (pickupScript.isContainInSet(item.getType()))
                        ScriptManager.addDrop(player.getUniqueId(), item);
                }
                continue;
            }

            if (script instanceof KillScript) {
                KillScript killScript = (KillScript) script;
                if (killScript.isContainInSet(entity.getType())) killScript.addProgress(player, 1);
                //EntityDeathEvent has drops
            }
        }
    }

    @EventHandler
    public void onKillMythic(MythicMobDeathEvent event){
        if (!(event.getKiller() instanceof Player))
            return;
        Player player = (Player) event.getKiller();
        if (ScriptManager.getPlayerDailyQuests(player) == null)
            return;
        List<Script> list = ScriptManager.getPlayerDailyQuests(player);
        for (Script script : list) {
            if (script.isClear())
                continue;

            if (script instanceof PickupScript) {
                PickupScript pickupScript = (PickupScript) script;
                for (ItemStack item : event.getDrops()) {
                    if (item == null) continue;
                    if (pickupScript.isContainInSet(item.getType()))
                        ScriptManager.addDrop(player.getUniqueId(), item);
                }
                continue;
            }

            if (script instanceof MythicMobKillScript) {
                MythicMobKillScript killScript = (MythicMobKillScript) script;
                if (killScript.isContainInSet(event.getMobType().getInternalName())) killScript.addProgress(player, 1);
                //EntityDeathEvent has drops
            }
        }
    }

}
