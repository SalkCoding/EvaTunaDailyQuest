package net.evatunadailyquest.salkcoding.quest;

public enum QuestType implements Cloneable {

    //아이템 (인벤토리에서확인)
    //행동 (부수기/걷기/설치하기등)
    //플레이타임
    //사냥 (엔티티별로)
    KILL_ENTITY, BLOCK_BREAK, PICKUP_ITEM, PLAY_TIME, WALK_ON_BLOCK, BLOCK_PLACE

}
