package ch.jtaf.boundry;

import ch.jtaf.control.DataService;
import java.security.Principal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;

public class BaseResource {

    @Resource
    protected SessionContext sessionContext;
    @EJB
    protected DataService dataService;

    protected boolean isUserGrantedForSeries(Long series_id) {
        Principal principal = sessionContext.getCallerPrincipal();
        try {
            dataService.getUserSpaceByUserAndSeries(principal.getName(), series_id);
            return true;
        } catch (Exception e) {
            Logger.getLogger(BaseResource.class.getName()).log(Level.WARNING, e.getMessage(), e);
            return false;
        }
    }

    protected boolean isUserGrantedForSpace(Long space_id) {
        Principal principal = sessionContext.getCallerPrincipal();
        try {
            dataService.getUserSpaceByUserAndSpace(principal.getName(), space_id);
            return true;
        } catch (Exception e) {
            Logger.getLogger(BaseResource.class.getName()).log(Level.WARNING, e.getMessage(), e);
            return false;
        }
    }
}
