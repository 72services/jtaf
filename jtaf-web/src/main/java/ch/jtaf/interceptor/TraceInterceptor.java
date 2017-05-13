package ch.jtaf.interceptor;

import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import java.security.Principal;

public class TraceInterceptor {

    @Resource
    private SessionContext sessionContext;

    @AroundInvoke
    public Object logCall(InvocationContext context) throws Exception {
        Principal principal = sessionContext.getCallerPrincipal();
        LoggerFactory.getLogger(context.getTarget().getClass()).debug(
                "{0}: {1}.{2}({3})",
                principal.getName(), context.getTarget().getClass().getSimpleName(), context.getMethod().getName(), extractParameters(context));
        return context.proceed();
    }

    private String extractParameters(InvocationContext context) {
        StringBuilder params = new StringBuilder();
        if (context != null && context.getParameters() != null) {
            boolean first = true;
            for (Object param : context.getParameters()) {
                if (param != null) {
                    if (!first) {
                        params.append(", ");
                    }
                    params.append(param.toString());
                    first = false;
                }
            }
        }
        return params.toString();
    }
}
