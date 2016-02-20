package p_SingleGameEngine;

import java.awt.Point;
import java.util.ArrayList;

import p_DotsAI.Protocol;
import p_DotsAI.Protocol.Game;

public class SingleGameEngine{

	public int sizeX;
	public int sizeY;
	private int redScore;
	private int blueScore;
	public Dot[][] field;
	private ArrayList<Surrounding> allSurroundings;
	//public int lastX;
	//public int lastY;
	public Point lastPoint;//координаты последнего хода
	public int lastMoveType;
	
	public int surroundingTypeRed=0;
	public int surroundingTypeBlue=1;
	public int surroundingTypeRedCtrl=2;
	public int surroundingTypeBlueCtrl=3;
	
	public int dotTypeEmpty=0;
	public int dotTypeBlue=1;
	public int dotTypeRed=2;
	public int dotTypeRedEatedBlue=3;
	public int dotTypeBlueEatedRed=4;
	public int dotTypeRedTired=5;
	public int dotTypeBlueTired=6;
	public int dotTypeBlueCtrl=7;
	public int dotTypeRedCtrl=8;
	public int dotTypeBlueEatedEmpty=9;
	public int dotTypeRedEatedEmpty=10;

	public SingleGameEngine clone(){
    	SingleGameEngine obj=new SingleGameEngine(sizeX-2,sizeY-2);
    	obj.lastMoveType=lastMoveType;
    	
    	obj.lastPoint=new Point(lastPoint.x,lastPoint.y);
    	
    	obj.sizeX=sizeX;
    	obj.sizeY=sizeY;
    	obj.blueScore=blueScore;
    	obj.redScore=redScore;
    	Dot[][] f = new Dot[sizeX][sizeY];
		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				f[x][y]=new Dot(field[x][y].x, field[x][y].y,dotTypeEmpty);
				f[x][y].type=field[x][y].type;
			}
		}   
		obj.allSurroundings = new ArrayList<Surrounding>();
		for (int surroundingIndex = 0; surroundingIndex < allSurroundings.size(); surroundingIndex++) {
			//<clone all of allSurroundings>
			Surrounding s=new Surrounding();
			s.type=allSurroundings.get(surroundingIndex).type;
			//System.out.println("path size="+allSurroundings.get(surroundingIndex).path.size());
			//System.out.println("capturedPoints size="+allSurroundings.get(surroundingIndex).capturedPoints.size());
			for (int i = 0; i < allSurroundings.get(surroundingIndex).path.size(); i++) {
				s.path.add(f[allSurroundings.get(surroundingIndex).path.get(i).x][allSurroundings.get(surroundingIndex).path.get(i).y]);				
			}
			for (int i = 0; i < allSurroundings.get(surroundingIndex).capturedPoints.size(); i++) {	
				s.capturedPoints.add(f[allSurroundings.get(surroundingIndex).capturedPoints.get(i).x][allSurroundings.get(surroundingIndex).capturedPoints.get(i).y]);	
				//f[allSurroundings.get(surroundingIndex).capturedPoints.get(i).x][allSurroundings.get(surroundingIndex).capturedPoints.get(i).y].masterSurrounding=s;
			}			
			obj.allSurroundings.add(s);
			//</clone all of allSurroundings>
		}
		obj.field = f;	
		//System.out.println("end clone ------------------------------------------------");
        return obj;
    }
	
   public int getRedSurroundingCount(){
	   int surroundingCount=0;
	   for (int surroundingIndex = 0; surroundingIndex < allSurroundings.size(); surroundingIndex++) {
			if(allSurroundings.get(surroundingIndex).type==surroundingTypeRed)surroundingCount++;
	   }
	   return surroundingCount;
   }
   
   public int getBlueSurroundingCount(){
	   int surroundingCount=0;
	   for (int surroundingIndex = 0; surroundingIndex < allSurroundings.size(); surroundingIndex++) {
			if(allSurroundings.get(surroundingIndex).type==surroundingTypeBlue)surroundingCount++;
	   }
	   return surroundingCount;
   }
   
	public SingleGameEngine(int sizeX1, int sizeY1) {
		allSurroundings = new ArrayList<Surrounding>();
		redScore=0;
		blueScore=0;
		sizeX = sizeX1 + 2;
		sizeY = sizeY1 + 2;
		field = new Dot[sizeX][sizeY];
		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				field[x][y]=new Dot(x, y,dotTypeEmpty);
			}
		}
	}
	
	public int[][] getFieldState(){
		int[][] fieldState=new int[sizeX-2][sizeY-2];
		for (int x = 1; x <= sizeX-2; x++) 
			for (int y = 1; y <= sizeY-2; y++) {
				fieldState[x-1][y-1]=toStringDotType(field[x][y].type);// <get field state>
		}
		return fieldState;
	}

	public int[][] getTerrytoryState(){
		int[][]fieldTerrytoryState=new int[sizeX-2][sizeY-2];
		for (int x = 1; x <= sizeX-2; x++) 
			for (int y = 1; y <= sizeY-2; y++) {
				fieldTerrytoryState[x-1][y-1]=toTerrytoryStringDotType(field[x][y].type);// <get field state>
		}
		return fieldTerrytoryState;
	}
	
	public void setFieldState(Game game){
		for (int x = 1; x <= game.sizeX; x++) 
			for (int y = 1; y <= game.sizeY; y++) {
				game.fieldState[x-1][y-1]=toStringDotType(field[x][y].type);// <get field state>
		}
	}

	public void setTerrytoryState(Game game){
		for (int x = 1; x <= game.sizeX; x++) 
			for (int y = 1; y <= game.sizeY; y++) {
				game.fieldTerrytoryState[x-1][y-1]=toTerrytoryStringDotType(field[x][y].type);// <get field state>
		}
	}
	
	public byte toTerrytoryStringDotType(int type) {
		if(type==dotTypeEmpty)return Protocol.templateDotTypeNULL;//empty
		if(type==dotTypeBlue)return Protocol.templateDotTypeBLUE;//blue
		if(type==dotTypeBlueCtrl)return Protocol.templateDotTypeBLUE;//blue домик
		if(type==dotTypeBlueEatedEmpty)return Protocol.templateDotTypeBLUE;
		if(type==dotTypeBlueEatedRed)return Protocol.templateDotTypeBLUE;//my points
		if(type==dotTypeBlueTired)return Protocol.templateDotTypeBLUE;//null blue
		if(type==dotTypeRed)return Protocol.templateDotTypeRED;//red
		if(type==dotTypeRedCtrl)return Protocol.templateDotTypeRED;//red домик
		if(type==dotTypeRedEatedBlue)return Protocol.templateDotTypeRED;//
		if(type==dotTypeRedEatedEmpty)return Protocol.templateDotTypeRED;
		if(type==dotTypeRedTired)return Protocol.templateDotTypeRED;
		return 9;
	}
	
	public byte toStringDotType(int type) {
		if(type==dotTypeEmpty)return Protocol.templateDotTypeNULL;//empty
		if(type==dotTypeBlue)return Protocol.templateDotTypeBLUE;//blue
		if(type==dotTypeBlueCtrl)return Protocol.templateDotTypeNULL;//blue домик
		if(type==dotTypeBlueEatedEmpty)return Protocol.templateDotTypeBLUE;
		if(type==dotTypeBlueEatedRed)return Protocol.templateDotTypeBLUE;//my points
		if(type==dotTypeBlueTired)return Protocol.templateDotTypeBLUE;//null blue
		if(type==dotTypeRed)return Protocol.templateDotTypeRED;//red
		if(type==dotTypeRedCtrl)return Protocol.templateDotTypeNULL;//red домик
		if(type==dotTypeRedEatedBlue)return Protocol.templateDotTypeRED;//
		if(type==dotTypeRedEatedEmpty)return Protocol.templateDotTypeRED;
		if(type==dotTypeRedTired)return Protocol.templateDotTypeRED;
		return 9;
	}
	
	public int getRedScore() {return redScore;}
	public int getBlueScore() {return blueScore;}
	public ArrayList<Surrounding> getSurroundings(){return allSurroundings;}
	
	public boolean canMakeMove(int x, int y) {		
		if ((x < 1) || (y < 1) || (x > sizeX - 2) || (y > sizeY - 2)) {return false;}
		if (!hasType(field[x][y],dotTypeEmpty, dotTypeRedCtrl, dotTypeBlueCtrl)) {return false;}
		return true;
	}

	Dot getNeighbour(int direction,int x,int y) {
		direction %= 8; // the change of this variable is local in java
		if (direction < 0)direction = direction + 8;
		switch (direction) {
			case 0:return field[x][y+1];
			case 1:return field[x+1][y+1];
			case 2:return field[x+1][y];
			case 3:return field[x+1][y-1];
			case 4:return field[x][y-1];
			case 5:return field[x-1][y-1];
			case 6:return field[x-1][y];
			case 7:return field[x-1][y+1];
			default:return field[x][y+1];
		}
	}
	
	void undoCtrl(Surrounding surrounding,Dot dot) {
		if (((dot.type == dotTypeBlueCtrl) || (dot.type == dotTypeRedCtrl))	&& (dot.masterSurrounding == surrounding)) {
			dot.type = dotTypeEmpty;
			dot.masterSurrounding = null;
		}
	}
	
	boolean isSurrBuilder(int moveType,Dot dot) {
		return ((moveType == Protocol.moveTypeBlue) && (dot.type == dotTypeBlue))	|| ((moveType == Protocol.moveTypeRed) && (dot.type == dotTypeRed));
	}
	
	boolean hasType(Dot dot,int set) {
		if(set == dot.type)return true;
		return false;
	}
	
	boolean hasType(Dot dot,int set1,int set2,int set3) {
		if(set1 == dot.type | set2 == dot.type | set3 == dot.type)return true;
		return false;
	}
	
	boolean hasType(Dot dot,int set1,int set2,int set3,int set4) {
		if(set1 == dot.type | set2 == dot.type | set3 == dot.type | set4 == dot.type)return true;
		return false;
	}
	
	public void makeMove(Point p, int moveType) {
		if (!canMakeMove(p.x, p.y)) {return;}
			
		lastPoint=p; 
		lastMoveType=moveType;		
		
		Surrounding ctrlSurrounding=(field[p.x][p.y].type == dotTypeBlueCtrl || field[p.x][p.y].type == dotTypeRedCtrl) ? field[p.x][p.y].masterSurrounding : null;
		field[p.x][p.y].type = (moveType==Protocol.moveTypeBlue?dotTypeBlue:dotTypeRed);
		
		boolean surroundedSomething = false;
		if (ctrlSurrounding == null) { // there was no Ctrl surrounding			
			for (int i = 0; i < 8; i++) {
				if (isSurrBuilder(moveType,getNeighbour(i,p.x,p.y))) {
					surroundedSomething |= trySurround(field[p.x][p.y], i, moveType, null);
				}
			}
			return;
		}
		else { // there was a Ctrl surrounding
			if (((ctrlSurrounding.type == surroundingTypeBlueCtrl) && (moveType == Protocol.moveTypeRed))|| ((ctrlSurrounding.type == surroundingTypeRedCtrl) && (moveType == Protocol.moveTypeBlue))) { // dot was placed into an enemy surrounding

				// try to eat for the moving player
				for (int i = 0; i < 8; i++) {
					if (isSurrBuilder(moveType,getNeighbour(i,p.x,p.y))) {
						surroundedSomething |=trySurround(field[p.x][p.y], i, moveType, null);
					}
				}

				if (surroundedSomething == false) {// eat back if the player didn't surround anything
					ctrlSurrounding.type =(moveType==Protocol.moveTypeBlue)?surroundingTypeRed:surroundingTypeBlue;
					// getSurroundingType_Enemy(moveType);
					for (int indexOfDot = ctrlSurrounding.capturedPoints.size() - 1; indexOfDot >= 0; indexOfDot--) {
							eat(ctrlSurrounding.capturedPoints.get(indexOfDot),ctrlSurrounding, null);
						}
						return;
				} else {
					// the player moved into the Ctrl territory and survived (surrounded himself)
					// clear the old surrounding
					for (int i = 0; i < ctrlSurrounding.capturedPoints.size(); i++) {
						Dot dotToClear = ctrlSurrounding.capturedPoints.get(i);
						if (dotToClear.masterSurrounding == ctrlSurrounding) {
							undoCtrl(ctrlSurrounding,dotToClear);
							dotToClear.masterSurrounding = null;
						}
					}
					return;
				}
				} else {
					// dot was placed into your own surrounding ctrlSurrounding.wasDestroyed = false;
					for (int i = 0; i < 8; i++) {
						if (isSurrBuilder(moveType,getNeighbour(i,p.x,p.y))) {
							trySurround(field[p.x][p.y], i, moveType, ctrlSurrounding);
						}
					}
					return;
				}
			}
		}
	
	int getNewSurroundingPathIndexOf(ArrayList<Dot> path,Dot loopDot){
		for(int i = 0;i<path.size();i++){
			if(path.get(i).x==loopDot.x&path.get(i).y==loopDot.y){
				return i;
			}
		}
		return -1;
	}
	
	private boolean trySurround(Dot startDot, int startDirection,int moveType, Surrounding outerCtrlSurrounding) {
		// checking that the starting position is correct: if we start making a path, we return to the beginning.
		int newDirection = startDirection + 1;
		while (isSurrBuilder(moveType,getNeighbour(newDirection,startDot.x,startDot.y)) == false) {newDirection += 1;}
		if ((newDirection - startDirection == 1)||((newDirection - startDirection == 2) && (startDirection % 2 == 0))) {
			return false;
		}
		Surrounding newSurrounding = new Surrounding();
		Dot loopDot = startDot;
		int loopDirection = startDirection;
		do {
			if (loopDot.masterSurrounding == newSurrounding) {
				int indexInPath = getNewSurroundingPathIndexOf(newSurrounding.path,loopDot);
				for (int indexToDelete = newSurrounding.path.size() - 1; indexToDelete > indexInPath; indexToDelete--) {
					newSurrounding.path.remove(indexToDelete);
				}
			} else {
				newSurrounding.path.add(loopDot);
				loopDot.masterSurrounding = newSurrounding;
			}
			loopDot = getNeighbour(loopDirection,loopDot.x,loopDot.y);
			loopDirection = 4 + loopDirection;			
			newDirection = loopDirection - 1;
			while (isSurrBuilder(moveType,getNeighbour(newDirection,loopDot.x,loopDot.y)) == false) {newDirection -= 1;}
			loopDirection = newDirection;	
			
		} while ((loopDot.x!=startDot.x | loopDot.y!=startDot.y)||((loopDirection - startDirection) % 8 != 0));
		newSurrounding.path.add(startDot);
		if (newSurrounding.path.size() < 4) {return false;}// simple check - not necessary				
	
		// no optimization of using the smaller rectangle is used.
		// by the way, I think this can be done by the java optimizer. (vn91)
		boolean[][] verticalIntersectionPlaces = new boolean[sizeX][sizeY];
		// marking intersection-points
		for (int pathIndex = 0; pathIndex < newSurrounding.path.size() - 1; pathIndex++) {
			int dx = newSurrounding.path.get(pathIndex + 1).x - newSurrounding.path.get(pathIndex).x;
			int dy = newSurrounding.path.get(pathIndex + 1).y - newSurrounding.path.get(pathIndex).y;
				if ((dx == 1) && (dy == 1)) {
				verticalIntersectionPlaces[newSurrounding.path.get(pathIndex).x][newSurrounding.path.get(pathIndex).y + 1] ^=
						true;
			} else if ((dx == 1) && (dy == 0)) {
				verticalIntersectionPlaces[newSurrounding.path.get(pathIndex).x][newSurrounding.path.get(pathIndex).y] ^=
						true;
			} else if ((dx == 1) && (dy == -1)) {
				verticalIntersectionPlaces[newSurrounding.path.get(pathIndex).x][newSurrounding.path.get(pathIndex).y] ^=
						true;
			} else if ((dx == -1) && (dy == 1)) {
				verticalIntersectionPlaces[newSurrounding.path.get(pathIndex).x - 1][newSurrounding.path.get(pathIndex).y + 1] ^= true;
			} else if ((dx == -1) && (dy == 0)) {
				verticalIntersectionPlaces[newSurrounding.path.get(pathIndex).x - 1][newSurrounding.path.get(pathIndex).y] ^= true;
			} else if ((dx == -1) && (dy == -1)) {
				verticalIntersectionPlaces[newSurrounding.path.get(pathIndex).x - 1][newSurrounding.path.get(pathIndex).y] ^= true;
			}
				newSurrounding.path.get(pathIndex).masterSurrounding = newSurrounding;
		}
		Dot mustBeInnerDot = getNeighbour(startDirection + 1,startDot.x,startDot.y);
		boolean isInside = false;
		for (int i = 1; i <= mustBeInnerDot.y; i++) {
			isInside ^= verticalIntersectionPlaces[mustBeInnerDot.x][i];
		}
		// exiting if the starting point is out of the hypothetical path
		if (isInside == false) {return false;} 
		else {allSurroundings.add(newSurrounding);}
		boolean[][] innerTerritory = new boolean[sizeX][sizeY];
		boolean isInside1 = false;
		for (int x = 0; x < sizeX; x++) {
			// isInside doesn't need to be reseted
			for (int y = 0; y < sizeY; y++) {
				isInside1 ^= verticalIntersectionPlaces[x][y];
				innerTerritory[x][y] =
						isInside1 && (field[x][y].masterSurrounding != newSurrounding);
			}
		}
		boolean enemyCaptured = false;			
		for (int x = 0; x < sizeX; x++) {// searching for enemies
			for (int y = 0; y < sizeY; y++) {
				if((innerTerritory[x][y]==true)&&(((moveType==Protocol.moveTypeBlue)&&(field[x][y].type==dotTypeRed))||((moveType==Protocol.moveTypeRed)&&(field[x][y].type==dotTypeBlue)))){
					enemyCaptured = true;
				}
			}
		}
		newSurrounding.type = enemyCaptured?((moveType==Protocol.moveTypeBlue)?surroundingTypeBlue:surroundingTypeRed):((moveType == Protocol.moveTypeBlue) ? surroundingTypeBlueCtrl: surroundingTypeRedCtrl);
		
		// eating
		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				if (innerTerritory[x][y] == true) {
					eat(field[x][y],newSurrounding, outerCtrlSurrounding);
				}
			}
		}
		return true;
	}
	
	void eat(Dot dot, Surrounding newSurrounding, Surrounding outerCtrlSurrounding) {
		// Remove all code that works with Surroundings from here..
		if (dot.masterSurrounding == null) {dot.masterSurrounding = newSurrounding;newSurrounding.capturedPoints.add(dot);} 
		else if (dot.masterSurrounding == newSurrounding) {} 
		else {
			if (dot.masterSurrounding == outerCtrlSurrounding) {
				if (((dot.masterSurrounding.type==surroundingTypeBlueCtrl)||(dot.masterSurrounding.type==surroundingTypeRedCtrl))) {
					Surrounding surroundingToClear = dot.masterSurrounding;
					for (int i = 0; i < surroundingToClear.capturedPoints.size(); i++) {
						Dot dotToClear = surroundingToClear.capturedPoints.get(i);
						undoCtrl(surroundingToClear,dotToClear);
						dotToClear.masterSurrounding = null;
					}
				}
				dot.masterSurrounding = newSurrounding;
			} else {newSurrounding.capturedPoints.add(dot);}
		}

		// <changing Dot types>
		if(newSurrounding.type==surroundingTypeBlue){
			if (hasType(dot,dotTypeBlue)) {dot.type = dotTypeBlueTired;} 
			else if (hasType(dot,dotTypeRedEatedBlue)) {redScore -= 1;dot.type = dotTypeBlueTired;} 
			else if (hasType(dot,dotTypeRed)) {blueScore += 1;dot.type = dotTypeBlueEatedRed;} 
			else if (hasType(dot,dotTypeRedTired)) {blueScore += 1;dot.type = dotTypeBlueEatedRed;} 
			else if (hasType(dot,dotTypeBlueCtrl, dotTypeEmpty,dotTypeRedCtrl, dotTypeRedEatedEmpty)) {dot.type = dotTypeBlueEatedEmpty;}
		}else if(newSurrounding.type==surroundingTypeRed){
			if (hasType(dot,dotTypeRed)) {dot.type = dotTypeRedTired;} 
			else if (hasType(dot,dotTypeBlueEatedRed)) {blueScore -= 1;dot.type = dotTypeRedTired;	} 
			else if (hasType(dot,dotTypeBlue)) {redScore += 1;dot.type = dotTypeRedEatedBlue;} 
			else if (hasType(dot,dotTypeBlueTired)) {redScore += 1;dot.type = dotTypeRedEatedBlue;	} 
			else if (hasType(dot,dotTypeRedCtrl, dotTypeEmpty,dotTypeBlueCtrl, dotTypeBlueEatedEmpty)) {dot.type = dotTypeRedEatedEmpty;}
		}else if(newSurrounding.type==surroundingTypeBlueCtrl){if(hasType(dot,dotTypeEmpty)) {dot.type = dotTypeBlueCtrl;}}
		else if(newSurrounding.type==surroundingTypeRedCtrl){if(hasType(dot,dotTypeEmpty)) {dot.type = dotTypeRedCtrl;}}
		// <changing Dot types>
	}
}
