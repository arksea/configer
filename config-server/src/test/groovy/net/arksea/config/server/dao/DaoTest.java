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
 * @author xiaohaixing_dian91
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
        p.setName("test-project");
        p.setDescription("project for unit test");
        projectDao.save(p);

        Config cfg1 = new Config();
        cfg1.setProject(p);
        cfg1.setName("appBootConfig");
        cfg1.setDescription("app init config at startup");
        ConfigDoc doc1 = new ConfigDoc();
        doc1.setValue("123");
        doc1.setMetadata("456");
        cfg1.setDoc(doc1);
        configDao.save(cfg1);

        Project p1 = projectDao.getByName("test-project");
        assertThat(p1.getName()).isEqualTo("test-project");
        assertThat(p1.getDescription()).isEqualTo("project for unit test");
        Config cfg = configDao.getByNameAndProject("appBootConfig",p);
        assertThat(cfg.getName()).isEqualTo("appBootConfig");
        assertThat(cfg.getDescription()).isEqualTo("app init config at startup");
        assertThat(cfg.getDoc().getValue()).isEqualTo("123");
        assertThat(cfg.getDoc().getMetadata()).isEqualTo("456");
    }
    
}
