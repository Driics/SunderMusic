
package com.pigeon.sundermusic.commands.admin;

import java.util.List;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import com.pigeon.sundermusic.Bot;
import com.pigeon.sundermusic.commands.AdminCommand;
import com.pigeon.sundermusic.settings.Settings;
import com.pigeon.sundermusic.utils.FormatUtil;
import net.dv8tion.jda.core.entities.TextChannel;

public class SettcCmd extends AdminCommand 
{
    public SettcCmd(Bot bot)
    {
        this.name = "settc";
        this.help = "устанавливает текстовый канал для музыкальных команд";
        this.arguments = "<channel|NONE>";
        this.aliases = bot.getConfig().getAliases(this.name);
    }
    
    @Override
    protected void execute(CommandEvent event) 
    {
        if(event.getArgs().isEmpty())
        {
            event.reply(event.getClient().getError()+" Пожалуйста, укажите текстовый канал или NONE");
            return;
        }
        Settings s = event.getClient().getSettingsFor(event.getGuild());
        if(event.getArgs().equalsIgnoreCase("none"))
        {
            s.setTextChannel(null);
            event.reply(event.getClient().getSuccess()+" Музыкальные команды теперь можно использовать на любом канале");
        }
        else
        {
            List<TextChannel> list = FinderUtil.findTextChannels(event.getArgs(), event.getGuild());
            if(list.isEmpty())
                event.reply(event.getClient().getWarning()+" Текстовый канал \""+event.getArgs()+"\" не найден.");
            else if (list.size()>1)
                event.reply(event.getClient().getWarning()+FormatUtil.listOfTChannels(list, event.getArgs()));
            else
            {
                s.setTextChannel(list.get(0));
                event.reply(event.getClient().getSuccess()+" Музыкальные команды теперь можно использовать только в канале <#"+list.get(0).getId()+">");
            }
        }
    }
    
}
