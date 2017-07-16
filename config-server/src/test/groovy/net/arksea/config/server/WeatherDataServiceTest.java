package net.arksea.config.server;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;


/**
 *
 * @author xiaohaixing_dian91
 */
@ContextConfiguration(locations = { "/application-context.xml" })
@ActiveProfiles("unit-test")
public class WeatherDataServiceTest extends AbstractJUnit4SpringContextTests {

    @Before
    public void setUp() {
        //todo

    }

    /**
     * Test of getWeatherByCitycode method, of class WeatherDataService.
     */
    @Test
    public void testGetByCitycode() {
        //todo
    }
    
}
