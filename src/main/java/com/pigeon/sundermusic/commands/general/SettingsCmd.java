
package com.pigeon.sundermusic.commands.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.pigeon.sundermusic.Bot;
import com.pigeon.sundermusic.settings.Settings;
import com.pigeon.sundermusic.utils.FormatUtil;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;

public class SettingsCmd extends Command 
{
    private final static String EMOJI = "\uD83C\uDFA7"; // 🎧
    
    public SettingsCmd(Bot bot)
    {
        this.name = "settings";
        this.help = "показывает настройки бота";
        this.aliases = bot.getConfig().getAliases(this.name);
        this.guildOnly = true;
    }
    
    @Override
    protected void execute(CommandEvent event) 
    {
        Settings s = event.getClient().getSettingsFor(event.getGuild());
        MessageBuilder builder = new MessageBuilder()
                .append(EMOJI + " **")
                .append(FormatUtil.filter(event.getSelfUser().getName()))
                .append("** Настройки:");
        TextChannel tchan = s.getTextChannel(event.getGuild());
        VoiceChannel vchan = s.getVoiceChannel(event.getGuild());
        Role role = s.getRole(event.getGuild());
        EmbedBuilder ebuilder = new EmbedBuilder()
                .setColor(event.getSelfMember().getColor())
                .setDescription("Текстовый канал: " + (tchan == null ? "любой" : "**#" + tchan.getName() + "**")
                        + "\nГолосовой канал: " + (vchan == null ? "любой" : "**" + vchan.getName() + "**")
                        + "\nDJ роль: " + (role == null ? "-" : "**" + role.getName() + "**")
                        + "\nКастомный префикс: " + (s.getPrefix() == null ? "-" : "`" + s.getPrefix() + "`")
                        + "\nРежим повтора: **" + (s.getRepeatMode() ? "On" : "Off") + "**"
                        + "\nСтандартный плейлист: " + (s.getDefaultPlaylist() == null ? "-" : "**" + s.getDefaultPlaylist() + "**")
                        )
                .setFooter(event.getJDA().getGuilds().size() + " servers | "
                        + event.getJDA().getGuilds().stream().filter(g -> g.getSelfMember().getVoiceState().inVoiceChannel()).count()
                        + " audio connections", null);
        event.getChannel().sendMessage(builder.setEmbed(ebuilder.build()).build()).queue();
    }
    
}
