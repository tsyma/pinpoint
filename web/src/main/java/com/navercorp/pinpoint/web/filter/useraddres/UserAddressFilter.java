package com.navercorp.pinpoint.web.filter.useraddres;

import com.navercorp.pinpoint.common.server.bo.SpanBo;

import java.util.List;

/**
 * @author emeroad
 */
public interface UserAddressFilter {

    boolean ACCEPT = true;
    boolean REJECT = false;

    boolean accept(SpanBo spanBo);
}
