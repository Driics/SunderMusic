package com.pigeon.sundermusic.commands.general;

import java.time.format.DateTimeFormatter;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import com.pigeon.sundermusic.Bot;
import com.pigeon.sundermusic.commands.CommandExceptionListener.CommandErrorException;
import com.pigeon.sundermusic.utils.FormatUtil;
import java.util.List;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

public class RoleinfoCmd extends Command
{
    private final static String LINESTART = "\u25AB"; // ▫
    private final static String ROLE_EMOJI = "\uD83C\uDFAD"; // 🎭

    public RoleinfoCmd(Bot bot)
    {
        this.name = "roleinfo";
        this.aliases = new String[]{"rinfo","rankinfo","роль"};
        this.help = "shows info about a role";
        this.arguments = "<role>";
        this.guildOnly = true;
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
    }

    @Override
    protected void execute(CommandEvent event)
    {
        Role role;
        if(event.getArgs().isEmpty())
            throw new CommandErrorException("Пожалуйста, укажите название роли!");
        else
        {
            List<Role> found = FinderUtil.findRoles(event.getArgs(), event.getGuild());
            if(found.isEmpty())
            {
                event.replyError("Я не могу найти роль, которую вы искали!");
                return;
            }
            else if(found.size()>1)
            {
                event.replyWarning(FormatUtil.listOfRoles(found, event.getArgs()));
                return;
            }
            else
            {
                role = found.get(0);
            }
        }

        String title = ROLE_EMOJI + " Информация о роли **"+role.getName()+"**:";
        List<Member> list = role.isPublicRole() ? event.getGuild().getMembers() : event.getGuild().getMembersWithRoles(role);
        StringBuilder desr = new StringBuilder(LINESTART+"ID: **"+role.getId()+"**\n"
                + LINESTART+"Дата создания: **"+role.getCreationTime().format(DateTimeFormatter.ISO_LOCAL_DATE)+"**\n"
                + LINESTART+"Позиция: **"+role.getPosition()+"**\n"
                + LINESTART+"Цвет: **#"+(role.getColor()==null ? "000000" : Integer.toHexString(role.getColor().getRGB()).toUpperCase().substring(2))+"**\n"
                + LINESTART+"Можно ли упоминать: **"+role.isMentionable()+"**\n"
                + LINESTART+"Hoisted: **"+role.isHoisted()+"**\n"
                + LINESTART+"Модерация: **"+role.isManaged()+"**\n"
                + LINESTART+"Права: ");
        if(role.getPermissions().isEmpty())
            desr.append("-");
        else
            desr.append(role.getPermissions().stream().map(p -> "`, `"+p.getName()).reduce("", String::concat).substring(3)).append("`");
        desr.append("\n").append(LINESTART).append("Участники: **").append(list.size()).append("**\n");
        if(list.size()*24<=2048-desr.length())
            list.forEach(m -> desr.append("<@").append(m.getUser().getId()).append("> "));

        event.reply(new MessageBuilder()
                .append(FormatUtil.filter(title))
                .setEmbed(new EmbedBuilder()
                        .setDescription(desr.toString().trim())
                        .setColor(role.getColor()).build())
                .build());
    }
}