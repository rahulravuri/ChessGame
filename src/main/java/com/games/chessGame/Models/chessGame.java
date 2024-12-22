package com.games.chessGame.Models;

import java.util.ArrayList;
import java.util.List;

import com.games.chessGame.Models.chessBoard;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class chessGame {
	private int GameId;
	private chessBoard board;
	private gameStatus status;
	private List<chessMoves> Moves;
	private chessPlayer player1;
	private chessPlayer player2;
	private chessPlayer currentplayer;
	private chessPlayer nextplayer;
	public chessGame(chessBoard board,int GameId,chessPlayer player1,chessPlayer player2) {
		this.board=board;this.status=gameStatus.InProgress;
		Moves=new ArrayList<>();this.GameId=GameId;this.player2=player2;
		this.player1=player1;this.currentplayer=this.player1;
		this.nextplayer=player2;
	}

}
