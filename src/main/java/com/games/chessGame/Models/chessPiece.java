package com.games.chessGame.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class chessPiece {
	
	private PieceType PieceType;
	private PieceColor PieceColor;
	private chessBlock chessBlock;
	private chessPieceStatus chessPieceStatus;
	private char temprep;
	private String piecepath;
	

}
