
package com.pigeon.sundermusic.utils;
import net.dv8tion.jda.core.entities.User;
import java.util.List;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;

public class FormatUtil {

    public static String formatUser(User user)
    {
        return filter("**"+user.getName()+"**#"+user.getDiscriminator());
    }
    
    public static String formatTime(long duration)
    {
        if(duration == Long.MAX_VALUE)
            return "LIVE";
        long seconds = Math.round(duration/1000.0);
        long hours = seconds/(60*60);
        seconds %= 60*60;
        long minutes = seconds/60;
        seconds %= 60;
        return (hours>0 ? hours+":" : "") + (minutes<10 ? "0"+minutes : minutes) + ":" + (seconds<10 ? "0"+seconds : seconds);
    }
        
    public static String progressBar(double percent)
    {
        String str = "";
        for(int i=0; i<12; i++)
            if(i == (int)(percent*12))
                str+="\uD83D\uDD18"; // 🔘
            else
                str+="▬";
        return str;
    }
    
    public static String volumeIcon(int volume)
    {
        if(volume == 0)
            return "\uD83D\uDD07"; // 🔇
        if(volume < 30)
            return "\uD83D\uDD08"; // 🔈
        if(volume < 70)
            return "\uD83D\uDD09"; // 🔉

        return "\uD83D\uDD0A";     // 🔊
    }
    
    public static String listOfTChannels(List<TextChannel> list, String query)
    {
        String out = " Найдено несколько текстовых каналов \""+query+"\":";
        for(int i=0; i<6 && i<list.size(); i++)
            out+="\n - "+list.get(i).getName()+" (<#"+list.get(i).getId()+">)";
        if(list.size()>6)
            out+="\n**И ещё "+(list.size()-6)+"**";
        return out;
    }
    
    public static String listOfVChannels(List<VoiceChannel> list, String query)
    {
        String out = " Найдено несколько голосовых каналов \""+query+"\":";
        for(int i=0; i<6 && i<list.size(); i++)
            out+="\n - "+list.get(i).getName()+" (ID:"+list.get(i).getId()+")";
        if(list.size()>6)
            out+="\n**И ещё "+(list.size()-6)+"**";
        return out;
    }
    
    public static String listOfRoles(List<Role> list, String query)
    {
        String out = " Найдено несколько текстовых каналов \""+query+"\":";
        for(int i=0; i<6 && i<list.size(); i++)
            out+="\n - "+list.get(i).getName()+" (ID:"+list.get(i).getId()+")";
        if(list.size()>6)
            out+="\n**И ещё "+(list.size()-6)+"**";
        return out;
    }
    
    public static String filter(String input)
    {
        return input.replace("@everyone", "@\u0435veryone").replace("@here", "@h\u0435re").trim(); // cyrillic letter e
    }
}
