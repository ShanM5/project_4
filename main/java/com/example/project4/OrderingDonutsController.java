package com.example.project4;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import pkg.Donut;
import pkg.DonutOrder;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;



/**
 * menu controller for donut menu
 * @author Hieu Nguyen, Shan Malik
 */
public class OrderingDonutsController{
    DonutOrder DonutOrder = new DonutOrder();
    @FXML private ComboBox chooseDonut;
    @FXML private ComboBox numberOfDonuts;
    private String donutType = "Yeast Donuts";
    @FXML
    private ImageView donutMenuImage;
    @FXML private ListView<String> donutListView;
    @FXML private ListView<String> donutTempOrder;
    ObservableList<String> cakeFlavors;
    ObservableList<String> yeastFlavors;
    ObservableList<String> holesFlavors;
    DecimalFormat numFormat = new DecimalFormat("0.00");
    private Parent root;
    FXMLLoader loader;
    MainViewController mainViewController;
    @FXML TextField donutSubTotal;


    /**
     * initializes the menu
     * @throws IOException throws error if file is not found
     */
    public void initialize() throws IOException {
        chooseDonut.setDisable(false);
        chooseDonut.getItems().removeAll(chooseDonut.getItems());
        chooseDonut.getItems().addAll("Yeast Donuts", "Cake Donuts", "Donut Holes");
        chooseDonut.getSelectionModel().select("Yeast Donuts");
        yeastFlavors = FXCollections.observableArrayList("Jelly", "Glazed", "Chocolate Frosted", "Strawberry Frosted", "Sugar", "Lemon Filled");
        cakeFlavors = FXCollections.observableArrayList("Cinnamon Sugar", "Old Fashion", "Blueberry");
        holesFlavors = FXCollections.observableArrayList("Glazed Holes", "Jelly Holes", "Cinnamon Sugar Holes");
        donutListView.setItems(yeastFlavors);
        int maxDonuts = 20;
        ArrayList<Integer> donutNums = new ArrayList<Integer>();
        for(int i = 1; i <= maxDonuts; i++) {
            donutNums.add(i);
        }

        ObservableList<Integer> numbersForDonuts = FXCollections.observableArrayList(donutNums);
        numberOfDonuts.setItems(numbersForDonuts);
        numberOfDonuts.getSelectionModel().select(0);

        doDonutType();

        loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
        root = loader.load();
        mainViewController = loader.getController();

    }

    /**
     * checks donut type selection
     */
    public void doDonutType(){
        chooseDonut.valueProperty().addListener((observable, oldValue, newValue) -> {
            donutType = (newValue.toString());

            //*****CHANGE THE DONUT IMAGE HERE AS WELL AS LIST VIEW ITEMS*****
            if(newValue.toString() == "Donut Holes"){
                Image image = new Image(getClass().getResource("/com/example/project4/donutHole.jpg").toExternalForm());
                donutMenuImage.setImage(image);
                donutListView.setItems(holesFlavors);
            }
            if(newValue.toString() == "Yeast Donuts"){
                Image image = new Image(getClass().getResource("/com/example/project4/yeastDonut.jpg").toExternalForm());
                donutMenuImage.setImage(image);
                donutListView.setItems(yeastFlavors);
            }
            if(newValue.toString() == "Cake Donuts"){
                Image image = new Image(getClass().getResource("/com/example/project4/cakeDonut.jpg").toExternalForm());
                donutMenuImage.setImage(image);
                donutListView.setItems(cakeFlavors);
            }
        });
    }


    /**
     * adds selected donut + flavor to temp order
     */
    @FXML public void addToTempOrder(){
        if(donutListView.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Missing Input!");
            alert.setContentText("Have not selected a flavor to add.");
            alert.show();
        }else {
            String flavor = donutListView.getSelectionModel().getSelectedItem().toString();
            donutListView.getItems().remove(flavor);
            int numberOfDonut = Integer.parseInt(numberOfDonuts.getValue().toString());
            Donut addingDonut = new Donut(donutType, flavor, numberOfDonut);
            DonutOrder.addDonut(addingDonut);
            donutTempOrder.setItems(FXCollections.observableArrayList((ArrayList<String>) DonutOrder.getDonutListString()));
            String formattedNumber = numFormat.format(DonutOrder.getOrderPrice());
            donutSubTotal.setText("$" + formattedNumber + "");
        }
    }


    /**
     * removes donut from temp order
     */
    @FXML public void removeFromTempOrder() {
        if(donutTempOrder.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Missing Input!");
            alert.setContentText("Have not selected a flavor to remove.");
            alert.show();
        }else{
            String flavor = donutTempOrder.getSelectionModel().getSelectedItem().toString();
            int index = flavor.indexOf("(");
            flavor = flavor.substring(0,index);
            String removedType = DonutOrder.getDonutType(new Donut(flavor));
            DonutOrder.removeDonut(new Donut(flavor));

            donutTempOrder.setItems(FXCollections.observableArrayList((ArrayList<String>) DonutOrder.getDonutListString()));
            String formattedNumber = numFormat.format(DonutOrder.getOrderPrice());

            if(removedType.equals("Yeast Donuts")){
                yeastFlavors.add(flavor);
            }else if(removedType.equals("Cake Donuts")){
                cakeFlavors.add(flavor);
            }else{
                holesFlavors.add(flavor);
            }

            donutSubTotal.setText("$" + formattedNumber + "");
        }

    }

    /**
     * sets main view controller
     * @param mainView the main view controller to set to
     */
    public void setMainController(MainViewController mainView){
        mainViewController = mainView;
    }

    /**
     * adds the donut order to basket
     */
    @FXML public void addToBasket(){
        DonutOrder donutOrder = DonutOrder;
        mainViewController.addDonut(donutOrder);
    }


}