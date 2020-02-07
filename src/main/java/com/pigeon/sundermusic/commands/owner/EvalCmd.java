
package com.pigeon.sundermusic.commands.owner;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.pigeon.sundermusic.Bot;
import com.pigeon.sundermusic.commands.OwnerCommand;

public class EvalCmd extends OwnerCommand 
{
    private final Bot bot;
    
    public EvalCmd(Bot bot)
    {
        this.bot = bot;
        this.name = "eval";
        this.help = "выполнить код";
        this.aliases = bot.getConfig().getAliases(this.name);
        this.guildOnly = false;
    }
    
    @Override
    protected void execute(CommandEvent event) 
    {
        ScriptEngine se = new ScriptEngineManager().getEngineByName("Nashorn");
        se.put("bot", bot);
        se.put("event", event);
        se.put("jda", event.getJDA());
        se.put("guild", event.getGuild());
        se.put("channel", event.getChannel());
        try
        {
            event.reply(event.getClient().getSuccess()+" Выполнено:\n```\n"+se.eval(event.getArgs())+" ```");
        } 
        catch(Exception e)
        {
            event.reply(event.getClient().getError()+" Ошибка:\n```\n"+e+" ```");
        }
    }
    
}
