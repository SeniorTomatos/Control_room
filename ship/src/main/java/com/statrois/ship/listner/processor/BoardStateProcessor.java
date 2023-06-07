package com.statrois.ship.listner.processor;

import com.statrois.common.messages.BoardStateMessage;
import com.statrois.common.processor.MessageProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("BOARD_STATE")
public class BoardStateProcessor implements MessageProcessor<BoardStateMessage> {
    @Override
    public void process(String jsonMessage) {
        //IGNORE
    }
}
