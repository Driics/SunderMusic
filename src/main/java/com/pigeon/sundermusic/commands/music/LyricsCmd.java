
package com.pigeon.sundermusic.commands.music;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jlyrics.LyricsClient;
import com.pigeon.sundermusic.Bot;
import com.pigeon.sundermusic.audio.AudioHandler;
import com.pigeon.sundermusic.commands.MusicCommand;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;

public class LyricsCmd extends MusicCommand
{
    private final LyricsClient client = new LyricsClient();
    
    public LyricsCmd(Bot bot)
    {
        super(bot);
        this.name = "lyrics";
        this.arguments = "[song name]";
        this.help = "показывает текст песни к песне";
        this.aliases = bot.getConfig().getAliases(this.name);
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        this.bePlaying = true;
    }

    @Override
    public void doCommand(CommandEvent event)
    {
        event.getChannel().sendTyping().queue();
        String title;
        if(event.getArgs().isEmpty())
            title = ((AudioHandler)event.getGuild().getAudioManager().getSendingHandler()).getPlayer().getPlayingTrack().getInfo().title;
        else
            title = event.getArgs();
        client.getLyrics(title).thenAccept(lyrics -> 
        {
            if(lyrics == null)
            {
                event.replyError("Слов к песне `" + title + "` не найдены!" + (event.getArgs().isEmpty() ? " Попробуйте ввести название песни вручную (`lyrics [название песни]`)" : ""));
                return;
            }

            EmbedBuilder eb = new EmbedBuilder()
                    .setAuthor(lyrics.getAuthor())
                    .setColor(event.getSelfMember().getColor())
                    .setTitle(lyrics.getTitle(), lyrics.getURL());
            if(lyrics.getContent().length()>15000)
            {
                event.replyWarning("Тексты песни для `" + title + "` найден, но, вероятно, не правильный: " + lyrics.getURL());
            }
            else if(lyrics.getContent().length()>2000)
            {
                String content = lyrics.getContent().trim();
                while(content.length() > 2000)
                {
                    int index = content.lastIndexOf("\n\n", 2000);
                    if(index == -1)
                        index = content.lastIndexOf("\n", 2000);
                    if(index == -1)
                        index = content.lastIndexOf(" ", 2000);
                    if(index == -1)
                        index = 2000;
                    event.reply(eb.setDescription(content.substring(0, index).trim()).build());
                    content = content.substring(index).trim();
                    eb.setAuthor(null).setTitle(null, null);
                }
                event.reply(eb.setDescription(content).build());
            }
            else
                event.reply(eb.setDescription(lyrics.getContent()).build());
        });
    }
}
