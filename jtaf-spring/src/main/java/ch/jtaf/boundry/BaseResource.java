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

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

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

    protected static byte[] getBytesFromInputStream(InputStream is) {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[0xFFFF];
            for (int len; (len = is.read(buffer)) != -1; ) {
                os.write(buffer, 0, len);
            }
            os.flush();
            return os.toByteArray();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            return new byte[0];
        }
    }

    protected BufferedImage scaleImageByFixedHeight(BufferedImage image, int imageType, int newHeight) {
        double ratio = ((double) image.getWidth(null)) / ((double) image.getHeight(null));
        int newWidth = (int) (ratio * newHeight);
        Image scaled = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        BufferedImage newImage = new BufferedImage(newWidth, newHeight, imageType);
        Graphics g = newImage.getGraphics();
        g.drawImage(scaled, 0, 0, null);
        g.dispose();
        return newImage;
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
