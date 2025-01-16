package frost.commands;

import org.bukkit.command.*;
import frost.cooldown.*;
import frost.api.*;
import frost.main.*;
import org.bukkit.*;
import frost.utils.*;

public class UnmuteCommand implements CommandExecutor {

    public boolean onCommand(final CommandSender sender, final Command cmd, final String l, final String[] args) {
        if (!sender.hasPermission("abansystem.unmute")) {
            sender.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("no_permission")));
            return false;
        }
        if (!CooldownManager.hasCdw(sender.getName(), "unmute")) {
            sender.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("cooldown_unmute_command").replace("{time}", TimeUtils.getTimeС(CooldownManager.getLeftTime(sender.getName(), "unmute")))));
            return false;
        }
        if (args.length < 1) {
            sender.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("usage_unmute_command")));
            return false;
        }
        if (!BanManager.isMute(args[0])) {
            sender.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("player_null_unmute").replace("{target}", args[0])));
            return false;
        }
        BanManager.unMute(args[0]);
        if (sender.getName().toLowerCase().equalsIgnoreCase(args[0].toLowerCase())) {
            sender.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("successfully_unmute_me")));
            HoverChat.sendMessage(ConfigUtil.getMSG("unmuted_broadcast_me").replace("{admin}", sender.getName()));
        }
        else {
            sender.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("successfully_unmute").replace("{target}", args[0])));
            HoverChat.sendMessage(ConfigUtil.getMSG("unmuted_broadcast").replace("{admin}", sender.getName()).replace("{target}", args[0]));
        }
        if (!sender.hasPermission("abansystem.bypass") && ACore.config.getString("Cooldown-Manager.unmute." + VaultPermission.getGroupName(Bukkit.getPlayerExact(sender.getName()))) != null) {
            CooldownManager.createCooldown(sender.getName(), "unmute", TimeUtils.longTime(ACore.config.getString("Cooldown-Manager.unmute." + VaultPermission.getGroupName(Bukkit.getPlayer(sender.getName())))));
        }
        return false;
    }
}

