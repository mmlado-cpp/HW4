package presentation;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import domain.Customer;
import domain.Order;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import persistence.CustomerDataAccess;
import persistence.OrderDataAccess;

public class OrderScene {
	static List<Customer> customerList;
	static Order currentOrder = null;
	static Customer currentCustomer = null;
	public static Scene orderScene(Stage primaryStage) {
		customerList = CustomerDataAccess.getAllCustomers();
		
		//
		
		TextField numberTextField = new TextField();
		VBox numberBox = new VBox(new Label("Number"), numberTextField);
		
		DatePicker datePicker = new DatePicker();
		VBox dateBox = new VBox(new Label("Date"), datePicker);
		dateBox.setAlignment(Pos.CENTER_RIGHT);
		
		HBox numberDate = new HBox(numberBox, dateBox);
		numberDate.setPadding(new Insets(10));
		numberDate.setSpacing(50);
		
		ComboBox<String> customerComboBox = new ComboBox<String>();
		customerComboBox.setMinWidth(400);
		VBox customerBox = new VBox(new Label("Customer"), customerComboBox);
		customerBox.setPadding(new Insets(10));
		
		for(Customer customer : customerList) {
			customerComboBox.getItems().add(customer.getName());
		}
		
		ComboBox<String> itemComboBox = new ComboBox<String>();
		itemComboBox.minWidth(200);
		itemComboBox.getItems().addAll("Caesar Salad", "Greek Salad", "Cobb Salad");
		VBox itemBox = new VBox(new Label("Item"), itemComboBox);
		
		TextField priceTextField = new TextField();
		VBox priceBox = new VBox(new Label("Price ($)"), priceTextField);
		priceBox.setAlignment(Pos.CENTER_RIGHT);
		
		HBox itemPrice = new HBox(itemBox, priceBox);
		itemPrice.setPadding(new Insets(10));
		itemPrice.setSpacing(15);
		
		VBox fieldBox = new VBox(numberDate, customerBox, itemPrice);
		
		//
		
		Button search = new Button("Search");
		Button add = new Button("Add");
		Button update = new Button("Update");
		Button delete = new Button("Delete");
		Button back = new Button("Back");
		
		
		
		
		search.setOnAction(event -> {
			if(!numberTextField.getText().isBlank()) {
				if(OrderDataAccess.searchOrder(Integer.parseInt(numberTextField.getText())) != null){
					currentOrder = OrderDataAccess.searchOrder(Integer.parseInt(numberTextField.getText()));
				}
				
				if(currentOrder != null) {
					updateOrderFields(currentOrder, datePicker, customerComboBox, itemComboBox, priceTextField);
					
					
				} else {
					showCreatedAlert("Order Not Found", "Search for an existing order number");
				}
			}
		});
		
		add.setOnAction(event -> {
			String datePickerStr = (datePicker.getValue() == null) ? null : datePicker.getValue().toString();
			final String[] fields = {numberTextField.getText(), datePickerStr, customerComboBox.getValue(), itemComboBox.getValue(), priceTextField.getText()};
			
			if(!isEmpty(fields)) {
				LocalDate localDate = datePicker.getValue();
				Date date = Date.valueOf(localDate);
				final boolean added = OrderDataAccess.addOrder(Integer.parseInt(fields[0]), date, fields[3], Double.parseDouble(fields[4]), customerList.get(customerComboBox.getSelectionModel().getSelectedIndex()).getId());
				if(added) {
					clearOrderFields(numberTextField, datePicker, customerComboBox, itemComboBox, priceTextField);
					showCreatedAlert("Success", "Order #" + fields[0] + " was added!");
					
					
					currentOrder = null;
				} else {
					if(OrderDataAccess.searchOrder(Integer.parseInt(fields[0])) != null) {
						showCreatedAlert("Error", "order number already exists. Please try another");
					} else {
						showCreatedAlert("Error", "There was an error attempting to add the order");
					}					
				}
			} else {
				showCreatedAlert("Empty fields", "enter all information for the order");
			}
		});
		
		update.setOnAction(event -> {
			final String[] fields = {numberTextField.getText(), datePicker.getValue().toString(), customerComboBox.getValue(), itemComboBox.getValue(), priceTextField.getText()};
			
			if(!isEmpty(fields)) {
				LocalDate localDate = datePicker.getValue();
				Date date = Date.valueOf(localDate);
				final boolean added =  OrderDataAccess.updateOrder(Integer.parseInt(fields[0]), date, fields[3], Double.parseDouble(fields[4]), customerList.get(customerComboBox.getSelectionModel().getSelectedIndex()).getId());
				if(added) {
					showCreatedAlert("Success", "Order #" + fields[0] + " was updated!");
				} else {
					showCreatedAlert("Error", "There was an error attempting to update the order");
				}
			} else {
				showCreatedAlert("Empty fields", "Please enter all information for the order");
			}
		});
		
		delete.setOnAction(event -> {
			if(currentOrder != null) {
				int orderNumber = currentOrder.getNumber();
				
				final boolean success = OrderDataAccess.deleteOrder(currentOrder.getNumber());
					if(success) {
						showCreatedAlert("Order Deleted", "Order #" + orderNumber + " successfully deleted");
						clearOrderFields(numberTextField, datePicker, customerComboBox, itemComboBox, priceTextField);
						
						
						currentOrder = null;
					} else {
						showCreatedAlert("Error", "There was a problem attempting to delete the order");
					}
				
			} else {
				showCreatedAlert("Order Not Found", "Please search an order before attempting to delete");
			}
		});
		
		back.setOnAction(event ->{ 
			Scene scene = MenuScene.mainMenuScene(primaryStage);
			primaryStage.setScene(scene);
		});
		
		HBox buttons = new HBox(search, add, update, delete);
		
		buttons.setPadding(new Insets(10));
		buttons.setSpacing(10);
		buttons.setAlignment(Pos.CENTER_RIGHT);
		
		HBox customerButtonBox = new HBox(back);
		customerButtonBox.setPadding(new Insets(10));
		customerButtonBox.setAlignment(Pos.CENTER_LEFT);
		
		HBox allButtonBox = new HBox(customerButtonBox, buttons);
		allButtonBox.setSpacing(80);
		
		//
		
		BorderPane root = new BorderPane();
		
		root.setCenter(fieldBox);
		root.setBottom(allButtonBox);
		
		Scene scene = new Scene(root, 500, 300);
		primaryStage.setTitle("Order");
		
		return scene;
	}
	
	private static void updateOrderFields(Order order, DatePicker date, ComboBox<String> customerBox, ComboBox<String> itemBox, TextField price) {
		LocalDate localDate = order.getDate().toLocalDate();
		
		date.setValue(localDate);
		customerBox.setValue(order.getCustomer().getName());
		itemBox.setValue(order.getItem());
		price.setText(String.valueOf(order.getPrice()));
	}
	
	
	private static void showCreatedAlert(String header, String context) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(header);
		alert.setContentText(context);
		alert.showAndWait();
	}
	
	private static void clearOrderFields(TextField number, DatePicker date, ComboBox<String> customerBox, ComboBox<String> itemBox, TextField price) {
		number.clear();
		date.setValue(null);
		customerBox.setValue(null);
		itemBox.setValue(null);
		price.clear();
	}
	
	private static boolean isEmpty(String[] fields) {
		for(int i = 0; i < fields.length; i++) {
			if(fields[i] == null || fields[i].isBlank()) {
				return true;
			}
		}
		return false;
	}
}
