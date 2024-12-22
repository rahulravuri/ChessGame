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
public class BlackPawnMoveStatergy implements pawnMoveStatergy {

	@Autowired
	PieceHelperUtil PhelperUtil;
	@Autowired
	GameHelperUtil GhelperUtil;
	@Autowired
	BlockService blockService;

	private final int[][] straightMoves = { { 1, 0 }, { 2, 0 } };
	private final int[][] crossMoves = { { 1, -1 }, { 1, 1 } };

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
			return null;
		}
		chessGame game = GhelperUtil.validateGame(gameID);
		chessBlock[][] board = game.getBoard().board;

		ArrayList<int[]> data = new ArrayList<>();
		int r = piece.getChessBlock().getRow();
		int c = piece.getChessBlock().getCol();

		if (canMoveStraightCheck(board[r + 1][c], piece)) {
			data.add(new int[] { r + 1, c });
			if (r == 1 && canMoveStraightCheck(board[r + 2][c], piece)) {
				data.add(new int[] { r + 2, c });
			}
		}
		

		for (int[] i : crossMoves) {
			if (r + i[0] > 0 && r + i[0] < 8 && c + i[1] > 0 && c + i[1] < 8
					&& canMoveCrossCheck(board[r + i[0]][c + i[1]], piece)) {
				data.add(new int[] { r + i[0], c + i[1] });
			}
		}

		return data;
	}

	public boolean canMoveStraightCheck(chessBlock des, chessPiece piece) {
		if (des.getStatus() == blockStatus.Empty) {
			return true;
		}
		chessPiece dp = des.getPiece();
		if (dp != null) {
			return false;
		}

		return true;
	}

	public boolean canMoveCrossCheck(chessBlock des, chessPiece piece) {
		if (des.getStatus() == blockStatus.Empty) {
			return false;
		}
		chessPiece dp = des.getPiece();
		if (dp.getPieceColor() == piece.getPieceColor()) {
			return false;
		}

		return true;
	}

}
