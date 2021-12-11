package me.xneox.commons.java;

import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;

/**
 * This class checks for the latest available version from the provided URL.
 */
public final class UpdateChecker {
  private static String currentVersion;
  private static String latestVersionUrl;

  private static boolean updateAvailable;

  /**
   * Sets the current version of this software.
   *
   * @param version The current version.
   */
  public static void setCurrentVersion(@NotNull String version) {
    currentVersion = version;
  }

  /**
   * Sets the URL from which the latest version will be read.
   * The website should only contain a one-line version string.
   *
   * @param url URL to check for the latest version.
   */
  public static void setLatestVersionUrl(@NotNull String url) {
    latestVersionUrl = url;
  }

  /**
   * Checks the latest version to see if there's any update available.
   *
   * @param action Action to run when there's an update available.
   */
  public static void checkForUpdates(@NotNull Consumer<String> action) {
    if (currentVersion == null) {
      LogUtils.logger().error("Could not check for updates: the developer didn't set the current version.");
      return;
    }

    if (latestVersionUrl == null) {
      LogUtils.logger().error("Could not check for updates: the developer didn't set the latest version url.");
      return;
    }

    String latestVersion = URLUtils.readString(latestVersionUrl);
    if (latestVersion == null) {
      return; // a warning will be thrown by the URLUtils anyway.
    }

    var latest = new Version(latestVersion);
    var current = new Version(currentVersion);

    // 1 means outdated, 0 means up-to-date, -1 means newer than released.
    if (latest.compareTo(current) > 0) {
      updateAvailable = true;
      action.accept(latestVersion);
    }
  }

  /**
   * @return Whenever an update is available.
   */
  public static boolean updateAvailable() {
    return updateAvailable;
  }

  public static String currentVersion() {
    return currentVersion;
  }
}
