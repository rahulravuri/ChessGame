package com.games.chessGame.Models;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
public class chessPlayer {
	
	private int id;
	private String Name;
	private PieceColor PieceColor;
	private chessPieceStatus kingPieceStatus;
	private chessPiece kingpiece;
	private List<chessPiece> piecesAlive;
	private List<chessPiece> piecesDead;
	
	public chessPlayer(int id,String Name,PieceColor PieceColor,chessPieceStatus kingPieceStatus) {
		this.id=id;this.Name=Name;this.kingPieceStatus=kingPieceStatus;
		this.PieceColor=PieceColor;
		this.piecesAlive=new ArrayList<>();
		this.piecesDead=new ArrayList<>();		
	}
	
	

}
