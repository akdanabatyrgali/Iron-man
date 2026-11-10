package edu.stark.tests;

import edu.stark.core.ActionResult;
import edu.stark.facade.StarkSuitFacade;
import edu.stark.missions.Enemy;
import edu.stark.missions.MissionScenario;
import edu.stark.observers.SuitObserver;
import edu.stark.observers.WarningObserver;
import edu.stark.suits.IronManSuit;
import edu.stark.suits.MarkIII;
import edu.stark.suits.MarkL;
import edu.stark.suits.MarkV;
import edu.stark.suits.SuitType;
import edu.stark.states.ActiveState;
import edu.stark.weapons.MissileStrategy;
import edu.stark.weapons.NanoBladeStrategy;
import edu.stark.weapons.RepulsorStrategy;
import edu.stark.weapons.WeaponType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IronManEdgeCasesTest {
    @Test void observerCanUnsubscribe() {
        IronManSuit suit = new MarkIII();
        int[] notifications = {0};
        SuitObserver observer = status -> notifications[0]++;
        suit.addObserver(observer);
        assertEquals(1, notifications[0]);
        suit.useEnergy(10);
        assertEquals(2, notifications[0]);
        suit.removeObserver(observer);
        suit.useEnergy(10);
        assertEquals(2, notifications[0]);
    }

    @Test void duplicateObserverIsOnlyRegisteredOnce() {
        IronManSuit suit = new MarkIII();
        int[] notifications = {0};
        SuitObserver observer = status -> notifications[0]++;
        suit.addObserver(observer);
        suit.addObserver(observer);
        suit.useEnergy(1);
        assertEquals(2, notifications[0]); // initial status and one update
    }

    @Test void warningResetsAfterRecharging() {
        IronManSuit suit = new MarkIII();
        WarningObserver warnings = new WarningObserver(suit.assistant());
        suit.addObserver(warnings);
        suit.useEnergy(85);
        assertTrue(warnings.drain().contains("Energy critically low"));
        assertEquals("", warnings.drain());
        suit.restoreEnergy();
        suit.useEnergy(85);
        assertTrue(warnings.drain().contains("Energy critically low"));
    }

    @Test void stateTransitionRequiresCurrentStateObject() {
        IronManSuit suit = new MarkIII();
        assertThrows(IllegalStateException.class,
                () -> suit.transitionFrom(new ActiveState(), new ActiveState()));
        assertEquals("STANDBY", suit.stateName());
    }

    @Test void lowEnergyCannotBeOverdrawn() {
        IronManSuit suit = new MarkIII();
        assertFalse(suit.useEnergy(101));
        assertEquals(100, suit.energy());
        assertThrows(IllegalArgumentException.class, () -> suit.useEnergy(-1));
    }

    @Test void armorCannotBecomeNegative() {
        IronManSuit suit = new MarkV();
        suit.takeDamage(999);
        assertEquals(0, suit.armor());
        assertEquals("DAMAGED", suit.stateName());
    }

    @Test void nanotechShieldBlocksMoreDamage() {
        assertTrue(new MarkL().blockingEfficiency() > new MarkIII().blockingEfficiency());
    }

    @Test void strategyUsesEnemyArmor() {
        Enemy armored = new Enemy("Drone", 100, 2, true);
        Enemy normal = new Enemy("Guard", 100, 2, false);
        assertEquals(38, new MissileStrategy().calculateDamage(armored));
        assertEquals(30, new MissileStrategy().calculateDamage(normal));
        assertEquals(33, new NanoBladeStrategy().calculateDamage(armored));
        assertEquals(20, new RepulsorStrategy().calculateDamage(armored));
    }

    @Test void suitCannotChangeInCombat() {
        StarkSuitFacade facade = new StarkSuitFacade();
        assertTrue(facade.selectSuit(SuitType.MARK_III).success());
        assertTrue(facade.activate().success());
        assertTrue(facade.startMission(MissionScenario.MONACO).success());
        assertFalse(facade.selectSuit(SuitType.MARK_L).success());
        assertTrue(facade.retreat().success());
        assertTrue(facade.selectSuit(SuitType.MARK_L).success());
    }

    @Test void suitCannotRepairOrRechargeDuringCombat() {
        StarkSuitFacade facade = new StarkSuitFacade();
        facade.selectSuit(SuitType.MARK_III);
        facade.activate();
        facade.startMission(MissionScenario.EXPO);
        assertFalse(facade.repair().success());
        assertFalse(facade.recharge().success());
    }

    @Test void actionsWithoutSuitFailClearly() {
        StarkSuitFacade facade = new StarkSuitFacade();
        assertFalse(facade.attack().success());
        assertFalse(facade.activate().success());
        assertFalse(facade.startMission(MissionScenario.MONACO).success());
        assertFalse(facade.selectWeapon(WeaponType.REPULSOR).success());
    }

    @Test void invalidMissionOrWeaponFails() {
        StarkSuitFacade facade = new StarkSuitFacade();
        facade.selectSuit(SuitType.MARK_V);
        assertFalse(facade.selectWeapon(WeaponType.NANO_BLADE).success());
        assertFalse(facade.selectWeapon(null).success());
        facade.activate();
        assertFalse(facade.startMission(null).success());
    }

    @Test void missionHistorySurvivesSuitChange() {
        StarkSuitFacade facade = new StarkSuitFacade();
        facade.selectSuit(SuitType.MARK_L);
        facade.activate();
        facade.startMission(MissionScenario.MONACO);
        facade.selectWeapon(WeaponType.UNIBEAM);
        assertTrue(facade.attack().success());
        assertTrue(facade.attack().success());
        assertTrue(facade.missionHistory().contains("SUCCESS"));
        facade.selectSuit(SuitType.MARK_V);
        assertTrue(facade.missionHistory().contains("SUCCESS"));
    }

    @Test void missionStatusShowsEnemyDuringCombat() {
        StarkSuitFacade facade = new StarkSuitFacade();
        facade.selectSuit(SuitType.MARK_III);
        assertFalse(facade.status().contains("Enemy:"));
        facade.activate();
        facade.startMission(MissionScenario.MONACO);
        assertTrue(facade.status().contains("Enemy: Whiplash"));
    }

    @Test void defenseUsesEnergyAndArmor() {
        StarkSuitFacade facade = new StarkSuitFacade();
        facade.selectSuit(SuitType.MARK_III);
        facade.activate();
        facade.startMission(MissionScenario.MONACO);
        assertTrue(facade.defend().success());
        assertTrue(facade.status().contains("Energy: 95/100"));
        assertTrue(facade.status().contains("Armor: 111/120"));
    }
}
