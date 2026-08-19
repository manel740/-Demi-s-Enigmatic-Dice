package net.jrdemiurge.enigmaticdice.item.custom.unequalexchange;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.jrdemiurge.enigmaticdice.Config;

public class UnequalExchangeData {
    public static final Codec<UnequalExchangeData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("hitCount").forGetter(UnequalExchangeData::getHitCount),
            Codec.INT.fieldOf("timeLeftTicks").forGetter(UnequalExchangeData::getTimeLeftTicks)
    ).apply(instance, UnequalExchangeData::new));

    private int hitCount;
    private int timeLeftTicks;

    public UnequalExchangeData(int hitCount, int timeLeftTicks) {
        this.hitCount = hitCount;
        this.timeLeftTicks = timeLeftTicks;
    }

    public UnequalExchangeData() {
        this(0, -10);
    }

    public void onHit() {
        hitCount++;
        timeLeftTicks = 20 * Config.UnequalExchangeDebuffDuration;
    }

    public void tick() {
        if (timeLeftTicks > 0) {
            timeLeftTicks--;
        }
    }

    public boolean isExpired() {
        return timeLeftTicks <= 0 && timeLeftTicks != -10;
    }

    public int getHitCount() { return hitCount; }
    public int getTimeLeftTicks() { return timeLeftTicks; }

    public void reset() {
        hitCount = 0;
        timeLeftTicks = -10;
    }
}