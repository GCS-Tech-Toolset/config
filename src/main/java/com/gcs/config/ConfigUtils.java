/**
 * Author: kgoldstein
 * Date: Dec 11, 2022
 * Terms: Expressly forbidden for use without written consent from the author
 */





package com.gcs.config;





import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;



import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.lang3.StringUtils;



import lombok.extern.slf4j.Slf4j;





@Slf4j
public class ConfigUtils
{


    /**
     * this is used to extract the count of embedded keys. For example:
     * <SomeKey> <KeyName></KeyName> <KeyName></KeyName> </SomeKey> <br/>
     * extractCount(cnt_, "SomeKey.KeyName") == 2.
     */
    public static final int extractCount(XMLConfiguration config_, String key_)
    {
        if (config_.getProperty(key_) != null)
        {
            try
            {
                return ((Collection<?>) config_.getProperty(key_)).size();
            }
            catch (ClassCastException ex_)
            {
                return -1;
            }
        }
        return 0;
    }





    public static final void logToTrace(Object obj_)
    {
        String name;
        for (Field f : getAllFields(obj_.getClass()))
        {
            try
            {
                f.setAccessible(true);
                if (f.getName().startsWith("_"))
                {
                    name = StringUtils.substring(f.getName(), 1);
                    _logger.trace("{}::{}={}", obj_.getClass().getSimpleName(), name, f.get(obj_));
                }
            }
            catch (IllegalArgumentException ex_)
            {
                _logger.error(ex_.toString(), ex_);
            }
            catch (IllegalAccessException ex_)
            {
                _logger.error(ex_.toString(), ex_);
            }
        }
    }





    public static Collection<Field> getAllFields(Class<?> type_)
    {
        TreeSet<Field> fields = new TreeSet<Field>(new Comparator<Field>()
        {
            @Override
            public int compare(Field o1_, Field o2_)
            {
                int res = o1_.getName().compareTo(o2_.getName());
                if (0 != res)
                {
                    return res;
                }
                res = o1_.getDeclaringClass().getSimpleName().compareTo(o2_.getDeclaringClass().getSimpleName());
                if (0 != res)
                {
                    return res;
                }
                res = o1_.getDeclaringClass().getName().compareTo(o2_.getDeclaringClass().getName());
                return res;
            }
        });
        for (Class<?> c = type_; c != null; c = c.getSuperclass())
        {
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
        }
        return fields;
    }

}
