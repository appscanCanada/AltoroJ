package com.ibm.security.appscan.altoromutual.util;

import java.io.InputStream;
import java.util.Properties;

public class ConfigUtil {
  private static final Properties props = new Properties();

  static {
    try {
      InputStream in = ConfigUtil.class.getClassLoader().getResourceAsStream("app.properties");
      if (in == null) {
        throw new RuntimeException("Could not find app.properties");
      }

      props.load(in);

    } catch (Exception e) {
      throw new RuntimeException("Unable to load app.properties", e);
    }
  }

  public static String get(String key) {
    return props.getProperty(key);
  }
}
