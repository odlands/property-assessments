package ca.macewan.c305;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DataTab {

    private PropertyAssessments propertyAssessments;
    private ObservableList<PieChart.Data> pieData;
    private XYChart.Series<String, Number> scatterData;
    private XYChart.Series<String, Number> barData;
    private BorderPane borderPane;
    private VBox pieGraphBox;
    private VBox scatterBox;
    private VBox barBox;


    /**
     * Creates a new BorderPane that contains the map and a side menu of options
     *
     * @param propertyAssessments
     * @return borderPane that contains all the content for the map tab
     */
    public BorderPane start(PropertyAssessments propertyAssessments){
        this.propertyAssessments = propertyAssessments;
        borderPane = new BorderPane();
        borderPane.setPadding(new Insets(10));

        pieData = getPieData();
        VBox searchBox = createSearch();
        pieGraphBox = createGraph(pieData);

        borderPane.setLeft(searchBox);
        borderPane.setCenter(pieGraphBox);

        return borderPane;
    }

    public void update(PropertyAssessments propertyAssessments) {
        this.propertyAssessments = propertyAssessments;
    }

    private VBox createSearch() {
        VBox vbox = new VBox();
        final Label graphLabel =  new Label("Visualization Style");
        graphLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        vbox.setPadding(new Insets(10,10,10,10));
        vbox.setSpacing(10);
        vbox.setBorder(new Border(new BorderStroke(Color.SILVER,
                BorderStrokeStyle.SOLID, new CornerRadii(4), BorderWidths.DEFAULT)));

        Button scatterButton = new Button();
        Button pieChartButton = new Button();
        Button barButton = new Button();
        pieChartButton.setText("Pie Chart");
        scatterButton.setText("Scatter Plot");
        barButton.setText("Bar Graph");
        pieChartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                pieData = getPieData();
                pieGraphBox = createGraph(pieData);
                borderPane.setCenter(pieGraphBox);
            }
        });
        scatterButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                scatterData =  getScatterData();
                scatterBox = createScatter();
                borderPane.setCenter(scatterBox);
            }
        });
        barButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                barData = getBarData();
                barBox = createBarBox();
                borderPane.setCenter(barBox);
            }
        });
        vbox.getChildren().addAll(graphLabel, pieChartButton, scatterButton, barButton);
        return vbox;
    }

    private VBox createBarBox() {
        VBox vbox = new VBox();
        final Label graphLabel = new Label("Data Visualization");
        graphLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        vbox.setPadding(new Insets(10,10,10,10));
        vbox.setBorder(new Border(new BorderStroke(Color.SILVER,
                BorderStrokeStyle.SOLID, new CornerRadii(4), BorderWidths.DEFAULT)));

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Wards");
        // xAxis.setAutoRanging(false);
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Average Assessed Value");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.getData().add(barData);
        barChart.setTitle("Wards by Property Assessment Value");
        vbox.getChildren().add(barChart);
        vbox.setAlignment(Pos.CENTER);

        return vbox;
    }

    private VBox createScatter() {
        VBox vbox = new VBox();
        final Label graphLabel = new Label("Data Visualization");
        graphLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        vbox.setPadding(new Insets(10,10,10,10));
        vbox.setBorder(new Border(new BorderStroke(Color.SILVER,
                BorderStrokeStyle.SOLID, new CornerRadii(4), BorderWidths.DEFAULT)));

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Neighbourhoods");
        // xAxis.setAutoRanging(false);
        xAxis.setTickLabelsVisible(false);
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Average Assessed Value");

        ScatterChart<String, Number> chart = new ScatterChart<String, Number>(xAxis, yAxis);
        chart.getData().add(scatterData);
        chart.setTitle("Neighbourhoods by Property Assessment Value");
        vbox.getChildren().add(chart);
        vbox.setAlignment(Pos.CENTER);

        return vbox;
    }

    private VBox createGraph(ObservableList<PieChart.Data> pieData) {
        VBox vbox = new VBox();
        final Label graphLabel = new Label("Data Visualization");
        graphLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        vbox.setPadding(new Insets(10,10,10,10));
        vbox.setBorder(new Border(new BorderStroke(Color.SILVER,
                BorderStrokeStyle.SOLID, new CornerRadii(4), BorderWidths.DEFAULT)));

        final PieChart chart = new PieChart(pieData);
        chart.setTitle("Property Assessments by Range of Value");
        chart.setLabelsVisible(false);
        Node ns = chart.lookup(".chart-legend");
        ns.setStyle("-fx-background-color: #505078, blue;" +
                "-fx-background-insets: 0,1;" +
                "-fx-background-radius: 6,5;" +
                "-fx-padding: 6px;");
        vbox.getChildren().add(chart);
        vbox.setAlignment(Pos.CENTER);

        return vbox;
    }

    public XYChart.Series<String, Number> getScatterData()
    {
        // declare set
        Set<String> neighbourhoodSet = propertyAssessments.getNeighborhoodSet();
        XYChart.Series<String, Number> neighbourhoodData = new XYChart.Series<>();
        for (String neighbourhood : neighbourhoodSet) {
            PropertyAssessments neighbourhoodAssessments = propertyAssessments.getAssessmentsByNeighbourhood(neighbourhood);
            neighbourhoodData.getData().add(new XYChart.Data<>(neighbourhood, neighbourhoodAssessments.getMean()));
        }
        return neighbourhoodData;
    }

    public XYChart.Series<String, Number> getBarData()
    {
        // declare set
        String[] wardList = propertyAssessments.getSortedWardList();
        XYChart.Series<String, Number> wardData = new XYChart.Series<>();
        for (String ward : wardList) {
            PropertyAssessments wardAssessments = propertyAssessments.getAssessmentsByWard(ward);
            wardData.getData().add(new XYChart.Data<>(ward, wardAssessments.getMean()));
        }
        return wardData;
    }

    public ObservableList<PieChart.Data> getPieData()
    {
        long max = propertyAssessments.getMax();
        int maxInt = (int)max;
        List<Integer> list = new ArrayList<>();

        list.add(200000);
        list.add(500000);
        list.add(1000000);
        list.add(50000000);
        list.add(maxInt);

        List<Integer> quantity = new ArrayList<>();
        while(quantity.size() < 5)
            quantity.add(0);

        for (PropertyAssessment address : propertyAssessments.getPropertyAssessments()) {
            if (address.getValue() < list.get(0)) {
                quantity.set(0, quantity.get(0) + 1);
            } else if (address.getValue() < list.get(1)) {
                quantity.set(1, quantity.get(1) + 1);
            } else if (address.getValue() < list.get(2)) {
                quantity.set(2, quantity.get(2) + 1);
            } else if (address.getValue() < list.get(3)) {
                quantity.set(3, quantity.get(3) + 1);
            } else if (address.getValue() < list.get(4)) {
                quantity.set(4, quantity.get(4) + 1);
            }
        }
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList();
        for (int i = 0; i < 5 ; i++ ) {
            Integer key = (list.get(i)/100000);
            Integer value = quantity.get(i);
            String string = "Less than $" + key.toString() + "00000";
            pieChartData.add(new PieChart.Data(string, value));
        }
        return pieChartData;
    }
}
