package ch.jtaf.boundry;

import ch.jtaf.control.DataService;
import ch.jtaf.entity.UserSpace;
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

    protected boolean isUserGrantedForSeries(Long seriesId) {
        Principal principal = sessionContext.getCallerPrincipal();
        try {
            Object userSpaceByUserAndSeries = dataService.getUserSpaceByUserAndSeries(principal.getName(), seriesId);
            return userSpaceByUserAndSeries != null;
        } catch (Exception e) {
            Logger.getLogger(BaseResource.class.getName()).log(Level.WARNING, e.getMessage(), e);
            return false;
        }
    }

    protected boolean isUserGrantedForSpace(Long space_id) {
        Principal principal = sessionContext.getCallerPrincipal();
        try {
            UserSpace userSpaceByUserAndSpace = dataService.getUserSpaceByUserAndSpace(principal.getName(), space_id);
            return userSpaceByUserAndSpace != null;
        } catch (Exception e) {
            Logger.getLogger(BaseResource.class.getName()).log(Level.WARNING, e.getMessage(), e);
            return false;
        }
    }
}
