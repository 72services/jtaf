package ch.jtaf.boundry;

import ch.jtaf.entity.Series;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping(value = "/res/series", produces = MediaType.APPLICATION_JSON_VALUE)
public class SeriesResource extends BaseResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(SeriesResource.class);

    @GetMapping
    public List<Series> list(@RequestParam("space_id") Long spaceId,
                             @RequestParam("withCompetitions") String withCompetitions) {
        if (withCompetitions == null || !Boolean.parseBoolean(withCompetitions)) {
            return dataService.getSeriesList(spaceId);
        } else {
            return dataService.getSeriesWithCompetitions(spaceId);
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Series save(@RequestBody Series series) {
        if (isUserGrantedForSpace(series.getSpace_id())) {
            if (series.getId() != null) {
                Series fromDb = dataService.get(Series.class, series.getId());
                series.setLogo(fromDb.getLogo());
            }
            Series savedSeries = dataService.save(series);
            return dataService.getSeries(savedSeries.getId());
        } else {
            throw new ForbiddenException();
        }
    }

    @PostMapping(value = "recalculateCategories", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void recalculateCategories(@RequestBody Series series) {
        if (isUserGrantedForSpace(series.getSpace_id())) {
            dataService.recalculateCategories(series.getId());
        } else {
            throw new ForbiddenException();
        }
    }

    @PostMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void copy(@PathVariable Long id, @RequestParam("function") String function) {
        dataService.copySeries(id);
    }

    @GetMapping("{id}")
    public Series get(@PathVariable("id") Long id, @RequestParam(value = "function", required = false) String function) {
        Series s = dataService.getSeries(id);
        if (s == null) {
            throw new NotFoundException();
        } else {
            if (function != null && function.equals("export")) {
                return dataService.exportSeries(s);
            } else {
                return s;
            }
        }
    }

    @GetMapping(value = "/logo/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getLogo(@PathVariable("id") Long id) {
        Series s = dataService.getSeries(id);
        if (s == null) {
            throw new NotFoundException();
        } else {
            try {
                byte[] logo = s.getLogo();
                if (logo != null && logo.length > 0) {
                    BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(s.getLogo()));
                    if (bufferedImage != null) {
                        bufferedImage = scaleImageByFixedHeight(bufferedImage, BufferedImage.TYPE_INT_RGB, 60);
                        if (bufferedImage != null) {
                            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                                ImageIO.write(bufferedImage, "png", baos);
                                return baos.toByteArray();
                            }
                        }
                    }
                }
            } catch (IOException ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
            return new byte[0];
        }
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Long id) {
        Series s = dataService.get(Series.class, id);
        if (s == null) {
            throw new NotFoundException();
        } else if (isUserGrantedForSpace(s.getSpace_id())) {
            dataService.deleteSeries(s);
        } else {
            throw new ForbiddenException();
        }
    }

    @PostMapping(value = "/upload/{id}", consumes = "multipart/form-data")
    public void uploadFile(@RequestParam("series_logo") MultipartFile file, @PathVariable Long id) throws IOException {
        InputStream inputStream = file.getInputStream();
        byte[] bytes = getBytesFromInputStream(inputStream);

        Series series = dataService.get(Series.class, id);
        series.setLogo(bytes);
        dataService.save(series);
    }


}
