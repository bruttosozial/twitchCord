import java.io.FileInputStream;
import java.util.Properties;

public class Application {
    public static void main(String[] args) {
        // LOAD CONFIG
        Properties props = new Properties();
        try{
            FileInputStream fi = new FileInputStream("bot.config");
            props.load(fi);
        }catch(Exception e) {
            e.printStackTrace();
        }
        final String discordApiToken = props.getProperty("discord.apitoken");
        final String alertChannel = props.getProperty("discord.channelid");
        final String twitchClientId = props.getProperty("twitch.clientId");
        final String twitchSecret = props.getProperty("twitch.clientSecret");
        final String oAuthToken = props.getProperty("twitch.oauthtoken");

        String[] channels = props.getProperty("bot.channelsToMonitor").split(",");

        TwitchApiClient twitchClient = new TwitchApiClient(twitchClientId, twitchSecret, oAuthToken, channels);
        DiscordClient discordClientInstance = new DiscordClient(discordApiToken, alertChannel);
        twitchClient.registerForStreamEvent(channels, discordClientInstance);

    }
}
