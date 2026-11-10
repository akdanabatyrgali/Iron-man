package edu.stark.commands;

import edu.stark.core.ActionResult;
import edu.stark.missions.MissionSession;

public class DefendCommand implements Command {
    private final MissionSession mission;
    public DefendCommand(MissionSession mission) { this.mission = mission; }
    @Override public ActionResult execute() { return mission.defend(); }
}
