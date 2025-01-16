package frost.utils;

import frost.main.*;
import org.bukkit.configuration.file.*;
import java.io.*;
import net.md_5.bungee.api.*;

public class ConfigUtil {

    public static FileConfiguration getFile(final String fileName) {
        final File file = new File(ACore.getInstance().getDataFolder(), fileName);
        if (ACore.getInstance().getResource(fileName) == null) {
            try {
                YamlConfiguration.loadConfiguration(file).save(file);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return (FileConfiguration)YamlConfiguration.loadConfiguration(file);
        }
        if (!file.exists()) {
            ACore.getInstance().saveResource(fileName, false);
        }
        return (FileConfiguration)YamlConfiguration.loadConfiguration(file);
    }

    public static String getMSG(final String path) {
        return ChatColor.translateAlternateColorCodes('&', ACore.messages.getString("Messages." + path).replace("{new}", "\n"));
    }

    public static String getMSGHover(final String path) {
        return ChatColor.translateAlternateColorCodes('&', ACore.messages.getString("Hover-messages." + path).replace("{new}", "\n"));
    }

    public static String getMSGInfo(final String path) {
        return ChatColor.translateAlternateColorCodes('&', ACore.messages.getString("Info-messages." + path).replace("{new}", "\n"));
    }
}
