
package com.pigeon.sundermusic.commands.music;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.pigeon.sundermusic.Bot;
import com.pigeon.sundermusic.audio.AudioHandler;
import com.pigeon.sundermusic.audio.QueuedTrack;
import com.pigeon.sundermusic.commands.MusicCommand;
import com.pigeon.sundermusic.settings.Settings;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.User;

public class RemoveCmd extends MusicCommand 
{
    public RemoveCmd(Bot bot)
    {
        super(bot);
        this.name = "remove";
        this.help = "удаляет песню из очереди";
        this.arguments = "<position|ALL>";
        this.aliases = bot.getConfig().getAliases(this.name);
        this.beListening = true;
        this.bePlaying = true;
    }

    @Override
    public void doCommand(CommandEvent event) 
    {
        AudioHandler handler = (AudioHandler)event.getGuild().getAudioManager().getSendingHandler();
        if(handler.getQueue().isEmpty())
        {
            event.replyError("В очереди ничего нет!");
            return;
        }
        if(event.getArgs().equalsIgnoreCase("all"))
        {
            int count = handler.getQueue().removeAll(event.getAuthor().getIdLong());
            if(count==0)
                event.replyWarning("У вас нет песен в очереди!");
            else
                event.replySuccess("Успешно удалено "+count+" записей.");
            return;
        }
        int pos;
        try {
            pos = Integer.parseInt(event.getArgs());
        } catch(NumberFormatException e) {
            pos = 0;
        }
        if(pos<1 || pos>handler.getQueue().size())
        {
            event.replyError("Позиция должна быть действительным целым числом от 1 до "+handler.getQueue().size()+"!");
            return;
        }
        Settings settings = event.getClient().getSettingsFor(event.getGuild());
        boolean isDJ = event.getMember().hasPermission(Permission.MANAGE_SERVER);
        if(!isDJ)
            isDJ = event.getMember().getRoles().contains(settings.getRole(event.getGuild()));
        QueuedTrack qt = handler.getQueue().get(pos-1);
        if(qt.getIdentifier()==event.getAuthor().getIdLong())
        {
            handler.getQueue().remove(pos-1);
            event.replySuccess("Песня **"+qt.getTrack().getInfo().title+"** удалена из очереди");
        }
        else if(isDJ)
        {
            handler.getQueue().remove(pos-1);
            User u;
            try {
                u = event.getJDA().getUserById(qt.getIdentifier());
            } catch(Exception e) {
                u = null;
            }
            event.replySuccess("Песня **"+qt.getTrack().getInfo().title
                    +"** удалена из очереди (запросил "+(u==null ? "кто-то" : "**"+u.getName()+"**")+")");
        }
        else
        {
            event.replyError("Вы не можете удалить **"+qt.getTrack().getInfo().title+"** потому что не вы добавили это!");
        }
    }
}
