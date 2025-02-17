# ApexCore | Minecraft verssion 1.20 | 

## Russian

### Разработчик русский по этому плагин полностью на русском языке
### Плагин поддержует только базу данных MySQL

#### Команды и права к ним:

    - /ban - Забанить игрока на всегда | apexcore.ban
    - /mute - Замутить игрока навсегда | apexcore.mute
    - /tempban - Забанить игрока на время | apexcore.tempban
    - /tempmute - Замутить игрока на время | apexcore.mute
    - /kick - Кикнуть игрока с сервера | apexcore.kick
    - /unban - Разбанить игрока если он в бане | apexcore.unban
    - /unmute - Размутить игрока если он в муте | apexcore.unmute
    - /acore check - Вызвать игрока на проверку | apexcore.command.check
    - /acore spec - Следить за играком | apexcore.command.spec
    - /acore reload - Перезагрузить плагин | apexcore.command.reload

#### Права без комманд:

    - apexcore.protect - Защита на бан мут
    - apexcore.bypass - Обход задержки
    - В файлике config.yml есть настройка admins_list если туда вписать ник то игрока не смогут забанить и у него будут все команды без задержек
#### Функции:
    - Есть система задержек на бан, кик, мут
    - Есть система приорететов между группами
    - Можно ограничить время бана, кика, мута для каждой группы
    - Есть поддержка HexColor
    - Есть поддержка правил файлик rules.yml
    - Полная кастомизация всех сообщений
    - Так же можно отключить модули которые вам не нужны

## English

### Developer Note: This plugin is entirely in Russian
### The plugin only supports the MySQL database

#### Commands and Permissions:

    - /ban - Permanently ban a player | Permission: apexcore.ban
    - /mute - Mute a player forever | Permission: apexcore.mute
    - /tempban - Temporarily ban a player for a set time | Permission: apexcore.tempban
    - /tempmute - Temporarily mute a player for a set time | Permission: apexcore.mute
    - /kick - Kick a player from the server | Permission: apexcore.kick
    - /unban - Unban a banned player | Permission: apexcore.unban
    - /unmute - Unmute a muted player | Permission: apexcore.unmute
    - /acore check - Initiate a player check | Permission: apexcore.command.check
    - /acore spec - Spectate a player | Permission: apexcore.command.spec
    - /acore reload - Reload the plugin | Permission: apexcore.command.reload

#### Permissions without commands:

    - apexcore.protect - Protection against bans and mutes
    - apexcore.bypass - Bypass delay restrictions
    - In the config.yml file, there is an admins_list setting where you can add nicknames of players who cannot be banned and will have all commands available to them without delays.

#### Features:

    - Delay system for ban, kick, and mute actions
    - Priority system between groups
    - Ability to limit the duration of bans, kicks, and mutes for each group
    - Support for HexColor
    - Rules support via the rules.yml file
    - Full customization of all messages
