package com.navercorp.pinpoint.profiler.skipper;

import com.navercorp.pinpoint.bootstrap.sampler.Skipper;

/**
 * Created by Oleg Tsymaenko on 11/22/16.
 */
public class SkipperFactory {
    private final boolean isSkipperEnabled;
    private final int skipElapsedTimeInMsecLessThan;
    private final boolean isAlwaysLogExceptions;

    public SkipperFactory(boolean isSkipperEnabled, int skipElapsedTimeInMsecLessThan, boolean isAlwaysLogExceptions) {
        this.isSkipperEnabled = isSkipperEnabled;
        this.skipElapsedTimeInMsecLessThan = skipElapsedTimeInMsecLessThan;
        this.isAlwaysLogExceptions = isAlwaysLogExceptions;
    }

    public Skipper createSkipper() {
        if (!isSkipperEnabled || skipElapsedTimeInMsecLessThan <= 0) {
            return new FalseSkipper();
        }
        if (skipElapsedTimeInMsecLessThan == 1) {
            return new TrueSkipper();
        }
        return new DefaultSkipper(true, skipElapsedTimeInMsecLessThan, isAlwaysLogExceptions);
    }
}
