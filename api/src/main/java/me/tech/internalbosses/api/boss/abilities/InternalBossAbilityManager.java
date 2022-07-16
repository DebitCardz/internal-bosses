package me.tech.internalbosses.api.boss.abilities;

import java.util.Optional;
import java.util.Set;

public interface InternalBossAbilityManager {
    /**
     * Add an {@link InternalBossAbility} to storage to be used by other bosses.
     */
    void addAbility(InternalBossAbility ability);

    /**
     * @param id {@link InternalBossAbility} ID.
     * @return {@link Optional<InternalBossAbility>}
     */
    Optional<InternalBossAbility> getById(String id);

    /**
     * @return All registered {@link InternalBossAbility}.
     */
    Set<InternalBossAbility> getAbilities();
}
