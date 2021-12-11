package me.xneox.commons.paper;

import com.google.common.base.Joiner;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

/**
 * This utility helps with various operations on text, and on Adventure components.
 */
public final class TextUtils {
  private static final LegacyComponentSerializer SERIALIZER =
      LegacyComponentSerializer.builder()
          .character('&')
          .hexCharacter('#')
          .hexColors()
          .extractUrls()
          .build();

  /**
   * Returns a formatted TextComponent, colors are replaced
   * using the & symbol. Hex colors are supported (ex. &#cd7f32).
   *
   * @param message Raw string to be formatted.
   * @return A formatted TextComponent.
   */
  @NotNull
  public static TextComponent color(@NotNull String message) {
    return SERIALIZER.deserialize(message);
  }

  /**
   * Sends a formatted message to the player.
   */
  public static void sendMessage(@NotNull Player sender, @NotNull String message) {
    sender.sendMessage(color(message));
  }

  /**
   * Converts a Component to a plain string.
   *
   * @param component Component to be converted.
   * @return The converted string.
   */
  @NotNull
  public static String plainString(@NotNull Component component) {
    return PlainTextComponentSerializer.plainText().serialize(component);
  }

  /**
   * Sends a title to the player.
   *
   * @param player The player.
   * @param title The title message.
   * @param subtitle The subtitle message.
   */
  public static void sendTitle(@NotNull Player player, @NotNull String title, @NotNull String subtitle) {
    player.showTitle(Title.title(color(title), color(subtitle)));
  }

  @NotNull
  public static String join(char separator, @NotNull Object... objects) {
    return Joiner.on(separator).join(objects);
  }

  public static void actionBar(@NotNull Player player, @NotNull String message) {
    player.sendActionBar(color(message));
  }

  public static void broadcast(String message) {
    Bukkit.broadcast(color(message));
  }

  /**
   * Creates a string from an array of strings.
   *
   * @param args An array.
   * @param index The start index.
   * @return The joined string.
   */
  @NotNull
  public static String buildString(@NotNull String[] args, int index) {
    var builder = new StringBuilder();
    for (int i = index; i < args.length; i++) {
      String s = args[i] + " ";
      builder.append(s);
    }
    return builder.toString();
  }

  /**
   * Only used when interacting with plugins not supporting Adventure.
   */
  @NotNull
  public static String legacyColor(String string) {
    return ChatColor.translateAlternateColorCodes('&', string);
  }

  /**
   * Formats a list of players, by displaying their nicknames in a readable format.
   *
   * @param list List containing players.
   * @return The formatted string.
   */
  @NotNull
  public static String formatPlayerList(@NotNull List<Player> list) {
    StringBuilder builder = new StringBuilder();
    for (Player player : list) {
      builder.append(player.getName()).append(", ");
    }
    return builder.toString();
  }

  public static int percentageOf(double i, double of) {
    return (int) ((i * 100) / of);
  }

  /**
   * Displays progress bar by calculating a percentage of the provided numbers.
   *
   * @param size Amount of bar symbols (e.g. 10)
   * @param done How much has been done.
   * @param total How much needs to be done.
   * @param doneSymbol Symbol displayed when a certain percentage of work has been done.
   * @param totalSymbol Symbol displayed for the rest of the bar.
   * @return The formatted progress bar.
   */
  @NotNull
  public static String progressBar(int size, int done, int total, @NotNull String doneSymbol, @NotNull String totalSymbol) {
    int donePercents = (100 * done) / total;
    int doneLength = size * donePercents / 100;
    return IntStream.range(0, size)
        .mapToObj(i -> i < doneLength ? doneSymbol : totalSymbol)
        .collect(Collectors.joining());
  }

  /**
   * Sends a boss bar notification to the player, displayed for 10 seconds.
   * TODO: make this configurable.
   *
   * @param plugin Instance of the plugin that will own the task.
   * @param player Player to send this boss bar to.
   * @param color Color of the boss bar.
   * @param message Message displayed on the boss bar.
   */
  public static void bossBarNotification(@NotNull JavaPlugin plugin, @NotNull Player player, @NotNull BarColor color, @NotNull String message) {
    BossBar bossBar = Bukkit.createBossBar(legacyColor(message), color, BarStyle.SOLID);

    new BukkitRunnable() {
      int remaining = 10;

      @Override
      public void run() {
        if ((remaining -= 1) == 0) {
          // Boss bar expired.
          bossBar.removeAll();
          cancel();
        } else {
          bossBar.setProgress(remaining / 10D);
        }
      }
    }.runTaskTimer(plugin, 0, 20);

    bossBar.setVisible(true);
    bossBar.addPlayer(player);
  }

  public static void sendCenteredMessage(@NotNull Player player, @NotNull String message) {
    player.sendMessage(center(message));
  }

  public static void broadcastCenteredMessage(String message) {
    for (Player player : Bukkit.getOnlinePlayers()) {
      sendCenteredMessage(player, message);
    }
  }

  // CENTERED TEXT UTILITY
  // SOURCE: https://www.spigotmc.org/threads/free-code-sending-perfectly-centered-chat-message.95872/page-6#post-3661881

  private final static int CENTER_PX = 154;

  public static String center(@NotNull String message){
    String[] lines = ChatColor.translateAlternateColorCodes('&', message).split("\n", 40);
    StringBuilder returnMessage = new StringBuilder();


    for (String line : lines) {
      int messagePxSize = 0;
      boolean previousCode = false;
      boolean isBold = false;

      for (char c : line.toCharArray()) {
        if (c == '§') {
          previousCode = true;
        } else if (previousCode) {
          previousCode = false;
          isBold = c == 'l';
        } else {
          DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
          messagePxSize = isBold ? messagePxSize + dFI.getBoldLength() : messagePxSize + dFI.getLength();
          messagePxSize++;
        }
      }
      int toCompensate = CENTER_PX - messagePxSize / 2;
      int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
      int compensated = 0;
      StringBuilder sb = new StringBuilder();
      while(compensated < toCompensate){
        sb.append(" ");
        compensated += spaceLength;
      }
      returnMessage.append(sb.toString()).append(line).append("\n");
    }

    return returnMessage.toString();
  }
}
