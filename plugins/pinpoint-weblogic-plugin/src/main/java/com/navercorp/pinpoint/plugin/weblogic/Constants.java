package com.navercorp.pinpoint.plugin.weblogic;

import com.navercorp.pinpoint.common.trace.ServiceType;
import com.navercorp.pinpoint.common.trace.ServiceTypeFactory;
import com.navercorp.pinpoint.common.trace.ServiceTypeProperty;

/**
 * @author Roman Stepatskyi
 */
public final class Constants {
    private Constants() {}

    public static final String TYPE_NAME = "WEBLOGIC";

    public static final ServiceType WEBLOGIC = ServiceTypeFactory.of(1050, "WEBLOGIC", ServiceTypeProperty.RECORD_STATISTICS);
    public static final ServiceType WEBLOGIC_METHOD = ServiceTypeFactory.of(1051, "WEBLOGIC_METHOD");

    public static final String METADATA_TRACE = "trace";
    public static final String METADATA_ASYNC = "async";
    public static final String METADATA_ASYNC_TRACE_ID = "asyncTraceId";

    public static final String ATTRIBUTE_PINPOINT_TRACE = "PINPOINT_TRACE";
}
