package ch.jtaf.boundry;

import ch.jtaf.control.DataService;
import ch.jtaf.entity.UserSpace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

public class BaseResource {

    private final static Logger LOGGER = LoggerFactory.getLogger(BaseResource.class);

    @Autowired
    protected DataService dataService;

    protected boolean isUserGrantedForSeries(Long seriesId) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            Object userSpaceByUserAndSeries = dataService.getUserSpaceByUserAndSeries(userName, seriesId);
            return userSpaceByUserAndSeries != null;
        } catch (Exception e) {
            LOGGER.warn(e.getMessage(), e);
            return false;
        }
    }

    protected boolean isUserGrantedForSpace(Long space_id) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            UserSpace userSpaceByUserAndSpace = dataService.getUserSpaceByUserAndSpace(userName, space_id);
            return userSpaceByUserAndSpace != null;
        } catch (Exception e) {
            LOGGER.warn(e.getMessage(), e);
            return false;
        }
    }
}
