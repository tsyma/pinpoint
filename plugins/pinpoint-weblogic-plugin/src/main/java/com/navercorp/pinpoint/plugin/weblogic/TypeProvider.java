package com.navercorp.pinpoint.plugin.weblogic;

import com.navercorp.pinpoint.common.trace.TraceMetadataProvider;
import com.navercorp.pinpoint.common.trace.TraceMetadataSetupContext;

/**
 * @author Roman Stepatskyi
 */
public class TypeProvider implements TraceMetadataProvider {

    @Override
    public void setup(TraceMetadataSetupContext traceMetadataSetupContext) {
        traceMetadataSetupContext.addServiceType(Constants.WEBLOGIC);
        traceMetadataSetupContext.addServiceType(Constants.WEBLOGIC_METHOD);
    }
}
