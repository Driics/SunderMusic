
package com.pigeon.sundermusic;

import com.pigeon.sundermusic.entities.Prompt;
import com.pigeon.sundermusic.utils.FormatUtil;
import com.pigeon.sundermusic.utils.OtherUtil;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.typesafe.config.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;

public class BotConfig
{
    private final Prompt prompt;
    private final static String CONTEXT = "Config";
    private final static String START_TOKEN = "/// START OF SUNDERMUSIC CONFIG ///";
    private final static String END_TOKEN = "/// END OF SUNDERMUSIC CONFIG ///";
    
    private Path path = null;
    private String token, prefix, altprefix, helpWord, playlistsFolder,
            successEmoji, warningEmoji, errorEmoji, loadingEmoji, searchingEmoji;
    private boolean stayInChannel, songInGame, npImages, updatealerts, useEval, dbots;
    private long owner, maxSeconds;
    private OnlineStatus status;
    private Game game;
    private Config aliases;


    private boolean valid = false;
    
    public BotConfig(Prompt prompt)
    {
        this.prompt = prompt;
    }
    
    public void load()
    {
        valid = false;
        
        // read config from file
        try 
        {
            // get the path to the config, default config.txt
            path = Paths.get(System.getProperty("config.file", System.getProperty("config", "config.txt")));
            if(path.toFile().exists())
            {
                if(System.getProperty("config.file") == null)
                    System.setProperty("config.file", System.getProperty("config", "config.txt"));
                ConfigFactory.invalidateCaches();
            }
            
            // load in the config file, plus the default values
            //Config config = ConfigFactory.parseFile(path.toFile()).withFallback(ConfigFactory.load());
            Config config = ConfigFactory.load();
            
            // set values
            token = config.getString("token");
            prefix = config.getString("prefix");
            altprefix = config.getString("altprefix");
            helpWord = config.getString("help");
            owner = config.getLong("owner");
            successEmoji = config.getString("success");
            warningEmoji = config.getString("warning");
            errorEmoji = config.getString("error");
            loadingEmoji = config.getString("loading");
            searchingEmoji = config.getString("searching");
            game = OtherUtil.parseGame(config.getString("game"));
            status = OtherUtil.parseStatus(config.getString("status"));
            stayInChannel = config.getBoolean("stayinchannel");
            songInGame = config.getBoolean("songinstatus");
            npImages = config.getBoolean("npimages");
            updatealerts = config.getBoolean("updatealerts");
            useEval = config.getBoolean("eval");
            maxSeconds = config.getLong("maxtime");
            playlistsFolder = config.getString("playlistsfolder");
            aliases = config.getConfig("aliases");
            dbots = owner == 113156185389092864L;
            
            // we may need to write a new config file
            boolean write = false;

            // validate bot token
            if(token==null || token.isEmpty() || token.equalsIgnoreCase("BOT_TOKEN_HERE"))
            {
                token = prompt.prompt("Пожалуйста, укажите токен бота."
                        + "\nТокен бота: ");
                if(token==null)
                {
                    prompt.alert(Prompt.Level.WARNING, CONTEXT, "No token provided! Exiting.\n\nConfig Location: " + path.toAbsolutePath().toString());
                    return;
                }
                else
                {
                    write = true;
                }
            }
            
            // validate bot owner
            if(owner<=0)
            {
                try
                {
                    owner = Long.parseLong(prompt.prompt("Идентификатор владельца отсутствует, или предоставленный идентификатор владельца недействителен."
                        + "\nПожалуйста, предоставьте идентификатор владельца бота."
                        + "\nID создателя: "));
                }
                catch(NumberFormatException | NullPointerException ex)
                {
                    owner = 0;
                }
                if(owner<=0)
                {
                    prompt.alert(Prompt.Level.ERROR, CONTEXT, "Неверный идентификатор пользователя! Выход...\n\nРасположение файла конфигурации: " + path.toAbsolutePath().toString());
                    System.exit(0);
                }
                else
                {
                    write = true;
                }
            }
            
            if(write)
            {
                String original = OtherUtil.loadResource(this, "/reference.conf");
                byte[] bytes;
                if(original==null)
                {
                    bytes = ("token = "+token+"\r\nowner = "+owner).getBytes();
                }
                else
                {
                    bytes = original.substring(original.indexOf(START_TOKEN)+START_TOKEN.length(), original.indexOf(END_TOKEN))
                        .replace("BOT_TOKEN_HERE", token)
                        .replace("0 // OWNER ID", Long.toString(owner))
                        .trim().getBytes();
                }
                try 
                {
                    Files.write(path, bytes);
                }
                catch(IOException ex) 
                {
                    prompt.alert(Prompt.Level.WARNING, CONTEXT, "Не удалось записать новые параметры конфигурации в config.txt: "+ex
                        + "\nПожалуйста, убедитесь, что файлы не находятся на вашем рабочем столе или в какой-либо другой ограниченной области.\n\nРасположение файла конфигурации: "
                        + path.toAbsolutePath().toString());
                }
            }
            
            // if we get through the whole config, it's good to go
            valid = true;
        }
        catch (ConfigException ex)
        {
            prompt.alert(Prompt.Level.ERROR, CONTEXT, ex + ": " + ex.getMessage() + "\n\nРасположение файла конфигурации: " + path.toAbsolutePath().toString());
        }
    }
    
    public boolean isValid()
    {
        return valid;
    }
    
    public String getConfigLocation()
    {
        return path.toFile().getAbsolutePath();
    }
    
    public String getPrefix()
    {
        return prefix;
    }
    
    public String getAltPrefix()
    {
        return "NONE".equalsIgnoreCase(altprefix) ? null : altprefix;
    }
    
    public String getToken()
    {
        return token;
    }
    
    public long getOwnerId()
    {
        return owner;
    }
    
    public String getSuccess()
    {
        return successEmoji;
    }
    
    public String getWarning()
    {
        return warningEmoji;
    }
    
    public String getError()
    {
        return errorEmoji;
    }
    
    public String getLoading()
    {
        return loadingEmoji;
    }
    
    public String getSearching()
    {
        return searchingEmoji;
    }
    
    public Game getGame()
    {
        return game;
    }
    
    public OnlineStatus getStatus()
    {
        return status;
    }
    
    public String getHelp()
    {
        return helpWord;
    }
    
    public boolean getStay()
    {
        return stayInChannel;
    }
    
    public boolean getSongInStatus()
    {
        return songInGame;
    }
    
    public String getPlaylistsFolder()
    {
        return playlistsFolder;
    }
    
    public boolean getDBots()
    {
        return dbots;
    }
    
    public boolean useUpdateAlerts()
    {
        return updatealerts;
    }
    
    public boolean useEval()
    {
        return useEval;
    }
    
    public boolean useNPImages()
    {
        return npImages;
    }
    
    public long getMaxSeconds()
    {
        return maxSeconds;
    }
    
    public String getMaxTime()
    {
        return FormatUtil.formatTime(maxSeconds * 1000);
    }
    
    public boolean isTooLong(AudioTrack track)
    {
        if(maxSeconds<=0)
            return false;
        return Math.round(track.getDuration()/1000.0) > maxSeconds;
    }

    public String[] getAliases(String command)
    {
        try
        {
            return aliases.getStringList(command).toArray(new String[0]);
        }
        catch(NullPointerException | ConfigException.Missing e)
        {
            return new String[0];
        }
    }
}
