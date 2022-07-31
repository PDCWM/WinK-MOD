package i.WinKcode;

import baritone.api.BaritoneAPI;

public class BaritoneFS {

    public static void init() {
        BaritoneAPI.getSettings().allowSprint.value = true;
        BaritoneAPI.getSettings().primaryTimeoutMS.value = 2000L;
        BaritoneAPI.getSettings().allowBreak.value = true;
        BaritoneAPI.getSettings().allowPlace.value = true;
        BaritoneAPI.getSettings().allowInventory.value = true;
        BaritoneAPI.getSettings().allowParkour.value = true;
        BaritoneAPI.getSettings().antiCheatCompatibility.value = true;
        BaritoneAPI.getSettings().chatControl.value = true;
        BaritoneAPI.getSettings().chatDebug.value = false;
    }

    public static void status(){
        init();

        BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(new baritone.api.pathing.goals.GoalXZ(1000,1000));
    }

    public static void GoalBlock(Integer x, Integer y, Integer z) {
        init();

        BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(new baritone.api.pathing.goals.GoalBlock(x,y,z));
    }
}
