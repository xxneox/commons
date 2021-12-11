package me.xneox.commons.database;

import com.zaxxer.hikari.HikariConfig;
import java.io.File;
import me.xneox.commons.java.FileUtils;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class for obtaining an HikariConfig.
 */
public class HikariFactory {

  @NotNull
  public static HikariConfig createMySQL(@NotNull String host, int port, @NotNull String user, @NotNull String password, @NotNull String database) {
    var hikariConfig = new HikariConfig();
    hikariConfig.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
    hikariConfig.setUsername(user);
    hikariConfig.setPassword(password);

    hikariConfig.addDataSourceProperty("cachePrepStmts", true);
    hikariConfig.addDataSourceProperty("prepStmtCacheSize", 250);
    hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
    hikariConfig.addDataSourceProperty("useServerPrepStmts", true);

    return hikariConfig;
  }

  @NotNull
  public static HikariConfig createSQLite(@NotNull File file) {
    var hikariConfig = new HikariConfig();
    FileUtils.create(file);

    hikariConfig.setJdbcUrl("jdbc:sqlite:" + file.getPath());
    return hikariConfig;
  }
}
