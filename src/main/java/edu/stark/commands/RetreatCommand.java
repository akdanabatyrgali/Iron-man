package edu.stark.commands;

import edu.stark.core.ActionResult;
import edu.stark.missions.MissionManager;

public class RetreatCommand implements Command {
    private final MissionManager mission;
    public RetreatCommand(MissionManager mission) { this.mission = mission; }
    @Override public ActionResult execute() { return mission.retreat(); }
}
