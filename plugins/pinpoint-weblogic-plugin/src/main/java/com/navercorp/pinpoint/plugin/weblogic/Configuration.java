package com.navercorp.pinpoint.plugin.weblogic;

import com.navercorp.pinpoint.bootstrap.config.ExcludePathFilter;
import com.navercorp.pinpoint.bootstrap.config.Filter;
import com.navercorp.pinpoint.bootstrap.config.ProfilerConfig;
import com.navercorp.pinpoint.bootstrap.config.SkipFilter;

/**
 * @author Roman Stepatskyi
 */
public class Configuration {

    private final Filter<String> weblogicExcludeUrlFilter;

    public Configuration(ProfilerConfig config) {
        final String weblogicExcludeURL = config.readString("profiler.weblogic.excludeurl", "");

        if (!weblogicExcludeURL.isEmpty()) {
            this.weblogicExcludeUrlFilter = new ExcludePathFilter(weblogicExcludeURL);
        } else{
            this.weblogicExcludeUrlFilter = new SkipFilter<String>();
        }
    }

    public Filter<String> getWeblogicExcludeUrlFilter() {
        return weblogicExcludeUrlFilter;
    }
}
