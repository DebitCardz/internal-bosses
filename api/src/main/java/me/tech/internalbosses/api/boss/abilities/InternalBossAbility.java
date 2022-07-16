package me.tech.internalbosses.api.boss.abilities;

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
        return (Math.random() * 100) > (100.0 - chance);
    }
}
