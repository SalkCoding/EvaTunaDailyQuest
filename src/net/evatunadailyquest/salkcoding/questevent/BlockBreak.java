package net.evatunadailyquest.salkcoding.questevent;

import net.evatunadailyquest.salkcoding.Constants;
import net.evatunadailyquest.salkcoding.anticheat.AntiCheating;
import net.evatunadailyquest.salkcoding.quest.QuestEvent;
import net.evatunadailyquest.salkcoding.script.Script;
import net.evatunadailyquest.salkcoding.script.ScriptManager;
import net.evatunadailyquest.salkcoding.script.specificscript.BreakScript;
import net.evatunadailyquest.salkcoding.script.specificscript.PickupScript;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BlockBreak implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(BlockBreakEvent event) {
        if (event.isCancelled())
            return;
        if (event.getPlayer() == null)
            return;
        Player player = event.getPlayer();
        if (ScriptManager.getPlayerDailyQuests(player) == null)
            return;
        List<Script> list = ScriptManager.getPlayerDailyQuests(player);
        Block block = event.getBlock();
        for (Script script : list) {
            if (script.isClear())
                continue;
            QuestEvent questEvent = script.getQuestEvent();
            if (!AntiCheating.isUsed(player, block.getLocation()))
                if (script instanceof PickupScript) {
                    PickupScript pickupScript = (PickupScript) script;
                    for (ItemStack item : event.getBlock().getDrops(player.getInventory().getItemInMainHand())) {
                        if (pickupScript.isContainInSet(item.getType()))
                            ScriptManager.addDrop(player.getUniqueId(), item);
                    }
                    continue;
                }

            if (script instanceof BreakScript) {
                BreakScript breakScript = (BreakScript) script;
                if (breakScript.isContainInSet(block.getType())) {
                    if (!AntiCheating.isUsed(player, block.getLocation())) {
                        int added = 1;
                        double progress = questEvent.getProgress() + added;
                        if (questEvent.getCondition() <= progress) script.clear(player);
                        else questEvent.setProgress(progress);
                        AntiCheating.addLocation(player, block.getLocation());
                        Constants.sendPercentage(player, script, questEvent, added);
                    } else AntiCheating.removeLocation(player, block.getLocation());
                }
            }
        }
    }

}