package com.smartyr.discordbot.listeners;


import discord4j.core.event.domain.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public interface EventListener<T extends Event> {
    Logger LOG = LoggerFactory.getLogger(EventListener.class);

    Class<T> getEventType();
    Mono<Void> execute(T Event);

    default Mono<Void> handleError(Throwable error) {
        LOG.error("unable to process " + getEventType().getSimpleName(), error);
        return Mono.empty();
    }
}