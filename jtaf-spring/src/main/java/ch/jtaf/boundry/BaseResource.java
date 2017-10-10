package ch.jtaf.boundry;

import ch.jtaf.control.DataService;
import ch.jtaf.entity.UserSpace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

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


    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAppException(ForbiddenException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleAppException(NotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleAppException(BadRequestException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(NoContentException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String handleAppException(NoContentException ex) {
        return ex.getMessage();
    }

}
