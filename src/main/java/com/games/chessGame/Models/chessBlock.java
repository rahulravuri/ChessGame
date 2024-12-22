package com.games.chessGame.Models;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class chessBlock {
	int row;
	int col;
	blockStatus status;
	chessPiece piece;
}
