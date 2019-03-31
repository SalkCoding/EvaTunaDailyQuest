package net.evatunadailyquest.salkcoding.file;

import com.google.gson.*;
import net.evatunadailyquest.salkcoding.Constants;
import net.evatunadailyquest.salkcoding.script.Script;
import net.evatunadailyquest.salkcoding.script.ScriptManager;
import org.bukkit.Bukkit;

import java.io.*;
import java.util.*;

import static net.evatunadailyquest.salkcoding.Constants.timemap_dir;

public class SaveProgress {

    public static void saveTimeMap(HashMap<UUID, List<Script>> playerMap, HashSet<UUID> resetSet) throws IOException {
        Gson gson = new Gson();
        JsonObject json = new JsonObject();
        if (!timemap_dir.exists())
            timemap_dir.mkdirs();
        for (Map.Entry<UUID, List<Script>> o : playerMap.entrySet()) {
            File file = new File(timemap_dir, o.getKey().toString() + ".json");

            json.addProperty("Name", Bukkit.getOfflinePlayer(o.getKey()).getName());
            json.addProperty("UUID", o.getKey().toString());
            json.addProperty("DidReset", resetSet.contains(o.getKey()));

            JsonArray array = new JsonArray();
            for (Script script : o.getValue()) {
                JsonObject scriptObject = new JsonObject();
                scriptObject.addProperty("Name", script.getQuestName());
                scriptObject.addProperty("Progress", script.getQuestEvent().getProgress());
                scriptObject.addProperty("Clear", script.isClear());
                array.add(scriptObject);
            }
            json.add("QuestList", array);

            FileWriter writer = new FileWriter(file);
            writer.write(gson.toJson(json));
            writer.flush();
            writer.close();
        }
        System.out.println(Constants.Console_Format + playerMap.size() + " of player's progresses are saved");
    }

    public static HashMap<UUID, List<Script>> loadPlayerMap() throws IOException, CloneNotSupportedException {
        HashMap<UUID, List<Script>> playerMap = new HashMap<>();
        if (!timemap_dir.exists())
            timemap_dir.mkdirs();
        for (File file : Objects.requireNonNull(timemap_dir.listFiles())) {
            JsonObject json = readFile(file);
            List<Script> list = new ArrayList<>();
            for (JsonElement element : json.get("QuestList").getAsJsonArray()) {
                JsonObject scriptObject = element.getAsJsonObject();
                Script script = ScriptManager.getScript(scriptObject.get("Name").getAsString()).clone();
                script.getQuestEvent().setProgress(scriptObject.get("Progress").getAsFloat());
                if (scriptObject.get("Clear").getAsBoolean())
                    script.clear();
                list.add(script);
            }
            playerMap.put(UUID.fromString(json.get("UUID").getAsString()), list);
        }
        return playerMap;
    }

    public static HashSet<UUID> loadResetSet() throws IOException {
        HashSet<UUID> resetSet = new HashSet<>();
        if (!timemap_dir.exists())
            timemap_dir.mkdirs();
        for (File file : Objects.requireNonNull(timemap_dir.listFiles())) {
            JsonObject json = readFile(file);
            if (json.get("DidReset").getAsBoolean())
                resetSet.add(UUID.fromString(json.get("UUID").getAsString()));
        }
        return resetSet;
    }

    private static JsonObject readFile(File file) throws IOException {
        JsonParser parser = new JsonParser();
        return parser.parse(FileUtil.getJsonStringFromFile(file)).getAsJsonObject();
    }

}
