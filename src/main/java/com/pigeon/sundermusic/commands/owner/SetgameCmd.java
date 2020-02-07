
package com.pigeon.sundermusic.commands.owner;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.pigeon.sundermusic.Bot;
import com.pigeon.sundermusic.commands.OwnerCommand;
import net.dv8tion.jda.core.entities.Game;

public class SetgameCmd extends OwnerCommand
{
    public SetgameCmd(Bot bot)
    {
        this.name = "setgame";
        this.help = "устанавливает игру, в которую играет бот";
        this.arguments = "[action] [game]";
        this.aliases = bot.getConfig().getAliases(this.name);
        this.guildOnly = false;
        this.children = new OwnerCommand[]{
            new SetlistenCmd(),
            new SetstreamCmd(),
            new SetwatchCmd()
        };
    }
    
    @Override
    protected void execute(CommandEvent event) 
    {
        String title = event.getArgs().toLowerCase().startsWith("playing") ? event.getArgs().substring(7).trim() : event.getArgs();
        try
        {
            event.getJDA().getPresence().setGame(title.isEmpty() ? null : Game.playing(title));
            event.reply(event.getClient().getSuccess()+" **"+event.getSelfUser().getName()
                    +"** "+(title.isEmpty() ? "больше не играет." : "сейчас играет `"+title+"`"));
        }
        catch(Exception e)
        {
            event.reply(event.getClient().getError()+" The game could not be set!");
        }
    }
    
    private class SetstreamCmd extends OwnerCommand
    {
        private SetstreamCmd()
        {
            this.name = "stream";
            this.aliases = new String[]{"twitch","streaming"};
            this.help = "устанавливает стрим";
            this.arguments = "<username> <game>";
            this.guildOnly = false;
        }

        @Override
        protected void execute(CommandEvent event)
        {
            String[] parts = event.getArgs().split("\\s+", 2);
            if(parts.length<2)
            {
                event.replyError("Пожалуйста, включите имя пользователя и название игры, чтобы «поток»'");
                return;
            }
            try
            {
                event.getJDA().getPresence().setGame(Game.streaming(parts[1], "https://twitch.tv/"+parts[0]));
                event.replySuccess("**"+event.getSelfUser().getName()
                        +"** сейчас стримит `"+parts[1]+"`");
            }
            catch(Exception e)
            {
                event.reply(event.getClient().getError()+" Игра не может быть установлена!");
            }
        }
    }
    
    private class SetlistenCmd extends OwnerCommand
    {
        private SetlistenCmd()
        {
            this.name = "listen";
            this.aliases = new String[]{"listening"};
            this.help = "устанавливает что слушает бот";
            this.arguments = "<title>";
            this.guildOnly = false;
        }

        @Override
        protected void execute(CommandEvent event)
        {
            if(event.getArgs().isEmpty())
            {
                event.replyError("Пожалуйста, включите название для прослушивания!");
                return;
            }
            String title = event.getArgs().toLowerCase().startsWith("to") ? event.getArgs().substring(2).trim() : event.getArgs();
            try
            {
                event.getJDA().getPresence().setGame(Game.listening(title));
                event.replySuccess("**"+event.getSelfUser().getName()+"** сейчас слушаю `"+title+"`");
            } catch(Exception e) {
                event.reply(event.getClient().getError()+" Игра не может быть установлена!");
            }
        }
    }
    
    private class SetwatchCmd extends OwnerCommand
    {
        private SetwatchCmd()
        {
            this.name = "watch";
            this.aliases = new String[]{"watching"};
            this.help = "устанавливает игру, за которой наблюдает бот";
            this.arguments = "<title>";
            this.guildOnly = false;
        }

        @Override
        protected void execute(CommandEvent event)
        {
            if(event.getArgs().isEmpty())
            {
                event.replyError("Please include a title to watch!");
                return;
            }
            String title = event.getArgs();
            try
            {
                event.getJDA().getPresence().setGame(Game.watching(title));
                event.replySuccess("**"+event.getSelfUser().getName()+"** is now watching `"+title+"`");
            } catch(Exception e) {
                event.reply(event.getClient().getError()+" The game could not be set!");
            }
        }
    }
}
