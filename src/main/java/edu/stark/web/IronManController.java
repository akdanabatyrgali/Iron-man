package edu.stark.web;

import edu.stark.missions.MissionScenario;
import edu.stark.service.StarkGameService;
import edu.stark.suits.SuitType;
import edu.stark.weapons.WeaponType;
import java.util.Locale;
import java.util.List;
import edu.stark.persistence.MissionRecord;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/** REST interface called by the browser's fetch() requests. */
@RestController
@RequestMapping("/api")
public class IronManController {
    private final StarkGameService game;

    public IronManController(StarkGameService game) {
        this.game = game;
    }

    @GetMapping("/dashboard")
    public DashboardResponse dashboard() {
        return game.dashboard();
    }

    @GetMapping("/history")
    public List<MissionRecord> history() {
        return game.history();
    }

    @GetMapping("/catalog")
    public CatalogResponse catalog() {
        return game.catalog();
    }

    @PostMapping("/suits")
    public ActionResponse selectSuit(@RequestBody SelectionRequest selection) {
        return game.selectSuit(parse(SuitType.class, selection));
    }

    @PostMapping("/weapons")
    public ActionResponse selectWeapon(@RequestBody SelectionRequest selection) {
        return game.selectWeapon(parse(WeaponType.class, selection));
    }

    @PostMapping("/missions")
    public ActionResponse startMission(@RequestBody SelectionRequest selection) {
        return game.startMission(parse(MissionScenario.class, selection));
    }

    @PostMapping("/actions/{name}")
    public ActionResponse action(@PathVariable String name) {
        try {
            return game.action(name.toLowerCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    private static <T extends Enum<T>> T parse(Class<T> type, SelectionRequest selection) {
        if (selection == null || selection.value() == null || selection.value().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing selection value");
        }
        try {
            return Enum.valueOf(type, selection.value().strip().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Unsupported " + type.getSimpleName() + ": " + selection.value());
        }
    }
}
