package com.pigeon.sundermusic;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.pigeon.sundermusic.commands.admin.PrefixCmd;
import com.pigeon.sundermusic.commands.admin.SetdjCmd;
import com.pigeon.sundermusic.commands.admin.SettcCmd;
import com.pigeon.sundermusic.commands.admin.SetvcCmd;
import com.pigeon.sundermusic.commands.dj.*;
import com.pigeon.sundermusic.commands.general.RoleinfoCmd;
import com.pigeon.sundermusic.commands.general.ServerinfoCmd;
import com.pigeon.sundermusic.commands.general.SettingsCmd;
import com.pigeon.sundermusic.commands.music.*;
import com.pigeon.sundermusic.commands.owner.*;
import com.pigeon.sundermusic.entities.Prompt;
import com.pigeon.sundermusic.gui.GUI;
import com.pigeon.sundermusic.settings.SettingsManager;
import com.pigeon.sundermusic.utils.OtherUtil;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;

public class SunderBot
{
    public final static String PLAY_EMOJI  = "\u25B6"; // ▶
    public final static String PAUSE_EMOJI = "\u23F8"; // ⏸
    public final static String STOP_EMOJI  = "\u23F9"; // ⏹
    public final static Permission[] RECOMMENDED_PERMS = new Permission[]{Permission.MESSAGE_READ, Permission.MESSAGE_WRITE, Permission.MESSAGE_HISTORY, Permission.MESSAGE_ADD_REACTION,
                                Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_ATTACH_FILES, Permission.MESSAGE_MANAGE, Permission.MESSAGE_EXT_EMOJI,
                                Permission.MANAGE_CHANNEL, Permission.VOICE_CONNECT, Permission.VOICE_SPEAK, Permission.NICKNAME_CHANGE};
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        // startup log
        Logger log = LoggerFactory.getLogger("Startup");
        
        // create prompt to handle startup
        Prompt prompt = new Prompt("SunderMusic", "Switching to nogui mode. You can manually start in nogui mode by including the -Dnogui=true flag.",
                "true".equalsIgnoreCase(System.getProperty("nogui", "false")));
        
        // check deprecated nogui mode (new way of setting it is -Dnogui=true)
        for(String arg: args)
            if("-nogui".equalsIgnoreCase(arg))
            {
                prompt.alert(Prompt.Level.WARNING, "GUI", "The -nogui flag has been deprecated. "
                        + "Please use the -Dnogui=true flag before the name of the jar. Example: java -jar -Dnogui=true SunderMusic.jar");
                break;
            }
        
        // get and check latest version
        String version = OtherUtil.checkVersion(prompt);
        
        // load config
        BotConfig config = new BotConfig(prompt);
        config.load();
        if(!config.isValid())
            return;
        
        // set up the listener
        EventWaiter waiter = new EventWaiter();
        SettingsManager settings = new SettingsManager();
        Bot bot = new Bot(waiter, config, settings);

        
        // set up the command client
        CommandClientBuilder cb = new CommandClientBuilder()
                .setPrefix(config.getPrefix())
                .setAlternativePrefix(config.getAltPrefix())
                .setOwnerId(Long.toString(config.getOwnerId()))
                .setEmojis(config.getSuccess(), config.getWarning(), config.getError())
                .setHelpWord(config.getHelp())
                .setLinkedCacheSize(200)
                .setGuildSettingsManager(settings)
                .addCommands(new SettingsCmd(bot),
                        new ServerinfoCmd(bot),
                        new RoleinfoCmd(bot),
                        
                        new LyricsCmd(bot),
                        new QueueCmd(bot),
                        new NowplayingCmd(bot),
                        new PlayCmd(bot),
                        new PlaylistsCmd(bot),
                        new RemoveCmd(bot),
                        new SearchCmd(bot),
                        new ShuffleCmd(bot),
                        new SkipCmd(bot),

                        new ForceRemoveCmd(bot),
                        new ForceskipCmd(bot),
                        new MoveTrackCmd(bot),
                        new PauseCmd(bot),
                        new PlaynextCmd(bot),
                        new RepeatCmd(bot),
                        new SkiptoCmd(bot),
                        new StopCmd(bot),
                        new VolumeCmd(bot),
                        
                        new PrefixCmd(bot),
                        new SetdjCmd(bot),
                        new SettcCmd(bot),
                        new SetvcCmd(bot),
                        
                        new AutoplaylistCmd(bot),
                        new EvalCmd(bot),
                        new DebugCmd(bot),
                        new PlaylistCmd(bot),
                        new SetavatarCmd(bot),
                        new SetgameCmd(bot),
                        new SetnameCmd(bot),
                        new SetstatusCmd(bot),
                        new ShutdownCmd(bot)
                );
        if(config.useEval())
            cb.addCommand(new EvalCmd(bot));
        boolean nogame = false;
        if(config.getStatus()!=OnlineStatus.UNKNOWN)
            cb.setStatus(config.getStatus());
        if(config.getGame()==null)
            cb.useDefaultGame();
        else if(config.getGame().getName().equalsIgnoreCase("none"))
        {
            cb.setGame(null);
            nogame = true;
        }
        else
            cb.setGame(config.getGame());
        
        if(!prompt.isNoGUI())
        {
            try 
            {
                GUI gui = new GUI(bot);
                bot.setGUI(gui);
                gui.init();
            } 
            catch(Exception e) 
            {
                log.error("Не удалось запустить графический интерфейс. Если вы "
                        + "rработаете на сервере или в месте, где вы не можете отобразить "
                        + "окно, пожалуйста, запустите в режиме nogui, используя флаг -Dnogui=true.");
            }
        }
        
        log.info("Config loaded from "+config.getConfigLocation());
        
        // attempt to log in and start
        try
        {
            JDA jda = new JDABuilder(AccountType.BOT)
                    .setToken(config.getToken())
                    .setAudioEnabled(true)
                    .setGame(nogame ? null : Game.playing("Загрузка..."))
                    .setStatus(config.getStatus()==OnlineStatus.INVISIBLE || config.getStatus()==OnlineStatus.OFFLINE 
                            ? OnlineStatus.INVISIBLE : OnlineStatus.DO_NOT_DISTURB)
                    .addEventListener(cb.build(), waiter, new Listener(bot))
                    .setBulkDeleteSplittingEnabled(true)
                    .build();
            bot.setJDA(jda);
        }
        catch (LoginException ex)
        {
            prompt.alert(Prompt.Level.ERROR, "SunderMusic", ex + "\nПожалуйста, убедитесь, что вы "
                    + "редактировали правильный файл config.txt, и что вы использовали "
                    + "правильный токен (не «секрет»!)\nРасположение файла конфигурации: " + config.getConfigLocation());
            System.exit(1);
        }
        catch(IllegalArgumentException ex)
        {
            prompt.alert(Prompt.Level.ERROR, "SunderMusic", "Некоторый аспект конфигурации "
                    + "недействительный: " + ex + "\nРасположение файла конфигурации: " + config.getConfigLocation());
            System.exit(1);
        }
    }
}
