import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.helix.TwitchHelix;
import com.github.twitch4j.helix.TwitchHelixBuilder;
import com.github.twitch4j.helix.domain.Stream;
import com.github.twitch4j.helix.domain.StreamList;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class twitchTest {
    public static void main(String[] args) {
        String twitchClientId = "yom3flpxpiehb6dg61g0l22hd0iydr";
        String twitchSecret = System.getenv("TWITCHAPIAPP_SECRET");
        String discordApiToken = System.getenv("CALCULATER_TOKEN");

        String[] channels = {"hookedLive", "SuperKreuzburg", "zegermanguy"};

        TwitchApiClient twitchClient = new TwitchApiClient(twitchClientId, twitchSecret);







    }
}
