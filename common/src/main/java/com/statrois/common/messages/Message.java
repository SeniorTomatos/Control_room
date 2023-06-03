package com.statrois.common.messages;

import com.statrois.common.bean.Source;
import com.statrois.common.bean.Type;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Message {
    protected Type type;
    protected Source source;


    public String getCode(){
        return source.name() + "_" + type.name();
    }
}
