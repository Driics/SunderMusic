
package com.pigeon.sundermusic.commands.music;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.pigeon.sundermusic.Bot;
import com.pigeon.sundermusic.audio.AudioHandler;
import com.pigeon.sundermusic.commands.MusicCommand;

public class ShuffleCmd extends MusicCommand 
{
    public ShuffleCmd(Bot bot)
    {
        super(bot);
        this.name = "shuffle";
        this.help = "перемешивает песни, которые вы добавили";
        this.aliases = bot.getConfig().getAliases(this.name);
        this.beListening = true;
        this.bePlaying = true;
    }

    @Override
    public void doCommand(CommandEvent event) 
    {
        AudioHandler handler = (AudioHandler)event.getGuild().getAudioManager().getSendingHandler();
        int s = handler.getQueue().shuffle(event.getAuthor().getIdLong());
        switch (s) 
        {
            case 0:
                event.replyError("У вас нет музыки в очереди, чтобы перемешать!");
                break;
            case 1:
                event.replyWarning("У вас есть только одна песня в очереди!");
                break;
            default:
                event.replySuccess("Вы успешно перемешали свои "+s+" записей.");
                break;
        }
    }
    
}
