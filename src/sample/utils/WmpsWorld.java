package sample.utils;

import java.util.Random;

public class WmpsWorld {
	
	// 0 for normal 
	// 1 for wumpus
	// 2 for stench
	// 3 for pit
	// 4 for breeze
	// 5 for gold
	
	private int[][] world = new int[10][10];
	private String[][] mainWorld=new String[10][10];
	private Random rand = new Random();
	private String list[] = {"", "W", "S", "P", "B", "G","Gl"};
	public int[][] getWorld(){
		return world;
	}
	public WmpsWorld() {
		
		for(int i=0;i<10;i++)
			for(int j=0;j<10;j++) {
				world[i][j]=-1;
				mainWorld[i][j]="";
			}
	}
	
	public String[][] generateWorld(){
		
		 setGold();
		 setWumpus();
		 setPit();
		 finalizeWorld();
		
		return mainWorld;
	}

	

	private void setGold() {
		// TODO Auto-generated method stub
		
		int i =rand.nextInt(9)+1;
		int j =rand.nextInt(9)+1;
		world[i][j]=5;
		mainWorld[i][j] = list[5];
		
		setSurroundings(i,j,6);
		
	}



	private void setWumpus() {
		// TODO Auto-generated method stub
		int i =rand.nextInt(9)+1;
		int j =rand.nextInt(9)+1;
		
		if(world[i][j]==-1) {
			world[i][j]=1;
			mainWorld[i][j]=list[1];
		}
		else {
			world[i][j]=1;
			mainWorld[i][j]=mainWorld[i][j]+" "+list[1];
		}
		
		
		setSurroundings(i,j,2);
		
	}
	
	private void setPit() {
		// TODO Auto-generated method stub
		
		int pit = rand.nextInt(5)+5;
		
		for(int loop=1;loop<=pit;loop++) {
			int i =rand.nextInt(9)+1;
			int j =rand.nextInt(9)+1;
			
			if(world[i][j]!=1 && world[i][j]!=5 && world[i][j]!=3) {
				world[i][j]=3;
				mainWorld[i][j]=list[3];
				setSurroundings(i,j,4);
			}
	
		}
		
	}

	private void setSurroundings(int i, int j, int k) {
		// TODO Auto-generated method stub
		
		if(j-1>=0 && world[i][j-1]!=3) {
			
			if(world[i][j-1]==-1) {
				world[i][j-1]=k;
				mainWorld[i][j-1]=list[k];
			}
			else {
				world[i][j-1]=k;
				mainWorld[i][j-1]=mainWorld[i][j-1]+" "+list[k];
			}
			
		}	
		
		if(j+1<10 && world[i][j+1]!=3) {
			
			if(world[i][j+1]==-1) {
				world[i][j+1]=k;
				mainWorld[i][j+1]=list[k];
			}
			else {
				world[i][j+1]=k;
				mainWorld[i][j+1]=mainWorld[i][j+1]+" "+list[k];
			}
		}
			
		if(i-1>=0 && world[i-1][j]!=3) {
			
			if(world[i-1][j]==-1) {
				world[i-1][j]=k;
				mainWorld[i-1][j]=list[k];
			}
			else {
				world[i-1][j]=k;
				mainWorld[i-1][j]=mainWorld[i-1][j]+" "+list[k];
			}
		}
			
		if(i+1<10 && world[i+1][j]!=3) {
			
			if(world[i+1][j]==-1) {
				world[i+1][j]=k;
				mainWorld[i+1][j]=list[k];
			}
			else {
				world[i+1][j]=k;
				mainWorld[i+1][j]=mainWorld[i+1][j]+" "+list[k];
			}
		}
			
	}
	
	private void finalizeWorld() {
		// TODO Auto-generated method stub
		
		for(int i=0;i<10;i++)
			for(int j=0;j<10;j++) {
				
				if(world[i][j]==-1) {
					
					world[i][j]=0;
					mainWorld[i][j]=list[0];
					
				}
				
				
				
			}
		
	}
}
