package com.games.chessGame.statergies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.games.chessGame.Models.PieceColor;
import com.games.chessGame.Models.PieceType;
import com.games.chessGame.Models.chessPiece;

@Component
public class MoveStrategyFactory {

	@Autowired
	KingMoveStatergy blackKingMoveStatergy;
	@Autowired
	BishopMoveStatergy blackBishopMoveStatergy;
	@Autowired
	KnightMoveStatergy blackKnightMoveStatergy;
	@Autowired
	BlackPawnMoveStatergy blackPawnMoveStatergy;
	@Autowired
	QueenMoveStatergy blackQueenMoveStatergy;
	@Autowired
	RookMoveStatergy blackRookMoveStatergy;
	@Autowired
	WhitePawnMoveStatergy whitePawnMoveStatergy;

	public moveStatergy getmoveStatergy(chessPiece piece) {
		PieceType PieceType = piece.getPieceType();
		PieceColor PieceColor = piece.getPieceColor();

		switch (PieceType) {
		case KING:
			return blackKingMoveStatergy;

		case QUEEN:
			return blackQueenMoveStatergy;

		case ROOK:
			return blackRookMoveStatergy;

		case BISHOP:
			return blackBishopMoveStatergy;

		case KNIGHT:

			return blackKnightMoveStatergy;
		case PAWN:
			switch (PieceColor) {
			case Black:
				return blackPawnMoveStatergy;
			case White:
				return whitePawnMoveStatergy;

			}

		}

		throw new IllegalArgumentException("Invalid game piece: ");

	}

}
