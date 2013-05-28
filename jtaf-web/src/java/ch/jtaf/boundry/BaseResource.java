package ch.jtaf.boundry;

import ch.jtaf.control.DataService;
import ch.jtaf.entity.UserSpace;
import java.security.Principal;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;

public class BaseResource {

    @Resource
    private SessionContext sessionContext;
    @EJB
    protected DataService dataService;

    protected boolean isUserGrantedForSeries(Long series_id) {
        Principal principal = sessionContext.getCallerPrincipal();
        UserSpace userSpace =
                dataService.getUserSpaceByUserAndSeries(principal.getName(), series_id);
        return userSpace != null;
    }

    protected boolean isUserGrantedForSpace(Long space_id) {
        Principal principal = sessionContext.getCallerPrincipal();
        UserSpace userSpace =
                dataService.getUserSpaceByUserAndSpace(principal.getName(), space_id);
        return userSpace != null;
    }
}
