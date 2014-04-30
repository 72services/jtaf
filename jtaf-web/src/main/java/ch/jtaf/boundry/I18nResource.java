package ch.jtaf.boundry;

import ch.jtaf.i18n.I18n;
import ch.jtaf.interceptor.TraceInterceptor;
import java.util.Map;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

@Path("i18n")
@Produces({"application/json; charset=UTF-8"})
@Consumes({"application/json"})
@Interceptors({TraceInterceptor.class})
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class I18nResource extends BaseResource {
    
    @GET
    @Path("messages")
    public Map<String, String> calculatePoints(@Context HttpServletRequest hsr) {
        return I18n.getInstance().getMessages(hsr.getLocale());
    }
    
}
