package net.evatunadailyquest.salkcoding.file;

import com.google.gson.*;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper;
import net.evatunadailyquest.salkcoding.Constants;
import net.evatunadailyquest.salkcoding.quest.QuestEvent;
import net.evatunadailyquest.salkcoding.script.Script;
import net.evatunadailyquest.salkcoding.script.ScriptType;
import net.evatunadailyquest.salkcoding.script.specificscript.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ScriptReader {

    public static HashMap<String, Script> readScripts() throws IOException {
        HashMap<String, Script> map = new HashMap<>();

        if (!Constants.script_dir.exists())
            Constants.script_dir.mkdirs();

        File[] files = Constants.script_dir.listFiles();
        fileFor:
        for (File file : Objects.requireNonNull(files)) {
            Gson gson = new Gson();
            JsonParser parser = new JsonParser();
            JsonObject json = parser.parse(FileUtil.getJsonStringFromFile(file)).getAsJsonObject();

            String questName = ChatColor.translateAlternateColorCodes('&', json.get("QuestName").getAsString());

            List<String> GUILore = gson.fromJson(json.get("GUILore"), List.class);
            List<String> clearMessage = gson.fromJson(json.get("ClearMessages"), List.class);
            for (int i = 0; i < clearMessage.size(); i++)
                clearMessage.set(i, ChatColor.translateAlternateColorCodes('&', clearMessage.get(i)));
            for (int i = 0; i < GUILore.size(); i++)
                GUILore.set(i, ChatColor.translateAlternateColorCodes('&', GUILore.get(i)));

            JsonObject reward = json.get("Reward").getAsJsonObject();
            List<String> commands = gson.fromJson(reward.get("Commands"), List.class);
            JsonArray items = reward.get("Items").getAsJsonArray();
            List<ItemStack> itemList = new ArrayList<>();
            for (JsonElement element : items) {
                JsonObject object = element.getAsJsonObject();
                ItemStack item = new ItemStack(Material.valueOf(object.get("Type").getAsString()), object.get("Amount").getAsInt());
                ItemMeta meta = item.getItemMeta();
                List<String> itemLore = gson.fromJson(object.get("Lore"), List.class);
                for (int i = 0; i < itemLore.size(); i++)
                    itemLore.set(i, ChatColor.translateAlternateColorCodes('&', itemLore.get(i)));
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', object.get("Display-name").getAsString()));
                meta.setLore(itemLore);
                item.setItemMeta(meta);
                itemList.add(item);
            }

            //System.out.println(file.getName());
            JsonObject event = json.get("Event").getAsJsonObject();
            ScriptType scriptType = ScriptType.valueOf(event.get("QuestType").getAsString());
            List<String> objectiveTypes = gson.fromJson(event.get("ObjectiveTypes"), List.class);
            QuestEvent questEvent = new QuestEvent(event.get("Condition").getAsInt());
            //System.out.println(scriptType.toString() + " " + objectiveTypes.toString());
            switch (scriptType) {
                case Break:
                case Place:
                case Pickup: {
                    HashSet<Material> set = new HashSet<>();
                    try {
                        for (String type : objectiveTypes) set.add(Material.valueOf(type));
                    } catch (IllegalArgumentException e) {
                        System.out.println(Constants.Console_Format + "Config file named \"" + file.getName() + "\" has error and that script file won't be added");
                        continue fileFor;
                    }
                    switch (scriptType) {
                        case Break:
                            map.put(questName, new BreakScript(questName, GUILore, clearMessage, set, questEvent, commands, itemList));
                            break;
                        case Place:
                            map.put(questName, new PlaceScript(questName, GUILore, clearMessage, set, questEvent, commands, itemList));
                            break;
                        case Pickup:
                            map.put(questName, new PickupScript(questName, GUILore, clearMessage, set, questEvent, commands, itemList));
                            break;
                    }
                }
                break;
                case Kill: {
                    HashSet<EntityType> set = new HashSet<>();
                    try {
                        for (String type : objectiveTypes) set.add(EntityType.valueOf(type));
                    } catch (IllegalArgumentException e) {
                        System.out.println(Constants.Console_Format + "Config file named \"" + file.getName() + "\" has error and that script file won't be added");
                        continue fileFor;
                    }
                    map.put(questName, new KillScript(questName, GUILore, clearMessage, set, questEvent, commands, itemList));
                }
                break;
                case MythicMobKill: {
                    HashSet<String> set = new HashSet<>();
                    BukkitAPIHelper helper = MythicMobs.inst().getAPIHelper();
                    for (String type : objectiveTypes) {
                        if (helper.getMythicMob(type) != null)
                            set.add(type);
                        else {
                            try {
                                throw new IllegalArgumentException(type + " is not correct mythic mob's internal name!");
                            } catch (IllegalArgumentException e) {
                                System.out.println(Constants.Console_Format + "Config file named \"" + file.getName() + "\" has error and that script won't be added");
                                continue fileFor;
                            }
                        }
                    }
                    map.put(questName, new MythicMobKillScript(questName, GUILore, clearMessage, set, questEvent, commands, itemList));
                }
                break;
                case Walk:
                    map.put(questName, new WalkScript(questName, GUILore, clearMessage, questEvent, commands, itemList));
                    break;
                case Play:
                    map.put(questName, new PlayScript(questName, GUILore, clearMessage, questEvent, commands, itemList));
                    break;
                default:
                    System.out.println(Constants.Console_Format + "Config file named \"" + file.getName() + "\" has error and that script file won't be added");
            }
            //System.out.println(questName + GUILore.toString() + clearMessage.toString() + event.get("QuestType").getAsString() + set.toString() + event.get("Condition").getAsString() + commands.toString() + "" + itemList.toString());
        }
        System.out.println(Constants.Console_Format + map.size() + " of scripts are loaded");
        return map;
    }

}
