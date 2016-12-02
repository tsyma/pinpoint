package com.navercorp.pinpoint.bootstrap.sampler;

/**
 * Created by Oleg Tsymaenko on 11/22/16.
 */
public interface Skipper {
    boolean isEnabled();

    boolean needToLog(int elapsedTimeInMsec, boolean isException);
}
