package edu.stark.suits;

import edu.stark.weapons.WeaponType;
import java.util.EnumSet;

/** Portable suitcase armor: fewer weapons and less protection. */
public class MarkV extends IronManSuit {
    public MarkV() {
        super(SuitType.MARK_V, 90, 75, "J.A.R.V.I.S.",
                EnumSet.of(WeaponType.REPULSOR));
    }
}