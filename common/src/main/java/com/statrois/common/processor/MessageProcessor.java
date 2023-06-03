package com.statrois.common.processor;

import com.statrois.common.messages.Message;

public interface MessageProcessor<T extends Message> {

    void process(String jsonMessage);
}
