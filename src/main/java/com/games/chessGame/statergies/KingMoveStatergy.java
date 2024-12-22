package com.games.chessGame.statergies;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.games.chessGame.Models.PieceColor;
import com.games.chessGame.Models.PieceType;
import com.games.chessGame.Models.blockStatus;
import com.games.chessGame.Models.chessBlock;
import com.games.chessGame.Models.chessGame;
import com.games.chessGame.Models.chessMoves;
import com.games.chessGame.Models.chessPiece;
import com.games.chessGame.Models.chessPieceStatus;
import com.games.chessGame.Models.chessPlayer;
import com.games.chessGame.Models.gameStatus;
import com.games.chessGame.Services.BlockService;
import com.games.chessGame.utill.GameHelperUtil;
import com.games.chessGame.utill.PieceHelperUtil;

@Component
public class KingMoveStatergy implements moveStatergy {

	@Autowired
	PieceHelperUtil PhelperUtil;
	@Autowired
	GameHelperUtil GhelperUtil;
	@Autowired
	BlockService blockService;

	private final int[][] moves = { { 1, 0 }, { 0, -1 }, { 0, 1 }, { -1, 0 }, { 1, 1 }, { -1, -1 }, { 1, -1 },
			{ -1, 1 } };

	@Override
	public String makeMove(chessBlock start, chessBlock end, int gameID) {
		chessGame game = GhelperUtil.validateGame(gameID);
		chessPiece piece = start.getPiece();
		chessMoves chessMoves = new chessMoves(start, end, piece, null);
		if (piece == null || piece.getPieceColor() != game.getCurrentplayer().getPieceColor()) {
			throw new IllegalArgumentException("Invalid Move: " + gameID);
		}
		ArrayList<int[]> temp = avialbleMoves(piece, gameID);
		chessPiece ep = end.getPiece();
		for (int[] i : temp) {
			if (end.getCol() == i[1] && end.getRow() == i[0]) {
				if (ep != null) {
					chessMoves.setDiedPiece(ep);
					if (ep.getPieceType() == PieceType.KING) {
						game.getNextplayer().setKingPieceStatus(chessPieceStatus.Dead);
						game.setStatus(gameStatus.Ended);
					}
					PhelperUtil.kill(ep);

				}
				blockService.updateBlock(start, blockStatus.Empty, null);
				blockService.updateBlock(end, blockStatus.Filled, piece);
				PhelperUtil.changeBlock(piece, end);
				game.getMoves().add(chessMoves);
				return "Success";
			}
		}

		throw new IllegalArgumentException("Invalid move: " + gameID);
	}

	@Override
	public ArrayList<int[]> avialbleMoves(chessPiece piece, int gameID) {
		if (piece.getChessPieceStatus() == chessPieceStatus.Dead) {
			throw new IllegalArgumentException("Invalid game as King Already dead: " + gameID);
		}
		chessGame game = GhelperUtil.validateGame(gameID);
		chessBlock[][] board = game.getBoard().board;
		ArrayList<int[]> data = new ArrayList<>();
		int r = piece.getChessBlock().getRow();
		int c = piece.getChessBlock().getCol();
		for (int[] i : moves) {
			if (PhelperUtil.canMoveCheck(board, r + i[0], c + i[1], piece)) {

				data.add(new int[] { r + i[0], c + i[1] });
			}
		}
		return data;
	}

	final int[][] KNIGHT_MOVES = { { 2, 1 }, { 2, -1 }, { -2, 1 }, { -2, -1 }, { 1, 2 }, { 1, -2 }, { -1, 2 },
			{ -1, -2 } };

	final int[][] ROOK_DIRECTIONS = { { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 } };
	final int[][] BISHOP_DIRECTIONS = { { 1, 1 }, { -1, -1 }, { 1, -1 }, { -1, 1 } };
	final int[][] QUEEN_DIRECTIONS = { { 1, 1 }, { -1, -1 }, { 1, -1 }, { -1, 1 }, { 1, 0 }, { 0, 1 }, { -1, 0 },
			{ 0, -1 } };

