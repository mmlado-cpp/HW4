package presentation;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MenuScene extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Scene mainMenu = mainMenuScene(primaryStage);
			primaryStage.setScene(mainMenu);

			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static Scene mainMenuScene(Stage primaryStage) {
		Text text = new Text("Customer Order Management");
		Button btnCustomer = new Button("Customer");
		Button btnOrder = new Button("Order");

		text.setFont(new Font(30));

		btnCustomer.setMinWidth(150);
		btnCustomer.setMinHeight(50);

		btnOrder.setMinWidth(150);
		btnOrder.setMinHeight(50);

		btnOrder.setOnAction(e ->{
			Scene scene = OrderScene.orderScene(primaryStage);
			primaryStage.setScene(scene);
		});


		btnCustomer.setOnAction(e ->{
			Scene scene = CustomerScene.customerScene(primaryStage);
			primaryStage.setScene(scene);
		});


		VBox vbox = new VBox(text, btnCustomer, btnOrder);

		vbox.setSpacing(50);

		vbox.setAlignment(Pos.CENTER);

		Scene scene = new Scene(vbox, 600,600);
		return scene;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
