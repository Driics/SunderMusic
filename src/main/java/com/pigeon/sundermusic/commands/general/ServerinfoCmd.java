package com.pigeon.sundermusic.commands.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.pigeon.sundermusic.Bot;
import com.pigeon.sundermusic.utils.FormatUtil;
import java.time.format.DateTimeFormatter;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;

public class ServerinfoCmd extends Command {
    private final static String LINESTART = "\u25AB"; // ▫
    private final static String GUILD_EMOJI = "\uD83D\uDDA5"; // 🖥
    private final static String NO_REGION = "\u2754"; // ❔

    public ServerinfoCmd(Bot bot) {
        this.name = "serverinfo";
        this.aliases = new String[]{"server", "guildinfo", "si"};
        this.help = "показывает информацию о сервере";
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        this.guildOnly = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        Guild guild = event.getGuild();
        long onlineCount = guild.getMembers().stream().filter((u) -> (u.getOnlineStatus() != OnlineStatus.OFFLINE)).count();
        long botCount = guild.getMembers().stream().filter(m -> m.getUser().isBot()).count();
        EmbedBuilder builder = new EmbedBuilder();
        String title = FormatUtil.filter(GUILD_EMOJI + " Информация о **" + guild.getName() + "**:");
        String verif;
        switch (guild.getVerificationLevel()) {
            case VERY_HIGH:
                verif = "┻━┻ミヽ(ಠ益ಠ)ノ彡┻━┻";
                break;
            case HIGH:
                verif = "(╯°□°）╯︵ ┻━┻";
                break;
            default:
                verif = guild.getVerificationLevel().name();
                break;
        }
        String str = LINESTART + "ID: **" + guild.getId() + "**\n"
                + LINESTART + "Владелец: " + FormatUtil.formatUser(guild.getOwner().getUser()) + "\n"
                + LINESTART + "Регион: " + (guild.getRegion().getEmoji() == null ? NO_REGION : guild.getRegion().getEmoji()) + " **" + guild.getRegion().getName() + "**\n"
                + LINESTART + "Дата создания: **" + guild.getCreationTime().format(DateTimeFormatter.ISO_LOCAL_DATE) + "**\n"
                + LINESTART + "Пользователей: **" + guild.getMemberCache().size() + "** (" + onlineCount + " online, " + botCount + " bots)\n"
                + LINESTART + "Каналы: **" + guild.getTextChannelCache().size() + "** Текстовых, **" + guild.getVoiceChannelCache().size() + "** Голосовых, **" + guild.getCategoryCache().size() + "** Категорий\n"
                + LINESTART + "Верификация: **" + verif + "**";
        if (!guild.getFeatures().isEmpty())
            str += "\n" + LINESTART + "Features: **" + String.join("**, **", guild.getFeatures()) + "**";
        if (guild.getSplashId() != null) {
            builder.setImage(guild.getSplashUrl() + "?size=1024");
            str += "\n" + LINESTART + "Splash: ";
        }
        if (guild.getIconUrl() != null)
            builder.setThumbnail(guild.getIconUrl());
        builder.setColor(guild.getOwner().getColor());
        builder.setDescription(str);
        event.reply(new MessageBuilder().append(title).setEmbed(builder.build()).build());
    }
}