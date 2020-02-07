
package com.pigeon.sundermusic.commands.dj;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.pigeon.sundermusic.Bot;
import com.pigeon.sundermusic.audio.AudioHandler;
import com.pigeon.sundermusic.commands.DJCommand;

public class SkiptoCmd extends DJCommand 
{
    public SkiptoCmd(Bot bot)
    {
        super(bot);
        this.name = "skipto";
        this.help = "переходит к указанной песне";
        this.arguments = "<position>";
        this.aliases = bot.getConfig().getAliases(this.name);
        this.bePlaying = true;
    }

    @Override
    public void doCommand(CommandEvent event) 
    {
        int index = 0;
        try
        {
            index = Integer.parseInt(event.getArgs());
        }
        catch(NumberFormatException e)
        {
            event.reply(event.getClient().getError()+" `"+event.getArgs()+"` не является допустимым целым числом!");
            return;
        }
        AudioHandler handler = (AudioHandler)event.getGuild().getAudioManager().getSendingHandler();
        if(index<1 || index>handler.getQueue().size())
        {
            event.reply(event.getClient().getError()+" Позиция должна быть действительным целым числом от 1 до "+handler.getQueue().size()+"!");
            return;
        }
        handler.getQueue().skip(index-1);
        event.reply(event.getClient().getSuccess()+" Пропущено до **"+handler.getQueue().get(0).getTrack().getInfo().title+"**");
        handler.getPlayer().stopTrack();
    }
}
