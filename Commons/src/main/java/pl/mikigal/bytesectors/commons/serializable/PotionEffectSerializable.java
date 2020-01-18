package pl.mikigal.bytesectors.commons.serializable;

import java.io.Serializable;

public class PotionEffectSerializable implements Serializable {

    private String potionType;
    private int amplifier;
    private int duration;

    public PotionEffectSerializable(String potionType, int amplifier, int duration) {
        this.potionType = potionType;
        this.amplifier = amplifier;
        this.duration = duration;
    }

    public String getPotionType() {
        return potionType;
    }

    public int getAmplifier() {
        return amplifier;
    }

    public int getDuration() {
        return duration;
    }
}
