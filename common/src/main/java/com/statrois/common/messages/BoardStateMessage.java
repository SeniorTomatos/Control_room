package com.statrois.common.messages;

import com.statrois.common.bean.AirPort;
import com.statrois.common.bean.Board;
import com.statrois.common.bean.Source;
import com.statrois.common.bean.Type;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardStateMessage extends Message{

    private Board board;

    public BoardStateMessage () {
        this.source = Source.BOARD;
        this.type = Type.STATE;
    }

    public BoardStateMessage(Board val) {
        this();
        this.board = val;
    }

}
