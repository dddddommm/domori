package com.github.dom.domori.view;

import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Objects;

public class ComicThumbnailCell extends TableCell<ComicListItem, ComicListItem> {

    private AnchorPane layout = null;
    private ImageView thumbnailView = null;
    private ImageView downloadedIcon = null;

    private ReadOnlyObjectProperty<LocalDateTime> downloadedProperty = null;

    @Override
    protected void updateItem(ComicListItem item, boolean empty) {
        super.updateItem(item, empty);

        // 前のアイテムに付けたリスナを削除
        if (downloadedProperty != null) {
            downloadedProperty.removeListener(this::updateDownloaded);
        }

        // 空の時はグラフィックを消して終了
        if (item == null || empty) {
            if (thumbnailView != null) {
                thumbnailView.setImage(null);
            }
            setGraphic(null);
            downloadedProperty = null;
            return;
        }

        // ダウンロード用のプロパティをリッスン
        downloadedProperty = item.downloadedDateTimeProperty();
        downloadedProperty.addListener(this::updateDownloaded);
        boolean isDownloaded = item.downloadedDateTimeProperty().get() != null;

        // 適宜ビューを初期化
        if (layout == null) {
            layout = new AnchorPane();
            layout.setPrefHeight(150);
            layout.setPrefWidth(150);
            thumbnailView = new ImageView();
            thumbnailView.fitWidthProperty().bind(layout.widthProperty());
            thumbnailView.fitHeightProperty().bind(layout.heightProperty());
            thumbnailView.setPreserveRatio(true);
            thumbnailView.setSmooth(true);
            layout.getChildren().add(thumbnailView);
            AnchorPane.setLeftAnchor(thumbnailView, 0.0);
            AnchorPane.setTopAnchor(thumbnailView, 0.0);
            AnchorPane.setRightAnchor(thumbnailView, 0.0);
            AnchorPane.setBottomAnchor(thumbnailView, 0.0);

            try (InputStream is = Objects.requireNonNull(getClass().getResourceAsStream("/com/github/dom/domori/view/downloaded.png"))) {
                var image = new Image(is);
                downloadedIcon = new ImageView(image);
                layout.getChildren().add(downloadedIcon);
                AnchorPane.setRightAnchor(downloadedIcon, 4.0);
                AnchorPane.setBottomAnchor(downloadedIcon, 4.0);
            } catch (IOException e) {
                LoggerFactory.getLogger(getClass()).error("ダウンロードアイコン読み込み失敗", e);
            }
        }

        // ダウンロード済なら表示
        downloadedIcon.setVisible(isDownloaded);

        thumbnailView.setImage(item.getImage());
        setGraphic(layout);
    }

    // リスナ用
    private void updateDownloaded(Observable ob, LocalDateTime older, LocalDateTime newer) {
        if (layout == null || newer == null)
            return;

        downloadedIcon.setVisible(true);
    }
}
