package me.tech.internalbosses.api.boss.abilities;

import me.tech.internalbosses.api.Chance;

public interface InternalBossAbility {
    /**
     * @return Ability ID.
     */
    String getId();

    /**
     * @param chance Chance between 1 and 100.
     * @return Whether the random outcome was successful.
     */
    static boolean successfulRandom(double chance) {
        return Chance.successfulRandom(chance);
    }
}
