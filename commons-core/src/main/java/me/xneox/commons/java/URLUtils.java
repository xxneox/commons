package me.xneox.commons.java;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This util helps with performing URL requests.
 */
public final class URLUtils {

  /**
   * Reads the content of the URL as a string.
   *
   * @param url URL to read from.
   * @return The content of the URL.
   */
  @Nullable
  public static String readString(@NotNull String url) {
    try {
      var connection = openConnection(url);
      try (Scanner scanner = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8.toString())) {
        scanner.useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
      }
    } catch (IOException exception) {
      LogUtils.logger().warn("Couldn't read the content of " + url + " [" + exception.getMessage() + "]");
    }
    return null;
  }

  /**
   * Opens a connection to the specified URL.
   * Sets timeout options and a valid User-Agent.
   *
   * @param url The URL.
   * @return The opened URLConnection.
   * @throws IOException If the connection cannot be opened.
   */
  @NotNull
  public static URLConnection openConnection(@NotNull String url) throws IOException {
    var connection = new URL(url).openConnection();
    connection.addRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:94.0) Gecko/20100101 Firefox/94.0");
    connection.setConnectTimeout(5000);
    connection.setReadTimeout(5000);
    return connection;
  }
}
