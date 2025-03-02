package frost.commands;

import org.bukkit.command.*;
import frost.cooldown.*;
import frost.main.*;
import frost.api.*;
import org.bukkit.*;
import frost.utils.*;

public class MuteCommand implements CommandExecutor {

    public boolean onCommand(final CommandSender sender, final Command cmd, final String l, final String[] args) {
        if (!sender.hasPermission("abansystem.mute")) {
            sender.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("no_permission")));
            return false;
        }
        if (!CooldownManager.hasCdw(sender.getName(), "mute")) {
            sender.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("cooldown_mute_command").replace("{time}", TimeUtils.getTimeС(CooldownManager.getLeftTime(sender.getName(), "mute")))));
            return false;
        }
        if (args.length < 3) {
            sender.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("usage_mute_command")));
            return false;
        }
        if (args[0].toLowerCase().equalsIgnoreCase(sender.getName().toLowerCase())) {
            sender.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("sender_is_target_mute")));
            return false;
        }
        if (BanManager.isMute(args[0])) {
            sender.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("player_is_muted").replace("{target}", args[0])));
            return false;
        }
        if (ACore.rules.getString("rules." + args[1].replace(".", "|")) == null) {
            sender.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("rules_is_null").replace("{rules}", args[1])));
            return false;
        }
        if (!ACore.rules.getStringList("rules." + args[1].replace(".", "|") + ".type").contains("mute")) {
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
        BanManager.createMute(args[0], sender.getName(), args[1], 0L, sb.substring(0, sb.length() - 1).toString(), BanType.MUTE);
        sender.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("successfully_mute").replace("{target}", args[0])));
        HoverChat.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("muted_broadcast").replace("{admin}", sender.getName()).replace("{target}", args[0])));
//        HoverChat.sendMessage(ConfigUtil.getMSG("muted_broadcast").replace("{admin}", sender.getName()).replace("{target}", args[0]), ConfigUtil.getMSGHover("mute_text").replace("{number-rules}", args[1]).replace("{rules}", ChatColor.stripColor(ASystemBans.rules.getString("rules." + args[1].replace(".", "|") + ".text").replace("&", "§"))).replace("{comment}", sb.substring(0, sb.length() - 1).toString()));
//        HoverChat.sendMessage(ConfigUtil.getMSGInfo("mute_text").replace("{admin}", sender.getName()).replace("{comment}", sb.substring(0, sb.length() - 1).toString()).replace("{number-rules}", args[1]).replace("{rules}", ASystemBans.rules.getString("rules." + args[1].replace(".", "|") + ".text")), args[0]);
        if (!sender.hasPermission("abansystem.bypass") && ACore.config.getString("Cooldown-Manager.mute." + VaultPermission.getGroupName(Bukkit.getPlayerExact(sender.getName()))) != null) {
            CooldownManager.createCooldown(sender.getName(), "mute", TimeUtils.longTime(ACore.config.getString("Cooldown-Manager.mute." + VaultPermission.getGroupName(Bukkit.getPlayer(sender.getName())))));
        }
        return false;
    }
}

