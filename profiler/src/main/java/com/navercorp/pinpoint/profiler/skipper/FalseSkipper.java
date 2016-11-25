package com.navercorp.pinpoint.profiler.skipper;

import com.navercorp.pinpoint.bootstrap.sampler.Skipper;

/**
 * Created by Oleg Tsymaenko on 11/22/16.
 */
public class FalseSkipper implements Skipper {//always skip
    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public int getSkipElapsedTimeInMsecLessThan() {
        return 0;
    }

    @Override
    public boolean isAlwaysLogExceptions() {
        return false;
    }

    @Override
    public boolean needToLog(int elapsedTimeInMsec, boolean isException) {
        return false;
    }

    @Override
    public String toString() {
        return "FalseSkipper";
    }
}
