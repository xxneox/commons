package me.xneox.commons.java;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a SemVer version.
 * Created by brianguertin (https://gist.github.com/brianguertin/ada4b65c6d1c4f6d3eee3c12b6ce021b)
 */
public class Version implements Comparable<Version> {
  public final int[] numbers;

  public Version(@NotNull String version) {
    var split = version.split("-")[0].split("\\.");
    numbers = new int[split.length];
    for (int i = 0; i < split.length; i++) {
      numbers[i] = Integer.parseInt(split[i]);
    }
  }

  @Override
  public int compareTo(@NotNull Version another) {
    int maxLength = Math.max(numbers.length, another.numbers.length);
    for (int i = 0; i < maxLength; i++) {
      int left = i < numbers.length ? numbers[i] : 0;
      int right = i < another.numbers.length ? another.numbers[i] : 0;
      if (left != right) {
        return left < right ? -1 : 1;
      }
    }
    return 0;
  }
}
