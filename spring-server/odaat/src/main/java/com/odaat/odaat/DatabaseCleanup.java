package com.odaat.odaat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseCleanup implements ApplicationListener<ContextClosedEvent> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        jdbcTemplate.update("DELETE FROM category");
        jdbcTemplate.update("DELETE FROM project");
        jdbcTemplate.update("DELETE FROM task");
        jdbcTemplate.update("DELETE FROM uzer");
    }
}
