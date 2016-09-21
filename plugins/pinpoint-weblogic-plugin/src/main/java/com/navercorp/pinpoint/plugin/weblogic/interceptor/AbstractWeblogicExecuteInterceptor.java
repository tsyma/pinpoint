package com.navercorp.pinpoint.plugin.weblogic.interceptor;

import com.navercorp.pinpoint.bootstrap.config.Filter;
import com.navercorp.pinpoint.bootstrap.context.*;
import com.navercorp.pinpoint.bootstrap.interceptor.AroundInterceptor;
import com.navercorp.pinpoint.bootstrap.logging.PLogger;
import com.navercorp.pinpoint.bootstrap.logging.PLoggerFactory;
import com.navercorp.pinpoint.bootstrap.sampler.SamplingFlagUtils;
import com.navercorp.pinpoint.bootstrap.util.NetworkUtils;
import com.navercorp.pinpoint.bootstrap.util.NumberUtils;
import com.navercorp.pinpoint.bootstrap.util.StringUtils;
import com.navercorp.pinpoint.common.trace.AnnotationKey;
import com.navercorp.pinpoint.common.trace.ServiceType;
import com.navercorp.pinpoint.plugin.weblogic.Constants;
import com.navercorp.pinpoint.plugin.weblogic.WeblogicSyncMethodDescriptor;
import weblogic.servlet.internal.ServletRequestImpl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Roman Stepatskyi
 */
public abstract class AbstractWeblogicExecuteInterceptor implements AroundInterceptor {

    public static final WeblogicSyncMethodDescriptor WEBLOGIC_SYNC_API_TAG = new WeblogicSyncMethodDescriptor();

    protected PLogger logger = PLoggerFactory.getLogger(this.getClass());
    private final boolean isDebug = logger.isDebugEnabled();
    private final boolean isTrace = logger.isTraceEnabled();

    private final MethodDescriptor methodDescriptor;
    private final TraceContext traceContext;
    private final Filter<String> excludeUrlFilter;

    public AbstractWeblogicExecuteInterceptor(TraceContext traceContext, MethodDescriptor methodDescriptor, Filter<String> excludeUrlFilter) {
        this.traceContext = traceContext;
        this.methodDescriptor = methodDescriptor;
        this.excludeUrlFilter = excludeUrlFilter;
        traceContext.cacheApi(WEBLOGIC_SYNC_API_TAG);
    }

    protected abstract ServletRequestImpl getRequest(Object[] args);


    @Override
    public void before(Object target, Object[] args) {
        if (isDebug) {
            logger.beforeInterceptor(target, args);
        }

        try {
            final Trace trace = createTrace(target, args);
            if (trace == null) {
                return;
            }
            // TODO STATDISABLE this logic was added to disable statistics tracing
            if (!trace.canSampled()) {
                return;
            }
            // ------------------------------------------------------
            SpanEventRecorder recorder = trace.traceBlockBegin();
            recorder.recordServiceType(Constants.WEBLOGIC_METHOD);
        } catch (Throwable th) {
            if (logger.isWarnEnabled()) {
                logger.warn("[WEBLOGIC] before. Caused:{}", th.getMessage(), th);
            }
        }
    }

    @Override
    public void after(Object target, Object[] args, Object result, Throwable throwable) {
        if (isDebug) {
            logger.afterInterceptor(target, args, result, throwable);
        }

        final Trace trace = traceContext.currentRawTraceObject();
        if (trace == null) {
            return;
        }
        // TODO STATDISABLE this logic was added to disable statistics tracing
        if (!trace.canSampled()) {
            traceContext.removeTraceObject();
            return;
        }
        // ------------------------------------------------------
        try {
            SpanEventRecorder recorder = trace.currentSpanEventRecorder();
            final ServletRequestImpl request = getRequest(args);
            final String parameters = getRequestParameter(request, 64, 512);
            if (parameters != null && parameters.length() > 0) {
                recorder.recordAttribute(AnnotationKey.HTTP_PARAM, parameters);
            }

            recorder.recordApi(methodDescriptor);
            recorder.recordException(throwable);
        } catch (Throwable th) {
            if (logger.isWarnEnabled()) {
                logger.warn("[WEBLOGIC] after. Caused:{}", th.getMessage(), th);
            }
        } finally {
            traceContext.removeTraceObject();
            deleteTrace(trace, target, args, result, throwable);
        }
    }


