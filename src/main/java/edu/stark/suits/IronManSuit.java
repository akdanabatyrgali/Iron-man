package edu.stark.suits;

import edu.stark.core.ActionResult;
import edu.stark.observers.SuitObserver;
import edu.stark.observers.SuitStatus;
import edu.stark.states.DamagedState;
import edu.stark.states.StandbyState;
import edu.stark.states.SuitState;
import edu.stark.weapons.WeaponType;

import java.util.*;

public abstract class IronManSuit {
    private final SuitType type;
    private final int maxEnergy;
    private final int maxArmor;
    private final String assistant;
    private final Set<WeaponType> supportedWeapons;
    private final List<SuitObserver> observers = new ArrayList<>();
    private int energy;
    private int armor;
    private SuitState state = new StandbyState();

    protected IronManSuit(SuitType type, int maxEnergy, int maxArmor,
                          String assistant, Set<WeaponType> supportedWeapons) {
        if (maxEnergy <= 0 || maxArmor <= 0) {
            throw new IllegalArgumentException("Suit energy and armor must be positive.");
        }
        this.type = Objects.requireNonNull(type, "type");
        this.maxEnergy = maxEnergy;
        this.maxArmor = maxArmor;
        this.assistant = Objects.requireNonNull(assistant, "assistant");
        this.supportedWeapons = Collections.unmodifiableSet(
                EnumSet.copyOf(Objects.requireNonNull(supportedWeapons, "supportedWeapons")));
        this.energy = maxEnergy;
        this.armor = maxArmor;
    }
    public SuitType type() { return type; }
    public String name() { return type.displayName(); }
    public String assistant() { return assistant; }
    public int energy() { return energy; }
    public int armor() { return armor; }
    public int maxEnergy() { return maxEnergy; }
    public int maxArmor() { return maxArmor; }
    public String stateName() { return state.name(); }
    public boolean canAttack() { return state.canAttack(); }
    public Set<WeaponType> supportedWeapons() { return supportedWeapons; }
    public boolean supports(WeaponType weapon) { return supportedWeapons.contains(weapon); }
    public double blockingEfficiency() { return 0.50; }
    public String defenseDescription() { return "Defensive stance"; }


    public void addObserver(SuitObserver observer) {
        Objects.requireNonNull(observer, "observer");
        if (!observers.contains(observer)) {
            observers.add(observer);
            observer.onSuitChanged(snapshot());
        }
    }

    public void removeObserver(SuitObserver observer) {
        observers.remove(observer);
    }

    public void transitionFrom(SuitState currentState, SuitState nextState) {
        Objects.requireNonNull(nextState, "nextState");
        if (state != currentState) {
            throw new IllegalStateException("State changed before this transition.");
        }
        state = nextState;
        notifyObservers();
    }

    public boolean useEnergy(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Negative energy cost");
        }
        if (energy < amount) {
            return false;
        }
        energy -= amount;
        notifyObservers();
        return true;
    }

    public void takeDamage(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Negative damage");
        }
        armor = Math.max(0, armor - amount);
        if (armor == 0) {
            state = new DamagedState();
        }
        notifyObservers();
    }
    public void restoreEnergy() { energy = maxEnergy; notifyObservers(); }
    public void restoreArmor() { armor = maxArmor; notifyObservers(); }

    public ActionResult activate() { return state.activate(this); }
    public ActionResult startMission() { return state.startMission(this); }
    public ActionResult endMission() { return state.endMission(this); }
    public ActionResult fly() { return state.fly(this); }
    public ActionResult recharge() { return state.recharge(this); }
    public ActionResult repair() { return state.repair(this); }

    private SuitStatus snapshot() {
        return new SuitStatus(type, energy, armor, state.name());
    }

    private void notifyObservers() {
        SuitStatus status = snapshot();

        for (SuitObserver observer : List.copyOf(observers)) {
            observer.onSuitChanged(status);
        }
    }

}

