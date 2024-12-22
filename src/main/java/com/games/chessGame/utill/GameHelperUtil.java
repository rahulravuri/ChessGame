package com.games.chessGame.utill;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.games.chessGame.Models.PieceType;
import com.games.chessGame.Models.chessBoard;
import com.games.chessGame.Models.chessGame;
import com.games.chessGame.Models.chessPiece;
import com.games.chessGame.Models.chessPlayer;
import com.games.chessGame.Models.gameManager;
import com.games.chessGame.Models.gameStatus;

@Component
public class GameHelperUtil {
	@Autowired
	gameManager gameManager;

	// printing a board
	public void printBoard(chessGame game) {
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				chessPiece p = game.getBoard().board[row][col].getPiece();
				if (p == null) {
					System.out.print(".");
				} else {
					System.out.print(p.getTemprep());

				}
			}
			System.out.println(" ");
		}
	}

	// Validating game
	public chessGame validateGame(int gameId) {

		chessGame game = gameManager.getGame(gameId);
		if (game == null) {
			throw new IllegalArgumentException("Invalid game ID: " + gameId);
		}
		if (game.getStatus() == gameStatus.Ended) {
			throw new IllegalArgumentException("Game has already ended.");
		}
		return game;
	}

	// create game record
	public String createGameRecord(int gameid, chessGame newGame) {

		gameManager.createGame(gameid, newGame);
		return "Game Created";

	}

	public void assignPieceToPlayer(chessPiece piece, chessPlayer player) {
		if (piece.getPieceType() == PieceType.KING) {
			player.setKingpiece(piece);
		}
		player.getPiecesAlive().add(piece);
	}

	public void initializePieces(chessGame game, chessPlayer player1, chessPlayer player2) {
		chessBoard board = game.getBoard();

		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				chessPiece piece = board.board[row][col].getPiece();

				if (piece != null) {
					if (row > 2) {
						assignPieceToPlayer(piece, player1);
					} else {
						assignPieceToPlayer(piece, player2);
					}
				}
			}
		}
	}
}
