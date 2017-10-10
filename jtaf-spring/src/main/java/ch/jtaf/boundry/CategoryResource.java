package ch.jtaf.boundry;

import ch.jtaf.entity.Category;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/res/categories", produces = MediaType.APPLICATION_JSON_VALUE)
public class CategoryResource extends BaseResource {

    @GetMapping
    public List<Category> list(@RequestParam("series_id") Long seriesId) {
        return dataService.getCategories(seriesId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Category save(@RequestBody Category c) {
        if (isUserGrantedForSeries(c.getSeries_id())) {
            return dataService.save(c);
        } else {
            throw new ForbiddenException();
        }
    }

    @GetMapping("{id}")
    public Category get(@PathVariable Long id) {
        Category c = dataService.get(Category.class, id);
        if (c == null) {
            throw new NotFoundException();
        } else {
            return c;
        }
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        Category c = dataService.get(Category.class, id);
        if (c == null) {
            throw new NotFoundException();
        } else if (isUserGrantedForSeries(c.getSeries_id())) {
            dataService.delete(c);
        } else {
            throw new ForbiddenException();
        }
    }
}
