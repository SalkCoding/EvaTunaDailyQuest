package net.evatunadailyquest.salkcoding.file;

import com.google.gson.*;
import net.evatunadailyquest.salkcoding.Constants;
import net.evatunadailyquest.salkcoding.quest.QuestEvent;
import net.evatunadailyquest.salkcoding.quest.QuestType;
import net.evatunadailyquest.salkcoding.script.Script;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class ScriptReader {

    public static HashMap<String, Script> readScripts() throws IOException {
        HashMap<String, Script> map = new HashMap<>();

        if (!Constants.script_dir.exists())
            Constants.script_dir.mkdirs();

        File[] files = Constants.script_dir.listFiles();
        fileFor:
        for (File file : files) {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            JsonParser parser = new JsonParser();
            Gson gson = new Gson();
            JsonObject json = parser.parse(builder.toString()).getAsJsonObject();

            String questName = ChatColor.translateAlternateColorCodes('&', json.get("QuestName").getAsString());

            List<String> GUILore = gson.fromJson(json.get("GUI_Lore"), List.class);
            List<String> clearMessage = gson.fromJson(json.get("Clear_Messages"), List.class);
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

            JsonObject event = json.get("Event").getAsJsonObject();
            List<String> materialObjectiveTypes = gson.fromJson(event.get("Material_Objective_Types"), List.class);
            List<String> entityTypeObjectiveTypes = gson.fromJson(event.get("EntityType_Objective_Types"), List.class);
            HashSet<Object> set = new HashSet<>();
            if (materialObjectiveTypes != null) {
                for (String string : materialObjectiveTypes) {
                    try {
                        Material material = Material.valueOf(string);
                        set.add(material);
                    } catch (IllegalArgumentException e) {
                        System.out.println(Constants.Console_Format + "Config file named \"" + file.getName() + "\" has error : " + string + " is a wrong Material value");
                        e.printStackTrace();
                        continue fileFor;
                    }
                }
            }
            if (entityTypeObjectiveTypes != null) {
                for (String string : entityTypeObjectiveTypes) {
                    try {
                        EntityType material = EntityType.valueOf(string);
                        set.add(material);
                    } catch (IllegalArgumentException e) {
                        System.out.println(Constants.Console_Format + "Config file named \"" + file.getName() + "\" has error : " + string + " is a wrong EntityType value");
                        e.printStackTrace();
                        continue fileFor;
                    }
                }
            }
            map.put(questName, new Script(questName, GUILore, clearMessage, new QuestEvent(QuestType.valueOf(event.get("QuestType").getAsString()), set,
                    event.get("Condition").getAsInt()), commands, itemList));
            //System.out.println(questName + GUILore.toString() + clearMessage.toString() + event.get("QuestType").getAsString() + set.toString() + event.get("Condition").getAsString() + commands.toString() + "" + itemList.toString());
        }
        System.out.println(Constants.Console_Format + map.size() + " of scripts are loaded");
        return map;
    }

}
