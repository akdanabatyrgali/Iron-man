package edu.stark.commands;

import edu.stark.core.ActionResult;
import edu.stark.missions.MissionManager;

public class DefendCommand implements Command {
    private final MissionManager mission;
    public DefendCommand(MissionManager mission) { this.mission = mission; }
    @Override public ActionResult execute() { return mission.defend(); }
}
