package com.pokemon.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "natures")
@Data
public class Nature {

    @Id
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "increased_stat", length = 20)
    private String increasedStat = null;

    @Column(name = "decreased_stat", length = 20)
    private String decreasedStat = null;

    @PrePersist
    @PreUpdate
    private void normalizeStats() {
        if (increasedStat != null && increasedStat.equals(decreasedStat)) {
            increasedStat = null;
            decreasedStat = null;
        }
    }

}
