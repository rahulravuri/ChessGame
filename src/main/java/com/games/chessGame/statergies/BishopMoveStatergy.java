package com.games.chessGame.statergies;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.games.chessGame.Models.PieceType;
import com.games.chessGame.Models.blockStatus;
import com.games.chessGame.Models.chessBlock;
import com.games.chessGame.Models.chessGame;
import com.games.chessGame.Models.chessMoves;
import com.games.chessGame.Models.chessPiece;
import com.games.chessGame.Models.chessPieceStatus;
import com.games.chessGame.Models.gameStatus;
import com.games.chessGame.Services.BlockService;
import com.games.chessGame.utill.GameHelperUtil;
import com.games.chessGame.utill.PieceHelperUtil;

@Component
public class BishopMoveStatergy implements moveStatergy {

	@Autowired
	PieceHelperUtil PhelperUtil;
	@Autowired
	GameHelperUtil GhelperUtil;
	@Autowired
	BlockService blockService;

	private final int[][] moves = { { 1, 1 }, { -1, -1 }, { 1, -1 }, { -1, 1 } };

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
					game.getNextplayer().getPiecesAlive().remove(ep);
					game.getNextplayer().getPiecesDead().add(ep);
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
			return null;
		}
		chessGame game = GhelperUtil.validateGame(gameID);
		chessBlock[][] board = game.getBoard().board;

		ArrayList<int[]> data = new ArrayList<>();
		for (int[] direction : moves) {
			int newRow = piece.getChessBlock().getRow();
			int newCol = piece.getChessBlock().getCol();
			while (true) {
				newRow += direction[0];
				newCol += direction[1];
				if (!PhelperUtil.isWithinBounds(newRow, newCol))
					break;

				chessPiece p = board[newRow][newCol].getPiece();
				if (p != null) {

					if (p.getPieceColor() != piece.getPieceColor()) {
						data.add(new int[] { newRow, newCol });
					}
					break; // Blocked by another piece
				} else {
					data.add(new int[] { newRow, newCol });
				}
			}

		}

		return data;
	}
}
