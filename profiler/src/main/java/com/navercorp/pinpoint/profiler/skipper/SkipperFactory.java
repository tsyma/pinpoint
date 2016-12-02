package com.navercorp.pinpoint.profiler.skipper;

import com.navercorp.pinpoint.bootstrap.sampler.Skipper;

/**
 * Created by Oleg Tsymaenko on 11/22/16.
 */
public class SkipperFactory {
    private final Skipper skipper;

    public SkipperFactory(boolean isSkipperEnabled, int skipElapsedTimeInMsecLessThan, boolean isAlwaysLogExceptions) {
        if (isSkipperEnabled && skipElapsedTimeInMsecLessThan > 1 || isSkipperEnabled && isAlwaysLogExceptions) {
            this.skipper = new RegularSkipper(skipElapsedTimeInMsecLessThan, isAlwaysLogExceptions);
        } else if (skipElapsedTimeInMsecLessThan == 1) {//always pass (always log)
            this.skipper = new NeverLogSkipper();
        } else { //always skip (never log)
            this.skipper = new AlwaysLogSkipper();
        }
    }

    public Skipper skipperInstance() {
        return skipper;
    }
}
