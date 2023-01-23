# Description
Helps read XML and configure props


# Usage

```java
ConfigFile cfgF = new ConfigFile("APP_CFG", "appname.xml");
ConcretePropps props = cfgF.loadPropertiesFromConfig(T1.class);
```

  * `ConcretePropps` must implement `IProps`
  
