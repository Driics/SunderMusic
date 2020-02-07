
package com.pigeon.sundermusic.commands.owner;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.JDAUtilitiesInfo;
import com.pigeon.sundermusic.Bot;
import com.pigeon.sundermusic.commands.OwnerCommand;
import com.pigeon.sundermusic.utils.OtherUtil;
import com.sedmelluq.discord.lavaplayer.tools.PlayerLibrary;
import net.dv8tion.jda.core.JDAInfo;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;

public class DebugCmd extends OwnerCommand 
{
    private final static String[] PROPERTIES = {"java.version", "java.vm.name", "java.vm.specification.version", 
        "java.runtime.name", "java.runtime.version", "java.specification.version",  "os.arch", "os.name"};
    
    private final Bot bot;
    
    public DebugCmd(Bot bot)
    {
        this.bot = bot;
        this.name = "debug";
        this.help = "показывает отладочную информацию";
        this.aliases = bot.getConfig().getAliases(this.name);
        this.guildOnly = false;
    }

    @Override
    protected void execute(CommandEvent event)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("System Properties:");
        for(String key: PROPERTIES)
            sb.append("\n  ").append(key).append(" = ").append(System.getProperty(key));
        sb.append("\n\nSunderMusic Information:")
                .append("\n  Версия = ").append(OtherUtil.getCurrentVersion())
                .append("\n  Владелец = ").append(bot.getConfig().getOwnerId())
                .append("\n  Префикс = ").append(bot.getConfig().getPrefix())
                .append("\n  Доп. префикс = ").append(bot.getConfig().getAltPrefix())
                .append("\n  Макс. длинна = ").append(bot.getConfig().getMaxSeconds())
                .append("\n  NPImages = ").append(bot.getConfig().useNPImages())
                .append("\n  Песня в статусе = ").append(bot.getConfig().getSongInStatus())
                .append("\n  StayInChannel = ").append(bot.getConfig().getStay())
                .append("\n  UseEval = ").append(bot.getConfig().useEval())
                .append("\n  Обновить оповещения = ").append(bot.getConfig().useUpdateAlerts());
        sb.append("\n\nИнформация о зависимостях:")
                .append("\n  JDA Version = ").append(JDAInfo.VERSION)
                .append("\n  JDA-Utilities Version = ").append(JDAUtilitiesInfo.VERSION)
                .append("\n  Lavaplayer Version = ").append(PlayerLibrary.VERSION);
        long total = Runtime.getRuntime().totalMemory() / 1024 / 1024;
        long used = total - (Runtime.getRuntime().freeMemory() / 1024 / 1024);
        sb.append("\n\nИнформация о памяти:")
                .append("\n  Всего памяти = ").append(total)
                .append("\n  Использовано памяти= ").append(used);
        sb.append("\n\nDiscord Информация:")
                .append("\n  ID = ").append(event.getJDA().getSelfUser().getId())
                .append("\n  Серверов = ").append(event.getJDA().getGuildCache().size())
                .append("\n  Пользователей = ").append(event.getJDA().getUserCache().size());
        
        if(event.isFromType(ChannelType.PRIVATE) 
                || event.getSelfMember().hasPermission(event.getTextChannel(), Permission.MESSAGE_ATTACH_FILES))
            event.getChannel().sendFile(sb.toString().getBytes(), "debug_information.txt").queue();
        else
            event.reply("Отладочная информация: ```\n" + sb.toString() + "\n```");
    }
}
