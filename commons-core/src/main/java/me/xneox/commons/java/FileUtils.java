package me.xneox.commons.java;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import org.jetbrains.annotations.NotNull;

/**
 * This class helps with various operations on files.
 */
public final class FileUtils {

  @SuppressWarnings("ResultOfMethodCallIgnored")
  public static void downloadFile(@NotNull String urlFrom, @NotNull File file) throws IOException {
    LogUtils.logger().info("Downloading file from " + urlFrom + " to " + file.getAbsolutePath());

    // Make sure the original file will be replaced, if it exists
    file.delete();
    file.createNewFile();

    var connection = URLUtils.openConnection(urlFrom);
    try (ReadableByteChannel channel = Channels.newChannel(connection.getInputStream());
        var outputStream = new FileOutputStream(file)) {

      outputStream.getChannel().transferFrom(channel, 0, Long.MAX_VALUE);
    }
  }

  /**
   * Creates new file, catches eventual exception and logs if created.
   *
   * @param file The file to be created.
   * @return The created file.
   */
  @NotNull
  public static File create(@NotNull File file) {
    try {
      if (file.createNewFile()) {
        LogUtils.logger().info("Created new file: " + file.getPath());
      }
    } catch (IOException e) {
      LogUtils.catchException("Can't create file", e);
    }

    return file;
  }
}
