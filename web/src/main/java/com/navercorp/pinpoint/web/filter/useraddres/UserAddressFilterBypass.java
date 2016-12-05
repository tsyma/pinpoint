package com.navercorp.pinpoint.web.filter.useraddres;

import com.navercorp.pinpoint.common.server.bo.SpanBo;

import java.util.List;

/**
 * Created by Oleg Tsymaenko on 12/5/16.
 */
public class UserAddressFilterBypass implements UserAddressFilter {

    @Override
    public boolean accept(SpanBo spanBoLi) {
        return ACCEPT;
    }

    @Override
    public String toString() {
        return "UserAddressFilterBypass{}";
    }
}
