package com.smartyr.discordbot.listeners;

import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.object.entity.Guild;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ReadyListener implements  EventListener<ReadyEvent>{
    @Override
    public Class<ReadyEvent> getEventType() {
        return ReadyEvent.class;
    }

    @Override
    public Mono<Void> execute(ReadyEvent Event) {
        LOG.info("Bot started...");

        return Event
                .getClient()
                .getGuilds()
                .cache()
                .flatMap(this::LogGuilds)
                .then();
    }

    private Mono<Void> LogGuilds(Guild guild){
        return guild.getMembers()
                .count()
                .flatMap(x -> {
                    LOG.info("Connected to: {} - With {} members", guild.getName(), x);
                    return Mono.empty();
                })
                .then();
    }

}