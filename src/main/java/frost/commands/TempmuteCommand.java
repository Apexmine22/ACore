package frost.commands;

import org.bukkit.command.*;
import frost.cooldown.*;
import org.bukkit.entity.*;
import frost.main.*;
import frost.api.*;
import org.bukkit.*;
import frost.utils.*;

public class TempmuteCommand implements CommandExecutor {

    public boolean onCommand(final CommandSender sender, final Command cmd, final String l, final String[] args) {
        if (!sender.hasPermission("abansystem.tempmute")) {
            sender.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("no_permission")));
            return false;
        }
        if (!CooldownManager.hasCdw(sender.getName(), "tempmute")) {
            sender.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("cooldown_tempmute_command").replace("{time}", TimeUtils.getTimeС(CooldownManager.getLeftTime(sender.getName(), "tempmute")))));
            return false;
        }
        if (args.length < 4) {
            sender.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("usage_tempmute_command")));
            return false;
        }
        if (BanManager.isMute(args[0])) {
            sender.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("player_is_muted").replace("{target}", args[0])));
            return false;
        }
        final Player player = Bukkit.getPlayerExact(args[0]);
        if (player == null) {
            sender.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("player_is_offline").replace("{target}", args[0])));
            return false;
        }
        if (args[0].toLowerCase().equalsIgnoreCase(sender.getName().toLowerCase())) {
            sender.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("sender_is_target_ban")));
            return false;
        }
        if (player.hasPermission("abansystem.protect")) {
            sender.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("protect_admin").replace("{target}", args[0])));
            return false;
        }
        if (sender instanceof Player && VaultPermission.isHigherGroup(player, Bukkit.getPlayerExact(sender.getName()))) {
            sender.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("higher_group_tempmute").replace("{target}", player.getName())));
            return false;
        }
        final long time = TimeUtils.longTime(args[1]);
        if (time < 0L) {
            sender.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("time_format_unknown")));
            return false;
        }
        if (!ACore.config.getStringList("admins").contains(sender.getName()) && !sender.hasPermission("Bans.tempmute-time.bypass") && time > TimeUtils.longTime(ACore.config.getString("max_tempmute_time." + VaultPermission.getGroupName(Bukkit.getPlayer(sender.getName()))))) {
            sender.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("max_time_tempmute").replace("{time}", TimeUtils.getTime(TimeUtils.longTime(ACore.config.getString("max_tempmute_time." + VaultPermission.getGroupName(Bukkit.getPlayer(sender.getName())))) * 1000L))));
            return false;
        }
        if (ACore.rules.getString("rules." + args[2].replace(".", "|")) == null) {
            sender.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("rules_is_null").replace("{rules}", args[2])));
            return false;
        }
        if (!ACore.rules.getStringList("rules." + args[2].replace(".", "|") + ".type").contains("tempmute")) {
            sender.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("corresponds_not_paragraph")));
            return false;
        }
        final StringBuffer sb = new StringBuffer();
        for (int i = 3; i < args.length; ++i) {
            sb.append(args[i]).append(" ");
        }
        if (sb.substring(0, sb.length() - 1).toString().length() < 3) {
            sender.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("comment_little")));
            return false;
        }
        final long m = System.currentTimeMillis() + time * 1000L;
        BanManager.createMute(args[0], sender.getName(), args[2], m, sb.substring(0, sb.length() - 1), BanType.TEMPMUTE);
//        HoverChat.sendHover(ConfigUtil.getMSG("tempmuted_broadcast").replace("{admin}", sender.getName()).replace("{target}", args[0]), ConfigUtil.getMSGHover("tempmute_text").replace("{number-rules}", args[2]).replace("{rules}", ChatColor.stripColor(ASystemBans.rules.getString("rules." + args[2].replace(".", "|") + ".text").replace("&", "§"))).replace("{comment}", sb.substring(0, sb.length() - 1).toString()).replace("{time}", TimeUtils.getTime(time * 1000L)));
        HoverChat.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("tempmuted_broadcast").replace("{admin}", sender.getName()).replace("{target}", args[0])));
//        HoverChat.sendHover(ConfigUtil.getMSG("tempmuted_broadcast").replace("{admin}", sender.getName()).replace("{target}", args[0]), ConfigUtil.getMSGHover("tempmute_text").replace("{number-rules}", args[2]).replace("{rules}", ChatColor.stripColor(ASystemBans.rules.getString("rules." + args[2].replace(".", "|") + ".text").replace("&", "§"))).replace("{comment}", sb.substring(0, sb.length() - 1).toString()).replace("{time}", TimeUtils.getTime(time * 1000L)));
        HoverChat.sendMessage(ConfigUtil.getMSGInfo("tempmute_text").replace("{admin}", sender.getName()).replace("{comment}", sb.substring(0, sb.length() - 1).toString()).replace("{time}", TimeUtils.getTime(time * 1000L)).replace("{number-rules}", args[2]).replace("{rules}", ACore.rules.getString("rules." + args[2].replace(".", "|") + ".text")), args[0]);
        if (!sender.hasPermission("abansystem.bypass") && ACore.config.getString("Cooldown-Manager.tempmute." + VaultPermission.getGroupName(Bukkit.getPlayerExact(sender.getName()))) != null) {
            CooldownManager.createCooldown(sender.getName(), "tempmute", TimeUtils.longTime(ACore.config.getString("Cooldown-Manager.tempmute." + VaultPermission.getGroupName(Bukkit.getPlayer(sender.getName())))));
        }
        return false;
    }
}

