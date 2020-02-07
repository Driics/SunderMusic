
package com.pigeon.sundermusic.commands.dj;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.pigeon.sundermusic.Bot;
import com.pigeon.sundermusic.commands.DJCommand;
import com.pigeon.sundermusic.settings.Settings;

public class RepeatCmd extends DJCommand
{
    public RepeatCmd(Bot bot)
    {
        super(bot);
        this.name = "repeat";
        this.help = "повторно добавляет музыку в очередь, когда заккончится";
        this.arguments = "[on|off]";
        this.aliases = bot.getConfig().getAliases(this.name);
        this.guildOnly = true;
    }
    
    // override musiccommand's execute because we don't actually care where this is used
    @Override
    protected void execute(CommandEvent event) 
    {
        boolean value;
        Settings settings = event.getClient().getSettingsFor(event.getGuild());
        if(event.getArgs().isEmpty())
        {
            value = !settings.getRepeatMode();
        }
        else if(event.getArgs().equalsIgnoreCase("true") || event.getArgs().equalsIgnoreCase("on") || event.getArgs().equalsIgnoreCase("да"))
        {
            value = true;
        }
        else if(event.getArgs().equalsIgnoreCase("false") || event.getArgs().equalsIgnoreCase("off") || event.getArgs().equalsIgnoreCase("нет"))
        {
            value = false;
        }
        else
        {
            event.replyError("Допустимые значения: `on` или` off` (или оставьте пустым для переключения)");
            return;
        }
        settings.setRepeatMode(value);
        event.replySuccess("Режим повтора сейчас `"+(value ? "включён" : "выключен")+"`");
    }

    @Override
    public void doCommand(CommandEvent event) { /* Intentionally Empty */ }
}
