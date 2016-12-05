package com.navercorp.pinpoint.web.filter.useraddres;

import com.navercorp.pinpoint.common.server.bo.SpanBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Oleg Tsymaenko on 12/5/16.
 */
public class UserAddressFilterImpl implements UserAddressFilter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String userAddress;

    public UserAddressFilterImpl(String userAddress) {
        if (userAddress == null){
            throw  new NullPointerException("userAddress should be not null");
        }
        this.userAddress=userAddress;
    }

    @Override
    public boolean accept(SpanBo spanBo) {
//        for (SpanBo spanBo : spanBoLi) {
            if (logger.isDebugEnabled()) {
                logger.debug("userAddress:{} getRemoteAddr:{}", userAddress, spanBo.getRemoteAddr());
//            }
            if (userAddress.equalsIgnoreCase(spanBo.getRemoteAddr())){
                return ACCEPT;
            }
        }
        return REJECT;
    }

    @Override
    public String toString() {
        return "UserAddressFilterImpl{" +
                "userAddress='" + userAddress + '\'' +
                '}';
    }
}
