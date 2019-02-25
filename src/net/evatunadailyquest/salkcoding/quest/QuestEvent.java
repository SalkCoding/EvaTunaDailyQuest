package net.evatunadailyquest.salkcoding.quest;

import java.util.HashSet;

public class QuestEvent {

    private QuestType type;
    private HashSet<Object> objectiveTypes;
    private double progress = 0;
    private int condition;

    public QuestEvent(QuestType type, HashSet<Object> objectiveTypes, int condition) {
        this.type = type;
        this.objectiveTypes = objectiveTypes;
        this.condition = condition;
    }

    public QuestType getType() {
        return type;
    }

    public HashSet<Object> getObjectiveTypes() {
        return objectiveTypes;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public double getProgress() {
        return progress;
    }

    public int getCondition() {
        return condition;
    }

    /*@Override
    public QuestEvent clone() throws CloneNotSupportedException {
        QuestEvent clone = (QuestEvent)super.clone();
        clone.progress = clone.progress;
        return clone;
    }*/

}
