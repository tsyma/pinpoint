package com.navercorp.pinpoint.profiler.skipper;

import com.navercorp.pinpoint.bootstrap.sampler.Skipper;

/**
 * Created by Oleg Tsymaenko on 11/22/16.
 */
public class AlwaysLogSkipper implements Skipper {//always pass (always log)
    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public boolean needToLog(int elapsedTimeInMsec, boolean isException) {
        return true;
    }

    @Override
    public String toString() {
        return "AlwaysLogSkipper";
    }
}
