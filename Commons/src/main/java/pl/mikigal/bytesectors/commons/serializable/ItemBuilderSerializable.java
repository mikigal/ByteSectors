package pl.mikigal.bytesectors.commons.serializable;

import java.io.Serializable;

public class ItemBuilderSerializable implements Serializable {

    private String material;
    private int amount;
    private short durability;
    private String name;
    private String[] lore;

    public ItemBuilderSerializable(String material, int amount, short durability, String name, String[] lore) {
        this.material = material;
        this.amount = amount;
        this.durability = durability;
        this.name = name;
        this.lore = lore;
    }

    public String getMaterial() {
        return material;
    }

    public int getAmount() {
        return amount;
    }

    public short getDurability() {
        return durability;
    }

    public String getName() {
        return name;
    }

    public String[] getLore() {
        return lore;
    }
}
