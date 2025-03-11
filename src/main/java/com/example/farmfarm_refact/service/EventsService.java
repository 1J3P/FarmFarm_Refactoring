package com.example.farmfarm_refact.service;

import com.example.farmfarm_refact.entity.oauth.EventsEntity;
import com.example.farmfarm_refact.repository.EventsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventsService {

    @Autowired
    private EventsRepository eventsRepository;

    // 이벤트 조회
    public EventsEntity getEvent(Long evId) {
        EventsEntity event = eventsRepository.findByEvId(evId);
        return event;
    }

    // 이벤트 전체 조회
    public List<EventsEntity> getEventList() {
        List<EventsEntity> events = eventsRepository.findAll();
        return events;
    }
}
