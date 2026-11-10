package edu.stark.commands;

import edu.stark.core.ActionResult;
import edu.stark.missions.MissionSession;

public class RetreatCommand implements Command {
    private final MissionSession mission;
    public RetreatCommand(MissionSession mission) { this.mission = mission; }
    @Override public ActionResult execute() { return mission.retreat(); }
}
