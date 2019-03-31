package net.evatunadailyquest.salkcoding.questevent;

import net.evatunadailyquest.salkcoding.Constants;
import net.evatunadailyquest.salkcoding.script.Script;
import net.evatunadailyquest.salkcoding.script.ScriptManager;
import net.evatunadailyquest.salkcoding.script.specificscript.PickupScript;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PickupItem implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPick(EntityPickupItemEvent event) {
        if (event.isCancelled())
            return;
        if (!(event.getEntity() instanceof Player))
            return;
        Player player = (Player) event.getEntity();
        if (ScriptManager.getPlayerDailyQuests(player) == null)
            return;
        List<Script> list = ScriptManager.getPlayerDailyQuests(player);
        for (Script script : list) {
            if (script.isClear() || !(script instanceof PickupScript))
                continue;
            ItemStack item = event.getItem().getItemStack();
            PickupScript pickupScript = (PickupScript) script;
            if (pickupScript.isContainInSet(item.getType())) {
                int added = ScriptManager.getDrop(player.getUniqueId(), item);
                if (added < 0 || Constants.getEmptySlot(player, item) <= 0)
                    return;
                script.addProgress(player, 1);
                ScriptManager.removeDrop(player.getUniqueId(), item);
                return;
            }
        }
    }

}
