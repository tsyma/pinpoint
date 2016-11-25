package com.navercorp.pinpoint.profiler.skipper;

import com.navercorp.pinpoint.bootstrap.sampler.Skipper;

/**
 * Created by Oleg Tsymaenko on 11/22/16.
 */
public class DefaultSkipper implements Skipper {
    private final boolean isSkipperEnabled;
    private final int skipElapsedTimeInMsecLessThan;
    private final String toString;
    private final boolean isAlwaysLogExceptions;

    public DefaultSkipper(boolean isSkipperEnabled, int skipElapsedTimeInMsecLessThan, boolean isAlwaysLogExceptions) {
        this.isSkipperEnabled = isSkipperEnabled;
        this.skipElapsedTimeInMsecLessThan = skipElapsedTimeInMsecLessThan;
        this.isAlwaysLogExceptions = isAlwaysLogExceptions;

        this.toString = "DefaultSkipper{enabled=" + isSkipperEnabled
                + ", skipElapsedTimeInMsecLessThan=" + skipElapsedTimeInMsecLessThan
                + ", isAlwaysLogExceptions=" + isAlwaysLogExceptions
                + '}';
    }

    @Override
    public boolean isEnabled() {
        return isSkipperEnabled;
    }

    @Override
    public int getSkipElapsedTimeInMsecLessThan() {
        return skipElapsedTimeInMsecLessThan;
    }

    @Override
    public boolean isAlwaysLogExceptions() {
        return isAlwaysLogExceptions;
    }

    @Override
    public boolean needToLog(int elapsedTimeInMsec, boolean isException) {
        return !isSkipperEnabled || needToLogByElapsedTime(elapsedTimeInMsec) || needToLogByException(isException);
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
