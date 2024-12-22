package com.games.chessGame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.games.chessGame.GUI.ChessGameGUI;

import javafx.application.Application;

@SpringBootApplication
public class ChessGameApplication {

	public static void main(String[] args) {
		// new chessBoard();
		// StartController.getInputs();
		  ApplicationContext context = SpringApplication.run(ChessGameApplication.class, args);

	        // Launch JavaFX Application
	        Application.launch(JavaFXSpringBootApp.class, args);
		// Launch JavaFX application in a separate thread to avoid blocking the Spring

	}
    public static class JavaFXSpringBootApp extends Application {

        @Override
        public void start(javafx.stage.Stage primaryStage) {
            // Get Spring ApplicationContext from the main Spring Boot application
            ApplicationContext context = SpringApplication.run(ChessGameApplication.class);
            
            // Create the ChessGameGUI with Spring context
            ChessGameGUI gui = context.getBean(ChessGameGUI.class);
            
            // Start the JavaFX UI
            gui.start(primaryStage);
        }
    }

// @Override
// public void run(String... args) throws Exception {
// Call the method from the controller after the application starts
// System.out.println("Application Started! Calling method from controller...");
// StartController.getInputs();; // Method call from controller
// }

}
