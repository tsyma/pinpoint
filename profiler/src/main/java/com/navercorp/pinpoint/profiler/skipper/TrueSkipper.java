package com.navercorp.pinpoint.profiler.skipper;

import com.navercorp.pinpoint.bootstrap.sampler.Skipper;

/**
 * Created by Oleg Tsymaenko on 11/22/16.
 */
public class TrueSkipper implements Skipper {//always log
    @Override
    public boolean isEnabled() {
        return true;
    }

    public int getSkipElapsedTimeInMsecLessThan() {
        return 0;
    }
    @Override
    public boolean isAlwaysLogExceptions() {
        return true;
    }

    @Override
    public boolean needToLog(int elapsedTimeInMsec, boolean isException) {
        return true;
    }


    @Override
    public String toString() {
        return "TrueSkipper";
    }
}
