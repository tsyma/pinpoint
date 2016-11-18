package com.navercorp.pinpoint.plugin.weblogic;

import com.navercorp.pinpoint.bootstrap.plugin.ApplicationTypeDetector;
import com.navercorp.pinpoint.bootstrap.resolver.ConditionProvider;
import com.navercorp.pinpoint.common.trace.ServiceType;

/**
 * @author Roman Stepatskyi
 */
public class WeblogicDetector implements ApplicationTypeDetector {

    private static final String MAIN_CLASS = "weblogic.Server";

    @Override
    public ServiceType getApplicationType() {
        return Constants.WEBLOGIC;
    }

    @Override
    public boolean detect(ConditionProvider conditionProvider) {
        return conditionProvider.checkMainClass(MAIN_CLASS);
    }
}
