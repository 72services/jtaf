package ch.jtaf.boundry;

import ch.jtaf.entity.Series;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Path("series")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Component
public class SeriesResource extends BaseResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(SeriesResource.class);

    @GET
    public List<Series> list(@QueryParam("space_id") Long spaceId,
            @QueryParam("withCompetitions") String withCompetitions) {
        if (withCompetitions == null || !Boolean.parseBoolean(withCompetitions)) {
            return dataService.getSeriesList(spaceId);
        } else {
            return dataService.getSeriesWithCompetitions(spaceId);
        }
    }

    @POST
    public Series save(Series series) {
        if (isUserGrantedForSpace(series.getSpace_id())) {
            if (series.getId() != null) {
                Series fromDb = dataService.get(Series.class, series.getId());
                series.setLogo(fromDb.getLogo());
            }
            Series savedSeries = dataService.save(series);
            return dataService.getSeries(savedSeries.getId());
        } else {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
    }

    @POST
    @Path("/recalculateCategories")
    public void recalculateCategories(Series series) {
        if (isUserGrantedForSpace(series.getSpace_id())) {
            dataService.recalculateCategories(series.getId());
        } else {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
    }

    @POST
    @Path("{id}")
    public void copy(@PathParam("id") Long id, @QueryParam("function") String function) {
        dataService.copySeries(id);
    }

    @GET
    @Path("{id}")
    public Series get(@PathParam("id") Long id, @QueryParam("function") String function) {
        Series s = dataService.getSeries(id);
        if (s == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            if (function != null && function.equals("export")) {
                return dataService.exportSeries(s);
            } else {
                return s;
            }
        }
    }

    @GET
    @Path("/logo/{id}")
    @Produces("image/png")
    public byte[] getLogo(@PathParam("id") Long id) {
        Series s = dataService.getSeries(id);
        if (s == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            try {
                byte[] logo = s.getLogo();
                if (logo != null && logo.length > 0) {
                    BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(s.getLogo()));
                    bufferedImage = scaleImageByFixedHeight(bufferedImage, BufferedImage.TYPE_INT_RGB, 60);
                    if (bufferedImage != null) {
                        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                            ImageIO.write(bufferedImage, "png", baos);
                            return baos.toByteArray();
                        }
                    }
                }
            } catch (IOException ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
            return new byte[0];
        }
    }

    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") Long id) {
        Series s = dataService.get(Series.class, id);
        if (s == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else if (isUserGrantedForSpace(s.getSpace_id())) {
            dataService.deleteSeries(s);
        } else {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
    }

//    @POST
//    @Path("/upload/{id}")
//    @Consumes("multipart/form-data")
//    public void uploadFile(MultipartFormDataInput input, @PathParam("id") Long id) throws IOException {
//
//        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
//        List<InputPart> inputParts = uploadForm.get("series_logo");
//
//        InputStream inputStream = inputParts.get(0).getBody(InputStream.class, null);
//        byte[] bytes = getBytesFromInputStream(inputStream);
//
//        Series series = dataService.get(Series.class, id);
//        series.setLogo(bytes);
//        dataService.save(series);
//    }

    private static byte[] getBytesFromInputStream(InputStream is) {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[0xFFFF];
            for (int len; (len = is.read(buffer)) != -1;) {
                os.write(buffer, 0, len);
            }
            os.flush();
            return os.toByteArray();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            return new byte[0];
        }
    }

    private BufferedImage scaleImageByFixedHeight(BufferedImage image, int imageType, int newHeight) {
        double ratio = ((double) image.getWidth(null)) / ((double) image.getHeight(null));
        int newWidth = (int) (ratio * newHeight);
        Image scaled = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        BufferedImage newImage = new BufferedImage(newWidth, newHeight, imageType);
        Graphics g = newImage.getGraphics();
        g.drawImage(scaled, 0, 0, null);
        g.dispose();
        return newImage;
    }
}
