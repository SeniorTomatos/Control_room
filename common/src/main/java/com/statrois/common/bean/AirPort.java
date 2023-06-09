package com.statrois.common.bean;

import lombok.Data;


import java.util.ArrayList;
import java.util.List;

@Data
public class AirPort {
    private String name;
    private List<String> boards = new ArrayList<>();
    private int x;
    private int y;

    public void addBoard(String boardName) {
        int ind = boards.indexOf(boardName);
        if (ind >= 0) {
            boards.set(ind, boardName);
        } else boards.add(boardName);
    }

    public void removeBoard(String boardName) {
        int ind = boards.indexOf(boardName);
        boards.remove(boardName);
    }
}
