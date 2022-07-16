package me.tech.internalbosses.api;

public final class Chance {
    private Chance() { }

    /**
     * @param chance Chance between 1 and 100.
     * @return Whether the random outcome was successful.
     */
    public static boolean successfulRandom(double chance) {
        return (Math.random() * 100) > (100.0 - chance);
    }
}
