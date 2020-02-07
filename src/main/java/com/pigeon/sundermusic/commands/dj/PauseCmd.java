
package com.pigeon.sundermusic.commands.dj;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.pigeon.sundermusic.Bot;
import com.pigeon.sundermusic.audio.AudioHandler;
import com.pigeon.sundermusic.commands.DJCommand;

public class PauseCmd extends DJCommand 
{
    public PauseCmd(Bot bot)
    {
        super(bot);
        this.name = "pause";
        this.help = "остановить проигрывание песни";
        this.aliases = bot.getConfig().getAliases(this.name);
        this.bePlaying = true;
    }

    @Override
    public void doCommand(CommandEvent event) 
    {
        AudioHandler handler = (AudioHandler)event.getGuild().getAudioManager().getSendingHandler();
        if(handler.getPlayer().isPaused())
        {
            event.replyWarning("Плеер уже приостановлен! Используйте `"+event.getClient().getPrefix()+"play` для проигрывания!");
            return;
        }
        handler.getPlayer().setPaused(true);
        event.replySuccess("Остановлена песня **"+handler.getPlayer().getPlayingTrack().getInfo().title+"**. Используйте `"+event.getClient().getPrefix()+"play` для проигрывания!");
    }
}
