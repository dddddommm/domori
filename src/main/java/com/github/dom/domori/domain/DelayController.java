package com.github.dom.domori.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class DelayController {

    private final Logger LOGGER = LoggerFactory.getLogger(DelayController.class);

    private TimeUnit timeUnit;

    private int duration;

    private int interval;

    public DelayController(TimeUnit timeUnit, int duration, int interval) {
        this.timeUnit = timeUnit;
        this.duration = duration;
        this.interval = interval;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    private int counter = -1;

    public synchronized void delay() {
        counter++;
        if (counter < interval)
            return;

        try {
            timeUnit.sleep(duration);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }

        counter = -1;
    }
}
