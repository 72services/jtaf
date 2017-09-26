package ch.jtaf.boundry;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.ApplicationPath;

@Configuration
@ApplicationPath("res")
public class AppConfig extends ResourceConfig {

    public AppConfig() {
        register(AthleteResource.class);
        register(CategoryResource.class);
        register(ClubResource.class);
        register(CompetitionResource.class);
        register(EventResource.class);
        register(I18nResource.class);
        register(RankingResource.class);
        register(ReportResource.class);
        register(ResultResource.class);
        register(SeriesResource.class);
        register(SpaceResource.class);
        register(UserResource.class);
        register(UserSpaceResource.class);
    }

}
