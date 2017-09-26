package ch.jtaf.boundry;

import ch.jtaf.control.DataService;
import ch.jtaf.entity.UserSpace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.logging.Level;
import java.util.logging.Logger;

public class BaseResource {

    @Autowired
    protected DataService dataService;

    protected boolean isUserGrantedForSeries(Long seriesId) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            Object userSpaceByUserAndSeries = dataService.getUserSpaceByUserAndSeries(userName, seriesId);
            return userSpaceByUserAndSeries != null;
        } catch (Exception e) {
            Logger.getLogger(BaseResource.class.getName()).log(Level.WARNING, e.getMessage(), e);
            return false;
        }
    }

    protected boolean isUserGrantedForSpace(Long space_id) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            UserSpace userSpaceByUserAndSpace = dataService.getUserSpaceByUserAndSpace(userName, space_id);
            return userSpaceByUserAndSpace != null;
        } catch (Exception e) {
            Logger.getLogger(BaseResource.class.getName()).log(Level.WARNING, e.getMessage(), e);
            return false;
        }
    }
}
