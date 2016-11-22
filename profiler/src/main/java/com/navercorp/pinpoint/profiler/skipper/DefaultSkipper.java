package com.navercorp.pinpoint.profiler.skipper;

import com.navercorp.pinpoint.bootstrap.sampler.Skipper;

/**
 * Created by Oleg Tsymaenko on 11/22/16.
 */
public class DefaultSkipper implements Skipper {
    private final boolean enabled;
    private final int skipLessThan;

    public DefaultSkipper(boolean enabled, int skipLessThan) {
        this.enabled = enabled;
        this.skipLessThan = skipLessThan;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public int getSkipLessThan() {
        return skipLessThan;
    }

    @Override
    public boolean needToSkip(int elapsedTimeInMsec) {
        return enabled && elapsedTimeInMsec < skipLessThan;
    }

    @Override
    public boolean needToLog(int elapsedTimeInMsec) {
        return !needToSkip(elapsedTimeInMsec);
    }

    @Override
    public String toString() {
        return "DefaultSkipper{" +
                "enabled=" + enabled +
                ", skipLessThan=" + skipLessThan +
                '}';
    }
}
