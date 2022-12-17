/**
 * Author: kgoldstein
 * Date: Dec 11, 2022
 * Terms: Expressly forbidden for use without written consent from the author
 */





package com.gcs.runtime;





import java.net.URL;
import java.util.jar.Attributes;
import java.util.jar.Manifest;



import org.apache.commons.lang3.StringUtils;



import lombok.extern.slf4j.Slf4j;





@Slf4j
public class VersionInfo
{
    private static String _version;


    public static String calcVersion(Class<?> class_)
    {
        if (StringUtils.isNotEmpty(_version))
        {
            return _version;
        }

        try
        {
            String className = class_.getSimpleName() + ".class";
            String classPath = class_.getResource(className).toString();
            if (!classPath.startsWith("jar"))
            {
                // Class not from JAR
                return "UNK";
            }


            String manifestPath = classPath.substring(0, classPath.lastIndexOf("!") + 1)
                    + "/META-INF/MANIFEST.MF";
            Manifest manifest = new Manifest(new URL(manifestPath).openStream());
            Attributes attr = manifest.getMainAttributes();
            String version = new String(attr.getValue("Implementation-Version"));
            _logger.debug("version:{}", version);
            return version;
        }
        catch (Exception ex_)
        {
            _logger.error(ex_.toString(), ex_);
        }

        return class_.getPackage().getImplementationVersion();
    }

}
