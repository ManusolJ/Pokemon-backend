package com.pokemon.utils.enums;

public enum DamageClass {
    physical,
    special,
    status;

    public static DamageClass fromString(String value) {
        for (DamageClass damageClass : DamageClass.values()) {
            if (damageClass.name().equalsIgnoreCase(value)) {
                return damageClass;
            }
        }
        throw new IllegalArgumentException("Unknown DamageClass: " + value);
    }
}
