package frost.commands;

import org.bukkit.command.*;
import frost.cooldown.*;
import frost.main.*;
import org.bukkit.*;
import frost.utils.*;
import frost.api.*;

public class BaninfoCommand implements CommandExecutor {

    public boolean onCommand(final CommandSender sender, final Command cmd, final String l, final String[] args) {
        if (!sender.hasPermission("abansystem.baninfo")) {
            sender.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("no_permission")));
            return false;
        }
        if (!CooldownManager.hasCdw(sender.getName(), "baninfo")) {
            sender.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("cooldown_baninfo_command").replace("{time}", TimeUtils.getTimeС(CooldownManager.getLeftTime(sender.getName(), "baninfo")))));
            return false;
        }
        if (args.length < 1) {
            sender.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("usage_baninfo_command")));
            return false;
        }
        if (MySQL.hasAccountBan(args[0]) && !BanManager.isBan(args[0])) {
            MySQL.loadBan(args[0]);
        }
        if (MySQL.hasAccountMute(args[0]) && !BanManager.isMute(args[0])) {
            MySQL.loadMute(args[0]);
        }
        final StringBuffer sb = new StringBuffer();
        if (!BanManager.isBan(args[0])) {
            sb.append(RGBUtils.toChatColorString(ConfigUtil.getMSG("player_null_ban").replace("{target}", args[0])));
        }
        else {
            final Ban ban = BanManager.getBan(args[0]);
            if (ban.getType() == BanType.TEMPBAN) {
                if (ban.isLeft()) {
                    BanManager.unBan(args[0]);
                }
                else {
                    sb.append(RGBUtils.toChatColorString(ConfigUtil.getMSG("tempban_format_info").replace("{admin}", ban.getAdmin()).replace("{comment}", ban.getComment()).replace("{time}", TimeUtils.getTime(ban.getTimeLeft())).replace("{number-rules}", ban.getReason()).replace("{rules}", ACore.rules.getString("rules." + ban.getReason().replace(".", "|") + ".text")))).append("\n");
                }
            }
            else {
                sb.append(RGBUtils.toChatColorString(ConfigUtil.getMSG("ban_format_info").replace("{admin}", ban.getAdmin()).replace("{comment}", ban.getComment()).replace("{number-rules}", ban.getReason()).replace("{rules}", ACore.rules.getString("rules." + ban.getReason().replace(".", "|") + ".text")))).append("\n");
            }
        }
        if (!BanManager.isMute(args[0])) {
            sb.append(RGBUtils.toChatColorString(ConfigUtil.getMSG("player_null_mute").replace("{target}", args[0])));
        }
        else {
            final Ban ban = BanManager.getMute(args[0]);
            if (ban.getType() == BanType.TEMPMUTE) {
                if (ban.isLeft()) {
                    BanManager.unMute(args[0]);
                }
                else {
                    sb.append(RGBUtils.toChatColorString(ConfigUtil.getMSG("tempmute_format_info").replace("{admin}", ban.getAdmin()).replace("{comment}", ban.getComment()).replace("{time}", TimeUtils.getTime(ban.getTimeLeft())).replace("{number-rules}", ban.getReason()).replace("{rules}", ACore.rules.getString("rules." + ban.getReason().replace(".", "|") + ".text")))).append("\n");
                }
            }
            else {
                sb.append(RGBUtils.toChatColorString(ConfigUtil.getMSG("mute_format_info").replace("{admin}", ban.getAdmin()).replace("{comment}", ban.getComment()).replace("{number-rules}", ban.getReason()).replace("{rules}", ACore.rules.getString("rules." + ban.getReason().replace(".", "|") + ".text")))).append("\n");
            }
        }
        sender.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("info_format_player").replace("{list}", sb.toString()).replace("{target}", args[0])));
        if (!sender.hasPermission("abansystem.bypass") && ACore.config.getString("Cooldown-Manager.baninfo." + VaultPermission.getGroupName(Bukkit.getPlayerExact(sender.getName()))) != null) {
            CooldownManager.createCooldown(sender.getName(), "baninfo", TimeUtils.longTime(ACore.config.getString("Cooldown-Manager.baninfo." + VaultPermission.getGroupName(Bukkit.getPlayer(sender.getName())))));
        }
        return false;
    }
}

