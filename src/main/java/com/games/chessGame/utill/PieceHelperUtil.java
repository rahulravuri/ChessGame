package com.games.chessGame.utill;

import org.springframework.stereotype.Component;

import com.games.chessGame.Models.blockStatus;
import com.games.chessGame.Models.chessBlock;
import com.games.chessGame.Models.chessPiece;
import com.games.chessGame.Models.chessPieceStatus;

import javafx.scene.image.Image;

@Component
public class PieceHelperUtil {

	public boolean canMoveCheck(chessBlock[][] board, int r, int c, chessPiece piece) {
		if(!isWithinBounds(r, c)) return false;
		chessBlock des = board[r][c];
		if (des.getStatus() == blockStatus.Empty) {
			return true;
		}
		chessPiece dp = des.getPiece();
		if (dp.getPieceColor() == piece.getPieceColor()) {
			return false;
		}

		return true;
	}

	public void kill(chessPiece d) {
		d.setChessBlock(null);
		d.setChessPieceStatus(chessPieceStatus.Dead);
	}

	public void changeBlock(chessPiece d, chessBlock c) {
		d.setChessBlock(c);
	}

	public boolean isWithinBounds(int row, int col) {
		return row >= 0 && row < 8 && col >= 0 && col < 8;
	}
	
	public static Image loadImage(String resource, int width, int height) {
		return new Image(PieceHelperUtil.class.getClassLoader().getResourceAsStream(resource), width, height, true, true);
	}
	
	
	

}
