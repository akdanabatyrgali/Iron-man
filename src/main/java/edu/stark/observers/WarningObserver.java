package edu.stark.observers;

import java.util.ArrayList;
import java.util.List;

public class WarningObserver implements SuitObserver {
    private final String assistant;
    private final List<String> pendingWarnings = new ArrayList<>();
    private boolean lowEnergy;
    private boolean lowArmor;

    public WarningObserver(String assistant) { this.assistant = assistant; }

    @Override
    public void onSuitChanged(SuitStatus status) {
        boolean energyLowNow = status.energy() <= 20;
        boolean armorLowNow = status.armor() <= 30;
        if (energyLowNow && !lowEnergy) {
            pendingWarnings.add("[" + assistant + "] WARNING: Energy critically low (" + status.energy() + ").");
        }
        if (armorLowNow && !lowArmor) {
            pendingWarnings.add("[" + assistant + "] WARNING: Armor integrity critical (" + status.armor() + ").");
        }
        lowEnergy = energyLowNow;
        lowArmor = armorLowNow;
    }

    public String drain() {
        String result = String.join("\n", pendingWarnings);
        pendingWarnings.clear();
        return result;
    }
}
