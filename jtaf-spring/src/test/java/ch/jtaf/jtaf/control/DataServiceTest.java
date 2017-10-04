package ch.jtaf.jtaf.control;

import ch.jtaf.control.DataService;
import ch.jtaf.entity.Series;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataServiceTest {

    @Autowired
    private DataService dataService;

    @Test
    public void getSeriesList() {
        List<Series> seriesList = dataService.getSeriesList(1l);

        assertEquals(0, seriesList.size());
    }

}
