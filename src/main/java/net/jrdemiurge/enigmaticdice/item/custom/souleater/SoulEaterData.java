package net.jrdemiurge.enigmaticdice.item.custom.souleater;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.jrdemiurge.enigmaticdice.Config;

public class SoulEaterData {
    public static final Codec<SoulEaterData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("hpCount").forGetter(SoulEaterData::getHpCount),
            Codec.INT.fieldOf("timeLeftTicks").forGetter(data -> data.timeLeftTicks)
    ).apply(instance, SoulEaterData::new));

    private float hpCount;
    private int timeLeftTicks;

    // Constructor para el Codec
    public SoulEaterData(float hpCount, int timeLeftTicks) {
        this.hpCount = hpCount;
        this.timeLeftTicks = timeLeftTicks;
    }

    // Constructor por defecto para nuevas instancias
    public SoulEaterData() {
        this(0.0f, -10);
    }

    public void onKill(float additionalHp) {
        this.hpCount = additionalHp;
        this.timeLeftTicks = 20 * Config.SoulEaterMaxHealthBuffDuration;
    }

    public void tick() {
        if (timeLeftTicks > 0) {
            timeLeftTicks--;
        }
    }

    public boolean isExpired() {
        return timeLeftTicks <= 0 && timeLeftTicks != -10;
    }

    public float getHpCount() {
        return hpCount;
    }

    public void reset() {
        this.hpCount = 0.0f;
        this.timeLeftTicks = -10;
    }
}