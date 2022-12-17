/**
 * Author: kgoldstein
 * Date: Dec 17, 2022
 * Terms: Expressly forbidden for use without written consent from the author
 */





package com.gcs.config;





import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;



import java.nio.file.Paths;



import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.Test;





public class ConfigFileTest
{

    @Test
    public void test()
    {
        try
        {
            ConfigFile.setConfigFile(Paths.get(".", "src", "test", "resources", "appname.xml"));
            ConfigFile cfgF = new ConfigFile("APP_CFG", "appname.xml");
            assertEquals("jar", cfgF.getConfig().getString("loadStyle"));
        }
        catch (ConfigurationException ex_)
        {
            fail(ex_.toString());
        }
    }

}
