package com.games.chessGame.Models;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class gameManager {
	private final HashMap<Integer, chessGame> activeGames = new HashMap<>();

    public String createGame(Integer gameId,chessGame chessGame) {
      
        activeGames.put(gameId, chessGame);
        return "added";
    }

    public chessGame getGame(Integer gameId) {
        return activeGames.get(gameId);
    }

    public void endGame(Integer gameId) {
        activeGames.remove(gameId);
    }
	

}
