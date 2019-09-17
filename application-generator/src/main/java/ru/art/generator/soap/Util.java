package ru.art.generator.soap;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Util {

  public static String getNameUpperCase(String name) {
    return name.substring(0, 1).toUpperCase() + name.substring(1);
  }

  public static String getNameLowerCase(String name) {
    return name.substring(0, 1).toLowerCase() + name.substring(1);
  }

}
