package com.games.chessGame.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class chessMoves {
	private chessBlock startBlock;
	private chessBlock destBlock;
	private chessPiece playedPiece;
	private chessPiece diedPiece;
	
	
}
