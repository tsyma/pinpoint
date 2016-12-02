package com.navercorp.pinpoint.profiler.skipper;

import com.navercorp.pinpoint.bootstrap.sampler.Skipper;

/**
 * Created by Oleg Tsymaenko on 11/22/16.
 */
public class NeverLogSkipper implements Skipper {//always log
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean needToLog(int elapsedTimeInMsec, boolean isException) {
        return false;
    }


    @Override
    public String toString() {
        return "NeverLogSkipper";
    }
}
