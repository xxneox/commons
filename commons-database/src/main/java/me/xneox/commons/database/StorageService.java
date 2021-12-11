package me.xneox.commons.database;


import java.sql.SQLException;
import me.xneox.commons.java.LogUtils;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a service that stores data in the database.
 */
public abstract class StorageService {
  protected final DatabaseManager database;

  public StorageService(@NotNull DatabaseManager database) {
    this.database = database;
    try {
      this.load();
    } catch (SQLException exception) {
      LogUtils.catchException("[" + getClass().getSimpleName() + "] Could not load data from the database", exception);
    }
  }

  public abstract void load() throws SQLException;

  public abstract void save() throws SQLException;
}
