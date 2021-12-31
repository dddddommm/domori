package com.github.dom.domori.view;

import com.github.dom.domori.domain.ComicTag;
import javafx.scene.control.ListCell;

public class ComicTagListCell extends ListCell<ComicTag> {

    @Override
    protected void updateItem(ComicTag item, boolean empty) {
        super.updateItem(item, empty);

        if (item == null || empty) {
            setText(null);
            return;
        }

        setText(item.getTitle());
    }
}
