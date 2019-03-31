package net.evatunadailyquest.salkcoding.quest;

public class QuestEvent {

    private double progress = 0;
    private int condition;

    public QuestEvent(int condition) {
        this.condition = condition;
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
