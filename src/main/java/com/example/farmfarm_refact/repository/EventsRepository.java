package com.example.farmfarm_refact.repository;

import com.example.farmfarm_refact.entity.oauth.EventsEntity;
import jdk.jfr.Event;
import org.springframework.data.repository.CrudRepository;

public interface EventsRepository extends CrudRepository<EventsEntity, Long> {

    public EventsEntity findByEvId(Long evId);
}
