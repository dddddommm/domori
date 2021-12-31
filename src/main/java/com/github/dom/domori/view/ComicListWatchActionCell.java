package com.github.dom.domori.view;

import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.Tooltip;

public class ComicListWatchActionCell extends TableCell<ComicListItem, ComicListItem> {

    private Button actionButton = null;

    @Override
    protected void updateItem(ComicListItem item, boolean empty) {
        super.updateItem(item, empty);

        if (item == null || empty) {
            setGraphic(null);
            return;
        }

        if (actionButton == null) {
            actionButton = new Button("閲覧");
            actionButton.setTooltip(new Tooltip("コミックを閲覧します。"));
            actionButton.setOnAction(e -> saveAction());
        }

        setGraphic(actionButton);
    }

    private void saveAction() {
//        var table = getTableView();
//        table.getSelectionModel().getSelectedItems().stream()
//                .filter(i -> !i.isDownloaded())
//                .filter(i -> i.canDownload())
//                .forEach(i -> i.download());
    }
}
