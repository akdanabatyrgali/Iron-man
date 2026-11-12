package edu.stark.service;

import edu.stark.core.ActionResult;
import edu.stark.facade.StarkSuitFacade;
import edu.stark.missions.MissionResult;
import edu.stark.missions.MissionScenario;
import edu.stark.persistence.MissionRecord;
import edu.stark.persistence.MissionRecordRepository;
import edu.stark.suits.IronManSuit;
import edu.stark.suits.MarkIIICreator;
import edu.stark.suits.MarkLCreator;
import edu.stark.suits.MarkVCreator;
import edu.stark.suits.SuitType;
import edu.stark.weapons.WeaponCatalog;
import edu.stark.weapons.WeaponStrategy;
import edu.stark.weapons.WeaponType;
import edu.stark.web.ActionResponse;
import edu.stark.web.CatalogResponse;
import edu.stark.web.DashboardResponse;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import org.springframework.stereotype.Service;

/**
 * A single-player demonstration session shared by all requests to this local server.
 * Synchronized operations prevent competing HTTP requests from corrupting its in-memory state.
 * Completed mission history is durable in the database; the active combat session is not.
 */
@Service
public class StarkGameService {
    private final StarkSuitFacade facade = new StarkSuitFacade();
    private final MissionRecordRepository records;
    private int persistedResultCount;

    public StarkGameService(MissionRecordRepository records) {
        this.records = records;
    }

    public synchronized DashboardResponse dashboard() {
        return new DashboardResponse(facade.snapshot(), history());
    }

    public synchronized List<MissionRecord> history() {
        return records.findAllByOrderByIdDesc();
    }

    public CatalogResponse catalog() {
        List<CatalogResponse.SuitOption> suits = Arrays.stream(SuitType.values())
                .map(type -> {
                    IronManSuit suit = switch (type) {
                        case MARK_III -> new MarkIIICreator().createSuit();
                        case MARK_V -> new MarkVCreator().createSuit();
                        case MARK_L -> new MarkLCreator().createSuit();
                    };
                    return new CatalogResponse.SuitOption(type.name(), suit.name(),
                            suit.maxEnergy(), suit.maxArmor(), suit.assistant());
                }).toList();
        List<CatalogResponse.WeaponOption> weapons = Arrays.stream(WeaponType.values())
                .map(type -> {
                    WeaponStrategy strategy = WeaponCatalog.select(type);
                    return new CatalogResponse.WeaponOption(type.name(), type.displayName(),
                            strategy.energyCost());
                }).toList();
        List<CatalogResponse.MissionOption> missions = Arrays.stream(MissionScenario.values())
                .map(scenario -> {
                    var enemy = scenario.createEnemy();
                    return new CatalogResponse.MissionOption(scenario.name(), scenario.missionName(),
                            enemy.name(), enemy.maxHealth(), enemy.isArmored());
                }).toList();
        return new CatalogResponse(suits, weapons, missions);
    }

    public synchronized ActionResponse selectSuit(SuitType type) {
        return perform(() -> facade.selectSuit(type));
    }

    public synchronized ActionResponse selectWeapon(WeaponType type) {
        return perform(() -> facade.selectWeapon(type));
    }

    public synchronized ActionResponse startMission(MissionScenario scenario) {
        return perform(() -> facade.startMission(scenario));
    }

    public synchronized ActionResponse action(String name) {
        return switch (name) {
            case "activate" -> perform(facade::activate);
            case "attack" -> perform(facade::attack);
            case "defend" -> perform(facade::defend);
            case "fly" -> perform(facade::fly);
            case "recharge" -> perform(facade::recharge);
            case "repair" -> perform(facade::repair);
            case "retreat" -> perform(facade::retreat);
            default -> throw new IllegalArgumentException("Unknown action: " + name);
        };
    }

    private ActionResponse perform(Supplier<ActionResult> operation) {
        ActionResult result = operation.get();
        saveNewMissionResults();
        return new ActionResponse(result.success(), result.message(), dashboard());
    }

    private void saveNewMissionResults() {
        List<MissionResult> results = facade.completedMissions();
        for (int i = persistedResultCount; i < results.size(); i++) {
            MissionResult result = results.get(i);
            records.save(new MissionRecord(result.mission(), result.suit().displayName(),
                    result.success(), Instant.now()));
            persistedResultCount++;
        }
    }
}
