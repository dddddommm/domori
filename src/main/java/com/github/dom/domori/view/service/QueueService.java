package com.github.dom.domori.view.service;

import javafx.concurrent.Service;

public abstract class QueueService<V> extends Service<V> {

    private int counter = 0;

    @Override
    protected void succeeded() {
        super.succeeded();

        if (counter < 1) {
            return;
        }

        counter--;
        restart();
    }

    @Override
    protected void failed() {
        super.failed();
        counter = 0;
    }

    public void queue() {
        if (isRunning()) {
            counter++;
        } else {
            restart();
        }
    }

}
