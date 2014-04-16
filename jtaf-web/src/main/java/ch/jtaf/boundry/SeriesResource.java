package ch.jtaf.boundry;

import ch.jtaf.entity.Series;
import ch.jtaf.interceptor.TraceInterceptor;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import org.jboss.logging.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

@Path("series")
@Produces({"application/json"})
@Consumes({"application/json"})
@Interceptors({TraceInterceptor.class})
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class SeriesResource extends BaseResource {

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
            Series fromDb = dataService.get(Series.class, series.getId());
            series.setLogo(fromDb.getLogo());
            dataService.save(series);
            return dataService.getSeries(series.getId());
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

    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") Long id) {
        Series s = dataService.get(Series.class, id);
        if (s == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else if (isUserGrantedForSpace(s.getSpace_id())) {
            dataService.delete(s);
        } else {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
    }

    @POST
    @Path("/upload/{id}")
    @Consumes("multipart/form-data")
    public void uploadFile(MultipartFormDataInput input, @PathParam("id") Long id) throws IOException {

        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<InputPart> inputParts = uploadForm.get("series_logo");

        InputStream inputStream = inputParts.get(0).getBody(InputStream.class, null);
        byte[] bytes = getBytesFromInputStream(inputStream);

        Series series = dataService.get(Series.class, id);
        series.setLogo(bytes);
        dataService.save(series);
    }

    private static byte[] getBytesFromInputStream(InputStream is) {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream();) {
            byte[] buffer = new byte[0xFFFF];
            for (int len; (len = is.read(buffer)) != -1;) {
                os.write(buffer, 0, len);
            }
            os.flush();
            return os.toByteArray();
        } catch (IOException e) {
            Logger.getLogger(SeriesResource.class).error(e.getMessage(), e);
            return new byte[0];
        }
    }
}
