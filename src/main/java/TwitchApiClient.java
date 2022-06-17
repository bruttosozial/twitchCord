import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.helix.domain.Stream;
import com.github.twitch4j.helix.domain.StreamList;

import java.util.*;
/**
 * Functions for TwitchClient Object, for use with the Twitch API
 * @author bruttosozial
 * */
public class TwitchApiClient {

    /**
     * Constructor for The Api Client. Builds a twitchClient Api Object for use in functions.
     * @param instanceClientId Client Id for the Api Client
     * @param instanceClientSecret The Secret for the Api Client
     * */
    public TwitchApiClient(String instanceClientId, String instanceClientSecret) {
        this.twitchClient = TwitchClientBuilder.builder()
                .withEnableHelix(true)
                .withClientId(instanceClientId)
                .withClientSecret(instanceClientSecret)
                .build();

        twitchClient.getClientHelper().enableStreamEventListener("twitch4j");
    }


    private TwitchClient twitchClient;

    private List<Stream> savedStreams = new LinkedList<>();

    /**
     * Checks for new Streams in channel list in parameter using the instance TwitchClient
     * @param channels Channels to monitor
     * */
    public List<Stream> checkForNewStream(List<String> channels) {


        List<Stream> newStreams = new LinkedList<>();

        StreamList streamsCheck = twitchClient.getHelix().getStreams(null, null, null, 1, null, null, null, channels).execute();

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
}
