package net.arksea.config.server.dao;

import net.arksea.config.server.entity.Config;
import net.arksea.config.server.entity.ConfigDoc;
import net.arksea.config.server.entity.Project;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import static org.assertj.core.api.Assertions.assertThat;


/**
 *
 * @author xiaohaixing
 */
@ContextConfiguration(locations = { "/application-context.xml" })
@ActiveProfiles("unit-test")
public class DaoTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    ProjectDao projectDao;
    @Autowired
    ConfigDao configDao;

    @Before
    public void setUp() {
        //todo

    }

    /**
     * Test of getWeatherByCitycode method, of class WeatherDataService.
     */
    @Test
    public void test() {
        Project p = new Project();
        p.setName("weather-api");
        p.setProfile("qa");
        p.setDescription("project for unit test");
        projectDao.save(p);

        Config cfg1 = new Config();
        cfg1.setProject(p);
        cfg1.setName("appBootConfig");
        cfg1.setDescription("app init config at startup");
        ConfigDoc doc1 = new ConfigDoc();
        doc1.setValue("{\n" +
            "   \"name\": \"Mike\",\n" +
            "   \"value\": 123456,\n" +
            "   \"loading\": {\n" +
            "        \"countdown\": 2,\n" +
            "        \"adid\": \"afedafew\",\n" +
            "        \"timeout\": 5\n" +
            "   }\n" +
            "}\n");
        doc1.setMetadata("json");
        cfg1.setDoc(doc1);
        configDao.save(cfg1);

        Config cfg2 = new Config();
        cfg2.setProject(p);
        cfg2.setName("config2");
        cfg2.setDescription("main page config2");
        ConfigDoc doc2 = new ConfigDoc();
        doc2.setValue("123456");
        doc2.setMetadata("json");
        cfg2.setDoc(doc2);
        configDao.save(cfg2);

        Config cfg3 = new Config();
        cfg3.setProject(p);
        cfg3.setName("config3");
        cfg3.setDescription("main page config3");
        ConfigDoc doc3 = new ConfigDoc();
        doc3.setValue("\"hello\"");
        doc3.setMetadata("json");
        cfg3.setDoc(doc3);
        configDao.save(cfg3);

        Config cfg4 = new Config();
        cfg4.setProject(p);
        cfg4.setName("config4");
        cfg4.setDescription("main page config4");
        ConfigDoc doc4 = new ConfigDoc();
        doc4.setValue("3.1415926");
        doc4.setMetadata("json");
        cfg4.setDoc(doc4);
        configDao.save(cfg4);

        Project p1 = projectDao.getByNameAndProfile("weather-api", "qa");
        assertThat(p1.getName()).isEqualTo("weather-api");
        assertThat(p1.getDescription()).isEqualTo("project for unit test");
        Config cfg = configDao.getByNameAndProject("appBootConfig",p);
        assertThat(cfg.getName()).isEqualTo("appBootConfig");
        assertThat(cfg.getDescription()).isEqualTo("app init config at startup");
        assertThat(cfg.getDoc().getMetadata()).isEqualTo("json");
    }
    
}
