package p_DotsAI;

import java.awt.Point;
import java.util.ArrayList;

import p_DotsAI.Protocol.Game;

public class Wall {

	public ArrayList<Point> points;//точки
	public ArrayList<Point> connections;//связи
	public ArrayList<Point> abstracts;//абстрактные точки
	public ArrayList<Point> links;//абстрактные связи
	int wallStartX;
	int wallStartY;//начало, конец
	public int wallEndX;
	public int wallEndY;
	int wallIndex;
	int wallLength;
	String sign;
	
	public Wall(Game game,int x,int y,int wallIndex1,String sign1,ArrayList<String> fieldOfWalls){		
		//System.out.println("added wall ("+sign+")="+(x-1)+";"+(y-1));
		points=new ArrayList<Point>();
		connections=new ArrayList<Point>();
		abstracts=new ArrayList<Point>();
		links=new ArrayList<Point>();
		points.add(new Point(x,y));
		wallStartX=x;wallStartY=y;
		wallIndex=wallIndex1;
		sign=sign1;
		fieldOfWalls.set((x-1)*game.sizeY+y-1,"P"+sign+wallIndex);
	}
	
	public void searchWallEnd(Game game,ArrayList<String> fieldOfWalls){//искать концы стен
		wallEndX=wallStartX;
		wallEndY=wallStartY;
		wallLength=0;
		for(int x=0;x<game.sizeX;x++)for(int y=0;y<game.sizeY;y++){
			if(fieldOfWalls.get(x*game.sizeY+y).equals("P"+sign+wallIndex)){
				if(wallLength<=(Math.abs(x-wallStartX+1)+Math.abs(y-wallStartY+1))
						&&
					Math.max(Math.abs(x+1-wallStartX),Math.abs(y+1-wallStartY))>Math.max(Math.abs(wallEndX-wallStartX),Math.abs(wallEndY-wallStartY))
					){
						wallLength=Math.abs(x-wallStartX+1)+Math.abs(y-wallStartY+1);
						wallEndX=x+1;
						wallEndY=y+1;
				}
			}
		}
		wallLength=Math.abs(wallEndX-wallStartX)+Math.abs(wallEndY-wallStartY);		
	}
	
	public void addPoint(Game game,int x,int y,ArrayList<String> fieldOfWalls){
		if(!fieldOfWalls.get((x-1)*game.sizeY+y-1).equals("N")){return;}
		fieldOfWalls.set((x-1)*game.sizeY+y-1,"P"+sign+wallIndex);
		points.add(new Point(x,y));
	}
	
	public void addAbstract(Game game,int x,int y,ArrayList<String> fieldOfWalls){
		if(!fieldOfWalls.get((x-1)*game.sizeY+y-1).equals("N")){return;}
		fieldOfWalls.set((x-1)*game.sizeY+y-1,"A"+sign+wallIndex);
		abstracts.add(new Point(x,y));
	}
	
	public void addLink(Game game,int x,int y,ArrayList<String> fieldOfWalls){
		if(!fieldOfWalls.get((x-1)*game.sizeY+y-1).equals("N")){return;}
		fieldOfWalls.set((x-1)*game.sizeY+y-1,"L"+sign+wallIndex);
		links.add(new Point(x,y));
	}
	
	public void addConnection(Game game,int x,int y,ArrayList<String> fieldOfWalls){//пустые клетки - пустые пространства в стенах
		if(!fieldOfWalls.get((x-1)*game.sizeY+y-1).equals("N")){return;}
		fieldOfWalls.set((x-1)*game.sizeY+y-1,"C"+sign+wallIndex);
		connections.add(new Point(x,y));
	}
	
	public int getLengthFromLastBlue(Point p){wallLength=Math.abs(wallEndX-p.x)+Math.abs(wallEndY-p.y);return wallLength;}
	public boolean isAtSide(Game game){
		if(wallEndX<3|wallEndY<3|wallEndX>game.sizeX-2|wallEndY>game.sizeY-2){return true;}else{return false;}
	}		
}
