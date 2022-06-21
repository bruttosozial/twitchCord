import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.events.ChannelGoLiveEvent;
import com.github.twitch4j.helix.domain.Stream;
import com.github.twitch4j.helix.domain.StreamList;
import com.github.twitch4j.helix.domain.User;
import com.github.twitch4j.helix.domain.UserList;
import java.util.*;

/**
 * Functions for TwitchClient Object, for use with the Twitch API
 * @author bruttosozial
 * */
public class TwitchApiClient {
    final private TwitchClient twitchClient;
    private List<Stream> savedStreams = new LinkedList<>();
    final private EventManager eventManager;
    public EventManager getEventManager() {
        return eventManager;
    }

    final private UserList channelUsers;

    /**
     * Constructor for The Api Client. Builds a twitchClient Api Object for use in functions.
     * @param instanceClientId Client Id for the Api Client
     * @param instanceClientSecret The Secret for the Api Client
     * */
    public TwitchApiClient(String instanceClientId, String instanceClientSecret, String oAuthToken, String[] channels) {

        this.twitchClient = TwitchClientBuilder.builder()
                .withDefaultAuthToken(new OAuth2Credential("twitch", oAuthToken))
                .withDefaultEventHandler(SimpleEventHandler.class)
                .withEnableHelix(true)
                .withClientId(instanceClientId)
                .withClientSecret(instanceClientSecret)
                .build();
        eventManager = this.twitchClient.getEventManager();

        channelUsers = twitchClient.getHelix().getUsers(null, null, List.of(channels)).execute();

        }

    /**
     * Checks for new Streams in channel list in parameter using the instance TwitchClient
     * @param channels Channels to monitor
     * */
    public List<Stream> checkForNewStream(List<String> channels) {
        List<Stream> newStreams = new LinkedList<>();
        StreamList streamsCheck = twitchClient.getHelix().getStreams(null, null, null, 1000, null, null, null, channels).execute();
        boolean isNew = true;

        for(Stream s : streamsCheck.getStreams()) {
            for(Stream savedStream : savedStreams) {
                if(savedStream.getUserLogin().equals(s.getUserLogin())) {
                    isNew = false;
                }
            }
            if(isNew) {
               newStreams.add(s);
            }
        }
        savedStreams = streamsCheck.getStreams();
        return newStreams;
    }

    /**
     * Registers for stream events given via parameter
     *
     * @param channels channels to register to
     * @param discordClient Discord Client to send Embed with
     * */
    public void registerForStreamEvent(String[] channels, DiscordClient discordClient) {
        for(String s : channels) {
            System.out.println("SUBSCRIBED TO " + s);
            twitchClient.getClientHelper().enableStreamEventListener(s);
        }
        twitchClient.getEventManager().onEvent(ChannelGoLiveEvent.class, event -> {

            User currentStreamUser = null;
            for(User u : channelUsers.getUsers()) {
                if (u.getLogin().equals(event.getStream().getUserLogin())) {
                    currentStreamUser = u;
                }
            }

            discordClient.streamerWentLifeMessage(event.getStream(), currentStreamUser);
        });
    }
}
