package ch.jtaf.boundry;

import ch.jtaf.i18n.I18n;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.Map;

@Path("i18n")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Component
public class I18nResource extends BaseResource {
    
    @GET
    @Path("messages")
    public Map<String, String> getMessages(@Context HttpServletRequest hsr) {
        return I18n.getInstance().getMessages(hsr.getLocale());
    }
    
}
