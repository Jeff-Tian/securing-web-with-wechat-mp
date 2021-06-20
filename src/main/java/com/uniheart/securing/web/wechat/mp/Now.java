package com.uniheart.securing.web.wechat.mp;

import java.time.Clock;
import java.time.Instant;

public class Now {
    private static final ThreadLocal<Clock> clock =
            ThreadLocal.withInitial(Clock::systemUTC);

    public static void setClock(Clock clock) {
        Now.clock.set(clock);
    }

    public static Instant instant() {
        return Instant.now(clock.get());
    }
}
