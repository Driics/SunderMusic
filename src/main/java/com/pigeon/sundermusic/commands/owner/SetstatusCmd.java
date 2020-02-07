
package com.pigeon.sundermusic.commands.owner;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.pigeon.sundermusic.Bot;
import com.pigeon.sundermusic.commands.OwnerCommand;
import net.dv8tion.jda.core.OnlineStatus;

public class SetstatusCmd extends OwnerCommand
{
    public SetstatusCmd(Bot bot)
    {
        this.name = "setstatus";
        this.help = "устанавливает статус, отображаемый ботом";
        this.arguments = "<status>";
        this.aliases = bot.getConfig().getAliases(this.name);
        this.guildOnly = false;
    }
    
    @Override
    protected void execute(CommandEvent event) 
    {
        try {
            OnlineStatus status = OnlineStatus.fromKey(event.getArgs());
            if(status==OnlineStatus.UNKNOWN)
            {
                event.replyError("Пожалуйста, включите один из следующих статусов: `ONLINE`, `IDLE`, `DND`, `INVISIBLE`");
            }
            else
            {
                event.getJDA().getPresence().setStatus(status);
                event.replySuccess("Статус изменен на `"+status.getKey().toUpperCase()+"`");
            }
        } catch(Exception e) {
            event.reply(event.getClient().getError()+" Статус не может быть установлен!");
        }
    }
}
