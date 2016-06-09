package ayudin.diplom;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public final class GuiChooserFx extends Application {

    private final ObservableList<Query> tableQueries = FXCollections.observableArrayList();
    private List<String> queries = new ArrayList<String>();
    private LogParser logParser = new LogParser();
    private QueriesModifier queriesModifier = new QueriesModifier();
    private XmlGenerator xmlGenerator = new XmlGenerator();

    private IntegerProperty index = new SimpleIntegerProperty();


    public final double getIndex() {
        return index.get();
    }


    public final void setIndex(Integer value) {
        index.set(value);
    }


    public IntegerProperty indexProperty() {
        return index;
    }

    public static class Query {

        private final SimpleStringProperty name;
        private final SimpleStringProperty body;

        Query(String name, String body) {
            this.name = new SimpleStringProperty(name);
            this.body = new SimpleStringProperty(body);
        }

        public String getName() {
            return name.get();
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public String getBody() {
            return body.get();
        }

        public void setBody(String body) {
            this.body.set(body);
        }
    }

    @Override
    public void start(final Stage stage) {
        stage.setTitle("Профилирование реляционных запросов");

        final FileChooser fileChooser = new FileChooser();
        final Button openButton = new Button("Открыть файл логов");
        final Button fireButton = new Button("Создать XML-файл");
        final Button settingsButton = new Button("Указать файл конфигурации");
        final Button delButton = new Button("Удалить запрос");
        delButton.setDisable(true);
        Label patternLabel = new Label("Шаблон журналирования");
        final TextField patternField = new TextField();
        patternField.setMinWidth(400);
        patternField.setText("%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n");


        openButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    public void handle(final ActionEvent e) {
                        File file = fileChooser.showOpenDialog(stage);
                        if (file != null) {
                            queries = queriesModifier.getModifiedQueries(logParser.getSqlQueries(file, patternField.getText()));
                            displayQueries();
                        }
                    }
                });

        fireButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    public void handle(final ActionEvent e) {
                        File file = fileChooser.showSaveDialog(stage);
                        addQueriesFromForm();
                        String resultingXml = xmlGenerator.getXml(queries);
                        try {
                            PrintWriter writer = new PrintWriter(file);
                            writer.println(resultingXml);
                            writer.close();
                        } catch (FileNotFoundException exception) {
                            exception.printStackTrace();
                        }
                    }
                });


        settingsButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    public void handle(final ActionEvent e) {
                        File file = fileChooser.showOpenDialog(stage);
                        if (file != null) {
                            try {
                                patternField.setText(logParser.extractPattern(file));
                            } catch (Exception exception){
                                System.out.println("Неверный формат файла конфигурации");
                                exception.printStackTrace();
                            }
                        }
                    }
                });


        final GridPane inputGridPane = new GridPane();

        GridPane.setConstraints(openButton, 0, 0);
        GridPane.setConstraints(fireButton, 1, 0);
        GridPane.setConstraints(settingsButton, 2, 0);
        GridPane.setConstraints(delButton, 3, 0);

        inputGridPane.setHgap(6);
        inputGridPane.setVgap(6);
        inputGridPane.getChildren().addAll(openButton, fireButton, settingsButton, delButton);

        final GridPane patternGridPane = new GridPane();
        GridPane.setConstraints(patternLabel, 0, 0);
        patternLabel.setMinWidth(180);
        GridPane.setConstraints(patternField, 0, 1);
        patternGridPane.getChildren().addAll(patternLabel, patternField);

        final Pane rootGroup = new VBox(12);
        rootGroup.getChildren().addAll(inputGridPane);
        rootGroup.getChildren().addAll(patternGridPane);
        rootGroup.setPadding(new Insets(12, 12, 12, 12));

        final TableView table = new TableView();

        final Label label = new Label("Запросы");
        label.setFont(new Font("Arial", 20));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn queryName = new TableColumn("Название запроса");
        TableColumn queryBody = new TableColumn("Тело запроса");
        queryName.setCellValueFactory(
                new PropertyValueFactory<Query, String>("name")
        );
        queryBody.setCellValueFactory(
                new PropertyValueFactory<Query, String>("body")
        );
        queryName.setPrefWidth(150);
        queryName.setMaxWidth(150);
        queryName.setMinWidth(100);
        queryBody.setPrefWidth(250);
        table.setItems(tableQueries);
        table.getColumns().addAll(queryName, queryBody);


        table.setEditable(true);
        queryName.setCellFactory(TextFieldTableCell.forTableColumn());
        queryName.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Query, String>>() {
                    public void handle(TableColumn.CellEditEvent<Query, String> t) {
                        ((Query) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setName(t.getNewValue());
                    }
                }
        );

        queryBody.setCellFactory(TextFieldTableCell.forTableColumn());
        queryBody.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Query, String>>() {
                    public void handle(TableColumn.CellEditEvent<Query, String> t) {
                        ((Query) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setName(t.getNewValue());
                    }
                }
        );


        table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {

            public void changed(ObservableValue observable, Object oldvalue, Object newValue) {
                delButton.setDisable(false);
                setIndex(tableQueries.indexOf(newValue));

            }
        });

        delButton.setOnAction(new EventHandler<ActionEvent>() {


            public void handle(ActionEvent e) {

                tableQueries.remove(index.get());
                table.getSelectionModel().clearSelection();
                delButton.setDisable(true);

            }
        });




        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table);


        final TextField addName = new TextField();
        addName.setPrefWidth(queryName.getPrefWidth());
        addName.setPromptText("Название");
        final TextField addBody = new TextField();
        addBody.setPrefWidth(queryBody.getPrefWidth());
        addBody.setPromptText("Тело запроса");

        final Button addButton = new Button("Добавить");
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                tableQueries.add(new Query(
                        addName.getText(),
                        addBody.getText()
                ));
                addName.clear();
                addBody.clear();
            }
        });
        final HBox hBox = new HBox();
        hBox.getChildren().addAll(addName, addBody, addButton);
        hBox.setSpacing(3);
        rootGroup.getChildren().addAll(vbox, hBox);
        Scene scene = new Scene(rootGroup);

        stage.setScene(scene);
        stage.setWidth(700);
        stage.setHeight(800);
        stage.show();
    }

    private void displayQueries(){
        tableQueries.removeAll();
        for (String query: queries){
            tableQueries.add(new Query(makeQueryName(query), query));
        }
    }

    private void addQueriesFromForm(){
        queries = new ArrayList<String>();
        for (Query tableQuery: tableQueries){
            queries.add(tableQuery.getBody());
        }
    }

    private String makeQueryName(String query){
        //до первого пробела после 15-го символа
        return query.substring(0, query.indexOf(" ", 15));
    }
    public static void main(String[] args) {
        Application.launch(args);
    }
}
