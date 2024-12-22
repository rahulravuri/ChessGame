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
public class KnightMoveStatergy implements moveStatergy {

	@Autowired
	PieceHelperUtil PhelperUtil;
	@Autowired
	GameHelperUtil GhelperUtil;
	@Autowired
	BlockService blockService;

	private final int[][] moves = { { -2, -1 }, // Two up, one left
			{ -2, 1 }, // Two up, one right
			{ 2, -1 }, // Two down, one left
			{ 2, 1 }, // Two down, one right
			{ -1, -2 }, // One up, two left
			{ -1, 2 }, // One up, two right
			{ 1, -2 }, // One down, two left
			{ 1, 2 } // One down, two right
	};

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
		for (int[] i : moves) {
			if (PhelperUtil.canMoveCheck(board, r + i[0], c + i[1], piece)) {
				data.add(new int[] { r + i[0], c + i[1] });
			}
		}
		return data;
	}
}
