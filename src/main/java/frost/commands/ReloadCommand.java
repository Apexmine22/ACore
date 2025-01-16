package frost.commands;

import org.bukkit.command.*;
import org.bukkit.entity.*;
import frost.main.*;
import frost.utils.*;

public class ReloadCommand implements CommandExecutor {

    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] strings) {
        if (commandSender instanceof Player && !ACore.config.getStringList("admins_list").contains(commandSender.getName())) {
            commandSender.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("no_permission")));
            return false;
        }
        ACore.config = ConfigUtil.getFile("config.yml");
        ACore.messages = ConfigUtil.getFile("messages.yml");
        ACore.rules = ConfigUtil.getFile("rules.yml");
        commandSender.sendMessage(RGBUtils.toChatColorString(ConfigUtil.getMSG("reload_configurations")));
        return false;
    }
}
