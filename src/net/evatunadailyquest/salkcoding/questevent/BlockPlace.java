package net.evatunadailyquest.salkcoding.questevent;

import net.evatunadailyquest.salkcoding.Constants;
import net.evatunadailyquest.salkcoding.QuickHash;
import net.evatunadailyquest.salkcoding.anticheat.AntiCheating;
import net.evatunadailyquest.salkcoding.quest.QuestEvent;
import net.evatunadailyquest.salkcoding.quest.QuestType;
import net.evatunadailyquest.salkcoding.script.Script;
import net.evatunadailyquest.salkcoding.script.ScriptManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.List;

public class BlockPlace implements Listener {
//First called event is PlayerInteractEvent, second called event is BlockPlaceEvent.

    //private static HashSet<UUID> playerSet = new HashSet<>();

    private static final QuickHash<Material> logSet = new QuickHash<>(
            Material.STRIPPED_OAK_LOG,
            Material.STRIPPED_ACACIA_LOG,
            Material.STRIPPED_BIRCH_LOG,
            Material.STRIPPED_DARK_OAK_LOG,
            Material.STRIPPED_JUNGLE_LOG,
            Material.STRIPPED_SPRUCE_LOG
    );

    /*private static QuickHash axeSet = new QuickHash<>(
            Material.WOODEN_AXE,
            Material.STONE_AXE,
            Material.IRON_AXE,
            Material.GOLDEN_AXE,
            Material.DIAMOND_AXE
    );*/

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlace(BlockPlaceEvent event) {
        if(event.isCancelled())
            return;
        if (event.getPlayer() == null)
            return;
        Player player = event.getPlayer();
        if (ScriptManager.getPlayerDailyQuests(player) == null)
            return;
        List<Script> list = ScriptManager.getPlayerDailyQuests(player);
        Block block = event.getBlockPlaced();
        //둘다 나무가 아니면서 위치가 일치하지 않음 = 일반적인 설치 이벤트
        if (!logSet.contains(event.getBlockAgainst().getType()) || !logSet.contains(block.getType()) || !block.getLocation().equals(event.getBlockAgainst().getLocation()))
            if (!AntiCheating.isUsed(player, block.getLocation()))
                AntiCheating.addLocation(player, block.getLocation());
        for (Script script : list) {
            if (script.isClear())
                continue;
            QuestEvent questEvent = script.getQuestEvent();
            if (questEvent.getType() == QuestType.BLOCK_PLACE && questEvent.getObjectiveTypes().contains(block.getType())) {
                int added = 1;
                double progress = questEvent.getProgress() + added;
                if (questEvent.getCondition() <= progress) script.clear(player);
                else questEvent.setProgress(progress);
                Constants.sendPercentage(player, script, questEvent, added);
                return;
            }
        }
    }


}
