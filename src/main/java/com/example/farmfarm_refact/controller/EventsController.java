package com.example.farmfarm_refact.controller;

import com.example.farmfarm_refact.apiPayload.ApiResponse;
import com.example.farmfarm_refact.entity.oauth.EventsEntity;
import com.example.farmfarm_refact.service.EventsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
public class EventsController {

    @Autowired
    private EventsService eventsService;

    // 이벤트 조회
    @GetMapping("/{evId}")
    public ApiResponse<EventsEntity> getEvent(@PathVariable("evId") Long evId) {
        return ApiResponse.onSuccess(eventsService.getEvent(evId));
    }

//    // 이벤트 전체 조회
//    @GetMapping("/list")
//    public ApiResponse<> getEventList {
//        return ApiResponse.onSuccess(eventsService.getEventList);
//    }
}
