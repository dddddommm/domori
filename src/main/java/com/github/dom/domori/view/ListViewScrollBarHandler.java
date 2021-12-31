package com.github.dom.domori.view;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.input.ScrollEvent;

public class ListViewScrollBarHandler<T> {

    private final ListView<T> listView;

    private ScrollBar scrollBar = null;

    public final DoubleProperty scrollValueProperty = new SimpleDoubleProperty();

    public ListViewScrollBarHandler(ListView<T> listView) {
        this.listView = listView;

        listView.addEventHandler(ScrollEvent.SCROLL, e -> {
            if (scrollBar == null) {
                for(Node node : listView.lookupAll(".scroll-bar")){
                    if(node instanceof ScrollBar){
                        this.scrollBar = (ScrollBar)node;
                        scrollValueProperty.bindBidirectional(scrollBar.valueProperty());
                    }
                }
            }
        });
    }

}
