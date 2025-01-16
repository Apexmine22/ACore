package frost.commands;


import frost.utils.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class YouTubeBroadcastCommand implements CommandExecutor {

    private static final String PERMISSION = "abansystem.youtube";
    private static final String PLAYER_ONLY_MSG = RGBUtils.toChatColorString(ConfigUtil.getMSG("player_on_command"));
    private static final String NO_PERMISSION_MSG = RGBUtils.toChatColorString(ConfigUtil.getMSG("no_permission"));
    private static final String YOUTUBE_USAGE_MSG = RGBUtils.toChatColorString(ConfigUtil.getMSG("youtube_usage"));

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(PERMISSION)) {
            sender.sendMessage(NO_PERMISSION_MSG);
            return false;
        }

        if (label.equalsIgnoreCase("youtube")) {
            if (args.length == 0 || !isValidSubcommand(args[0])) {
                sendUsage(sender);
                return true;
            }

            switch (args[0].toLowerCase()) {
                case "bc":
                    handleBroadcast(sender, args);
                    break;
                default:
                    sendUsage(sender);
                    break;
            }

            return true;
        }

        return false;
    }

    /**
     * Обработчик подкоманды "bc"
     *
     * @param sender отправитель команды
     * @param args   массив аргументов
     */
    private void handleBroadcast(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(PLAYER_ONLY_MSG);
            return;
        }

        StringBuilder message = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            message.append(args[i]).append(" ");
        }

        broadcastMessage(sender, String.valueOf(message));
    }

    /**
     * Обработчик подкоманды "suffix"
     *
     * @param sender отправитель команды
     * @param args   массив аргументов
     */

    /**
     * Отправляет сообщение всем игрокам на сервере
     *
     * @param sender отправитель команды
     * @param message сообщение
     */
    private void broadcastMessage(CommandSender sender, String message) {
        sender.getServer().broadcastMessage(
                RGBUtils.toChatColorString(
                        ConfigUtil.getMSG("youTube_broadcast_command_message")
                                .replace("{player}", sender.getName())
                                .replace("{msg}", message)
                )
        );
    }

    /**
     * Проверяет, является ли аргумент допустимой подкомандой
     *
     * @param subcommand строка подкоманды
     * @return true, если подкоманда валидна, иначе false
     */
    private boolean isValidSubcommand(String subcommand) {
        return subcommand.equalsIgnoreCase("bc") || subcommand.equalsIgnoreCase("suffix");
    }

    /**
     * Отправляет сообщение об использовании команды
     *
     * @param sender отправитель команды
     */
    private void sendUsage(CommandSender sender) {
        sender.sendMessage(YOUTUBE_USAGE_MSG);
    }
}