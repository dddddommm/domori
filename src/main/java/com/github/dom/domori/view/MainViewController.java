package com.github.dom.domori.view;

import com.github.dom.domori.domain.ComicTag;
import com.github.dom.domori.domain.RankingTerms;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;
import javafx.util.converter.LocalDateTimeStringConverter;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.TreeMap;

class MainViewController implements Initializable {

    @FXML
    public BorderPane root;

    @FXML
    public GridPane rankingGrid;

    @FXML
    public ComboBox<RankingTerms> tagTermComboBox;

    @FXML
    public ComboBox<RankingTerms> productTermComboBox;

    @FXML
    public ListView<ComicTag> tagRankingListView;

    @FXML
    public ListView<ComicTag> productRankingListView;

    @FXML
    public Button watchHistoryButton;

    @FXML
    public Button saveHistoryButton;

    @FXML
    public Button recentlyButton;

    @FXML
    public MenuButton rankingMenuButton;

    @FXML
    public TextField keywordTextField;

    @FXML
    public Button keywordSearchButton;

    @FXML
    public Label contentTypeLabel;

    @FXML
    public TableView<ComicListItem> comicTableView;

    @FXML
    public TableColumn<ComicListItem, Integer> numberColumn;

    @FXML
    public TableColumn<ComicListItem, String> titleColumn;

    @FXML
    public TableColumn<ComicListItem, ComicListItem> thumbnailColumn;

    @FXML
    public TableColumn<ComicListItem, LocalDateTime> saveDateColumn;

    @FXML
    public TableColumn<ComicListItem, ComicListItem> watchActionColumn;

    @FXML
    public TableColumn<ComicListItem, ComicListItem> saveActionColumn;

    @FXML
    public Label taskLabel;

    @FXML
    public Label progressLabel;

    @FXML
    public MenuItem openFolderMenuItem;

    @FXML
    public MenuItem settingsMenuItem;

    @FXML
    public MenuItem closeMenuItem;

    @FXML
    public MenuItem aboutMenuItem;

    private MainViewModel viewModel;

    public MainViewController() {
        URL url = MainViewController.class.getResource("/com/github/dom/domori/view/MainView.fxml");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(url);
        loader.setController(this);
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initItems();
        initCells();

        viewModel = new MainViewModel();
        bindViewModel();
    }

    private void initItems() {
        TreeMap<RankingTerms, String> terms = new TreeMap<>();
        terms.put(RankingTerms.ALL, "全期間");
        terms.put(RankingTerms.DAILY, "デイリー");
        terms.put(RankingTerms.WEEKLY, "ウィークリー");
        terms.put(RankingTerms.MONTHLY, "マンスリー");

        tagTermComboBox.getItems().addAll(terms.keySet());
        productTermComboBox.getItems().addAll(terms.keySet());

        StringConverter<RankingTerms> converter = new StringConverter<>() {
            @Override
            public String toString(RankingTerms object) {
                if (object == null)
                    return "";
                return terms.get(object);
            }

            @Override
            public RankingTerms fromString(String string) {
                return null;
            }
        };
        tagTermComboBox.setConverter(converter);
        productTermComboBox.setConverter(converter);

        terms.forEach((k, v) -> {
            MenuItem item = new MenuItem(v);
            item.setOnAction(e -> {
                // TODO コミックランキング
            });
        });
    }

    private void initCells() {
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        thumbnailColumn.setCellFactory(c -> new ComicThumbnailCell());
        thumbnailColumn.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue()));
        saveDateColumn.setCellValueFactory(new PropertyValueFactory<>("downloadedDate"));
        saveDateColumn.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateTimeStringConverter()));
        watchActionColumn.setCellFactory(c -> new ComicListWatchActionCell());
        watchActionColumn.setCellValueFactory(thumbnailColumn.getCellValueFactory());
        saveActionColumn.setCellFactory(c -> new ComicListSaveActionCell());
        saveActionColumn.setCellValueFactory(thumbnailColumn.getCellValueFactory());
    }

    private void bindViewModel() {
        tagTermComboBox.getSelectionModel().selectedItemProperty().addListener((ob, o, n) -> {
            viewModel.updateTagRankingTerm(n);
        });
        productTermComboBox.getSelectionModel().selectedItemProperty().addListener((ob, o, n) -> {
            viewModel.updateProductsRankingTerm(n);
        });
        tagTermComboBox.getSelectionModel().select(RankingTerms.DAILY);
        productTermComboBox.getSelectionModel().select(RankingTerms.DAILY);

        tagRankingListView.setCellFactory(c -> new ComicTagListCell());
        productRankingListView.setCellFactory(c -> new ComicTagListCell());

        ListViewScrollBarHandler<?> scrollBarHandler = new ListViewScrollBarHandler<>(productRankingListView);
        scrollBarHandler.scrollValueProperty.addListener((ob, o, n) -> {
            if (n.doubleValue() == 1.0) {
                viewModel.crawlProductRanking();
            }
        });

        tagRankingListView.setOnMouseClicked(e -> {
            if (e.getClickCount() != 2)
                return;

            var tag = tagRankingListView.getSelectionModel().getSelectedItem();
            if (tag == null)
                return;

            viewModel.openRanking(tag);
        });
        productRankingListView.setOnMouseClicked(e -> {
            if (e.getClickCount() != 2)
                return;

            var tag = productRankingListView.getSelectionModel().getSelectedItem();
            if (tag == null)
                return;

            viewModel.openRanking(tag);
        });

        Bindings.bindContentBidirectional(tagRankingListView.getItems(), viewModel.rankingTags);
        Bindings.bindContentBidirectional(productRankingListView.getItems(), viewModel.rankingProducts);
        Bindings.bindContentBidirectional(comicTableView.getItems(), viewModel.comics);
    }
}
