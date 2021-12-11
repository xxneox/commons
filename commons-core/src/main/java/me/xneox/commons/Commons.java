package me.xneox.commons;

import me.xneox.commons.java.LogUtils;
import me.xneox.commons.java.UpdateChecker;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This utility simplifies setup of all components of this library.
 */
public final class Commons {

  /**
   * Setup all components by obtaining required information
   * from the provided plugin instance.
   *
   * @param plugin The plugin instance.
   * @param latestVersionURL Optional URL which returns the latest version string.
   */
  public static void setup(@NotNull JavaPlugin plugin, @Nullable String latestVersionURL) {
    LogUtils.logger(plugin.getSLF4JLogger());

    UpdateChecker.setCurrentVersion(plugin.getDescription().getVersion());
    if (latestVersionURL != null) {
      UpdateChecker.setLatestVersionUrl(latestVersionURL);
    }
  }
}
