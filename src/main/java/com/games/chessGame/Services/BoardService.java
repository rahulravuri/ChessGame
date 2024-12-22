package com.games.chessGame.Services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.games.chessGame.Models.PieceColor;
import com.games.chessGame.Models.PieceType;
import com.games.chessGame.Models.blockStatus;
import com.games.chessGame.Models.chessBlock;
import com.games.chessGame.Models.chessBoard;
import com.games.chessGame.Models.chessGame;
import com.games.chessGame.Models.chessPiece;
import com.games.chessGame.Models.chessPieceStatus;
import com.games.chessGame.Models.chessPlayer;
import com.games.chessGame.Models.gameManager;

@Component
public class BoardService {

	@Autowired
	BlockService blockService;
	@Autowired
	gameManager gameManager;
	@Autowired
	PieceService pieceService;

	public chessBoard createNewBoard() {
		chessBlock[][] board = getnewBlankBoard();
		fillBoard(board);
		return new chessBoard(board);

	}

	private chessBlock[][] getnewBlankBoard() {
		return new chessBlock[8][8];
	}

	private void fillBoard(chessBlock[][] board) {

		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				board[row][col] = blockService.createNewBlock(row, col, blockStatus.Empty);
			}
		}
		blockService.updateBlock(board[0][0], blockStatus.Filled,
				pieceService.getNewPiece(PieceType.ROOK, PieceColor.Black, board[0][0], chessPieceStatus.Alive, 'r',"black_rook.png"));
		blockService.updateBlock(board[0][7], blockStatus.Filled,
				pieceService.getNewPiece(PieceType.ROOK, PieceColor.Black, board[0][7], chessPieceStatus.Alive, 'r',"black_rook.png"));
		blockService.updateBlock(board[0][1], blockStatus.Filled,
				pieceService.getNewPiece(PieceType.KNIGHT, PieceColor.Black, board[0][1], chessPieceStatus.Alive, 'n',"black_knight.png"));
		blockService.updateBlock(board[0][6], blockStatus.Filled,
				pieceService.getNewPiece(PieceType.KNIGHT, PieceColor.Black, board[0][6], chessPieceStatus.Alive, 'n',"black_knight.png"));
		blockService.updateBlock(board[0][2], blockStatus.Filled,
				pieceService.getNewPiece(PieceType.BISHOP, PieceColor.Black, board[0][2], chessPieceStatus.Alive, 'b',"black_bishop.png"));
		blockService.updateBlock(board[0][5], blockStatus.Filled,
				pieceService.getNewPiece(PieceType.BISHOP, PieceColor.Black, board[0][5], chessPieceStatus.Alive, 'b',"black_bishop.png"));
		blockService.updateBlock(board[0][3], blockStatus.Filled,
				pieceService.getNewPiece(PieceType.QUEEN, PieceColor.Black, board[0][3], chessPieceStatus.Alive, 'q',"black_queen.png"));
		blockService.updateBlock(board[0][4], blockStatus.Filled,
				pieceService.getNewPiece(PieceType.KING, PieceColor.Black, board[0][4], chessPieceStatus.Alive, 'k',"black_king.png"));

		for (int col = 0; col < 8; col++)
			blockService.updateBlock(board[1][col], blockStatus.Filled, pieceService.getNewPiece(PieceType.PAWN,
					PieceColor.Black, board[1][col], chessPieceStatus.Alive, 'p',"black_pawn.png"));

		blockService.updateBlock(board[7][0], blockStatus.Filled,
				pieceService.getNewPiece(PieceType.ROOK, PieceColor.White, board[7][0], chessPieceStatus.Alive, 'R',"white_rook.png"));
		blockService.updateBlock(board[7][7], blockStatus.Filled,
				pieceService.getNewPiece(PieceType.ROOK, PieceColor.White, board[7][7], chessPieceStatus.Alive, 'R',"white_rook.png"));
		blockService.updateBlock(board[7][1], blockStatus.Filled,
				pieceService.getNewPiece(PieceType.KNIGHT, PieceColor.White, board[7][1], chessPieceStatus.Alive, 'N',"white_knight.png"));
		blockService.updateBlock(board[7][6], blockStatus.Filled,
				pieceService.getNewPiece(PieceType.KNIGHT, PieceColor.White, board[7][6], chessPieceStatus.Alive, 'N',"white_knight.png"));
		blockService.updateBlock(board[7][2], blockStatus.Filled,
				pieceService.getNewPiece(PieceType.BISHOP, PieceColor.White, board[7][2], chessPieceStatus.Alive, 'B',"white_bishop.png"));
		blockService.updateBlock(board[7][5], blockStatus.Filled,
				pieceService.getNewPiece(PieceType.BISHOP, PieceColor.White, board[7][5], chessPieceStatus.Alive, 'B',"white_bishop.png"));
		blockService.updateBlock(board[7][3], blockStatus.Filled,
				pieceService.getNewPiece(PieceType.QUEEN, PieceColor.White, board[7][3], chessPieceStatus.Alive, 'Q',"white_queen.png"));
		blockService.updateBlock(board[7][4], blockStatus.Filled,
				pieceService.getNewPiece(PieceType.KING, PieceColor.White, board[7][4], chessPieceStatus.Alive, 'K',"white_king.png"));

		for (int col = 0; col < 8; col++)
			blockService.updateBlock(board[6][col], blockStatus.Filled, pieceService.getNewPiece(PieceType.PAWN,
					PieceColor.White, board[6][col], chessPieceStatus.Alive, 'P',"white_pawn.png"));

	}

	public chessPlayer makeamove(chessPlayer currentplayer, int gameID, int cr, int cc, int mr, int mc) {
		chessGame game = gameManager.getGame(gameID);
		if (game == null) {
			throw new IllegalArgumentException("Invalid game ID: " + game);
		}

		chessBlock[][] board = game.getBoard().board;
		chessPiece t = board[cr][cc].getPiece();
		chessPiece d = board[mr][mc].getPiece();
		return pieceService.makePieceMoves(gameID, board[cr][cc], board[mr][mc], t);
	}

	public ArrayList<int[]> getAllMoves(int gameID, chessPiece piece) {
		// TODO Auto-generated method stub
		return pieceService.getAllMoves(gameID, piece);
	}

	public void returnToBack(int gameId) {
		pieceService.returnToBack(gameId);

	}

	public chessBoard createNewDBoard() {
		chessBlock[][] board = getnewBlankBoard();
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				board[row][col] = blockService.createNewBlock(row, col, blockStatus.Empty);
			}
		}
		return new chessBoard(board);
	}

}
