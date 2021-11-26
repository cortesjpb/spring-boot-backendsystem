package com.benjacortes.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ClickEvent {
    
    private Long sessionId;
    private Long pageId;
    private Integer frequency;

    public ClickEvent() {}

    public ClickEvent(Long sessionId, Long pageId, Integer frequency) {
        this.sessionId = sessionId;
        this.pageId = pageId;
        this.frequency = frequency;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Long getPageId() {
        return pageId;
    }

    public void setPageId(Long pageId) {
        this.pageId = pageId;
    }


    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public static ClickEvent fromString(String eventString) {
        ObjectMapper om = new ObjectMapper();
        try {
            ClickEvent event = om.readValue(eventString, ClickEvent.class);
            return event;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

}
