package com.games.chessGame.statergies;

import java.util.ArrayList;

import com.games.chessGame.Models.chessBlock;
import com.games.chessGame.Models.chessPiece;

public interface moveStatergy {

	public String makeMove(chessBlock start, chessBlock end, int gameID);

	public ArrayList<int[]> avialbleMoves(chessPiece piece, int gameID);

}
