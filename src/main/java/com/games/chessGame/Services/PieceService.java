package com.games.chessGame.Services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.games.chessGame.Models.PieceColor;
import com.games.chessGame.Models.PieceType;
import com.games.chessGame.Models.blockStatus;
import com.games.chessGame.Models.chessBlock;
import com.games.chessGame.Models.chessGame;
import com.games.chessGame.Models.chessMoves;
import com.games.chessGame.Models.chessPiece;
import com.games.chessGame.Models.chessPieceStatus;
import com.games.chessGame.Models.chessPlayer;
import com.games.chessGame.statergies.MoveStrategyFactory;
import com.games.chessGame.statergies.KingMoveStatergy;
import com.games.chessGame.statergies.moveStatergy;
import com.games.chessGame.utill.GameHelperUtil;
import com.games.chessGame.utill.PieceHelperUtil;

@Service
public class PieceService {

	@Autowired
	MoveStrategyFactory MoveStrategyFactory;
	@Autowired
	KingMoveStatergy kingMoveStatergy;
	@Autowired
	GameHelperUtil GhelperUtil;
	@Autowired
	PieceHelperUtil PhelperUtil;
	@Autowired
	BlockService blockService;

	public chessPiece getNewPiece(PieceType PieceType, PieceColor PieceColor, chessBlock chessBlock,
			chessPieceStatus chessPieceStatus, char c,String piecepath) {
		return new chessPiece(PieceType, PieceColor, chessBlock, chessPieceStatus, c,piecepath);
	}

	public ArrayList<int[]> getAllMoves(int gameID, chessPiece piece) {
		moveStatergy statgery = MoveStrategyFactory.getmoveStatergy(piece);

		return statgery.avialbleMoves(piece, gameID);
	}

	public chessPlayer makePieceMoves(int gameID, chessBlock start, chessBlock end, chessPiece piece) {
		if (piece == null) {
			throw new IllegalArgumentException("Invalid Block chosed: " + gameID);
		}
		moveStatergy statgery = MoveStrategyFactory.getmoveStatergy(piece);

		statgery.makeMove(start, end, gameID);

		return  kingMoveStatergy.kingSafeCheck(gameID);
	}

	public void returnToBack(int gameId) {
		chessGame game = GhelperUtil.validateGame(gameId);
		int s = game.getMoves().size() - 1;
		chessMoves l = game.getMoves().get(s);
		PhelperUtil.changeBlock(l.getPlayedPiece(), l.getStartBlock());
		if (l.getDiedPiece() != null) {
			PhelperUtil.changeBlock(l.getDiedPiece(), l.getDestBlock());
			l.getDiedPiece().setChessPieceStatus(chessPieceStatus.Alive);
		}
		blockService.updateBlock(l.getStartBlock(), blockStatus.Filled, l.getPlayedPiece());
		blockService.updateBlock(l.getDestBlock(), l.getDiedPiece() == null ? blockStatus.Empty : blockStatus.Filled,
				l.getDiedPiece());
		game.getMoves().remove(s);

	}

}
