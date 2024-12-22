package com.games.chessGame.Services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.games.chessGame.DTO.playerDTO;
import com.games.chessGame.Exceptions.InvalidPieceException;
import com.games.chessGame.Exceptions.KingNotSafeException;
import com.games.chessGame.Models.PieceColor;
import com.games.chessGame.Models.chessBlock;
import com.games.chessGame.Models.chessBoard;
import com.games.chessGame.Models.chessGame;
import com.games.chessGame.Models.chessPieceStatus;
import com.games.chessGame.Models.chessPlayer;
import com.games.chessGame.utill.GameHelperUtil;

@Component
public class GameService {

	private static int gameid = 2;

	@Autowired
	BoardService boardService;
	@Autowired
	GameHelperUtil helperUtil;
	
	public int StartNewGame(playerDTO a, playerDTO b) {
		chessBoard board = boardService.createNewBoard();
		chessPlayer o = createPlayer(a, 1);
		chessPlayer t = createPlayer(b, 2);
		chessGame newGame = new chessGame(board, gameid, o, t);
		helperUtil.createGameRecord(gameid, newGame);
		helperUtil.initializePieces(newGame, o, t);
		helperUtil.printBoard(newGame);
		gameid++;
		return gameid-1;
	}

	public ArrayList<int[]> getAllUserMoves(int r, int c, int gameID)  {
		chessGame game = helperUtil.validateGame(gameID);
		chessPlayer p = game.getCurrentplayer();
		chessBlock block = game.getBoard().board[r][c];
		if (block.getPiece() == null || block.getPiece().getPieceColor() != p.getPieceColor()) {
			//throw new InvalidPieceException("Invalid Block " + game);
			System.out.print("not working");
		}
		ArrayList<int[]> out = boardService.getAllMoves(gameID, block.getPiece());
		if (out.size() == 0) {
			System.out.println("No Moves to make ma " + game);
		}
		for (int[] i : out) {
			System.out.println(i[0] + " " + i[1]);
		}

		return out;
	}

	public void makeamove(int cr, int cc, int mr, int mc, int gameID) throws InvalidPieceException, KingNotSafeException {
		chessGame game = helperUtil.validateGame(gameID);
		chessPlayer c = game.getCurrentplayer();
		chessPlayer p=boardService.makeamove(c, gameID, cr, cc, mr, mc);
		game.setCurrentplayer(game.getNextplayer());
		game.setNextplayer(c);
		if(p==game.getNextplayer()) {
			returnToBack(game.getGameId());
			throw new KingNotSafeException("King Not Safe if you move this" + game);
		}
		helperUtil.printBoard(game);
	}

	public chessPlayer createPlayer(playerDTO player, int n) {
		if (n == 1) {
			return new chessPlayer(n, player.Name, PieceColor.White, chessPieceStatus.Alive);
		} else {
			return new chessPlayer(n, player.Name, PieceColor.Black, chessPieceStatus.Alive);
		}

	}

	public void returnToBack(int gameId) {
		chessGame game = helperUtil.validateGame(gameId);
		boardService.returnToBack(gameId);
		helperUtil.printBoard(game);
		chessPlayer c = game.getCurrentplayer();
		game.setCurrentplayer(game.getNextplayer());
		game.setNextplayer(c);
	}

	public chessGame getCurrentGame() {
		// TODO Auto-generated method stub
		return helperUtil.validateGame(1);
	}
	
	public chessGame getCurrentGame(int gameID) {
		return helperUtil.validateGame(gameID);
	}

	public boolean canUndo(int gameId) {
		chessGame game = helperUtil.validateGame(gameId);
		return game.getMoves().size()>0;
	}

	public int StartNewdGame() {
		chessBoard board = boardService.createNewDBoard();
		chessGame newGame = new chessGame(board, gameid, null, null);
		helperUtil.createGameRecord(1, newGame);
		return 1;
	}

}
