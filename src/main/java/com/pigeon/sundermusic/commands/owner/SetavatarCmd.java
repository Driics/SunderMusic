
package com.pigeon.sundermusic.commands.owner;

import java.io.IOException;
import java.io.InputStream;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.pigeon.sundermusic.Bot;
import com.pigeon.sundermusic.commands.OwnerCommand;
import com.pigeon.sundermusic.utils.OtherUtil;
import net.dv8tion.jda.core.entities.Icon;

public class SetavatarCmd extends OwnerCommand 
{
    public SetavatarCmd(Bot bot)
    {
        this.name = "setavatar";
        this.help = "устанавливает аватар бота";
        this.arguments = "<url>";
        this.aliases = bot.getConfig().getAliases(this.name);
        this.guildOnly = false;
    }
    
    @Override
    protected void execute(CommandEvent event) 
    {
        String url;
        if(event.getArgs().isEmpty())
            if(!event.getMessage().getAttachments().isEmpty() && event.getMessage().getAttachments().get(0).isImage())
                url = event.getMessage().getAttachments().get(0).getUrl();
            else
                url = null;
        else
            url = event.getArgs();
        InputStream s = OtherUtil.imageFromUrl(url);
        if(s==null)
        {
            event.reply(event.getClient().getError()+" Неверный или отсутствующий URL");
        }
        else
        {
            try {
            event.getSelfUser().getManager().setAvatar(Icon.from(s)).queue(
                    v -> event.reply(event.getClient().getSuccess()+" Успешно измененный аватар."),
                    t -> event.reply(event.getClient().getError()+" Не удалось установить аватар."));
            } catch(IOException e) {
                event.reply(event.getClient().getError()+" Не удалось загрузить с предоставленного URL.");
            }
        }
    }
}
