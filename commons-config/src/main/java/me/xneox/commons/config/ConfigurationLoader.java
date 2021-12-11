package me.xneox.commons.config;

import me.xneox.commons.java.LogUtils;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.objectmapping.ObjectMapper;
import org.spongepowered.configurate.serialize.SerializationException;

/**
 * This class loads the configuration, maps values, and saves to the file.
 *
 * @param <C> the configuration class
 */
public class ConfigurationLoader<C> {
  private final HoconConfigurationLoader loader;
  private ObjectMapper<C> mapper;

  public ConfigurationLoader(@NotNull Class<C> implementation, @NotNull HoconConfigurationLoader loader) {
    this.loader = loader;

    try {
      this.mapper = ObjectMapper.factory().get(implementation);
    } catch (SerializationException ex) {
      LogUtils.catchException("Couldn't create the object mapper for configuration: " + implementation.getSimpleName(), ex);
    }
  }

  /**
   * Loads the configuration, and writes the default values.
   *
   * @return The loaded config implementation.
   * @throws ConfigurateException if any invalid data is present.
   */
  @NotNull
  public C load() throws ConfigurateException {
    var configuration = this.mapper.load(this.loader.load());
    this.save(configuration); // write default values
    return configuration;
  }

  /**
   * Saves the nodes to the configuration file.
   *
   * @param config The loaded config implementation.
   * @throws ConfigurateException if unable to fully save / if an error occurs at any stage of loading
   */
  public void save(@NotNull C config) throws ConfigurateException {
    var node = this.loader.createNode();
    this.mapper.save(config, node);
    this.loader.save(node);
  }
}