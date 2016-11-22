package com.navercorp.pinpoint.profiler.skipper;

import com.navercorp.pinpoint.bootstrap.sampler.Skipper;

/**
 * Created by Oleg Tsymaenko on 11/22/16.
 */
public class SkipperFactory {
    public Skipper createSkipper(boolean enabled, int skipLessThanMsec) {
        if (!enabled || skipLessThanMsec <= 0) {
            return new FalseSkipper();
        }
        if (skipLessThanMsec == 1) {
            return new TrueSkipper();
        }
        return new DefaultSkipper(true, skipLessThanMsec);
    }
}
