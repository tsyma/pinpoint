package com.navercorp.pinpoint.profiler.skipper;

import com.navercorp.pinpoint.bootstrap.sampler.Skipper;

/**
 * Created by Oleg Tsymaenko on 11/22/16.
 */
public class RegularSkipper implements Skipper {
    private final int skipElapsedTimeInMsecLessThan;
    private final String toString;
    private final boolean isAlwaysLogExceptions;

    public RegularSkipper( int skipElapsedTimeInMsecLessThan, boolean isAlwaysLogExceptions) {
        this.skipElapsedTimeInMsecLessThan = skipElapsedTimeInMsecLessThan;
        this.isAlwaysLogExceptions = isAlwaysLogExceptions;
        this.toString = "RegularSkipper{skipElapsedTimeInMsecLessThan=" + skipElapsedTimeInMsecLessThan + ", isAlwaysLogExceptions=" + isAlwaysLogExceptions + '}';
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean needToLog(int elapsedTimeInMsec, boolean isException) {
        return needToLogByElapsedTime(elapsedTimeInMsec) || needToLogByException(isException);
    }

    private boolean needToLogByException(boolean isException) {
        return isAlwaysLogExceptions && isException;
    }

    private boolean needToLogByElapsedTime(int elapsedTimeInMsec) {
        return elapsedTimeInMsec >= skipElapsedTimeInMsecLessThan;
    }

    @Override
    public String toString() {
        return toString;
    }
}
