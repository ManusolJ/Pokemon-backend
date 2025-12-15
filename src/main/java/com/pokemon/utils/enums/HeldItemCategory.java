package com.pokemon.utils.enums;

public enum HeldItemCategory {
    STAT_BOOSTS("stat-boosts"),
    TYPE_PROTECTION("type-protection"),
    HELD_ITEMS("held-items"),
    CHOICE("choice"),
    PLATES("plates"),
    SPECIES_SPECIFIC("species-specific"),
    TYPE_ENHANCEMENT("type-enhancement"),
    SCARVES("scarves"),
    JEWELS("jewels"),
    MEGA_STONES("mega-stones"),
    MEMORIES("memories"),
    Z_CRYSTALS("z-crystals"),
    DYNAMAX_CRYSTALS("dynamax-crystals"),
    TERA_SHARD("tera-shard"),
    BAD_HELD_ITEMS("bad-held-items");

    private final String categoryName;

    HeldItemCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public static HeldItemCategory fromString(String categoryName) {
        for (HeldItemCategory category : values()) {
            if (category.categoryName.equals(categoryName)) {
                return category;
            }
        }
        throw new IllegalArgumentException("No category with name: " + categoryName);
    }

    public static boolean isHeldItemCategory(String categoryName) {
        for (HeldItemCategory category : values()) {
            if (category.categoryName.equals(categoryName)) {
                return true;
            }
        }
        return false;
    }
}
