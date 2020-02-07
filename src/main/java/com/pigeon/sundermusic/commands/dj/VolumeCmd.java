
package com.pigeon.sundermusic.commands.dj;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.pigeon.sundermusic.Bot;
import com.pigeon.sundermusic.audio.AudioHandler;
import com.pigeon.sundermusic.commands.DJCommand;
import com.pigeon.sundermusic.settings.Settings;
import com.pigeon.sundermusic.utils.FormatUtil;

public class VolumeCmd extends DJCommand
{
    public VolumeCmd(Bot bot)
    {
        super(bot);
        this.name = "volume";
        this.aliases = bot.getConfig().getAliases(this.name);
        this.help = "устанавливает или показывает громкость";
        this.arguments = "[0-150]";
    }

    @Override
    public void doCommand(CommandEvent event)
    {
        AudioHandler handler = (AudioHandler)event.getGuild().getAudioManager().getSendingHandler();
        Settings settings = event.getClient().getSettingsFor(event.getGuild());
        int volume = handler.getPlayer().getVolume();
        if(event.getArgs().isEmpty())
        {
            event.reply(FormatUtil.volumeIcon(volume)+" Текущая громкость `"+volume+"`");
        }
        else
        {
            int nvolume;
            try{
                nvolume = Integer.parseInt(event.getArgs());
            }catch(NumberFormatException e){
                nvolume = -1;
            }
            if(nvolume<0 || nvolume>150)
                event.reply(event.getClient().getError()+" Громкость должна быть действительным целым числом от 0 до 150!");
            else
            {
                handler.getPlayer().setVolume(nvolume);
                settings.setVolume(nvolume);
                event.reply(FormatUtil.volumeIcon(nvolume)+" Громкость изменена с `"+volume+"` на `"+nvolume+"`");
            }
        }
    }
    
}
