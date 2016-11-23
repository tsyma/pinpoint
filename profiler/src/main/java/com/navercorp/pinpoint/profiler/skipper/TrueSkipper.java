package com.navercorp.pinpoint.profiler.skipper;

import com.navercorp.pinpoint.bootstrap.sampler.Skipper;

/**
 * Created by Oleg Tsymaenko on 11/22/16.
 */
public class TrueSkipper implements Skipper {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public int getSkipLessThan() {
        return 0;
    }

    @Override
    public boolean needToSkip(int elapsedTimeInMsec) {
        return false;
    }

    @Override
    public boolean needToLog(int elapsedTimeInMsec) {
        return true;
    }

    @Override
    public String toString() {
        return "TrueSkipper";
    }
}
