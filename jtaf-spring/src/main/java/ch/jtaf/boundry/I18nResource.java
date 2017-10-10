package ch.jtaf.boundry;

import ch.jtaf.i18n.I18n;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping(value = "/res/i18n", produces = MediaType.APPLICATION_JSON_VALUE)
public class I18nResource extends BaseResource {

    @GetMapping("messages")
    public Map<String, String> getMessages(HttpServletRequest hsr) {
        return I18n.getInstance().getMessages(hsr.getLocale());
    }

}
