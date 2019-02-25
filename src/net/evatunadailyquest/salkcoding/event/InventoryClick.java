package net.evatunadailyquest.salkcoding.event;

import net.evatunadailyquest.salkcoding.Constants;
import net.evatunadailyquest.salkcoding.GUI.GUICreator;
import net.evatunadailyquest.salkcoding.script.ScriptManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClick implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null)
            return;
        if (!event.getClickedInventory().getName().equalsIgnoreCase(Constants.GUI_Name))
            return;
        Player player = (Player) event.getWhoClicked();
        if(event.getRawSlot() == 26 && !ScriptManager.didPlayerReset(player) && !ScriptManager.hasClearData(player)){
            ScriptManager.resetPlayerDailyQuests(player);
            ScriptManager.addPlayerReset(player);
            player.closeInventory();
            GUICreator.createGUI(player);
        }
        event.setCancelled(true);
    }

}
