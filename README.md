# Description
Helps read XML and configure props


# Usage

```java
ConfigFile cfgF = new ConfigFile("APP_CFG", "appname.xml");
T1 props = cfgF.loadPropertiesFromConfig(T1.class);
```

  * T1 must implement `IProps`
  
