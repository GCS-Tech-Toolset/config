/****************************************************************************
 * FILE: AppPropsLoadTest.java
 * DSCRPT: 
 ****************************************************************************/





package com.gcs.config;





import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;



import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.Test;



import lombok.extern.slf4j.Slf4j;





@Slf4j
public class EnvNameConfigTest
{

    @Test
    public void test()
    {
        try
        {
            ConfigFile cfg = new ConfigFile("APP_PROPS", "appname.xml");
            assertNotNull(cfg.getConfig());
        }
        catch (ConfigurationException ex_)
        {
            fail(ex_.toString());
        }

    }

}
