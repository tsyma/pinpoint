package com.navercorp.pinpoint.plugin.weblogic;

import com.navercorp.pinpoint.bootstrap.context.MethodDescriptor;
import com.navercorp.pinpoint.common.trace.MethodType;

/**
 * @author Roman Stepatskyi
 */
public class WeblogicSyncMethodDescriptor implements MethodDescriptor {

    private int apiId = 0;

    @Override
    public String getMethodName() {
        return "";
    }

    @Override
    public String getClassName() {
        return "";
    }

    @Override
    public String[] getParameterTypes() {
        return null;
    }

    @Override
    public String[] getParameterVariableName() {
        return null;
    }

    @Override
    public String getParameterDescriptor() {
        return "()";
    }

    @Override
    public int getLineNumber() {
        return -1;
    }

    @Override
    public String getFullName() {
        return WeblogicSyncMethodDescriptor.class.getName();
    }

    @Override
    public void setApiId(int i) {
        this.apiId = i;
    }

    @Override
    public int getApiId() {
        return apiId;
    }

    @Override
    public String getApiDescriptor() {
        return "Weblogic Servlet Process";
    }

    @Override
    public int getType() {
        return MethodType.WEB_REQUEST;
    }
}
