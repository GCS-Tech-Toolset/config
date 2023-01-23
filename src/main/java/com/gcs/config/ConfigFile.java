/****************************************************************************
 * FILE: AppProps.java
 * DSCRPT: A template for loading configuration files
 ****************************************************************************/





package com.gcs.config;





import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;



import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.lang3.StringUtils;



import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;





@Slf4j
public class ConfigFile
{


    @Getter @Setter private static boolean _FAIL_ON_MISSING_VAL = false;
    @Getter @Setter private static boolean _VALIDATING          = false;

    @Getter @Setter private static Path _configFile;

    @Getter private static String _appPropsSysName;
    @Getter private static String _defaultXmlFileName;




    //
    // this is the actual target 
    //
    @Getter private XMLConfiguration _config;





    public ConfigFile(@NonNull final String appPropsSysName_, @NonNull final String defFileName_) throws ConfigurationException
    {
        _appPropsSysName = appPropsSysName_;
        _defaultXmlFileName = defFileName_;
        try
        {
            _configFile = determineConfigFile();
            Parameters params = new Parameters();
            FileBasedConfigurationBuilder<XMLConfiguration> builder = new FileBasedConfigurationBuilder<XMLConfiguration>(XMLConfiguration.class).configure(params.xml()
                    .setEncoding("UTF-8")
                    .setThrowExceptionOnMissing(ConfigFile._FAIL_ON_MISSING_VAL)
                    .setListDelimiterHandler(new DefaultListDelimiterHandler(';'))
                    .setValidating(ConfigFile._VALIDATING)
                    .setFileName(_configFile.toString()));

            _config = builder.getConfiguration();

            if (_logger.isDebugEnabled())
            {
                _logger.debug("loaded config:{}", _configFile.toString());
            }
        }
        catch (Exception ex_)
        {
            _logger.error(ex_.toString());
            throw new ConfigurationException(ex_);
        }
    }





    public <T extends IProps> T loadPropertiesFromConfig(Class<T> clazz_) throws ConfigurationException
    {
        try
        {
            if (_config == null)
            {
                throw new ConfigurationException("configuration not yet loaded or is invalid");
            }

            T cfg = clazz_.newInstance();
            cfg.loadFromXml(_config);
            return cfg;
        }
        catch (Exception ex_)
        {
            throw new ConfigurationException(ex_);
        }
    }





    /**
     * inspects several levels of assumptions and returns the calculated config
     * file.
     * 
     * @throws FileNotFoundException
     *             if the config file is specified but not present
     */
    protected static Path determineConfigFile() throws FileNotFoundException
    {
        //
        // either this was called before (I can't change dynamically)
        // or this was set statically
        //
        if (_configFile != null)
        {
            if (_logger.isTraceEnabled())
            {
                _logger.trace("config file present:{}", _configFile);
            }
            return _configFile;
        }


        //
        // needs to be determined
        //
        Path cfgFilePath;
        String cfgOverride = System.getProperty(getAppPropsSysName());
        if (!StringUtils.isEmpty(cfgOverride))
        {
            cfgFilePath = Paths.get(cfgOverride);
            if (Files.exists(cfgFilePath))
            {
                return cfgFilePath;
            }
            else
            {
                //
                // if someone actually specified the file, and its not found
                // then I should not continue
                //
                throw new FileNotFoundException(getAppPropsSysName() + " specified, but file not found:" + cfgOverride);
            }
        }


        final String defXml = getDefaultXmlFileName();
        cfgFilePath = Paths.get(".", defXml);
        _logger.debug("looking for cfg file, path:{}", cfgFilePath.toString());
        if (Files.exists(cfgFilePath))
        {
            return cfgFilePath;
        }


        cfgFilePath = Paths.get("./etc", defXml);
        _logger.debug("looking for cfg file, path:{}", cfgFilePath.toString());
        if (Files.exists(cfgFilePath))
        {
            return cfgFilePath;
        }



        cfgFilePath = Paths.get("/etc", defXml);
        _logger.debug("looking for cfg file, path:{}", cfgFilePath.toString());
        if (Files.exists(cfgFilePath))
        {
            return cfgFilePath;
        }


        cfgFilePath = Paths.get("/opt/app/etc", defXml);
        _logger.debug("looking for cfg file, path:{}", cfgFilePath.toString());
        if (Files.exists(cfgFilePath))
        {
            return cfgFilePath;
        }




        URL fileFromJar = ConfigFile.class.getResource("/" + defXml);
        if (fileFromJar != null)
        {
            try
            {
                cfgFilePath = Paths.get(fileFromJar.toURI());
                if (Files.exists(cfgFilePath))
                {
                    return cfgFilePath;
                }
            }
            catch (URISyntaxException ex_)
            {
                throw new FileNotFoundException(ex_.toString());
            }

        }

        throw new FileNotFoundException("unable to locate config file");
    }





}
