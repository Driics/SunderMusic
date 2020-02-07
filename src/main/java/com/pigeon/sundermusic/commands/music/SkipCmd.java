
package com.pigeon.sundermusic.commands.music;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.pigeon.sundermusic.Bot;
import com.pigeon.sundermusic.audio.AudioHandler;
import com.pigeon.sundermusic.commands.MusicCommand;
import net.dv8tion.jda.core.entities.User;

public class SkipCmd extends MusicCommand 
{
    public SkipCmd(Bot bot)
    {
        super(bot);
        this.name = "skip";
        this.help = "голосование, чтобы пропустить текущую песню";
        this.aliases = bot.getConfig().getAliases(this.name);
        this.beListening = true;
        this.bePlaying = true;
    }

    @Override
    public void doCommand(CommandEvent event) 
    {
        AudioHandler handler = (AudioHandler)event.getGuild().getAudioManager().getSendingHandler();
        if(event.getAuthor().getIdLong() == handler.getRequester())
        {
            event.reply(event.getClient().getSuccess()+" Песня **"+handler.getPlayer().getPlayingTrack().getInfo().title+"** пропущена");
            handler.getPlayer().stopTrack();
        }
        else
        {
            int listeners = (int)event.getSelfMember().getVoiceState().getChannel().getMembers().stream()
                    .filter(m -> !m.getUser().isBot() && !m.getVoiceState().isDeafened()).count();
            String msg;
            if(handler.getVotes().contains(event.getAuthor().getId()))
                msg = event.getClient().getWarning()+" Вы уже проголосовали, чтобы пропустить эту песню `[";
            else
            {
                msg = event.getClient().getSuccess()+" Вы проголосовали за пропуск песни `[";
                handler.getVotes().add(event.getAuthor().getId());
            }
            int skippers = (int)event.getSelfMember().getVoiceState().getChannel().getMembers().stream()
                    .filter(m -> handler.getVotes().contains(m.getUser().getId())).count();
            int required = (int)Math.ceil(listeners * .55);
            msg+= skippers+" голосов, "+required+"/"+listeners+" необходимо]`";
            if(skippers>=required)
            {
                User u = event.getJDA().getUserById(handler.getRequester());
                msg+="\n"+event.getClient().getSuccess()+" Песня **"+handler.getPlayer().getPlayingTrack().getInfo().title
                    +"** пропущена"+(handler.getRequester()==0 ? "" : " (запросил "+(u==null ? "кто-то" : "**"+u.getName()+"**")+")");
                handler.getPlayer().stopTrack();
            }
            event.reply(msg);
        }
    }
    
}
