
package com.pigeon.sundermusic.commands.admin;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.pigeon.sundermusic.Bot;
import com.pigeon.sundermusic.commands.AdminCommand;
import com.pigeon.sundermusic.settings.Settings;

public class PrefixCmd extends AdminCommand
{
    public PrefixCmd(Bot bot)
    {
        this.name = "prefix";
        this.help = "установит префикс для сервера";
        this.arguments = "<prefix|NONE>";
        this.aliases = bot.getConfig().getAliases(this.name);
    }
    
    @Override
    protected void execute(CommandEvent event) 
    {
        if(event.getArgs().isEmpty())
        {
            event.replyError("Пожалуйста, укажите префикс или NONE");
            return;
        }
        
        Settings s = event.getClient().getSettingsFor(event.getGuild());
        if(event.getArgs().equalsIgnoreCase("none"))
        {
            s.setPrefix(null);
            event.replySuccess("Префикс очищен.");
        }
        else
        {
            s.setPrefix(event.getArgs());
            event.replySuccess("Префикс установлен на `" + event.getArgs() + "` на сервере **" + event.getGuild().getName() + "**");
        }
    }
}
