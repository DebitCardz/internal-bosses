package me.tech.internalbosses.plugin.boss;

import lombok.Getter;
import me.tech.internalbosses.api.boss.abilities.InternalBossAbility;
import me.tech.internalbosses.api.boss.abilities.InternalBossAbilityManager;
import me.tech.internalbosses.api.premade.abilities.FireballAbility;

import java.util.*;

@Getter
public class InternalBossAbilityManagerImpl implements InternalBossAbilityManager {
    private final Set<InternalBossAbility> abilities = new HashSet<>();

    @Override
    public void addAbility(InternalBossAbility ability) {
        abilities.add(ability);
    }

    public InternalBossAbilityManagerImpl() {
        this.abilities.add(new FireballAbility());
    }


    public Optional<InternalBossAbility> getById(String id) {
        return abilities.stream().filter(ability -> ability.getId().equalsIgnoreCase(id)).findFirst();
    }
}
