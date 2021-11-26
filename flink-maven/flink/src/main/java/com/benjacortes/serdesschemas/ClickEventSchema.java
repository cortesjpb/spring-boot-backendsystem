package com.benjacortes.serdesschemas;

import java.io.IOException;

import com.benjacortes.models.ClickEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;

public class ClickEventSchema implements DeserializationSchema<ClickEvent>, SerializationSchema<ClickEvent> {

    @Override
    public TypeInformation<ClickEvent> getProducedType() {
        return TypeInformation.of(ClickEvent.class);
    }

    @Override
    public byte[] serialize(ClickEvent event) {
        ObjectMapper om = new ObjectMapper();
        String eventString;
        try {
            eventString = om.writeValueAsString(event);
            return eventString.getBytes();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ClickEvent deserialize(byte[] message) throws IOException {
        return ClickEvent.fromString(new String(message));
    }

    @Override
    public boolean isEndOfStream(ClickEvent nextElement) {
        return false;
    }

}
