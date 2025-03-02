package frost.main;

import org.bukkit.event.*;
import org.bukkit.event.player.*;
import frost.api.*;
import frost.utils.*;

public class ACoreListeners implements Listener {

    @EventHandler
    public void onConnect(final PlayerLoginEvent e) {
        if (MySQL.hasAccountBan(e.getPlayer().getName()) && !BanManager.isBan(e.getPlayer().getName())) {
            MySQL.loadBan(e.getPlayer().getName());
        }
        if (MySQL.hasAccountMute(e.getPlayer().getName()) && !BanManager.isMute(e.getPlayer().getName())) {
            MySQL.loadMute(e.getPlayer().getName());
        }
        if (BanManager.isBan(e.getPlayer().getName())) {
            final Ban ban = BanManager.getBan(e.getPlayer().getName());
            if (BanType.TEMPBAN == ban.getType()) {
                if (ban.isLeft()) {
                    BanManager.unBan(e.getPlayer().getName());
                }
                else {
                    e.disallow(PlayerLoginEvent.Result.KICK_BANNED, RGBUtils.toChatColorString(ConfigUtil.getMSGInfo("tempban_text").replace("{admin}", ban.getAdmin()).replace("{comment}", ban.getComment()).replace("{time}", TimeUtils.getTime(ban.getTimeLeft())).replace("{number-rules}", ban.getReason()).replace("{rules}", ACore.rules.getString("rules." + ban.getReason().replace(".", "|") + ".text"))));
                }
            }
            else if (BanType.BAN == ban.getType()) {
                e.disallow(PlayerLoginEvent.Result.KICK_BANNED, RGBUtils.toChatColorString(ConfigUtil.getMSGInfo("ban_text").replace("{admin}", ban.getAdmin()).replace("{comment}", ban.getComment()).replace("{number-rules}", ban.getReason()).replace("{rules}", ACore.rules.getString("rules." + ban.getReason().replace(".", "|") + ".text"))));
            }
        }
    }

    @EventHandler
    public void onWhite(final AsyncPlayerChatEvent e) {
        if (BanManager.isMute(e.getPlayer().getName())) {
            final Ban ban = BanManager.getMute(e.getPlayer().getName());
            if (BanType.TEMPMUTE == ban.getType()) {
                if (ban.isLeft()) {
                    BanManager.unBan(e.getPlayer().getName());
                }
                else {
                    e.getPlayer().sendMessage(RGBUtils.toChatColorString(RGBUtils.toChatColorString(ConfigUtil.getMSGInfo("tempmute_text").replace("{admin}", ban.getAdmin()).replace("{comment}", ban.getComment()).replace("{time}", TimeUtils.getTime(ban.getTimeLeft())).replace("{number-rules}", ban.getReason()).replace("{rules}", ACore.rules.getString("rules." + ban.getReason().replace(".", "|") + ".text")))));
                    e.setCancelled(true);
                }
            }
            else if (BanType.MUTE == ban.getType()) {
                e.getPlayer().sendMessage(RGBUtils.toChatColorString(RGBUtils.toChatColorString(ConfigUtil.getMSGInfo("mute_text").replace("{admin}", ban.getAdmin()).replace("{comment}", ban.getComment()).replace("{number-rules}", ban.getReason()).replace("{rules}", ACore.rules.getString("rules." + ban.getReason().replace(".", "|") + ".text")))));
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onCommand(final PlayerCommandPreprocessEvent e) {
        if (BanManager.isMute(e.getPlayer().getName())) {
            ACore.config.getStringList("blocked_commands_mute").forEach(command -> {
                if (e.getMessage().split(" ")[0].toLowerCase().equalsIgnoreCase(command.toLowerCase())) {
                    e.getPlayer().sendMessage(RGBUtils.toChatColorString(RGBUtils.toChatColorString(ConfigUtil.getMSG("command_blocked_mute"))));
                    e.setCancelled(true);
                }
            });
        }
    }
}
