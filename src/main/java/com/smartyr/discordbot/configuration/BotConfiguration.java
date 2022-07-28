package com.smartyr.discordbot.configuration;

import java.util.List;
import java.util.Objects;

import com.smartyr.discordbot.listeners.EventListener;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.lifecycle.ResumeEvent;
import discord4j.core.object.entity.Guild;
import discord4j.gateway.intent.Intent;
import discord4j.gateway.intent.IntentSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Configuration
public class BotConfiguration {

    private static final Logger log = LoggerFactory.getLogger( BotConfiguration.class );
    @Value("${token}")
    private String token;

    @Bean
    public <T extends Event> GatewayDiscordClient gatewayDiscordClient(List<EventListener<T>> eventListeners) {
        //appears to bug if ready event isn't first. So we set the ready event to the front.
        var events = eventListeners
                .stream()
                .sorted(
                        (e1, e2) -> ReadyEvent.class.equals(e1.getEventType()) ? -1 :
                                ReadyEvent.class.equals(e2.getEventType()) ? 1 : 0)
                .toList();

        GatewayDiscordClient client = DiscordClientBuilder.create(token)
                .build()
                .gateway()
                .setEnabledIntents(IntentSet.of(
                        Intent.GUILD_MEMBERS,
                        Intent.GUILDS,
                        Intent.GUILD_MESSAGES,
                        Intent.GUILD_PRESENCES))
                .login()
                .block();



        for(EventListener<T> listener : events) {
            client.getEventDispatcher().on(listener.getEventType())
                    .flatMap(listener::execute)
                    .onErrorResume(listener::handleError)
                    .subscribe();
        }
        return client;
    }
}