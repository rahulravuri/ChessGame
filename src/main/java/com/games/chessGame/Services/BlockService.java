package com.games.chessGame.Services;

import org.springframework.stereotype.Component;

import com.games.chessGame.Models.blockStatus;
import com.games.chessGame.Models.chessBlock;
import com.games.chessGame.Models.chessPiece;

@Component
public class BlockService {

	public chessBlock createNewBlock(int row, int col, blockStatus Empty) {

		return new chessBlock(row, col, blockStatus.Empty, null);
	}

	public chessBlock updateBlock(chessBlock chessBlock, blockStatus status, chessPiece Piece) {

		chessBlock.setStatus(status);
		chessBlock.setPiece(Piece);
		return chessBlock;
	}

}
