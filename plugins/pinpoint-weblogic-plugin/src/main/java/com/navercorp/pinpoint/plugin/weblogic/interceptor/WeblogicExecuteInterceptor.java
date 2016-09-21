package com.navercorp.pinpoint.plugin.weblogic.interceptor;

import com.navercorp.pinpoint.bootstrap.config.Filter;
import com.navercorp.pinpoint.bootstrap.context.MethodDescriptor;
import com.navercorp.pinpoint.bootstrap.context.TraceContext;
import com.navercorp.pinpoint.bootstrap.interceptor.annotation.TargetMethod;
import weblogic.servlet.internal.ServletRequestImpl;

/**
 * @author Roman Stepatskyi
 */
@TargetMethod(name = "execute", paramTypes = { "weblogic.servlet.internal.ServletRequestImpl" })
public class WeblogicExecuteInterceptor extends AbstractWeblogicExecuteInterceptor {

    public WeblogicExecuteInterceptor(TraceContext traceContext, MethodDescriptor descriptor, Filter<String> excludeFilter) {
        super(traceContext, descriptor, excludeFilter);
    }

    @Override
    protected ServletRequestImpl getRequest(Object[] args) {
        final Object requestObject = args[0];
        if (!(requestObject instanceof ServletRequestImpl)) {
            return null;
        }
        return (ServletRequestImpl) requestObject;
    }
}
