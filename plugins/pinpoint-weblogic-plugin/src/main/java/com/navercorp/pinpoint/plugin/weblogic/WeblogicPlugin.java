package com.navercorp.pinpoint.plugin.weblogic;

import com.navercorp.pinpoint.bootstrap.instrument.InstrumentClass;
import com.navercorp.pinpoint.bootstrap.instrument.InstrumentException;
import com.navercorp.pinpoint.bootstrap.instrument.InstrumentMethod;
import com.navercorp.pinpoint.bootstrap.instrument.Instrumentor;
import com.navercorp.pinpoint.bootstrap.instrument.transformer.TransformCallback;
import com.navercorp.pinpoint.bootstrap.instrument.transformer.TransformTemplate;
import com.navercorp.pinpoint.bootstrap.instrument.transformer.TransformTemplateAware;
import com.navercorp.pinpoint.bootstrap.plugin.ProfilerPlugin;
import com.navercorp.pinpoint.bootstrap.plugin.ProfilerPluginSetupContext;
import com.navercorp.pinpoint.common.util.VarArgs;

import java.security.ProtectionDomain;

/**
 * @author Roman Stepatskyi
 */
public class WeblogicPlugin implements ProfilerPlugin, TransformTemplateAware {

    private TransformTemplate transformTemplate;

    @Override
    public void setup(ProfilerPluginSetupContext profilerPluginSetupContext) {
        profilerPluginSetupContext.addApplicationTypeDetector(new WeblogicDetector());
        addWeblogicInterceptor(new Configuration(profilerPluginSetupContext.getConfig()));
    }

    private void addWeblogicInterceptor(final Configuration configuration) {
        transformTemplate.transform("weblogic.servlet.internal.WebAppServletContext", new TransformCallback() {
            @Override
            public byte[] doInTransform(Instrumentor instrumentor, ClassLoader classLoader, String className, Class<?> aClass,
                                        ProtectionDomain protectionDomain, byte[] buffer) throws InstrumentException {
                final InstrumentClass targetClass = instrumentor.getInstrumentClass(classLoader, className, buffer);
                final InstrumentMethod targetMethod = targetClass.getDeclaredMethod("execute",
                        "weblogic.servlet.internal.ServletRequestImpl", "weblogic.servlet.internal.ServletResponseImpl");
                if (targetMethod != null) {
                    targetMethod.addInterceptor("com.navercorp.pinpoint.plugin.weblogic.interceptor.WeblogicExecuteInterceptor", VarArgs.va(configuration.getWeblogicExcludeUrlFilter()));
                }
                return targetClass.toBytecode();
            }
        });
    }

    @Override
    public void setTransformTemplate(TransformTemplate transformTemplate) {
        this.transformTemplate = transformTemplate;
    }
}
