package net.evatunadailyquest.salkcoding.quest;

import net.evatunadailyquest.salkcoding.script.Script;
import org.bukkit.event.Listener;

import java.util.*;

public class DailyQuestCreator implements Listener {

    private static final int maxCreate = 4;

    public static List<Script> createQuestList(HashMap<String, Script> scriptMap) {
        List<Script> scriptList = new ArrayList<>();
        List<Script> returnList = new ArrayList<>();
        for (Map.Entry<String, Script> element : scriptMap.entrySet()) scriptList.add(element.getValue());
        check:
        for (int i = 0; i < (maxCreate <= scriptList.size() ? maxCreate : scriptList.size()); i++) {
            Random random = new Random(System.currentTimeMillis());
            Script script = scriptList.get(random.nextInt(scriptList.size()));
            for (Script returnScript : returnList) {
                if (returnScript.getQuestName().equals(script.getQuestName())) {
                    i--;
                    continue check;
                }
            }
            try {
                returnList.add(script.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        return returnList;
    }

}
