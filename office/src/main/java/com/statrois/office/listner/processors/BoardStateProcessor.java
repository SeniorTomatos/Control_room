package com.statrois.office.listner.processors;

import com.statrois.common.bean.AirPort;
import com.statrois.common.bean.Board;
import com.statrois.common.bean.Route;
import com.statrois.common.messages.AirPortStateMessage;
import com.statrois.common.messages.BoardStateMessage;
import com.statrois.common.processor.MessageConverter;
import com.statrois.common.processor.MessageProcessor;
import com.statrois.office.provider.AirPortsProvider;
import com.statrois.office.provider.BoardsProvider;
import com.statrois.office.service.WaitingRouteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component("BOARD_STATE")
@RequiredArgsConstructor
public class BoardStateProcessor implements MessageProcessor<BoardStateMessage> {

    private final MessageConverter messageConverter;
    private final WaitingRouteService waitingRouteService;
    private final BoardsProvider boardsProvider;
    private final AirPortsProvider airPortsProvider;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void process(String jsonMessage) {
        BoardStateMessage message = messageConverter.extractMessage(jsonMessage, BoardStateMessage.class);
        Board board = message.getBoard();
        Optional<Board> previousOpt = boardsProvider.getBoard(board.getName());
        AirPort airPort = airPortsProvider.getAirPort(board.getLocation());

        boardsProvider.addBoard(board);
        if (previousOpt.isPresent() && board.hasRoute() && !previousOpt.get().hasRoute()) {
            Route route = board.getRoute();
            waitingRouteService.remove(route);
        }

        if (previousOpt.isEmpty() || !board.isBusy() && previousOpt.get().isBusy()) {
            airPort.addBoard(board.getName());
            kafkaTemplate.sendDefault(messageConverter.toJson(new AirPortStateMessage(airPort)));
        }

    }
}
