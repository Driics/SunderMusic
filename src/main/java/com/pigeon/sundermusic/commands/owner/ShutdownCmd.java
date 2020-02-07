
package com.pigeon.sundermusic.commands.owner;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.pigeon.sundermusic.Bot;
import com.pigeon.sundermusic.commands.OwnerCommand;

public class ShutdownCmd extends OwnerCommand
{
    private final Bot bot;
    
    public ShutdownCmd(Bot bot)
    {
        this.bot = bot;
        this.name = "shutdown";
        this.help = "безопасное выключение";
        this.aliases = bot.getConfig().getAliases(this.name);
        this.guildOnly = false;
    }
    
    @Override
    protected void execute(CommandEvent event)
    {
        event.replyWarning("Выключение...");
        bot.shutdown();
    }
}
