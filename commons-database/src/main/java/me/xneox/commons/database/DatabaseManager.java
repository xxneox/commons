package me.xneox.commons.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import me.xneox.commons.java.LogUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Manages the connection to the databse
 */
public class DatabaseManager {
  private final ExecutorService executor;
  private final HikariDataSource source;

  public DatabaseManager(@NotNull HikariConfig config) {
    this.executor = Executors.newCachedThreadPool();
    this.source = new HikariDataSource(config);
  }

  @NotNull
  public ResultSet executeQuery(@NotNull String query) throws SQLException {
    try (var connection = this.source.getConnection();
        var statement = connection.prepareStatement(query);
        var rs = statement.executeQuery()) {
      return rs;
    }
  }

  public void executeUpdate(@NotNull String update, @Nullable Object... params) throws SQLException {
    try (var connection = this.source.getConnection();
        var statement = connection.prepareStatement(update)) {

      for (int i = 0; i < params.length; i++) {
        statement.setObject(i + 1, params[i]);
      }
      statement.executeUpdate();
    }
  }

  public void executeUpdateAsync(@NotNull String update, @Nullable Object... params) {
    this.executor.submit(() -> {
      try {
        executeUpdate(update, params);
      } catch (SQLException exception) {
        LogUtils.catchException("Could not execute asynchronous update to the database.", exception);
      }
    });
  }

  // Shut down the Hikari connection pool.
  public void shutdown() {
    this.source.close();
  }

  // Utility methods for lists

  @NotNull
  public static List<String> stringToList(@NotNull String result) {
    return new ArrayList<>(Arrays.asList(result.split(",")));
  }

  @NotNull
  public static String listToString(@NotNull List<String> list) {
    return String.join(",", list);
  }
}
