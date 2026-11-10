package edu.stark.facade;

import edu.stark.commands.*;
import edu.stark.core.ActionResult;
import edu.stark.missions.MissionSession;
import edu.stark.missions.MissionResult;
import edu.stark.missions.MissionScenario;
import edu.stark.observers.WarningObserver;
import edu.stark.core.SuitOutputFormatter;
import edu.stark.suits.*;
import edu.stark.weapons.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StarkSuitFacade {
    private final CommandInvoker invoker = new CommandInvoker();
    private final List<MissionResult> history = new ArrayList<>();
    private IronManSuit suit;
    private WarningObserver warnings;
    private MissionSession mission;
    private WeaponStrategy weapon;
    private final SuitOutputFormatter formatter = new SuitOutputFormatter();

    public ActionResult selectSuit(SuitType type) {
        if (mission != null && mission.inMission()) {
            return ActionResult.fail("Cannot change armor during a mission. Retreat or win first.");
        }
        if (type == null) return ActionResult.fail("Unknown suit type.");
        SuitCreator creator = switch (type) {
            case MARK_III -> new MarkIIICreator();
            case MARK_V -> new MarkVCreator();
            case MARK_L -> new MarkLCreator();
        };
        suit = creator.createSuit(); // Factory Method is in each concrete creator
        warnings = new WarningObserver(suit.assistant());
        suit.addObserver(warnings); // Observer subscription
        mission = new MissionSession(suit, history);
        weapon = new RepulsorStrategy();
        return ActionResult.ok("[" + suit.assistant() + "] " + suit.name()
                + " selected. State: STANDBY. Default weapon: Repulsor.");
    }

    public ActionResult activate() {
        return suit == null ? noSuit() : run(new ActivateCommand(suit));
    }
    public ActionResult fly() {
        return suit == null ? noSuit() : run(new FlyCommand(suit));
    }
    public ActionResult recharge() {
        return suit == null ? noSuit() : run(new RechargeCommand(suit));
    }
    public ActionResult repair() {
        return suit == null ? noSuit() : run(new RepairCommand(suit));
    }
    public ActionResult startMission(MissionScenario scenario) {
        if (suit == null) return noSuit();
        return addWarnings(mission.start(scenario));
    }
    public ActionResult selectWeapon(WeaponType type) {
        if (suit == null) return noSuit();
        if (type == null) return ActionResult.fail("Unknown weapon.");
        if (!suit.supports(type)) return ActionResult.fail(suit.name() + " does not support " + type.displayName() + ".");
        weapon = WeaponCatalog.select(type); // Strategy replacement at runtime
        return ActionResult.ok("Weapon switched to " + type.displayName() + ".");
    }
    public ActionResult attack() {
        return suit == null ? noSuit() : run(new AttackCommand(mission, weapon));
    }
    public ActionResult defend() {
        return suit == null ? noSuit() : run(new DefendCommand(mission));
    }
    public ActionResult retreat() {
        return suit == null ? noSuit() : run(new RetreatCommand(mission));
    }
    public String status() {
        if (suit == null) return "No suit selected. Choose a suit first.";
        return formatter.status(suit, weapon, mission.inMission() ? mission.enemy() : null);
    }

    public String missionHistory() {
        if (history.isEmpty()) return "No completed missions yet.";
        StringBuilder sb = new StringBuilder("MISSION HISTORY\n");
        for (int i = 0; i < history.size(); i++) {
            sb.append(i + 1).append(". ").append(history.get(i)).append('\n');
        }
        return sb.toString().stripTrailing();
    }
    private ActionResult run(Command command) { return addWarnings(invoker.executeCommand(command)); }
    private ActionResult addWarnings(ActionResult result) { return result.add(warnings.drain()); }
    private ActionResult noSuit() { return ActionResult.fail("Select a suit first (menu option 1)."); }
}
