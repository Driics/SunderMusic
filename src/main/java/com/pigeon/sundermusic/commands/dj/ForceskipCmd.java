
package com.pigeon.sundermusic.commands.dj;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.pigeon.sundermusic.Bot;
import com.pigeon.sundermusic.audio.AudioHandler;
import com.pigeon.sundermusic.commands.DJCommand;
import net.dv8tion.jda.core.entities.User;

public class ForceskipCmd extends DJCommand 
{
    public ForceskipCmd(Bot bot)
    {
        super(bot);
        this.name = "forceskip";
        this.help = "пропускает текущую песню";
        this.aliases = bot.getConfig().getAliases(this.name);
        this.bePlaying = true;
    }

    @Override
    public void doCommand(CommandEvent event) 
    {
        AudioHandler handler = (AudioHandler)event.getGuild().getAudioManager().getSendingHandler();
        User u = event.getJDA().getUserById(handler.getRequester());
        event.reply(event.getClient().getSuccess()+"Пропущена песня **"+handler.getPlayer().getPlayingTrack().getInfo().title
                +"** (запросил "+(u==null ? "кто-то" : "**"+u.getName()+"**")+")");
        handler.getPlayer().stopTrack();
    }
}
