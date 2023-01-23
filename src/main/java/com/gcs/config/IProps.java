/**
 * Author: kgoldstein
 * Date: Jan 8, 2023
 * Terms: Expressly forbidden for use without written consent from the author
 */





package com.gcs.config;





import java.util.Map;



import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;



import lombok.NonNull;





public interface IProps
{
    public void loadFromXml(@NonNull final XMLConfiguration cfg_) throws ConfigurationException;





    public Map<String, String> toMap();

}
