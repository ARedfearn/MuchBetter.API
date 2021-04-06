package util;

import org.apache.commons.lang3.RandomStringUtils;

public class TokenGenerator {

  private static final int LENGTH = 10;

  public static String generate() {
    return RandomStringUtils.randomAlphanumeric(LENGTH);
  }
}
