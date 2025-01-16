package frost.commands;

import org.bukkit.command.*;
import frost.cooldown.*;
import frost.main.*;
import frost.api.*;
import org.bukkit.*;
import frost.utils.*;

public class BanCommand implements CommandExecutor {

    public boolean onCommand(final CommandSender sender, final Command cmd, final String l, final String[] args) {
        if (!sender.hasPermission("abansystem.ban")) {
            sender.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("no_permission")));
            return false;
        }
        if (!CooldownManager.hasCdw(sender.getName(), "ban")) {
            sender.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("cooldown_ban_command").replace("{time}", TimeUtils.getTimeС(CooldownManager.getLeftTime(sender.getName(), "ban")))));
            return false;
        }
        if (args.length < 3) {
            sender.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("usage_ban_command")));
            return false;
        }
        if (args[0].toLowerCase().equalsIgnoreCase(sender.getName().toLowerCase())) {
            sender.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("sender_is_target_ban")));
            return false;
        }
        if (BanManager.isBan(args[0])) {
            sender.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("player_is_banned").replace("{target}", args[0])));
            return false;
        }
        if (ACore.rules.getString("rules." + args[1].replace(".", "|")) == null) {
            sender.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("rules_is_null").replace("{rules}", args[1])));
            return false;
        }
        if (!ACore.rules.getStringList("rules." + args[1].replace(".", "|") + ".type").contains("ban")) {
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
        BanManager.createBan(args[0], sender.getName(), args[1], 0L, sb.substring(0, sb.length() - 1).toString(), BanType.BAN);
        sender.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("successfully_ban").replace("{target}", args[0])));
        HoverChat.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("banned_broadcast").replace("{admin}", sender.getName()).replace("{target}", args[0])));
//        HoverChat.sendHover(RGBUtils.toChatColorString(ConfigUtil.getMSG("banned_broadcast").replace("{admin}", sender.getName()).replace("{target}", args[0])), ConfigUtil.getMSGHover("ban_text").replace("{number-rules}", args[1]).replace("{rules}", ChatColor.stripColor(ASystemBans.rules.getString("rules." + args[1].replace(".", "|") + ".text").replace("&", "§"))).replace("{comment}", sb.substring(0, sb.length() - 1).toString()));
        if (Bukkit.getPlayerExact(args[0]) != null) {
            Bukkit.getPlayerExact(args[0]).kickPlayer(RGBUtils.toChatColorString(ConfigUtil.getMSGInfo("ban_text").replace("{admin}", sender.getName()).replace("{comment}", sb.substring(0, sb.length() - 1).toString()).replace("{number-rules}", args[1]).replace("{rules}", ACore.rules.getString("rules." + args[1].replace(".", "|") + ".text"))));
        }
        if (!sender.hasPermission("abansystem.bypass") && ACore.config.getString("Cooldown-Manager.ban." + VaultPermission.getGroupName(Bukkit.getPlayerExact(sender.getName()))) != null) {
            CooldownManager.createCooldown(sender.getName(), "ban", TimeUtils.longTime(ACore.config.getString("Cooldown-Manager.ban." + VaultPermission.getGroupName(Bukkit.getPlayer(sender.getName())))));
        }
        return false;
    }
}

