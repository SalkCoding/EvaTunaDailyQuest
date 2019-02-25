package net.evatunadailyquest.salkcoding.script;

import net.evatunadailyquest.salkcoding.Constants;
import net.evatunadailyquest.salkcoding.GUI.GUICreator;
import net.evatunadailyquest.salkcoding.file.SaveProgress;
import net.evatunadailyquest.salkcoding.file.ScriptReader;
import net.evatunadailyquest.salkcoding.main.Main;
import net.evatunadailyquest.salkcoding.quest.DailyQuestCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.io.IOException;
import java.util.*;

public class ScriptManager {

    private static HashMap<UUID, List<ItemStack>> dropMap = new HashMap<>();
    private static HashMap<UUID, List<Script>> playerMap = new HashMap<>();
    private static HashMap<String, Script> scriptMap = new HashMap<>();
    private static HashSet<UUID> resetSet = new HashSet<>();
    private static BukkitTask task;
    private static boolean isReset = false;

    public static void addDrop(UUID uuid, ItemStack add) {
        //System.out.println(dropMap);
        if (dropMap.containsKey(uuid)) {
            List<ItemStack> list = dropMap.get(uuid);
            for (ItemStack item : list) if (item.isSimilar(add)) item.setAmount(item.getAmount() + add.getAmount());
        } else {
            List<ItemStack> list = new ArrayList<>();
            list.add(add);
            dropMap.put(uuid, list);
        }
        //System.out.println(dropMap);
    }

    public static int getDrop(UUID uuid, ItemStack compare) {
        if (dropMap.containsKey(uuid)) for (ItemStack item : dropMap.get(uuid))
            if (item.isSimilar(compare)) return compare.getAmount();
        return -1;
    }

    public static void removeDrop(UUID uuid, ItemStack compare) {
        //System.out.println(dropMap);
        if (dropMap.containsKey(uuid)) {
            List<ItemStack> list = dropMap.get(uuid);
            int i = 0;
            for (ItemStack item : list) {
                if(item.getType() == Material.AIR)
                    list.remove(item);
                if (item.isSimilar(compare)) {
                    int amount = item.getAmount() - compare.getAmount();
                    if (amount > 0) item.setAmount(amount);
                    else {
                        list.remove(i);
                        if(list.size() <= 0)
                            dropMap.remove(uuid);
                    }
                    break;
                }
                i++;
            }
        }
        //System.out.println(dropMap);
    }


    public static void addPlayerReset(Player player) {
        resetSet.add(player.getUniqueId());
    }

    public static boolean didPlayerReset(Player player) {
        return resetSet.contains(player.getUniqueId());
    }

    public static List<Script> getPlayerDailyQuests(OfflinePlayer player) {
        //System.out.println(player);
        if (!playerMap.containsKey(player.getUniqueId()))
            addPlayerDailyQuests(player);
        return playerMap.get(player.getUniqueId());
    }

    public static Script getScript(String name) {
        return scriptMap.get(name);
    }

    public static void resetPlayerDailyQuests(OfflinePlayer player) {
        //System.out.println(player);
        if (player.isOnline()) {
            player.getPlayer().sendMessage(Constants.Info_Format + "일일 퀘스트가 초기화되었습니다.");
            if (player.getPlayer().getOpenInventory().getTitle().equalsIgnoreCase(Constants.GUI_Name)) {
                player.getPlayer().closeInventory();
                GUICreator.createGUI(player.getPlayer());
            }
        }
        UUID uuid = player.getUniqueId();
        playerMap.replace(uuid, DailyQuestCreator.createQuestList(scriptMap));
        for (Script script : getPlayerDailyQuests(player)) {
            script.setClear(false);
            script.getQuestEvent().setProgress(0);
        }
        resetSet.remove(uuid);
    }

    public static void addPlayerDailyQuests(OfflinePlayer player) {
        UUID uuid = player.getUniqueId();
        if (!playerMap.containsKey(uuid)) playerMap.put(uuid, DailyQuestCreator.createQuestList(scriptMap));
    }

    public static void loadScripts() throws IOException, CloneNotSupportedException {
        scriptMap = ScriptReader.readScripts();
        playerMap = SaveProgress.loadPlayerMap();
        resetSet = SaveProgress.loadResetSet();
        task = Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getInstance(), ScriptManager::run, 0, 200);
        System.out.println(Constants.Console_Format + playerMap.size() + " of player's progresses are loaded");
    }

    public static void save() throws IOException {
        SaveProgress.saveTimeMap(playerMap, resetSet);
        task.cancel();
    }

    public static boolean hasClearData(Player player) {
        for (Script script : playerMap.get(player.getUniqueId())) {
            if (script.isClear())
                return true;
        }
        return false;
    }

    //reset player quest when 12 O' Clock
    private static void run() {
        Calendar now = Calendar.getInstance();
        if (now.get(Calendar.HOUR_OF_DAY) == 12) {
            if (!isReset) {
                for (Map.Entry<UUID, List<Script>> element : playerMap.entrySet())
                    resetPlayerDailyQuests(Bukkit.getOfflinePlayer(element.getKey()));
                isReset = true;
                System.out.println(Constants.Console_Format + "Last reset Time : " + now.getTime());
            }
        } else {
            isReset = false;
        }
    }

}
