package com.pokemon.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Entity representing a Pokémon Nature.
 * Each nature can increase one stat and decrease another, or have no effect.
 */
@Entity
@Table(name = "natures")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Nature {

    /**
     * Unique identifier for the nature.
     */
    @Id
    @EqualsAndHashCode.Include
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    /**
     * Name of the nature.
     */
    @NotBlank
    @Size(max = 50)
    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    /**
     * Stat that is increased by this nature.
     * Can be null if the nature does not increase any stat.
     */
    @Size(max = 20)
    @Column(name = "increased_stat", length = 20)
    private String increasedStat = null;

    /**
     * Stat that is decreased by this nature.
     * Can be null if the nature does not decrease any stat.
     */
    @Size(max = 20)
    @Column(name = "decreased_stat", length = 20)
    private String decreasedStat = null;

    /**
     * Normalizes the increased and decreased stats.
     * If both are the same, they are set to null to indicate no effect.
     */
    @PrePersist
    @PreUpdate
    private void normalizeStats() {
        if (increasedStat != null && increasedStat.equals(decreasedStat)) {
            throw new IllegalArgumentException("Increased and decreased stats cannot be the same.");
        }
    }

}
