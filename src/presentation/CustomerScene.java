package presentation;

import domain.Address;
import domain.Customer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import persistence.CustomerDataAccess;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

public class CustomerScene {
	static Customer currentCustomer = null;
	
	public static Scene customerScene(Stage primaryStage) {
		
		TextField nameTxt = new TextField();
		VBox nameBox = new VBox(new Label("Name"), nameTxt);
		
		TextField phoneTxt = new TextField();
		VBox phoneBox = new VBox(new Label("Phone"), phoneTxt);
		
		TextField emailTxt = new TextField();
		emailTxt.setMinWidth(325);
		VBox emailBox = new VBox(new Label("Email"), emailTxt);
		
		HBox phoneEmailBox = new HBox(phoneBox, emailBox);
		phoneEmailBox.setSpacing(5);
		
		VBox customerBin = new VBox(nameBox, phoneEmailBox);
		
		customerBin.setPadding(new Insets(5));
		
		TextField streetTxt = new TextField();
		TextField stateTxt = new TextField();
		
		VBox streetStateBox = new VBox(new VBox(new Label("Street"), streetTxt),new VBox(new Label("State"), stateTxt));
		
		streetStateBox.setSpacing(5);
		
		TextField cityTextField = new TextField();
		TextField zipTextField = new TextField();
		
		VBox cityZIPBox = new VBox(new VBox(new Label("City"), cityTextField), new VBox(new Label("ZIP Code"), zipTextField));
		
		cityZIPBox.setSpacing(5);
		
		HBox addressInputBox = new HBox(streetStateBox, cityZIPBox);
		
		addressInputBox.setAlignment(Pos.CENTER);
		addressInputBox.setSpacing(80);
		
		VBox addressBox = new VBox(new Label("Address"), addressInputBox);
		
		addressBox.setSpacing(5);
		addressBox.setPadding(new Insets(5));
		
		//
		
		Button search = new Button("Search");
		Button add = new Button("Add");
		Button update = new Button("Update");
		Button delete = new Button("Delete");
		Button back = new Button("back");
		
		
		search.setOnAction(event -> {
			if(!nameTxt.getText().isBlank()) {
				if(CustomerDataAccess.searchCustomer(nameTxt.getText()) != null){
					currentCustomer = CustomerDataAccess.searchCustomer(nameTxt.getText());
				}
				
				if(currentCustomer != null) {
					Address address = currentCustomer.getAddress();
					updateCustomerFields(currentCustomer, address, nameTxt, phoneTxt, emailTxt, streetTxt, cityTextField, stateTxt, zipTextField);
					
					
				} else {
					showCreatedAlert("Customer Not Found", "Please type the correct name to find the customer");
				}
			}
		});
		
		add.setOnAction(event -> {
			final String[] fields = {nameTxt.getText(), phoneTxt.getText(), emailTxt.getText(),
					streetTxt.getText(), cityTextField.getText(), stateTxt.getText(), zipTextField.getText()};
			
			if(!isEmpty(fields)) {
				final boolean added = CustomerDataAccess.addCustomer(fields[0], fields[1], fields[2], new Address(fields[3], fields[4], fields[5], Integer.parseInt(fields[6])));
				if(added) {
					clearCustomerFields(nameTxt, phoneTxt, emailTxt, streetTxt, cityTextField, stateTxt, zipTextField);
					showCreatedAlert("Success", "Customer " + fields[0] + " was added!");
					update.setDisable(true);
					
					currentCustomer = null;
				} else {
					showCreatedAlert("Error", "There was an error attempting to add the customer");
				}
			} else {
				showCreatedAlert("Empty Fields", "Enter all customer information");
			}
		});
		
		update.setOnAction(event -> {
			final String[] fields = {nameTxt.getText(), phoneTxt.getText(), emailTxt.getText(),
					streetTxt.getText(), cityTextField.getText(), stateTxt.getText(), zipTextField.getText()};
			
			if(!isEmpty(fields)) {
				final boolean added = CustomerDataAccess.updateCustomer(currentCustomer.getId(), fields[0], fields[1], fields[2], new Address(fields[3], fields[4], fields[5], Integer.parseInt(fields[6])));
				if(added) {
					showCreatedAlert("Success", "Customer " + fields[0] + " was updated!");
				} else {
					showCreatedAlert("Error", "There was an error attempting to update the customer");
				}
			} else {
				showCreatedAlert("Empty Fields", "Enter all customer information");
			}
		});
		
		delete.setOnAction(event -> {
			if(currentCustomer != null) {
				String customerName = currentCustomer.getName();

				final boolean success = CustomerDataAccess.deleteCustomer(currentCustomer.getId());
				if(success) {
					showCreatedAlert("Customer Deleted", "Customer " + customerName + " successfully deleted");
					clearCustomerFields(nameTxt, phoneTxt, emailTxt, streetTxt, cityTextField, stateTxt, zipTextField);
					
					
					currentCustomer = null;
				} else {
					showCreatedAlert("Error", "There was a problem attempting to delete the customer");
				}
			} else {
				showCreatedAlert("Customer Not Found", "Enter a customer to delete");
			}
		});
		
		back.setOnAction(event -> {
			Scene scene = MenuScene.mainMenuScene(primaryStage);
			primaryStage.setScene(scene);
		});
		
		HBox buttons = new HBox(search, add, update, delete);
		
		buttons.setPadding(new Insets(10));
		buttons.setSpacing(10);
		buttons.setAlignment(Pos.CENTER_RIGHT);
		
		HBox backButtonBox = new HBox(back);
		backButtonBox.setPadding(new Insets(10));
		backButtonBox.setAlignment(Pos.CENTER_LEFT);
		
		HBox allButtonBox = new HBox(backButtonBox, buttons);
		allButtonBox.setSpacing(100);
		
		//
		
		BorderPane root = new BorderPane();
		
		root.setTop(customerBin);
		root.setCenter(addressBox);
		root.setBottom(allButtonBox);
		
		Scene scene = new Scene(root, 500, 300);
		primaryStage.setTitle("Customer");
		
		return scene;
	}
	
	private static void updateCustomerFields(Customer customer, Address address, 
			TextField name, TextField phone, TextField email, TextField street, TextField city, TextField state, TextField zipCode) {
		name.setText(customer.getName());
		phone.setText(customer.getPhone());
		email.setText(customer.getEmail());
		street.setText(address.getStreet());
		city.setText(address.getCity());
		state.setText(address.getState());
		zipCode.setText(String.valueOf(address.getZipCode()));
	}
	
	private static void clearCustomerFields(TextField name, TextField phone, TextField email, TextField street, TextField city, TextField state, TextField zipCode) {
		name.clear();
		phone.clear();
		email.clear();
		street.clear();
		city.clear();
		state.clear();
		zipCode.clear();
	}
	
	private static void showCreatedAlert(String header, String context) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(header);
		alert.setContentText(context);
		alert.showAndWait();
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
