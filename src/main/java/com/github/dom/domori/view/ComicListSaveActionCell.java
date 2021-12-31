package com.github.dom.domori.view;

import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.Tooltip;

public class ComicListSaveActionCell extends TableCell<ComicListItem, ComicListItem> {

    private Button actionButton = null;

    @Override
    protected void updateItem(ComicListItem item, boolean empty) {
        super.updateItem(item, empty);

        if (actionButton != null) {
            actionButton.disableProperty().unbind();
        }

        if (item == null || empty) {
            setGraphic(null);
            return;
        }

        if (actionButton == null) {
            actionButton = new Button("保存");
            actionButton.setTooltip(new Tooltip("PDFファイルをダウンロードします。"));
            actionButton.setOnAction(e -> saveAction());
        }

        actionButton.disableProperty().bind(item.canDownloadProperty().not());

        setGraphic(actionButton);
    }

    private void saveAction() {
        var table = getTableView();
        table.getSelectionModel().getSelectedItems().stream()
                .filter(i -> !i.isDownloaded())
                .filter(i -> i.canDownload())
                .forEach(i -> i.download());
    }
}
