package vc.andro.poketest;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ObjectFloatMap;

public class Timers {

    public enum Id {
        ;

        public final float startTime;

        Id(float startTime) {
            this.startTime = startTime;
        }
    }

    private ObjectFloatMap<Id> timers;

    public Timers() {
        timers = new ObjectFloatMap<>();
        for (Id id : Id.values()) {
            timers.put(id, id.startTime);
        }
    }

    public void tickTimers(float delta) {
        for (Id id : timers.keys()) {
            float value = timers.get(id, 0f);
            timers.put(id, Math.max(value - delta, 0f));
        }
    }

    public boolean isTimerExpired(Id id) {
        float timerValue = timers.get(id, Float.NaN);
        assert !Float.isNaN(timerValue) : "timer missing??";
        return MathUtils.isEqual(timerValue, 0f);
    }

    public void resetTimer(Id id) {
        timers.put(id, id.startTime);
    }
}
