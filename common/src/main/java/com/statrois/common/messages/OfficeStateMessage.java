package com.statrois.common.messages;

import com.statrois.common.bean.Source;
import com.statrois.common.bean.Type;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OfficeStateMessage extends Message{
    public OfficeStateMessage() {
        this.source = Source.OFFICE;
        this.type = Type.STATE;

    }
}
