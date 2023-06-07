package com.statrois.office.job;

import com.statrois.common.bean.AirPort;
import com.statrois.common.bean.Board;
import com.statrois.common.bean.Route;
import com.statrois.common.bean.RoutePath;
import com.statrois.common.messages.AirPortStateMessage;
import com.statrois.common.messages.OfficeRouteMessage;
import com.statrois.common.processor.MessageConverter;
import com.statrois.office.provider.AirPortsProvider;
import com.statrois.office.provider.BoardsProvider;
import com.statrois.office.service.PathService;
import com.statrois.office.service.WaitingRouteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class RouteDistributeJob {

    private final PathService pathService;
    private final BoardsProvider boardsProvider;
    private final WaitingRouteService waitingRouteService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final MessageConverter messageConverter;
    private final AirPortsProvider airPortsProvider;


    @Scheduled(initialDelay = 500, fixedDelay = 2500)
    private void distribute() {
        waitingRouteService.list().stream()
                .filter(Route::notAssigned)
                .forEach(route -> {
                    String startLocation = route.getPath().get(0).getFrom().getName();

                    boardsProvider.getBoards().stream()
                            .filter(board -> startLocation.equals(board.getLocation()) && board.noBusy())
                            .findFirst()
                            .ifPresent(board -> sendBoardRoute(route, board));
                    if(route.notAssigned()){
                        boardsProvider.getBoards().stream()
                                .filter(Board::noBusy)
                                .findFirst()
                                .ifPresent(board -> {
                                    String currentLocation = board.getLocation();
                                    if(!currentLocation.equals(startLocation)) {
                                        RoutePath routePath = pathService.makePath(currentLocation, startLocation);
                                        route.getPath().add(0, routePath);
                                    }
                                    sendBoardRoute(route, board);
                                });
                    }
                });
    }

    private void sendBoardRoute(Route route, Board board) {
        route.setBoardName(board.getName());
        AirPort airPort = airPortsProvider.findAirPortAndRemovePort(board.getName());
        board.setLocation(null);
        kafkaTemplate.sendDefault(messageConverter.toJson(new OfficeRouteMessage(route)));
        kafkaTemplate.sendDefault(messageConverter.toJson(new AirPortStateMessage(airPort)));
    }
}
