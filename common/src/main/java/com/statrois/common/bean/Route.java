package com.statrois.common.bean;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
@Data
public class Route {
    private String boardName;
    private List<RoutePath> path = new ArrayList<>();

    public boolean notAssigned() {
        return boardName == null;
    }
}
