package com.rcircle.service.gateway.filters;

import com.netflix.zuul.context.RequestContext;
import org.springframework.stereotype.Component;

@Component
public class FilterUtils {

    public static final String CORRELATION_ID = "tmx-correlation-id";
    public static final String AUTH_TOKEN = "tmx-auth-token";
    public static final String USER_ID = "tmx-user-id";
    public static final String ORG_ID = "tmx-org-id";
    public static final String PRE_FILTER_TYPE = "pre";
    public static final String POST_FILTER_TYPE = "post";
    public static final String ROUTE_FILTER_TYPE = "route";

    public String getValueFromHeader(String label){
        RequestContext ctx = RequestContext.getCurrentContext();
        if (ctx.getRequest().getHeader(label) != null) {
            return ctx.getRequest().getHeader(label);
        } else {
            return ctx.getZuulRequestHeaders().get(label);
        }
    }

    public void setValueToHeader(String label, String value){
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.addZuulRequestHeader(label, value);
    }

    public void setCorrelationId(String correlationId) {
        setValueToHeader(CORRELATION_ID, correlationId);
    }

    public void setOrgId(String orgId) {
        setValueToHeader(ORG_ID, orgId);
    }

    public void setUserId(String userId) {
        setValueToHeader(USER_ID, userId);
    }

    public final String getAuthToken() {
        RequestContext ctx = RequestContext.getCurrentContext();
        return ctx.getRequest().getHeader(AUTH_TOKEN);
    }

    public String getServiceId() {
        RequestContext ctx = RequestContext.getCurrentContext();

        //We might not have a service id if we are using a static, non-eureka route.
        if (ctx.get("serviceId") == null) return "";
        return ctx.get("serviceId").toString();
    }
}