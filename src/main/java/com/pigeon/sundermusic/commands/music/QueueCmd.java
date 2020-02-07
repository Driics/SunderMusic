package com.pigeon.sundermusic.commands.music;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.menu.Paginator;
import com.pigeon.sundermusic.Bot;
import com.pigeon.sundermusic.SunderBot;
import com.pigeon.sundermusic.audio.AudioHandler;
import com.pigeon.sundermusic.audio.QueuedTrack;
import com.pigeon.sundermusic.commands.MusicCommand;
import com.pigeon.sundermusic.settings.Settings;
import com.pigeon.sundermusic.utils.FormatUtil;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.exceptions.PermissionException;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class QueueCmd extends MusicCommand
{
    private final static String REPEAT = "\uD83D\uDD01"; // 🔁

    private final Paginator.Builder builder;

    public QueueCmd(Bot bot)
    {
        super(bot);
        this.name = "queue";
        this.help = "показывает текущую очередь";
        this.arguments = "[pagenum]";
        this.aliases = bot.getConfig().getAliases(this.name);
        this.bePlaying = true;
        this.botPermissions = new Permission[]{Permission.MESSAGE_ADD_REACTION,Permission.MESSAGE_EMBED_LINKS};
        builder = new Paginator.Builder()
                .setColumns(1)
                .setFinalAction(m -> {try{m.clearReactions().queue();}catch(PermissionException ignore){}})
                .setItemsPerPage(10)
                .waitOnSinglePage(false)
                .useNumberedItems(true)
                .showPageNumbers(true)
                .wrapPageEnds(true)
                .setEventWaiter(bot.getWaiter())
                .setTimeout(1, TimeUnit.MINUTES);
    }

    @Override
    public void doCommand(CommandEvent event)
    {
        int pagenum = 1;
        try
        {
            pagenum = Integer.parseInt(event.getArgs());
        }
        catch(NumberFormatException ignore){}
        AudioHandler ah = (AudioHandler)event.getGuild().getAudioManager().getSendingHandler();
        List<QueuedTrack> list = ah.getQueue().getList();
        if(list.isEmpty())
        {
            Message nowp = ah.getNowPlaying(event.getJDA());
            Message nonowp = ah.getNoMusicPlaying(event.getJDA());
            Message built = new MessageBuilder()
                    .setContent(event.getClient().getWarning() + " В очереди нет музыки!")
                    .setEmbed((nowp==null ? nonowp : nowp).getEmbeds().get(0)).build();
            event.reply(built, m ->
            {
                if(nowp!=null)
                    bot.getNowplayingHandler().setLastNPMessage(m);
            });
            return;
        }
        String[] songs = new String[list.size()];
        long total = 0;
        for(int i=0; i<list.size(); i++)
        {
            total += list.get(i).getTrack().getDuration();
            songs[i] = list.get(i).toString();
        }
        Settings settings = event.getClient().getSettingsFor(event.getGuild());
        long fintotal = total;
        builder.setText((i1,i2) -> getQueueTitle(ah, event.getClient().getSuccess(), songs.length, fintotal, settings.getRepeatMode()))
                .setItems(songs)
                .setUsers(event.getAuthor())
                .setColor(event.getSelfMember().getColor())
        ;
        builder.build().paginate(event.getChannel(), pagenum);
    }

    private String getQueueTitle(AudioHandler ah, String success, int songslength, long total, boolean repeatmode)
    {
        StringBuilder sb = new StringBuilder();
        if(ah.getPlayer().getPlayingTrack()!=null)
        {
            sb.append(ah.getPlayer().isPaused() ? SunderBot.PAUSE_EMOJI : SunderBot.PLAY_EMOJI).append(" **")
                    .append(ah.getPlayer().getPlayingTrack().getInfo().title).append("**\n");
        }
        return FormatUtil.filter(sb.append(success).append(" Текущая очередь | ").append(songslength)
                .append(" записей | `").append(FormatUtil.formatTime(total)).append("` ")
                .append(repeatmode ? "| " + REPEAT : "").toString());
    }
}