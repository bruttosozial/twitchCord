import com.github.twitch4j.helix.domain.Stream;
import com.github.twitch4j.helix.domain.User;
import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import reactor.core.publisher.Mono;
import java.time.Instant;
/**
 * A Discord Client for discord4j, builds a GatewayDiscordClient
 * @author bruttosozial
 * */
public class DiscordClient {
    /**
     * The client to send messages with
     * */
    final private GatewayDiscordClient client;
    /**
     * The alert channel to send messages into.
     * */
    final private Snowflake alertChannel;
    public DiscordClient(String apiToken, String alertChannelId) {
        this.client = DiscordClientBuilder.create(apiToken)
                .build()
                .login()
                .block();

        this.alertChannel = Snowflake.of(alertChannelId);
    }

    /**
     * Sends a Message to the Channel specified in the parameter
     * @param message The Message to send
     */
    public void sendMessage(String message) {
        Mono<MessageChannel>  messageChannel = client.getChannelById(this.alertChannel).ofType(MessageChannel.class);
        messageChannel.flatMap(channel -> channel.createMessage(message)).subscribe();
    }

    /**
     * Template for sending an embed message when a streamer goes life
     *
     * @param stream the Stream that went life
     * @param user the user of the stream
     * */
    public void streamerWentLifeMessage(Stream stream, User user) {
        StringBuilder title = new StringBuilder();
        title.append(stream.getUserLogin());
        title.append(" went live");
        if(!stream.getGameName().isEmpty()) {title.append(" with"); title.append(stream.getGameName());} title.append("!");

        StringBuilder streamUrl = new StringBuilder();
        streamUrl.append("https://twitch.tv/");
        streamUrl.append(stream.getUserLogin());

        EmbedCreateSpec embedSpec = EmbedCreateSpec.builder()
                .color(Color.DEEP_SEA)
                .title(title.toString())
                .url(streamUrl.toString())
                .author("TwitchAlert", "https://github.com/bruttosozial", "https://i.imgur.com/F9BhEoz.png")
                .description(stream.getTitle())
                .image(user.getProfileImageUrl())
                .timestamp(Instant.now())
                .build();

        Mono<MessageChannel>  messageChannel = client.getChannelById(this.alertChannel).ofType(MessageChannel.class);
        messageChannel.flatMap(channel -> channel.createMessage(embedSpec)).subscribe();
    }
}
