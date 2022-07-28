package com.smartyr.discordbot.listeners;


import java.util.List;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class MessageCreatedListener extends MessageListener implements  EventListener<MessageCreateEvent> {
    @Override
    public Class<MessageCreateEvent> getEventType() {
        return MessageCreateEvent.class;
    }

    private List<String> commands = List.of("!winning", "!help");
    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        LOG.info("{} event raised", MessageCreateEvent.class.toString());
        Message eventMessage = event.getMessage();
//        return processCommand(eventMessage);
        return Mono.just(eventMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(this::validCommand)
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage("Things to do today:\n - write a bot\n - eat lunch\n - play a game"))
                .then();
    }

    private boolean validCommand(Message toCheck) {
       return commands.contains(toCheck.getContent().toLowerCase());
    }
}