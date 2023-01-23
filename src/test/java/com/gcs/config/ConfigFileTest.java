/**
 * Author: kgoldstein
 * Date: Dec 17, 2022
 * Terms: Expressly forbidden for use without written consent from the author
 */





package com.gcs.config;





import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;



import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;



import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.Test;



import lombok.Data;
import lombok.NonNull;





public class ConfigFileTest
{





    @Test
    public void test()
    {
        try
        {
            ConfigFile.setConfigFile(Paths.get(".", "src", "test", "resources", "appname.xml"));
            ConfigFile cfgF = new ConfigFile("APP_CFG", "appname.xml");
            T1 props = cfgF.loadPropertiesFromConfig(T1.class);
            assertNotNull(props);
            assertEquals("jar", props.getLoadStyle());
        }
        catch (ConfigurationException ex_)
        {
            fail(ex_.toString());
        }
    }



    @Data
    static class T1 implements IProps
    {

        private String _loadStyle;





        @Override
        public void loadFromXml(@NonNull XMLConfiguration cfg_) throws ConfigurationException
        {
            setLoadStyle(cfg_.getString("loadStyle"));
        }





        @Override
        public Map<String, String> toMap()
        {
            return null;
        }
    }

}
