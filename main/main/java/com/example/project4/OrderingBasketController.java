package com.example.project4;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import pkg.*;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;


/**
 * Controller for the ordering basket menu
 * @author Hieu Nguyem, Shan Malik
 */
public class OrderingBasketController {
    Order order;
    MainViewController mainViewController;
    FXMLLoader loader;
    Parent root;


    @FXML
    private ListView<String> orderBasketView;

    @FXML
    private TextField subTotal;

    @FXML
    private TextField salesTax;

    @FXML
    private TextField totalAmount;

    private ArrayList<String> listViewString;

    DecimalFormat numFormat;

    /**
     * initializes the menu
     *
     * @throws IOException throws error if file is not found
     */
    @FXML
    public void initialize() throws IOException {
        //DEFAULT VALUES SET THE SUBTOTAL, SALES TAX and TOTAL AMOUNT TO ZERO

        numFormat = new DecimalFormat("0.00");
        subTotal.setText("$0.00");
        salesTax.setText("$0.00");
        totalAmount.setText("$0.00");
        loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
        root = loader.load();
        mainViewController = loader.getController();

        order = mainViewController.getOrder();

        listViewString = order.getOrderItemsStrings();
        orderBasketView.setItems(FXCollections.observableArrayList(listViewString));
        listViewString = new ArrayList<String>();


        orderBasketView.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            // if (newValue.intValue() == 0 && orderBasketView.getItems().get(0).isEmpty()) {
            //       orderBasketView.getSelectionModel().select(1);
            //  }
        });

    }

    public void setMainController(MainViewController mainView) {
        mainViewController = mainView;
    }

    /**
     * places order and sends order to store orders
     */
    @FXML
    public void placeOrder() {

        if (listViewString.size() != 0) {
            order.newOrder();
            orderBasketView.getItems().clear();
            subTotal.setText("$0.00");
            salesTax.setText("$0.00");
            totalAmount.setText("$0.00");
        }

        /******************************hello how ur*/
        //CREATE A METHOD TO RESET THE BASEKT, THE PRICE, THE TAX, THE TOTAL WHEN FRESH WHEN WE PLACE THE ORDER and order
        //mainViewController.resetBasket()
    }


    /**
     * finds index of item within the order basket given a string
     *
     * @param item the string to be found in the basket
     * @return returns the index of the item in the order
     */
    private int findItem(String item) {
        int ret = 0;
        for (String ind : orderBasketView.getItems()) {
            if (ind.equals(item)) break;
            ret++;
        }
        if (ret == 1) {
            return ret - 1;
        }
        return ret;
    }


    /**
     * removes a selected item from the basket
     */
    @FXML
    public void removeSelectedItem() {
        Object selectedItem = orderBasketView.getSelectionModel().getSelectedItem();
        if (orderBasketView.getItems().size() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Item not selected!");
            alert.setContentText("Need to select an item in a basket inorder to perform removal");
            alert.show();
            return;
        }

        orderBasketView.getItems().remove(selectedItem);
        double subtotal, tax, total;

        int selectedIndex = orderBasketView.getSelectionModel().getSelectedIndex();

        if (orderBasketView.getItems().size() == 0) {
            orderBasketView.getItems().clear();
            order.clearOrder();
            subTotal.setText("$0.00");
                salesTax.setText("$0.00");
                totalAmount.setText("$0.00");
            } else if (selectedIndex == 0 && orderBasketView.getItems().size() == 1) {
                order.deleteItem(0);
                subtotal = Double.parseDouble(numFormat.format(order.getOrderPrice()));
                tax = Double.parseDouble(numFormat.format(order.getOrderPrice() * 0.06625));
                total = Double.parseDouble(numFormat.format(subtotal + tax));
                subTotal.setText(subtotal + "");
                totalAmount.setText(total + "");
                salesTax.setText(tax + "");

            } else if (orderBasketView.getSelectionModel().getSelectedItem() != null && orderBasketView.getItems().size() != 1.0) {
                String removed = orderBasketView.getSelectionModel().getSelectedItem().toString();
                int removeIndex = findItem(removed);
                order.deleteItem((removeIndex));
                subtotal = Double.parseDouble(numFormat.format(order.getOrderPrice()));
                tax = Double.parseDouble(numFormat.format(order.getOrderPrice() * 0.06625));
                total = Double.parseDouble(numFormat.format(subtotal + tax));
                subTotal.setText(subtotal + "");
                totalAmount.setText(total + "");
                salesTax.setText(tax + "");

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Item not selected!");
                alert.setContentText("Need to select an item in a basket inorder to perform removal");
                alert.show();
                return;
            }


        }



    /**
     * sets basket view and prices
     */
    public void setBasket(){
        order = mainViewController.getOrder();
        listViewString = order.getOrderItemsStrings();

        listViewString.toString();

        orderBasketView.setItems(FXCollections.observableArrayList(listViewString));

        DecimalFormat numFormat = new DecimalFormat("0.00");

        double subtotal = Double.parseDouble(numFormat.format(order.getOrderPrice()));

        subTotal.setText("$" + subtotal);

        double tax = Double.parseDouble(numFormat.format(order.getOrderPrice() * 0.06625));

        salesTax.setText("$" + tax);

        double total = Double.parseDouble(numFormat.format(subtotal + tax));

        totalAmount.setText("$" + total);


    }

}