package com.games.chessGame.GUI;

import com.games.chessGame.Models.chessGame;
import com.games.chessGame.DTO.playerDTO;
import com.games.chessGame.Exceptions.InvalidPieceException;
import com.games.chessGame.Exceptions.KingNotSafeException;
import com.games.chessGame.Models.chessBlock;
import com.games.chessGame.Models.chessPiece;
import com.games.chessGame.Models.gameStatus;
import com.games.chessGame.Services.GameService;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.games.chessGame.Services.ImageCache;

import java.util.ArrayList;

@Component
public class ChessGameGUI extends Application {

    @Autowired
    private GameService gameService;
    @Autowired
    private ImageCache ImageCache;

    private chessGame game;
    private chessPiece selectedPiece;
    private GridPane boardGrid;
    private Label gameStatusLabel ;
    private Label player1Label;
    private Label player2Label;
    private Label currentPlayerLabel;
    private int GameId =1;
    

    @Override
    public void start(Stage primaryStage) {
    	GameId=gameService.StartNewdGame();
        game = gameService.getCurrentGame(GameId);
        primaryStage.setTitle("Chess Game");

        boardGrid = new GridPane();
        boardGrid.setHgap(6);
        boardGrid.setVgap(6);

        initBoard(); 
        displayBoard();
     // Add a Step Back Button
        Button stepBackButton = new Button("Step Back");
        stepBackButton.setOnAction(e -> handleStepBack());
        boardGrid.add(stepBackButton, 8, 0, 1, 1); 
        
        Button EndGameButton = new Button("EndGame");
        EndGameButton.setOnAction(e -> EndGame());
        boardGrid.add(EndGameButton, 8, 1, 1, 1);
        
        Button StartGameButton = new Button("StartNewGame");
        StartGameButton.setOnAction(e -> startNewGame());
        boardGrid.add(StartGameButton, 8, 2, 1, 1);
        VBox infoBox = new VBox(16); 
        infoBox.setAlignment(Pos.TOP_LEFT);
        player1Label = new Label("Player 1: *********");
        player2Label = new Label("Player 2: *********");
        currentPlayerLabel = new Label("Current Player: *********");
        gameStatusLabel = new Label("Game Status: Not Yet Started");
        infoBox.getChildren().addAll(player1Label, player2Label, currentPlayerLabel, gameStatusLabel);
        boardGrid.add(infoBox, 8, 3, 2, 2); 
       

        Scene scene = new Scene(boardGrid, 800, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
        startNewGame();
    }

    private Object EndGame() {
		game.setStatus(gameStatus.Ended);
    	gameStatusLabel.setText("Game Status: Ended");
		return null;
	}

	private void initBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Button button = new Button();
                button.setMinSize(80, 80);
                button.setStyle("-fx-font-size: 24px;");
                if ((row + col) % 2 == 0) {
                    button.setStyle("-fx-base: lightgray;");
                } else {
                    button.setStyle("-fx-base: darkgray;");
                }
                final int r = row;
                final int c = col;
                button.setOnAction(e -> handleSquareClick(r, c));

                boardGrid.add(button, col, row);
            }
        }
    }

    private void displayBoard() {
        chessBlock[][] board = game.getBoard().board;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Button button = (Button) boardGrid.getChildren().get(row * 8 + col);
                chessBlock block = board[row][col];

                if (block.getPiece() != null) {
                   Image image = ImageCache.getImage(block.getPiece().getPiecepath());
                    		//new Image(getClass().getResourceAsStream("/images/black_knight.png"));
                    if(image!=null) {
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(50);   // Adjust width
                    imageView.setFitHeight(50); 
                    button.setGraphic(imageView);}
                    else {
                    	button.setText(block.getPiece().getPieceType().toString().substring(0, 2));
                    }
                    
                } else {
                    button.setText("");
                    button.setGraphic(null);
                  
                }
            }
        }
    }

    private void handleSquareClick(int row, int col) {
    	if(gameStatusLabel.getText().contains("Not Yet Started")) {
    		showAlert("Game Not Yet Started", "Please Start New Game");
    		return ;
    	}
    	if(game.getStatus()==gameStatus.Ended) {
         	showAlert("Game Ended", "User Ended Game");
         	return ;
         }
    	
        chessBlock block = game.getBoard().board[row][col];

        if (selectedPiece == null) {
            if (block.getPiece() != null && block.getPiece().getPieceColor() == game.getCurrentplayer().getPieceColor()) {
                selectedPiece = block.getPiece();
                highlightAvailableMoves(selectedPiece);
            } else {
                showAlert("Invalid Selection", "Please Select Valid Piece to Make a Move");
            }
        } else {
        	if(selectedPiece==block.getPiece()) {
        		selectedPiece = null;
                resetHighlightedSquares(); 
                displayBoard();
        	}
        	
        	else  {
        		try {
                gameService.makeamove(selectedPiece.getChessBlock().getRow(), selectedPiece.getChessBlock().getCol(),
                        row, col, game.getGameId());
                selectedPiece = null;
                resetHighlightedSquares(); 
                displayBoard();
            }  
        		catch(KingNotSafeException e) {
        			showAlert("Invalid Move", "King Is Not Safe if you do this");
        		}
        		catch(Exception e){
                showAlert("Invalid Move", "This move is not allowed.");
            }
        		currentPlayerLabel.setText("Current Player: " + game.getCurrentplayer().getName());
                
        }}}
    
    private void handleStepBack() {
    	if(game.getStatus()==gameStatus.Ended) {
         	showAlert("Game Ended", "User Ended Game");
         	return ;
         }
    	selectedPiece = null;
        resetHighlightedSquares(); 
        if (gameService.canUndo(game.getGameId())) {
            gameService.returnToBack(game.getGameId());  // Implement undo logic in the service
            displayBoard();  // Update the board UI after undoing
        } else {
            showAlert("Cannot Undo", "No moves to undo.");
        }
        currentPlayerLabel.setText("Current Player: " + game.getCurrentplayer().getName());
        
    }

    private void highlightAvailableMoves(chessPiece piece)  {
    	 if(game.getStatus()==gameStatus.Ended) {
         	showAlert("Game Ended", "King Got Killed");
         	return ;
         }
        ArrayList<int[]> moves = gameService.getAllUserMoves(piece.getChessBlock().getRow(),
                piece.getChessBlock().getCol(), game.getGameId());
        if(moves.size()==0) {
        	selectedPiece=null;
        	showAlert("No Moves", "No moves");
        	
        }
        else {
        Button sp = (Button) boardGrid.getChildren().get(piece.getChessBlock().getRow() * 8 + piece.getChessBlock().getCol());
        sp.setStyle("-fx-background-color: blue;");
        for (int[] move : moves) {
            Button button = (Button) boardGrid.getChildren().get(move[0] * 8 + move[1]);
            button.setStyle("-fx-background-color: green;");
        }
        }
    }

    private void resetHighlightedSquares() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Button button = (Button) boardGrid.getChildren().get(row * 8 + col);
                if (button.getStyle().contains("green") || button.getStyle().contains("blue")) {  // If the button is highlighted
                    // Revert the color back to default
                    if ((row + col) % 2 == 0) {
                        button.setStyle("-fx-base: lightgray;");
                    } else {
                        button.setStyle("-fx-base: darkgray;");
                    }
                }
            }
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void startNewGame() {
        // Create a custom dialog
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Start New Game");
        dialog.setHeaderText("Enter Player Names");

        // Create TextFields for input
        TextField player1Field = new TextField();
        player1Field.setPromptText("Player 1 Name");
        TextField player2Field = new TextField();
        player2Field.setPromptText("Player 2 Name");

        // Add the TextFields to a VBox
        VBox vbox = new VBox(10, new Label("Player 1:"), player1Field, new Label("Player 2:"), player2Field);
        dialog.getDialogPane().setContent(vbox);

        // Add buttons to the dialog
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Handle the result of the dialog
        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String player1Name = player1Field.getText().trim();
                String player2Name = player2Field.getText().trim();

                if (player1Name.isEmpty() || player2Name.isEmpty()) {
                    showAlert("Invalid Input", "Both player names must be provided!");
                    return;
                } 
                GameId=gameService.StartNewGame(new playerDTO(player1Name), new playerDTO(player2Name));
                game = gameService.getCurrentGame(GameId);

                player1Label.setText("Player 1: " + game.getCurrentplayer().getName());
                player2Label.setText("Player 2: " + game.getNextplayer().getName());
                currentPlayerLabel.setText("Current Player: " + game.getCurrentplayer().getName());
                gameStatusLabel.setText("Game Status: Ongoing");
                selectedPiece = null;
                resetHighlightedSquares();
                displayBoard();
            }
        });
    }

}
