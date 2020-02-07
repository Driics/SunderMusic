
package com.pigeon.sundermusic.commands.dj;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.pigeon.sundermusic.Bot;
import com.pigeon.sundermusic.audio.AudioHandler;
import com.pigeon.sundermusic.commands.DJCommand;

public class StopCmd extends DJCommand 
{
    public StopCmd(Bot bot)
    {
        super(bot);
        this.name = "stop";
        this.help = "останавливает текущую песню и очищает очередь";
        this.aliases = bot.getConfig().getAliases(this.name);
        this.bePlaying = false;
    }

    @Override
    public void doCommand(CommandEvent event) 
    {
        AudioHandler handler = (AudioHandler)event.getGuild().getAudioManager().getSendingHandler();
        handler.stopAndClear();
        event.getGuild().getAudioManager().closeAudioConnection();
        event.reply(event.getClient().getSuccess()+" Плеер остановился и очередь была очищена.");
    }
}
