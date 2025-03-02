package frost.commands;


import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.*;
import frost.cooldown.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import frost.main.*;
import frost.utils.*;

public class KickCommand implements CommandExecutor {

    public boolean onCommand(final CommandSender sender, final Command cmd, final String l, final String[] args) {
        if (!sender.hasPermission("abansystem.kick")) {
            sender.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("no_permission")));
            return false;
        }
        if (!CooldownManager.hasCdw(sender.getName(), "kick")) {
            sender.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("cooldown_kick_command").replace("{time}", TimeUtils.getTimeС(CooldownManager.getLeftTime(sender.getName(), "kick")))));
            return false;
        }
        if (args.length < 3) {
            sender.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("usage_kick_command")));
            return false;
        }
        final Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("player_is_offline").replace("{target}", args[0])));
            return false;
        }
        if (args[0].toLowerCase().equalsIgnoreCase(sender.getName().toLowerCase())) {
            sender.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("sender_is_target_kick")));
            return false;
        }
        if (sender instanceof Player && VaultPermission.isHigherGroup(player, Bukkit.getPlayerExact(sender.getName()))) {
            sender.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("higher_group_kick").replace("{target}", player.getName())));
            return false;
        }
        if (ACore.rules.getString("rules." + args[1].replace(".", "|")) == null) {
            sender.sendMessage(ConfigUtil.getMSG("rules_is_null").replace("{rules}", args[1]));
            return false;
        }
        if (!ACore.rules.getStringList("rules." + args[1].replace(".", "|") + ".type").contains("kick")) {
            sender.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("corresponds_not_paragraph")));
            return false;
        }
        final StringBuffer sb = new StringBuffer();
        for (int i = 2; i < args.length; ++i) {
            sb.append(args[i]).append(" ");
        }
        if (sb.substring(0, sb.length() - 1).toString().length() < 3) {
            sender.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("comment_little")));
            return false;
        }
        player.kickPlayer(RGBUtils.toChatColorString(ConfigUtil.getMSGInfo("kick_text").replace("{admin}", sender.getName()).replace("{number-rules}", args[1]).replace("{rules}", ChatColor.stripColor(ACore.rules.getString("rules." + args[1].replace(".", "|") + ".text").replace("&", "§"))).replace("{comment}", sb.substring(0, sb.length() - 1).toString())));
        sender.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("successfully_kick").replace("{target}", args[0])));
        sender.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("kicked_broadcast").replace("{admin}", sender.getName()).replace("{target}", player.getName())));
//        HoverChat.sendHover(ConfigUtil.getMSG("kicked_broadcast").replace("{admin}", sender.getName()).replace("{target}", player.getName()), ConfigUtil.getMSGHover("kick_text").replace("{number-rules}", args[1]).replace("{rules}", ChatColor.stripColor(ASystemBans.rules.getString("rules." + args[1].replace(".", "|") + ".text").replace("&", "§"))).replace("{comment}", sb.substring(0, sb.length() - 1).toString()));
        if (!sender.hasPermission("abansystem.bypass") && ACore.config.getString("Cooldown-Manager.kick." + VaultPermission.getGroupName(Bukkit.getPlayerExact(sender.getName()))) != null) {
            CooldownManager.createCooldown(sender.getName(), "kick", TimeUtils.longTime(ACore.config.getString("Cooldown-Manager.kick." + VaultPermission.getGroupName(Bukkit.getPlayer(sender.getName())))));
        }
        return false;
    }
}

