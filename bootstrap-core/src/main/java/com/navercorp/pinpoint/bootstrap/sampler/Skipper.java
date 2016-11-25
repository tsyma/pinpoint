package com.navercorp.pinpoint.bootstrap.sampler;

/**
 * Created by Oleg Tsymaenko on 11/22/16.
 */
public interface Skipper {
    boolean isEnabled();

    int getSkipElapsedTimeInMsecLessThan();

    boolean isAlwaysLogExceptions();

    boolean needToLog(int elapsedTimeInMsec, boolean isException);
}
