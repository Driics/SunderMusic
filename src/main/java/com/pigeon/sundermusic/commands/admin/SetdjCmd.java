
package com.pigeon.sundermusic.commands.admin;

import java.util.List;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import com.pigeon.sundermusic.Bot;
import com.pigeon.sundermusic.commands.AdminCommand;
import com.pigeon.sundermusic.settings.Settings;
import com.pigeon.sundermusic.utils.FormatUtil;
import net.dv8tion.jda.core.entities.Role;

public class SetdjCmd extends AdminCommand
{
    public SetdjCmd(Bot bot)
    {
        this.name = "setdj";
        this.help = "устанавливает роль DJ для определенных музыкальных команд";
        this.arguments = "<rolename|NONE>";
        this.aliases = bot.getConfig().getAliases(this.name);
    }
    
    @Override
    protected void execute(CommandEvent event) 
    {
        if(event.getArgs().isEmpty())
        {
            event.reply(event.getClient().getError()+" Пожалуйста, укажите название роли или NONE");
            return;
        }
        Settings s = event.getClient().getSettingsFor(event.getGuild());
        if(event.getArgs().equalsIgnoreCase("none"))
        {
            s.setDJRole(null);
            event.reply(event.getClient().getSuccess()+" Роль DJ очищена; Только администраторы могут использовать команды DJ.");
        }
        else
        {
            List<Role> list = FinderUtil.findRoles(event.getArgs(), event.getGuild());
            if(list.isEmpty())
                event.reply(event.getClient().getWarning()+" No Roles found matching \""+event.getArgs()+"\"");
            else if (list.size()>1)
                event.reply(event.getClient().getWarning()+FormatUtil.listOfRoles(list, event.getArgs()));
            else
            {
                s.setDJRole(list.get(0));
                event.reply(event.getClient().getSuccess()+" Команды DJ теперь могут использоваться пользователями с ролью **"+list.get(0).getName()+"**.");
            }
        }
    }
    
}