	public chessPlayer kingSafeCheck(int gameID) {
		chessGame game = GhelperUtil.validateGame(gameID);
		chessBlock[][] board = game.getBoard().board;
		System.out.println(game.getCurrentplayer());
		System.out.println(game.getNextplayer());
		if (!isKingSafe(game.getCurrentplayer().getKingpiece(), board))
			return game.getCurrentplayer();
		if (!isKingSafe(game.getNextplayer().getKingpiece(), board))
			return game.getNextplayer();
		
		return null;

	}

	public boolean isKingSafe(chessPiece king, chessBlock[][] board) {

		int kingRow = king.getChessBlock().getRow();
		int kingCol = king.getChessBlock().getCol();
		PieceColor kingColor = king.getPieceColor();

		// Pawn Check
		int pawnDirection = (kingColor == PieceColor.Black) ? 1 : -1;
		if (isPawnThreatening(kingRow, kingCol, pawnDirection, board, kingColor)) {
			return false;
		}

		// Knight Check
		if (isKnightThreatening(kingRow, kingCol, board, kingColor)) {
			return false;
		}

		// Rook Check
		if (isSlidingPieceThreatening(kingRow, kingCol, ROOK_DIRECTIONS, board, kingColor, PieceType.ROOK)) {
			return false;
		}

		// Bishop Check
		if (isSlidingPieceThreatening(kingRow, kingCol, BISHOP_DIRECTIONS, board, kingColor, PieceType.BISHOP)) {
			return false;
		}

		// Queen Check
		// if (isSlidingPieceThreatening(kingRow, kingCol, QUEEN_DIRECTIONS, board,
		// kingColor, PieceType.QUEEN)) {
		// return false;
		// }

		return true; // King is safe
	}

	// Helper methods
	private boolean isPawnThreatening(int row, int col, int direction, chessBlock[][] board, PieceColor kingColor) {
		int[][] pawnAttacks = { { direction, 1 }, { direction, -1 } };
		for (int[] move : pawnAttacks) {
			int newRow = row + move[0];
			int newCol = col + move[1];
			if (PhelperUtil.isWithinBounds(newRow, newCol) && isEnemyPawn(newRow, newCol, board, kingColor)) {
				return true;
			}
		}
		return false;
	}

	private boolean isEnemyPawn(int row, int col, chessBlock[][] board, PieceColor kingColor) {
		chessPiece piece = board[row][col].getPiece();
		return piece != null && piece.getPieceColor() != kingColor && piece.getPieceType() == PieceType.PAWN;
	}

	private boolean isKnightThreatening(int row, int col, chessBlock[][] board, PieceColor kingColor) {
		for (int[] move : KNIGHT_MOVES) {
			int newRow = row + move[0];
			int newCol = col + move[1];
			if (PhelperUtil.isWithinBounds(newRow, newCol) && isEnemyKnight(newRow, newCol, board, kingColor)) {
				return true;
			}
		}
		return false;
	}

	private boolean isEnemyKnight(int row, int col, chessBlock[][] board, PieceColor kingColor) {
		chessPiece piece = board[row][col].getPiece();
		return piece != null && piece.getPieceColor() != kingColor && piece.getPieceType() == PieceType.KNIGHT;
	}

	private boolean isSlidingPieceThreatening(int row, int col, int[][] directions, chessBlock[][] board,
			PieceColor kingColor, PieceType pieceType) {
		for (int[] direction : directions) {
			int newRow = row;
			int newCol = col;
			while (true) {
				newRow += direction[0];
				newCol += direction[1];
				if (!PhelperUtil.isWithinBounds(newRow, newCol))
					break;
				chessPiece piece = board[newRow][newCol].getPiece();
				if (piece != null) {
					if (piece.getPieceColor() != kingColor
							&& (piece.getPieceType() == pieceType || piece.getPieceType() == PieceType.QUEEN)) {
						System.out.println("came here");
						return true; // Threat detected
					}
					break; // Blocked by another piece
				}
			}
		}
		return false;
	}

}
