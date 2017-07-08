package application;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

import java.rmi.RemoteException;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class Battleship_client_fx extends Application implements playerInterface {

	ServerInterface Server;

	String Pname = null;
	static Button[] Buttons = null;	
	Label topLabel = new Label("Battle Ship");
	int shipplaced =0;
	GridPane gp;
	static Stage ps;
	
	public void DisableShipPlacement(Button[] cells){
		for(int i=0;i<100;i++){
			cells[i].setDisable(true);
		}
		
	}
	
	void PlaceFleet(Stage primaryStage) {
		
		GridPane board = new GridPane();
		GridPane board1 = new GridPane();
		Button Hbtn = new Button();
		Button Vbtn = new Button();
		Button[] cells = new Button[100];

		EventHandler<ActionEvent> myHandler = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Button x = (Button) event.getSource();
				try {
					if(shipplaced == Server.getTotalShips()){
						DisableShipPlacement(cells);
					}
					else{
					String ss = x.getId();
					String ff = Pname;
					if (Server.canBePlaced(ss,((Vbtn.getId()=="true")?"1":"0"),ff)) {
						Server.Place(x.getId(),((Vbtn.getId()=="true")?"1":"0"),ff);
						shipplaced++;
						x.setDisable(true);
						x.setText("X");		

					}}
				} catch (Exception e) {
					e.printStackTrace();
				}

				
			}

		};
		
		

		Button ebtn = new Button();
        ebtn.setText("Exit");
        
        ebtn.setOnMouseClicked( new EventHandler< MouseEvent >() {

            @Override
            public void handle( MouseEvent event ) {

                try {
                     primaryStage.close();
                } catch (Exception ex) {
                    System.out.println("Exception while closing");
                }
            }
        } );
        
        
        board.add(ebtn, 1, 4);
        
		int btnBox = 40;

		for (int i = 0; i < cells.length; i++) {
			cells[i] = new Button("" + i);
			cells[i].setMinSize(btnBox, btnBox);
			cells[i].setMaxSize(btnBox, btnBox);
			cells[i].setOnAction(myHandler);
			cells[i].setId("" + i);
		}

		for (int i = 1, cell = 0; i <= 10; i++) {
			for (int j = 1; j <= 10; j++) {
				board1.add(cells[cell++], j, i);
			}
		}

		topLabel.setFont(new Font("Arial", 30));
		topLabel.setText("Click to place the ships");
		
		Hbtn.setText("Horizontal");
		Hbtn.setId("Horizontal");
		Vbtn.setText("Vertical");
		Vbtn.setId("Vertical");
		Vbtn.setStyle("-fx-background-color: #EBE4E8");
		Hbtn.setStyle("-fx-background-color: #00ff00");
		Vbtn.setId("true");

		Button Pbtn = new Button();
		Pbtn.setText("Play");

		Hbtn.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				try {
					Hbtn.setStyle("-fx-background-color: #00ff00");
					Hbtn.setId("true");
					Vbtn.setStyle("-fx-background-color: #EBE4E8");
					Vbtn.setId("false");
				} catch (Exception ex) {

				}
			}
		});
		Vbtn.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				try {
					Vbtn.setStyle("-fx-background-color: #00ff00");
					Vbtn.setId("true");
					Hbtn.setStyle("-fx-background-color: #EBE4E8");
					Hbtn.setId("false");
				} catch (Exception ex) {

				}
			}
		});

		Pbtn.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				try {
					Server.setPlay();
					grid(primaryStage);
					
				} catch (Exception ex) {

				}
			}
		});

		board.add(topLabel, 0, 0);
		board.add(board1, 0, 1);
		board.add(Vbtn, 0, 2);
		board.add(Hbtn, 0, 3);
		board.add(Pbtn, 0, 4);

		Scene scene = new Scene(board);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	@Override
	public void start(Stage primaryStage) {
		ps = primaryStage;
		TextField name_field = new TextField();
		// prompt text does not appear until user clicks in the gui.
		name_field.setPromptText("Enter your name here");
		name_field.setPrefColumnCount(24);
		FlowPane name_pane = new FlowPane(new Label("\tName:\t"), name_field);

		// Button join_btn = new Button();
		// join_btn.setText("Join");

		Button exit_btn = new Button();
		exit_btn.setText("next");

		// // join_btn.setOnAction(new EventHandler<ActionEvent>() {
		// @Override
		// public void handle(ActionEvent event) {
		// System.out.println("In Join method");
		// }
		// });

		exit_btn.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				try {
					Pname = name_field.getText();
					PlaceFleet(primaryStage);
					connect();
					// grid( primaryStage);
				} catch (Exception ex) {

				}
			}
		});

		Label title = new Label("\t\tBATTLE SHIP");
		title.setFont(new Font("Arial", 25));
		title.prefWidth(100);

		Label alabel = new Label();
		alabel.setPrefWidth(100);

		Label blabel = new Label();
		blabel.setPrefWidth(100);

		HBox hbox = new HBox(alabel, blabel, exit_btn);

		VBox vbox = new VBox(title, new Label(), name_pane, new Label(), hbox, new Label());

		Scene scene = new Scene(vbox);

		primaryStage.setTitle("Battle-Ship");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	void grid(Stage primaryStage) {
		GridPane board = new GridPane();
		EventHandler<ActionEvent> myHandler = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Button x = (Button) event.getSource();
				try {
						if(Server.isHit1(x.getId())){
							x.setDisable(true);
							x.setText("$$");
							Server.PlayWithRMI1();
						}
						else {
							x.setDisable(true);
							x.setText("X");
							Server.PlayWithRMI1();
						}

					
				} catch (Exception e) {
					e.printStackTrace();
				}

				
			}

		};
		
		EventHandler<KeyEvent> BHandler = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				board.setDisable(true);
			}

		};
		
		

		Button[] cells = new Button[100];
		
		int btnBox = 40;
		for (int i = 0; i < cells.length; i++) {
			cells[i] = new Button("O");
			cells[i].setMinSize(btnBox, btnBox);
			cells[i].setMaxSize(btnBox, btnBox);
			cells[i].setOnAction(myHandler);
			cells[i].setId(""+i);
		}
		//Buttons = cells;
		 
		GridPane board1 = new GridPane();

		for (int i = 1, cell = 0; i <= 10; i++) {
			for (int j = 1; j <= 10; j++) {
				board1.add(cells[cell++], i, j);
			}
		}

		Button ebtn = new Button();
        ebtn.setText("Exit");
        
        ebtn.setOnMouseClicked( new EventHandler< MouseEvent >() {

            @Override
            public void handle( MouseEvent event ) {

                try {
                     primaryStage.close();
                } catch (Exception ex) {
                    System.out.println("Exception while closing");
                }
            }
        } );
        
        board.add(ebtn, 0, 2);
		
		topLabel.setFont(new Font("Arial", 30));

		board.add(topLabel, 0, 0);
		board.add(board1, 0, 1);
		gp = board;
		Scene scene = new Scene(board);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	void connect() {
		try {
			Registry registry2 = LocateRegistry.getRegistry("127.0.0.1");
			Server = (ServerInterface) registry2.lookup("Server");

			Battleship_client_fx obj1 = new Battleship_client_fx();
			// PlayerClient obj2 = new PlayerClient();
			playerInterface stub1 = (playerInterface) UnicastRemoteObject.exportObject(obj1, 0);
			// playerInterface stub2 = (playerInterface)
			// UnicastRemoteObject.exportObject(obj2, 0);
			Registry registry = LocateRegistry.getRegistry();
			registry.bind("Player" + Pname, stub1);
			// registry.bind("PlayerB", stub2);

			Server.setClient("127.0.0.1", Pname);
			// Server.setClient("127.0.0.1");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public String getData() throws RemoteException {
		return null;
	}

	@Override
	public void displayText(String s) throws RemoteException {
		topLabel.setText(s);
	}

	public void displayText1(String s) throws RemoteException {
		topLabel.setText(s);
	}

	@Override
	public void Disable() throws RemoteException {
		//gp.setDisable(true);
		
		KeyEvent ke = new KeyEvent(KeyEvent.KEY_RELEASED, 
			    KeyCode.A.toString(), KeyCode.A.toString(), 
			    KeyCode.A, false, false, false, false);
		ps.fireEvent(ke);
	}

	@Override
	public void Enable() throws RemoteException {
		//gp.setDisable(false);
		//ps.fireEvent();
	}
}
