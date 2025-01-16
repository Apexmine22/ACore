package frost.main;

import frost.commands.*;
import org.bukkit.plugin.java.*;
import org.bukkit.configuration.file.*;
import org.bukkit.*;
import org.bukkit.plugin.*;
import frost.utils.*;
import org.bukkit.event.*;
import org.bukkit.command.*;
import frost.api.*;

public class ACore extends JavaPlugin {

    public static MySQL sql;
    private static ACore inst;
    public static FileConfiguration config;
    public static FileConfiguration rules;
    public static FileConfiguration messages;

    public void onEnable() {
        (ACore.inst = this).saveDefaultConfig();
        ACore.config = ConfigUtil.getFile("config.yml");
        ACore.messages = ConfigUtil.getFile("messages.yml");
        ACore.rules = ConfigUtil.getFile("rules.yml");
        Bukkit.getMessenger().registerOutgoingPluginChannel((Plugin)this, "BungeeCord");
        System.out.println("[" + this.getDescription().getName() + "] DataBase loading..");
        (ACore.sql = new MySQL(ACore.config.getString("MySQL.host"), ACore.config.getString("MySQL.username"), ACore.config.getString("MySQL.password"), ACore.config.getString("MySQL.database"), ACore.config.getString("MySQL.port"))).loadBans();
        System.out.println("[" + this.getDescription().getName() + "] " + "DataBase loading finish!");
        VaultPermission.setupPermission();
        Bukkit.getPluginManager().registerEvents((Listener)new ACoreListeners(), (Plugin)this);

        if ("true".equals(ACore.config.getString("Settings.module_ban"))) {

            this.getCommand("ban").setExecutor(new BanCommand());
            this.getCommand("unban").setExecutor((CommandExecutor)new UnbanCommand());
            this.getCommand("tempban").setExecutor((CommandExecutor)new TempbanCommand());
            System.out.println("[" + this.getDescription().getName() + "] " + "[SystemBan} Enabled!");

        } else {

            System.out.println("[" + this.getDescription().getName() + "] " + "[SystemBan] Disabled!");

        }

        if ("true".equals(ACore.config.getString("Settings.module_kick"))) {

            this.getCommand("kick").setExecutor((CommandExecutor)new KickCommand());
            System.out.println("[" + this.getDescription().getName() + "] " + "[SystemKick] Enabled!");

        } else {

            System.out.println("[" + this.getDescription().getName() + "] " + "[SystemKick] Disabled!");

        }

        if ("true".equals(ACore.config.getString("Settings.module_mute"))) {

            this.getCommand("mute").setExecutor((CommandExecutor)new MuteCommand());
            this.getCommand("unmute").setExecutor((CommandExecutor)new UnmuteCommand());
            this.getCommand("tempmute").setExecutor((CommandExecutor)new TempmuteCommand());
            System.out.println("[" + this.getDescription().getName() + "] " + "[SystemMute] Enabled!");

        } else {

            System.out.println("[" + this.getDescription().getName() + "] " + "[SystemMute] Disabled!");

        }

        if ("true".equals(ACore.config.getString("module_youtube"))) {

            this.getCommand("youtube").setExecutor((CommandExecutor)new YouTubeBroadcastCommand());
            System.out.println("[" + this.getDescription().getName() + "] " + "[SystemYoyTube] Enabled!");

        } else {

            System.out.println("[" + this.getDescription().getName() + "] " + "[SystemYouTube] Disabled!");

        }

        this.getCommand("rules").setExecutor((CommandExecutor)new RulesCommand());
        this.getCommand("baninfo").setExecutor((CommandExecutor)new BaninfoCommand());
        this.getCommand("reload").setExecutor((CommandExecutor)new ReloadCommand());

        new MySQL(ACore.config.getString("MySQL.host"), ACore.config.getString("MySQL.username"), ACore.config.getString("MySQL.password"), ACore.config.getString("MySQL.database"), ACore.config.getString("MySQL.port")).start();
        System.out.println("[" + this.getDescription().getName() + "] " + "Enabled!");
    }

    public void onDisable() {
        try {
            for (final Ban ban : BanManager.getBans().values()) {
                if (ban.getType() == BanType.TEMPBAN && ban.isLeft()) {
                    BanManager.unBan(ban.getName());
                }
            }
            for (final Ban ban : BanManager.getMutes().values()) {
                if (ban.getType() == BanType.TEMPMUTE && ban.isLeft()) {
                    BanManager.unMute(ban.getName());
                }
            }
            ACore.sql.disconnect();
        }
        catch (Exception ex) {}
        System.out.println("[" + this.getDescription().getName() + "] " + "Disabled!");
    }

    public static ACore getInstance() {
        return ACore.inst;
    }

    public MySQL getMySQL() {
        return ACore.sql;
    }
}
