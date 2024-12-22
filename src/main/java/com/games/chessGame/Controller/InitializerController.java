package com.games.chessGame.Controller;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.games.chessGame.DTO.playerDTO;


@Controller
public class InitializerController {
	
	public InitializerController() {
	}
	@Autowired
	com.games.chessGame.Services.GameService gameService;
	
	public void getInputs() {
		Scanner sc=new Scanner(System.in);

		System.out.println("Starting Game");
		gameService.StartNewGame(new playerDTO("rahul"), new playerDTO("akhil"));
		while(1==1) {
		System.out.print("Select Piece to find all Moves");
		int cr=sc.nextInt();
		int cc=sc.nextInt();
		try {
		gameService.getAllUserMoves(cr,cc,1);
		System.out.println("Make a move");
		
		//int cr1=sc.nextInt();
		//int cc1=sc.nextInt();
		int mr=sc.nextInt();
		int mc=sc.nextInt();
		try {
		gameService.makeamove(cr,cc,mr,mc,1);
		}
		catch(Exception e){
		System.out.println(e);
		}
		System.out.println("do you want to make move back");
		int my=sc.nextInt();
		if(my==99) {
			gameService.returnToBack(1);
		}
		
		}
		catch(Exception e){
		System.out.println(e);
		}
		

		}
		
	}
	
	

}
