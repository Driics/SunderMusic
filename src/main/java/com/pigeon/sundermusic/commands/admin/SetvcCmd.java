
package com.pigeon.sundermusic.commands.admin;

import java.util.List;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import com.pigeon.sundermusic.Bot;
import com.pigeon.sundermusic.commands.AdminCommand;
import com.pigeon.sundermusic.settings.Settings;
import com.pigeon.sundermusic.utils.FormatUtil;
import net.dv8tion.jda.core.entities.VoiceChannel;

public class SetvcCmd extends AdminCommand 
{
    public SetvcCmd(Bot bot)
    {
        this.name = "setvc";
        this.help = "устанавливает голосовой канал для воспроизведения музыки";
        this.arguments = "<channel|NONE>";
        this.aliases = bot.getConfig().getAliases(this.name);
    }
    
    @Override
    protected void execute(CommandEvent event) 
    {
        if(event.getArgs().isEmpty())
        {
            event.reply(event.getClient().getError()+" Пожалуйстка, укажите голосовой канал или NONE");
            return;
        }
        Settings s = event.getClient().getSettingsFor(event.getGuild());
        if(event.getArgs().equalsIgnoreCase("none"))
        {
            s.setVoiceChannel(null);
            event.reply(event.getClient().getSuccess()+" Музыка теперь будет проигрываться в любом канале");
        }
        else
        {
            List<VoiceChannel> list = FinderUtil.findVoiceChannels(event.getArgs(), event.getGuild());
            if(list.isEmpty())
                event.reply(event.getClient().getWarning()+" Не обнаружено Голосовых каналов с названием \""+event.getArgs()+"\"");
            else if (list.size()>1)
                event.reply(event.getClient().getWarning()+FormatUtil.listOfVChannels(list, event.getArgs()));
            else
            {
                s.setVoiceChannel(list.get(0));
                event.reply(event.getClient().getSuccess()+" Теперь музыку можно проигрывать только в канале **"+list.get(0).getName()+"**");
            }
        }
    }
}
