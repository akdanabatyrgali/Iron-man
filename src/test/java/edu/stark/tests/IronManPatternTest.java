package edu.stark.tests;

import edu.stark.facade.StarkSuitFacade;
import edu.stark.missions.MissionScenario;
import edu.stark.observers.WarningObserver;
import edu.stark.suits.*;
import edu.stark.weapons.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IronManPatternTest {
    @Test void factoryCreatesDistinctSuits() {
        assertInstanceOf(MarkIII.class, new MarkIIICreator().createSuit());
        assertInstanceOf(MarkV.class, new MarkVCreator().createSuit());
        assertInstanceOf(MarkL.class, new MarkLCreator().createSuit());
    }
    @Test void observerRaisesThresholdAlertOnce() {
        IronManSuit suit = new MarkIIICreator().createSuit();
        WarningObserver observer = new WarningObserver(suit.assistant());
        suit.addObserver(observer);
        assertTrue(suit.useEnergy(85));
        assertTrue(observer.drain().contains("Energy critically low"));
        assertTrue(suit.useEnergy(1));
        assertEquals("", observer.drain());
    }
    @Test void stateRejectsIllegalActionsAndAllowsRepair() {
        IronManSuit suit = new MarkIIICreator().createSuit();
        assertFalse(suit.fly().success());
        assertTrue(suit.activate().success());
        suit.takeDamage(999);
        assertEquals("DAMAGED", suit.stateName());
        assertTrue(suit.repair().success());
        assertEquals("STANDBY", suit.stateName());
    }
    @Test void strategySwitchesAndRejectsUnsupportedWeapon() {
        StarkSuitFacade app = new StarkSuitFacade();
        assertTrue(app.selectSuit(SuitType.MARK_V).success());
        assertFalse(app.selectWeapon(WeaponType.NANO_BLADE).success());
        assertTrue(app.selectWeapon(WeaponType.REPULSOR).success());
    }
    @Test void commandFacadeAndMissionFlow() {
        StarkSuitFacade app = new StarkSuitFacade();
        assertFalse(app.attack().success());
        assertTrue(app.selectSuit(SuitType.MARK_L).success());
        assertTrue(app.activate().success());
        assertTrue(app.selectWeapon(WeaponType.UNIBEAM).success());
        assertTrue(app.startMission(MissionScenario.MONACO).success());
        assertTrue(app.attack().success());
        assertTrue(app.attack().success());
        assertTrue(app.missionHistory().contains("SUCCESS"));
    }
}
