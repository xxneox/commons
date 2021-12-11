package me.xneox.commons.java;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This util helps with various logging operations, using SLF4J.
 * It is suggested to set your own logger instance.
 */
public final class LogUtils {
  private static Logger logger = LoggerFactory.getLogger("LogUtils");
  private static boolean debug = false;

  /**
   * Sets the logger instance.
   * @param newLogger The new logger instance.
   */
  public static void logger(@NotNull Logger newLogger) {
    logger = newLogger;
  }

  /**
   * @return The current logger instance.
   */
  @NotNull
  public static Logger logger() {
    return logger;
  }

  /**
   * Changes the state of the debug mode.
   *
   * @param enabled The new state of debug mode.
   */
  public void debug(boolean enabled) {
    debug = enabled;
  }

  /**
   * Catches a Throwable and prints a detailed error message.
   *
   * @param details details about where the error has occurred
   * @param throwable the caught exception
   */
  public static void catchException(@NotNull String details, @NotNull Throwable throwable) {
    logger.error("An error occurred in " + logger.getName() + " v" + UpdateChecker.currentVersion());
    if (UpdateChecker.updateAvailable()) {
      logger.error("  (!) Your version is outdated. Update before sending bug report!");
    }

    logger.error(" > Details: " + details);
    logger.error(" > Stacktrace: ");
    logger.error("", throwable);
  }

  /**
   * Logs a message if debug is enabled.
   *
   * @param message message to be logged
   */
  public static void debug(@NotNull String message) {
    if (debug) {
      logger().info("(Debug) " + message);
    }
  }
}