    private Trace createTrace(Object target, Object[] args) {
        final ServletRequestImpl request = getRequest(args);

        final String requestURI = request.getRequestURI();
        if (excludeUrlFilter.filter(requestURI)) {
            if (isTrace) {
                logger.trace("[WEBLOGIC] filter requestURI:{}", requestURI);
            }
            return null;
        }
        // check sampling flag from client. If the flag is false, do not sample this request.
        final boolean sampling = samplingEnable(request);
        if (!sampling) {
            // Even if this transaction is not a sampling target, we have to create Trace object to mark 'not sampling'.
            // For example, if this transaction invokes rpc call, we can add parameter to tell remote node 'don't sample this transaction'
            final Trace trace = traceContext.disableSampling();
            if (isDebug) {
                logger.debug("[WEBLOGIC] remotecall sampling flag found. skip trace requestUrl:{}, remoteAddr:{}", request.getRequestURI(), request.getRemoteAddr());
            }
            return trace;
        }

        final TraceId traceId = populateTraceIdFromRequest(request);
        if (traceId != null) {
            final Trace trace = traceContext.continueTraceObject(traceId);
            if (trace.canSampled()) {
                SpanRecorder recorder = trace.getSpanRecorder();
                recordRootSpan(recorder, request);
                if (isDebug) {
                    logger.debug("[WEBLOGIC] TraceID exist. continue trace. traceId:{}, requestUrl:{}, remoteAddr:{}", traceId, request.getRequestURI(), request.getRemoteAddr());
                }
            } else {
                if (isDebug) {
                    logger.debug("[WEBLOGIC] TraceID exist. camSampled is false. skip trace. traceId:{}, requestUrl:{}, remoteAddr:{}", traceId, request.getRequestURI(), request.getRemoteAddr());
                }
            }
            return trace;
        } else {
            final Trace trace = traceContext.newTraceObject();
            if (trace.canSampled()) {
                SpanRecorder recorder = trace.getSpanRecorder();
                recordRootSpan(recorder, request);
                if (isDebug) {
                    logger.debug("[WEBLOGIC] TraceID not exist. start new trace. requestUrl:{}, remoteAddr:{}", request.getRequestURI(), request.getRemoteAddr());
                }
            } else {
                if (isDebug) {
                    logger.debug("[WEBLOGIC] TraceID not exist. camSampled is false. skip trace. requestUrl:{}, remoteAddr:{}", request.getRequestURI(), request.getRemoteAddr());
                }
            }
            return trace;
        }
    }

    private TraceId populateTraceIdFromRequest(ServletRequestImpl request) {

        String transactionId = request.getHeader(Header.HTTP_TRACE_ID.toString());
        if (transactionId != null) {
            long parentSpanID = NumberUtils.parseLong(request.getHeader(Header.HTTP_PARENT_SPAN_ID.toString()), SpanId.NULL);
            long spanID = NumberUtils.parseLong(request.getHeader(Header.HTTP_SPAN_ID.toString()), SpanId.NULL);
            short flags = NumberUtils.parseShort(request.getHeader(Header.HTTP_FLAGS.toString()), (short) 0);

            final TraceId id = traceContext.createTraceId(transactionId, parentSpanID, spanID, flags);
            if (isDebug) {
                logger.debug("[WEBLOGIC] TraceID exist. continue trace. {}", id);
            }
            return id;
        } else {
            return null;
        }
    }

    private void deleteTrace(Trace trace, Object target, Object[] args, Object result, Throwable throwable) {
        trace.traceBlockEnd();
        trace.close();
    }

    private void recordRootSpan(final SpanRecorder recorder, final ServletRequestImpl request) {
        // root
        recorder.recordServiceType(Constants.WEBLOGIC);

        final String requestURL = request.getRequestURI();
        recorder.recordRpcName(requestURL);

        final int port = request.getServerPort();
        final String endPoint = request.getServerName() + ":" + port;
        recorder.recordEndPoint(endPoint);

        final String remoteAddr = request.getRemoteAddr();
        recorder.recordRemoteAddress(remoteAddr);

        if (!recorder.isRoot()) {
            recordParentInfo(recorder, request);
        }
        recorder.recordApi(WEBLOGIC_SYNC_API_TAG);
    }

    private void recordParentInfo(SpanRecorder recorder, ServletRequestImpl request) {
        String parentApplicationName = request.getHeader(Header.HTTP_PARENT_APPLICATION_NAME.toString());
        if (parentApplicationName != null) {
            final String host = request.getHeader(Header.HTTP_HOST.toString());
            if (host != null) {
                recorder.recordAcceptorHost(host);
            } else {
                recorder.recordAcceptorHost(NetworkUtils.getHostFromURL(request.getRequestURI()));
            }
            final String type = request.getHeader(Header.HTTP_PARENT_APPLICATION_TYPE.toString());
            final short parentApplicationType = NumberUtils.parseShort(type, ServiceType.UNDEFINED.getCode());
            recorder.recordParentApplication(parentApplicationName, parentApplicationType);
        }
    }

    private boolean samplingEnable(ServletRequestImpl request) {
        // optional value
        final String samplingFlag = request.getHeader(Header.HTTP_SAMPLED.toString());
        if (isDebug) {
            logger.debug("[WEBLOGIC] SamplingFlag:{}", samplingFlag);
        }
        return SamplingFlagUtils.isSamplingFlag(samplingFlag);
    }

    private String getRequestParameter(ServletRequestImpl request, int eachLimit, int totalLimit) {
        String queryString = request.getQueryString();
        final StringBuilder params = new StringBuilder(64);
        try {
            Map<String, String> query_pairs = splitQuery(queryString);

            Iterator<String> attrs = query_pairs.keySet().iterator();

            while (attrs.hasNext()) {
                if (params.length() != 0) {
                    params.append('&');
                }
                // skip appending parameters if parameter size is bigger than totalLimit
                if (params.length() > totalLimit) {
                    params.append("...");
                    return params.toString();
                }
                String key = attrs.next();
                params.append(StringUtils.drop(key, eachLimit));
                params.append("=");
                String value = query_pairs.get(key);
                if (value != null) {
                    params.append(StringUtils.drop(StringUtils.toString(value), eachLimit));
                }
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("[WEBLOGIC] Fail to parse query {}", queryString, e);
        }
        return params.toString();
    }

    private Map<String, String> splitQuery(String query) throws UnsupportedEncodingException {
        Map<String, String> query_pairs = new LinkedHashMap<String, String>();
        if (StringUtils.isEmpty(query)) {
            return query_pairs;
        }
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
        return query_pairs;
    }

}
