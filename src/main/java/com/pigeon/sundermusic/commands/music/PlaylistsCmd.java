
package com.pigeon.sundermusic.commands.music;

import java.util.List;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.pigeon.sundermusic.Bot;
import com.pigeon.sundermusic.commands.MusicCommand;

public class PlaylistsCmd extends MusicCommand 
{
    public PlaylistsCmd(Bot bot)
    {
        super(bot);
        this.name = "playlists";
        this.help = "показывает доступные плейлисты";
        this.aliases = bot.getConfig().getAliases(this.name);
        this.guildOnly = true;
        this.beListening = false;
        this.beListening = false;
    }
    
    @Override
    public void doCommand(CommandEvent event) 
    {
        if(!bot.getPlaylistLoader().folderExists())
            bot.getPlaylistLoader().createFolder();
        if(!bot.getPlaylistLoader().folderExists())
        {
            event.reply(event.getClient().getWarning()+" Папка плейлистов не существует и не может быть создана!");
            return;
        }
        List<String> list = bot.getPlaylistLoader().getPlaylistNames();
        if(list==null)
            event.reply(event.getClient().getError()+" Не удалось загрузить доступные плейлисты!");
        else if(list.isEmpty())
            event.reply(event.getClient().getWarning()+" В папке «Плейлисты» нет плейлистов!");
        else
        {
            StringBuilder builder = new StringBuilder(event.getClient().getSuccess()+" Доступные плейлисты:\n");
            list.forEach(str -> builder.append("`").append(str).append("` "));
            builder.append("\nВведите `").append(event.getClient().getTextualPrefix()).append("play playlist <название>`для воспроизведения плейлиста");
            event.reply(builder.toString());
        }
    }
}
