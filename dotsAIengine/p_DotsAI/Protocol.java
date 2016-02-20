package p_DotsAI;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import p_MakrosEngine.MakrosApplicationInGame;
import p_SingleGameEngine.SingleGameEngine;
import p_Statistics.MoveStatType;
import p_Statistics.StatisticsFrame;
import p_TemplateEngine.TemplateEngine;
import p_TemplateEngine.TemplateFieldSideType;
import p_TemplateEngine.TemplateType;

public class Protocol {
	
	public static byte sizeX_TE=9;//размер поля клеток (X x Y) template editor
	public static byte sizeY_TE=9;//размер поля клеток (X x Y) template editor
	public static byte sizeX_ME=15;//размер поля клеток (X x Y) makros editor
	public static byte sizeY_ME=15;//размер поля клеток (X x Y) makros editor
	public static byte sizeX_ME_half=8;//размер поля клеток (X x Y) makros editor
	public static byte sizeY_ME_half=8;//размер поля клеток (X x Y) makros editor
	public static String appName="DotsAI";//название программы
	public static String appVersion="1.70";
	public static String appDate="20 feb 2016";
	
	public static byte templateDotTypeANY=0;//"A";
	public static byte templateDotTypeLAND=1;//"L";
	public static byte templateDotTypeNULL=2;//"N";	
	public static byte templateDotTypeBLUE=3;//"B";
	public static byte templateDotTypeBLUE_EMPTY=4;//"C";
	public static byte templateDotTypeRED=5;//"R";
	public static byte templateDotTypeRED_EMPTY=6;//"P";	
	
	//в базе хранятся без знака -	
	public static byte templateDotTypeRED_ATTACK=-1;
	public static byte templateDotTypeRED_PROTECTION=-2;
	public static byte templateDotTypeRED_NORMAL=-3;
	public static byte templateDotTypeBLUE_ATTACK=-6;
	
	public static byte moveTypeRed=0;
	public static byte moveTypeBlue=1;
	
	Random rand=new Random();
	
	public TemplateEngine templateEngine;	
	public ArrayList<Game> games;
	public StatisticsFrame stat;
	
	public Protocol(){
		stat=new StatisticsFrame();
		templateEngine=new TemplateEngine();
		games=new ArrayList<Game>();
	}	
	
	public Game getGame(int gameIdx) {
		for(int i=0;i<games.size();i++){
			if(games.get(i).gameIdx==gameIdx)return games.get(i);
		}
		return null;
	}
	
	public void deleteGame(int gameIdx) {
		for(int i=0;i<games.size();i++){
			if(games.get(i).gameIdx==gameIdx)games.remove(i);
		}
	}
	
	public void addNewGame(int gameIdx,EnemyType enemyType1,int sizeX,int sizeY,int redX1,int redY1,int blueX1,int blueY1,int redX2,int redY2,int blueX2,int blueY2){
		games.add(new Game(gameIdx,enemyType1,sizeX,sizeY,redX1,redY1,blueX1,blueY1,redX2,redY2,blueX2,blueY2));
	}
	
	public class Game{
		public int gameIdx;
		public int sizeX;//размер поля клеток (X x Y)
		public int sizeY;//размер поля клеток (X x Y)
		public EnemyType enemyType;
		public SingleGameEngine singleGameEngine;//модель игрового поля
		public Point lastAimove;
		public byte[][] movesMas;
		int movesCount;//число ходов в игре
		
		public ArrayList<Point> BSSpoints;
		public ArrayList<Point> abstractPoints;
		public ArrayList<MakrosApplicationInGame> makrosApplicationInGame;
		boolean isCanGroung;
		public byte[][] fieldState;
		public byte[][] fieldTerrytoryState;
		//public String[][] fieldGroundedAIPoints;
		boolean isUseGlobalAttackTemplate;//использовать только один раз в игре
		public Point moveAI;
		public int crossCount;//число скрестов
		public int smallerRedWallIdx;	
		public ArrayList<Wall> wallRed;
		public ArrayList<Wall> wallBlue;
		public ArrayList<String> fieldOfWalls;
		boolean isMoveByBeginTemplate;//вначале игры ходить по BeginTemplate до первого хода другим шаблоном
		private int lossLevel;//какое преимущество надо добыть над ИИ, чтобы ИИ сдался
		
		public double fieldSpectrumRed[][];
		public double fieldSpectrumBlue[][];
		public double fieldSpectrumRedMax;
		public double fieldSpectrumBlueMax;
		
		public boolean isSearchedByAbstractAtCurrentMove;//т.к. абстрактные ищутся одинаково во всех isMoveByRedOriented, 
						//которые вызываются несколько раз за ход, то искать только один раз
		
		public Game(int gameIdx1,EnemyType enemyType1,int sizeX1,int sizeY1,
					int redX1,int redY1,int blueX1,int blueY1,int redX2,int redY2,int blueX2,int blueY2
				){
			stat.clearStat();
			
			lossLevel=rand.nextInt(30)+17;
			
			sizeX=sizeX1;
			sizeY=sizeY1;
			fieldState=new byte[sizeX][sizeY];
			fieldTerrytoryState=new byte[sizeX][sizeY];
			/*fieldGroundedAIPoints=new String[sizeX][sizeY];
			for(int i=0;i<sizeX;i++)for(int j=0;j<sizeY;j++){
				fieldGroundedAIPoints[i][j]="";
			}*/
			
			//влияние синих и красных на окружающие точки
			fieldSpectrumRed=new double[sizeX][sizeY];
			fieldSpectrumBlue=new double[sizeX][sizeY];
			for(int i=0;i<sizeX;i++)for(int j=0;j<sizeY;j++){
				fieldSpectrumRed[i][j]=0;
				fieldSpectrumBlue[i][j]=0;
			}
			fieldSpectrumRedMax=0;
			fieldSpectrumBlueMax=0;
			
			gameIdx=gameIdx1;
			smallerRedWallIdx=-1;
			isMoveByBeginTemplate=true;			
			enemyType=enemyType1;
			isSearchedByAbstractAtCurrentMove=false;
			
			moveAI=new Point();
			isCanGroung=false;
			isUseGlobalAttackTemplate=true;
			
			lastAimove=new Point(redX2,redY2);	
			
			BSSpoints=new ArrayList<Point>();
			abstractPoints=new ArrayList<Point>();
			makrosApplicationInGame=new ArrayList<MakrosApplicationInGame>();
			
			movesCount=0;
			movesMas=new byte[sizeX*sizeY][3];
			singleGameEngine=new SingleGameEngine(sizeX,sizeY);//модель игрового поля
			
			singleGameEngine.makeMove(new Point(redX1,redY1),Protocol.moveTypeRed);
			singleGameEngine.makeMove(new Point(blueX1,blueY1), Protocol.moveTypeBlue);
			singleGameEngine.makeMove(new Point(redX2,redY2),Protocol.moveTypeRed);
			singleGameEngine.makeMove(new Point(blueX2,blueY2), Protocol.moveTypeBlue);
		}

		public void addInMovesMas(Point p, int type){//red=0,blue=1		
			movesMas[movesCount][0]=(byte) p.x;
			movesMas[movesCount][1]=(byte) p.y;
			movesMas[movesCount][2]=(byte) type;
			movesCount++;
		}

		public int getMovesCount(){return movesCount;}
		
	}
	
	public void addAIenemyMove(int gameIdx,int x,int y){
		Game game=getGame(gameIdx);
		if(game==null)return;
		game.addInMovesMas(new Point(x,y), moveTypeBlue);//moves++;
		game.singleGameEngine.makeMove(new Point(x,y), moveTypeBlue);
		if(game.movesCount%7==0){
			//System.out.println("System.gc");
			System.gc();
		}
	}
	
	public Point getAImove(int gameIdx,int level,Point recommendedMove){
		Game game=getGame(gameIdx);
		//if(recommendedMove!=null)game.lastAimove=recommendedMove;
		//else 		
			game.lastAimove=getAImovePoint(game,gameIdx,level,recommendedMove);		
		game.addInMovesMas(game.lastAimove, moveTypeRed);//moves++;
		game.singleGameEngine.makeMove(game.lastAimove, moveTypeRed);		
		return game.lastAimove;
	}
	
	Point getAImovePoint(Game game,int gameIdx,int level,Point recommendedMove){		
		//если последний ход человека был вблизи действия makrosApplication, но не по макросу, то 'ma' удаляется
		//лучше не удалять 'ma', если ход в ANY или Pink точки
		/*for(int i=game.makrosApplicationInGame.size()-1;i>=0;i--){
			MakrosApplicationInGame ma=game.makrosApplicationInGame.get(i);
			if(Math.abs(ma.center.x-game.singleGameEngine.lastPoint.x)<5&Math.abs(ma.center.y-game.singleGameEngine.lastPoint.y)<5){
				System.out.println("remove makros by last human, centerPoint="+ma.center.toString());
				game.makrosApplicationInGame.remove(i);
			}
		}*/
			
		//move by makros
		if(isMoveByMakros(game,recommendedMove)){return game.moveAI;}
		
		game.singleGameEngine.setFieldState(game);
				
		wallsSearch(game);
		
		if(isEndGame(game,moveTypeRed)){	
			if(isMoveByGround(game,recommendedMove))return game.moveAI;
			else{
				game.smallerRedWallIdx=-1;
				//System.gc();
				//System.out.println("ИИ заземлился, ИИ заканчивает игру");
				return new Point(-2,-2);//ИИ заземлился, ИИ заканчивает игру
			}
		}
		if(isEndGame(game,moveTypeBlue)){
			game.smallerRedWallIdx=-1;
			//System.gc();
			//System.out.println("человек заземлился, ИИ сдается");
			return new Point(-3,-3);//человек заземлился, ИИ сдается
		}
		if(game.singleGameEngine.getBlueScore()-game.singleGameEngine.getRedScore()>game.lossLevel){
			game.smallerRedWallIdx=-1;
			//System.gc();
			//System.out.println("ИИ сдается, т.к. потерял много точек");
			return new Point(-4,-4);//ИИ сдается, т.к. потерял много точек
		}
						
		//цель BEGIN шаблона только найти макрос и добавить его в список используемых в игре
		if(game.isMoveByBeginTemplate){
			if(templateEngine.getPointIfEqualsLikeAreaByPoint(this,game,TemplateType.templateTypeBEGIN,game.singleGameEngine.lastPoint,game.fieldState,recommendedMove).x!=-1){
				//System.out.println("------------------- TemplateType.BEGIN ------------------");						
				if(isMoveByMakros(game,recommendedMove)){return game.moveAI;}//move by makros
			}else{
				game.isMoveByBeginTemplate=false;
				//System.out.println("------------------- MoveByBeginTemplate=false ------------------");
			}
		}
		
		//move by blue surround security
		if(isMoveBlueSurroundSecurity(game)){stat.moveStatMas.addMoveStat(MoveStatType.BLUE_SURROUND_SECURITY,false);return game.moveAI;}

		//move by template
		
		try{if(TemplateFieldSideType.getFieldSideType(game, game.singleGameEngine.lastPoint)!=TemplateFieldSideType.templateFieldSideTypeINSIDE){
			if(templateEngine.getPointIfEqualsLikeAreaByPoint(this,game,TemplateType.templateTypeSQUARE_SIDE,game.singleGameEngine.lastPoint,game.fieldState,recommendedMove).x!=-1)return game.moveAI;
		}}catch(Exception e){}

		try{if(templateEngine.getPointIfEqualsLikeAreaByPoint(this,game,TemplateType.templateTypeBLUE_SURROUND,game.singleGameEngine.lastPoint,game.fieldState,recommendedMove).x!=-1)return game.moveAI;}catch(Exception e){}
		try{if(templateEngine.getPointIfEqualsLikeAreaByPoint(this,game,TemplateType.templateTypeRED_SURROUND,game.singleGameEngine.lastPoint,game.fieldState,recommendedMove).x!=-1)return game.moveAI;}catch(Exception e){}		
		try{if(templateEngine.getPointIfEqualsLikeAreaByPoint(this,game,TemplateType.templateTypeSQUARE,game.singleGameEngine.lastPoint,game.fieldState,recommendedMove).x!=-1)return game.moveAI;}catch(Exception e){}
		
		//найти для всех концов синих стен ход по GLOBAL_ATTACK шаблону
		/*try{if(game.isUseGlobalAttackTemplate&&game.getMovesCount()>15){
			ArrayList<Point> pBlueWallEnds = new ArrayList<Point>();
			for(int i=0;i<game.wallBlue.size();i++){
				pBlueWallEnds.add(new Point(game.wallBlue.get(i).wallEndX,game.wallBlue.get(i).wallEndY));
			}
			if(templateEngine.getPointIfEqualsLikeAreaByPointList(this,game,TemplateType.templateTypeGLOBAL_ATTACK,pBlueWallEnds,game.fieldState).x!=-1){
				game.isUseGlobalAttackTemplate=false;
				return game.moveAI;
			}
		}}catch(Exception e){}*/
			
		//<расчет спектра>
		if(!game.isCanGroung){
			for(int i=0;i<game.sizeX;i++){for(int j=0;j<game.sizeY;j++){//обнуление спектра
				game.fieldSpectrumRed[i][j]=0;
				game.fieldSpectrumBlue[i][j]=0;
			}}
			int depth=5;//глубина влияния точки на окружающее пространство
			for(int i=0;i<game.sizeX;i++){for(int j=0;j<game.sizeY;j++){
				if(game.fieldState[i][j]==templateDotTypeRED){//.equals("R")){
					for(int i1=-depth;i1<=depth;i1++){for(int j1=-depth;j1<=depth;j1++){
						if(i1==0&j1==0)continue;
						try{game.fieldSpectrumRed[i+i1][j+j1]+=1.0/Math.sqrt(i1*i1+j1*j1);}catch(Exception e){continue;}
					}}
				}else if(game.fieldState[i][j]==templateDotTypeBLUE){//.equals("B")){
					for(int i1=-depth;i1<=depth;i1++){for(int j1=-depth;j1<=depth;j1++){
						if(i1==0&j1==0)continue;
						try{game.fieldSpectrumBlue[i+i1][j+j1]+=1.0/Math.sqrt(i1*i1+j1*j1);}catch(Exception e){continue;}
					}}
				}		
			}}
			game.fieldSpectrumRedMax=0;
			game.fieldSpectrumBlueMax=0;
			//коррекция, чтобы в местах точек красных и синих были значения max
			for(int i=0;i<game.sizeX;i++){for(int j=0;j<game.sizeY;j++){
				if(game.fieldState[i][j]==templateDotTypeBLUE){game.fieldSpectrumBlue[i][j]=Integer.MAX_VALUE;continue;}
				if(game.fieldState[i][j]==templateDotTypeRED){game.fieldSpectrumRed[i][j]=Integer.MAX_VALUE;continue;}
				if(game.fieldSpectrumBlue[i][j]>game.fieldSpectrumBlueMax)game.fieldSpectrumBlueMax=game.fieldSpectrumBlue[i][j];
				if(game.fieldSpectrumRed[i][j]>game.fieldSpectrumRedMax)game.fieldSpectrumRedMax=game.fieldSpectrumRed[i][j];
			}}
		}
		//</расчет спектра>
		
		//искать ходы для всех точек ближней красной стены, затем для всех других красных стен
		game.smallerRedWallIdx=getSmallerRedWallIdx(game);	
		if(game.smallerRedWallIdx==-1){game.isCanGroung=true;}else{game.isCanGroung=false;}
		game.isSearchedByAbstractAtCurrentMove=false;
		if(!game.isCanGroung){
			if(isMoveByRedOriented(game,game.wallRed.get(game.smallerRedWallIdx),level,recommendedMove))return game.moveAI;
		}
		for(int i=0;i<game.wallRed.size();i++){
			if(!game.isCanGroung)if(i==game.smallerRedWallIdx)continue;
			if(isMoveByRedOriented(game,game.wallRed.get(i),level,recommendedMove))return game.moveAI;
		}
			
		//move by ground
		if(isMoveByGround(game,recommendedMove))return game.moveAI;
		
		//move by random
		game.singleGameEngine.setTerrytoryState(game);//чтобы не ходить в домики соперника
		game.moveAI.x=game.lastAimove.x;game.moveAI.y=game.lastAimove.y;	
		while(!game.singleGameEngine.canMakeMove(game.moveAI.x,game.moveAI.y)||game.fieldTerrytoryState[game.moveAI.x-1][game.moveAI.y-1]!=templateDotTypeNULL){
			game.moveAI.x=rand.nextInt(game.sizeX)+1;
			game.moveAI.y=rand.nextInt(game.sizeY)+1;			
		}
		stat.moveStatMas.addMoveStat(MoveStatType.RANDOM,false);
		return game.moveAI;
	}

	private boolean isMoveByRedOriented(Game game,Wall wall,int level,Point recommendedMove){
		//System.out.println("--------------------------------------------------------");
		
		try{if(templateEngine.getPointIfEqualsLikeAreaByPoint(this,game,TemplateType.templateTypeWALL_DESTROY,new Point(wall.wallEndX,wall.wallEndY),game.fieldState,recommendedMove).x!=-1)return true;}catch(Exception e){}
		try{if(templateEngine.getPointIfEqualsLikeAreaByPoint(this,game,TemplateType.templateTypeFINAL_RED_ATTACK,new Point(wall.wallEndX,wall.wallEndY),game.fieldState,recommendedMove).x!=-1)return true;}catch(Exception e){}
		//try{if(templateEngine.getPointIfEqualsLikeAreaByPoint(this,game,TemplateType.templateTypeCONTINUED_RED_ATTACK,new Point(wall.wallEndX,wall.wallEndY),game.fieldState,recommendedMove).x!=-1)return true;}catch(Exception e){}
		//if(!game.isCanGroung)if(isMoveByWall(game,wall.wallEndX,wall.wallEndY,recommendedMove))return true;//MoveByWall
		
		try{if(templateEngine.getPointIfEqualsLikeAreaByPointList(this,game,TemplateType.templateTypeWALL_DESTROY,wall.points,game.fieldState,recommendedMove).x!=-1)return true;}catch(Exception e){}
		try{if(templateEngine.getPointIfEqualsLikeAreaByPointList(this,game,TemplateType.templateTypeFINAL_RED_ATTACK,wall.points,game.fieldState,recommendedMove).x!=-1)return true;}catch(Exception e){}
		try{if(templateEngine.getPointIfEqualsLikeAreaByPointList(this,game,TemplateType.templateTypeCONTINUED_RED_ATTACK,wall.points,game.fieldState,recommendedMove).x!=-1)return true;}catch(Exception e){}
		
		//move by abstract attack wall
		/*if(
				(game.singleGameEngine.getBlueScore()>=game.singleGameEngine.getRedScore()&game.movesCount>20)//если ИИ не выигрывает
				||
				game.wallBlue.size()==0//или против ИИ глобальная атака
		){*/
			//System.out.println("search abstract move, wall idx = "+wall.wallIndex);
			//if(wall.abstracts.size()>0){//только для текущей стены
			if(!game.isSearchedByAbstractAtCurrentMove&game.movesCount>20){
				game.isSearchedByAbstractAtCurrentMove=true;
				//System.out.println("search abstract move, wall.abstracts.size()>0");
				//System.out.println("abstractPoints size="+game.abstractPoints.size());
				//try{if(templateEngine.getPointIfEqualsLikeAreaByPointList(this,game,TemplateType.templateTypeABSTRACT_ATTACK_WALL,wall.abstracts,game.fieldState,recommendedMove).x!=-1)game.abstractPoints.add(game.moveAI);return true;}catch(Exception e){}			
				for(int i=game.abstractPoints.size()-1;i>=0;i--){
					/*boolean isFound=false;
					for(int j=0;j<wall.abstracts.size();j++){
						if(wall.abstracts.get(j).x==game.abstractPoints.get(i).x&wall.abstracts.get(j).y==game.abstractPoints.get(i).y){
							System.out.println("abstract move found for "+game.abstractPoints.get(i).toString());
							isFound=true;
						}
					}
					if(!isFound){
						System.out.println("abstract not found for "+game.abstractPoints.get(i).toString());
						continue;//если абстрактная точка не принадлежит рассматриваемой стене, то пропускаем ее
					}*/
					try{if(templateEngine.getPointIfEqualsLikeAreaByPoint(this,game,TemplateType.templateTypeABSTRACT_ATTACK_WALL,game.abstractPoints.get(i),game.fieldState,recommendedMove).x!=-1){
						game.abstractPoints.add(new Point(game.moveAI.x,game.moveAI.y));
						//System.out.println("found abstract move is "+game.moveAI.toString());
						return true;
					}}catch(Exception e){}
				}
				//для конца стены
				try{if(templateEngine.getPointIfEqualsLikeAreaByPoint(this,game,TemplateType.templateTypeABSTRACT_ATTACK_WALL,new Point(wall.wallEndX,wall.wallEndY),game.fieldState,recommendedMove).x!=-1){
					game.abstractPoints.add(new Point(game.moveAI.x,game.moveAI.y));
					game.abstractPoints.add(new Point(wall.wallEndX,wall.wallEndY));
					return true;
				}}catch(Exception e){}
			}
			//System.out.println("search abstract move by wall end");			
		//}		
		
		//move by wall
		if(!wall.isAtSide(game)){
		//if(!game.isCanGroung){
			double koef=1.0+(10-level)/10.0;//определить уровень сложности, где level=[0;10], чем выше, тем сильнее		
			for(double s=0;s<=game.fieldSpectrumBlueMax;s+=game.fieldSpectrumBlueMax/10.0){
				for(int i=0;i<wall.points.size()/koef;i++){//искать ход с конца стен
					if(game.fieldSpectrumBlue[wall.points.get(i).x-1][wall.points.get(i).y-1]<s
							||
						game.fieldSpectrumBlue[wall.points.get(i).x-1][wall.points.get(i).y-1]>s+game.fieldSpectrumBlueMax/10.0
					){
						continue;
					}
					try{if(isMoveByWall(game,wall.points.get(i).x,wall.points.get(i).y,recommendedMove)){
						return true;
					}}catch(Exception e){}
				}
			}
		}		
		
		return false;
	}

	private boolean isMoveByGround(Game game,Point recommendedMove){	
		for(int i=0;i<game.wallRed.size();i++){
			for(int j=0;j<game.wallRed.get(i).points.size();j++){
				int x=game.wallRed.get(i).points.get(j).x;
				int y=game.wallRed.get(i).points.get(j).y;
				//if(game.fieldGroundedAIPoints[x-1][y-1].equals("R"))continue;
				if(x<8||x>game.sizeX-7||y<8||y>game.sizeY-7){
					if(templateEngine.getPointIfEqualsLikeAreaByPoint(this,game,TemplateType.templateTypeGROUND_SIDE,new Point(x,y),game.fieldState,recommendedMove).x!=-1)return true;
				}
			}
		}
		for(int i=0;i<game.wallRed.size();i++){
			if(templateEngine.getPointIfEqualsLikeAreaByPointList(this,game,TemplateType.templateTypeGROUND,game.wallRed.get(i).points,game.fieldState,recommendedMove).x!=-1)return true;
		}
		
		return false;
	}

	private boolean isMoveBlueSurroundSecurity(Game game){//<move by BLUE_SURROUND_SECURITY>	
		for(int it=0;it<game.BSSpoints.size();it++){
			SingleGameEngine e=game.singleGameEngine.clone();
			e.makeMove(game.BSSpoints.get(it), moveTypeBlue);
			//System.out.println("try bss move to "+game.BSSpoints.get(it).x+","+game.BSSpoints.get(it).y);
			if(e.getBlueSurroundingCount()>game.singleGameEngine.getBlueSurroundingCount()){
				game.moveAI=game.BSSpoints.get(it);
				return true;
			}
		}
		return false;
	}

	public boolean isAICanMakeMove(int x,int y,Game game,int templateType){
		if(game.singleGameEngine.canMakeMove(x, y)){
			//move by express surround security 2 (максимизация захватываемой территории)
			Point p1=getExpressSurroundSecurity2Move(game, x, y);
			if(p1.x!=x||p1.y!=y){
				game.moveAI=p1;
				stat.moveStatMas.addMoveStat(MoveStatType.EXPRESS_SURROUND_SECURITY_2,false);
				return true;
			}
			
			//move by express surround security 1
			if(templateType!=-1&templateType!=5){//для макросов (templateType=-1) and BST не искать по SurSec1
				if(Math.abs(x-game.singleGameEngine.lastPoint.x)<2&&Math.abs(y-game.singleGameEngine.lastPoint.y)<2){
					Point p=getExpressSurroundSecurity1Move(game, new Point(x,y));
					if(p.x!=x||p.y!=y){
						game.moveAI=p;
						stat.moveStatMas.addMoveStat(MoveStatType.EXPRESS_SURROUND_SECURITY_1,false);
						return true;
					}
				}
			}
			
			game.moveAI.x=x;
			game.moveAI.y=y;
			return true;
		}else return false;			
	}

	private boolean isMoveByMakros(Game game,Point recommendedMove){//можно ли найти ответный ход в макросе, переход по всему списку сохраненных макросов	
		//System.out.println("try find size="+game.makrosApplicationInGame.size());
		for(int i=game.makrosApplicationInGame.size()-1;i>=0;i--){
			MakrosApplicationInGame ma=game.makrosApplicationInGame.get(i);
			/*if(!ma.isEnabled){
				game.makrosApplicationInGame.remove(it);
				continue;
			}*/

			//int x=game.singleGameEngine.lastX,y=game.singleGameEngine.lastY;
			
			boolean isMoveOnlyByDefault=false;//если ход синего делается вдали от макроса, то поиск хода ИИ только в default
			if(Math.abs(ma.center.x-game.singleGameEngine.lastPoint.x)>7||Math.abs(ma.center.y-game.singleGameEngine.lastPoint.y)>7){isMoveOnlyByDefault=true;}
			
			try{
				if(ma.isExistsLevelMove(game.singleGameEngine.lastPoint,isMoveOnlyByDefault,recommendedMove)){
					//<перехват макросов, когда один кончается, а другой из другого шаблона начинается>
					/*
					try{if((x<7&&y>5&&y<game.sizeY-5)||(x>game.sizeX-6&&y>5&&y<game.sizeY-5)||(y<7&&x>5&&x<game.sizeX-5)||(y>game.sizeY-6&&x>5&&x<game.sizeX-5)){
						protocol.templateEngine.getPointIfEqualsLikeArea(protocol,game,TemplateType.SQUARE_SIDE,p,true);
					}}catch(Exception e){}
					try{protocol.templateEngine.getPointIfEqualsLikeArea(protocol,game,TemplateType.BLUE_SURROUND,p,true);}catch(Exception e){}
					try{protocol.templateEngine.getPointIfEqualsLikeArea(protocol,game,TemplateType.RED_SURROUND,p,true);}catch(Exception e){}
					try{protocol.templateEngine.getPointIfEqualsLikeArea(protocol,game,TemplateType.SQUARE,p,true);}catch(Exception e){}
					try{protocol.templateEngine.getPointIfEqualsLikeArea(protocol,game,TemplateType.WALL_DESTROY,getRedPointsForTemplateSearch(game,x,y),true);}catch(Exception e){}
					try{protocol.templateEngine.getPointIfEqualsLikeArea(protocol,game,TemplateType.RED_ATTACK,getRedPointsForTemplateSearch(game,x,y),true);}catch(Exception e){}
					*/
					//</перехват макросов, когда один кончается, а другой начинается>
					
					game.moveAI=ma.transformAIPoint;
					if(isAICanMakeMove(game.moveAI.x,game.moveAI.y,game,-1)){
						if(templateEngine.foundedNumber!=0){
							templateEngine.foundedNumber=templateEngine.getBaseIndexByTemplateIndex(ma.makros.templateIndex);
							templateEngine.foundedIndex=ma.templateIndex;
							templateEngine.foundedTemplateType=ma.templateType;
						}
						stat.moveStatMas.addMoveStat(TemplateType.getMoveStatType(ma.templateType),true);
						//if(!isMakrosEnabled(game,ma))game.makrosApplicationInGame.remove(it);
						return true;				
					}
					
					//System.out.println("remove makros by last ai, not applic, centerPoint="+ma.center.toString());
					game.makrosApplicationInGame.remove(i);
					
				}else{
					//не удалять 'ma', если ход singleGameEngine.lastPoint в ANY или CYAN точки
					/*if(Math.abs(ma.center.x-game.singleGameEngine.lastPoint.x)<5&Math.abs(ma.center.y-game.singleGameEngine.lastPoint.y)<5){
						System.out.println("remove makros by last ai, so close, centerPoint="+ma.center.toString());
						game.makrosApplicationInGame.remove(i);
					}*/
				}
			}catch(Exception e){return false;}//обработка исключения, если макрос применим за пределами поля	
		}
		return false;
	}

	private boolean isMoveByWall(Game game,int x,int y,Point recommendedMove){
		//System.out.println("move by wall "+x+","+y);
		//if(x,y)возле границ, то использовать WALL_SIDE, т.к. обычный WALL у края может давать неоптимальные ходы
		if(x<8||x>game.sizeX-7||y<8||y>game.sizeY-7){
			if(templateEngine.getPointIfEqualsLikeAreaByPoint(this,game,TemplateType.templateTypeWALL_SIDE,new Point(x,y),game.fieldState,recommendedMove).x!=-1)return true;
			if(x>4&y>4&x<game.sizeX-3&y<game.sizeY-3){
				if(templateEngine.getPointIfEqualsLikeAreaByPoint(this,game,TemplateType.templateTypeWALL,new Point(x,y),game.fieldState,recommendedMove).x!=-1)return true;
			}		
		}else{
			if(templateEngine.getPointIfEqualsLikeAreaByPoint(this,game,TemplateType.templateTypeWALL,new Point(x,y),game.fieldState,recommendedMove).x!=-1)return true;
		}	
		return false;
	}
	
	/*
	 
	 ExpressSurroundSecurity1 создан на основе TrajectoryDepth1 для окружения синих точек в случаях,
	 1. когда красная точка ходит в область, не окружая ее, но ходом в другое место она может окружить эту область
	 2. когда красная точка ходит в ответ, не окружая последнюю синию, но ходом в другое место может окружить синию
	 Работает только в случае, когда последняя синяя и ожидаемая красная точка (результат расчета шаблона или другим способом) 
	 находятся на расстоянии 1 друг от друга,
	 и когда ожидаемая красная точка (результат расчета шаблона или другим способом)
	 близка к точке, входящей в окружение, полученное ходом из точки, полученной рассчетом из ExpressSurroundSecurity
	 
	*/

	public Point getExpressSurroundSecurity1Move(Game game,Point nextRed){
		int sign=templateDotTypeRED;
		
		SingleGameEngine singleGameEngine = game.singleGameEngine.clone();
		singleGameEngine.makeMove(nextRed,moveTypeRed);	
		if(game.singleGameEngine.getRedSurroundingCount()<singleGameEngine.getRedSurroundingCount())return nextRed;
		
		int scoreMax=game.singleGameEngine.getRedScore()-game.singleGameEngine.getBlueScore();
		Point AImove=new Point(nextRed.x,nextRed.y);
		int moveMax=0;
		
		for(int x=1;x<=game.sizeX;x++){for(int y=1;y<=game.sizeY;y++){
			if(!isCanMoveForSurroundSecurity(game,x,y,sign))continue;
			singleGameEngine = game.singleGameEngine.clone();
			singleGameEngine.makeMove(new Point(x,y),moveTypeRed);		
			if(
					singleGameEngine.getBlueSurroundingCount()<=game.singleGameEngine.getBlueSurroundingCount()
					&&
					singleGameEngine.getRedSurroundingCount()>game.singleGameEngine.getRedSurroundingCount()				
					
					){
					for(int i=0;i<singleGameEngine.field[x][y].masterSurrounding.path.size();i++){
						if(Math.abs(nextRed.x-singleGameEngine.field[x][y].masterSurrounding.path.get(i).x)<2&&Math.abs(nextRed.y-singleGameEngine.field[x][y].masterSurrounding.path.get(i).y)<2){
							
							int move=0;
							int[][] field=singleGameEngine.getTerrytoryState();
							for(int i1=0;i1<game.sizeX;i1++){for(int j=0;j<game.sizeY;j++){
								if(field[i1][j]==sign){move++;}
							}}				
							if(scoreMax==(singleGameEngine.getRedScore()-singleGameEngine.getBlueScore())){
								if(move>moveMax){
									AImove=new Point(x,y);
									moveMax=move;
								}
							}else{
								scoreMax=singleGameEngine.getRedScore()-singleGameEngine.getBlueScore();
								AImove=new Point(x,y);
								moveMax=move;
							}						
						}
					}				
				}
		}}
		
		return AImove;
		
	}

	/*

	ExpressSurroundSecurity2 создан на основе TrajectoryDepth1 для окружения синих точек в случае,
	1. когда красная точка ходит в область, но ходом в другое место эта точка входила бы в эту область
	для максимизации захваченной территории

	*/

	public Point getExpressSurroundSecurity2Move(Game game,int nextRedX,int nextRedY){
		int sign=templateDotTypeRED;
		
		int scoreMax=game.singleGameEngine.getRedScore()-game.singleGameEngine.getBlueScore();	
		Point AImove=new Point(nextRedX,nextRedY);
		int moveMax=0;
		
		for(int x=1;x<=game.sizeX;x++){for(int y=1;y<=game.sizeY;y++){
			if(!isCanMoveForSurroundSecurity(game,x,y,sign))continue;
			if(x==nextRedX&&y==nextRedY)continue;
			SingleGameEngine singleGameEngine = game.singleGameEngine.clone();

			singleGameEngine.makeMove(new Point(x,y),moveTypeRed);
			if(!singleGameEngine.canMakeMove(nextRedX,nextRedY)){
				int move=0;
				int[][] field=singleGameEngine.getTerrytoryState();
				for(int i1=0;i1<game.sizeX;i1++){for(int j=0;j<game.sizeY;j++){
					if(field[i1][j]==sign){move++;}
				}}				
				if(scoreMax==(singleGameEngine.getRedScore()-singleGameEngine.getBlueScore())){
					if(move>moveMax){
						AImove=new Point(x,y);
						moveMax=move;
					}
				}else{
					scoreMax=singleGameEngine.getRedScore()-singleGameEngine.getBlueScore();
					AImove=new Point(x,y);
					moveMax=move;
				}
			}
		}}
		
		return AImove;
		
	}

	private boolean isCanMoveForSurroundSecurity(Game game,int x,int y,int sign){	
		if(game.fieldState[x-1][y-1]!=templateDotTypeNULL)return false;
		
		//если одна своя точка вокруг целевой точки, то окружение будет невозможно
		int move=0;
		try{if(game.fieldState[x-2][y-2]==sign)move++;}catch(Exception e){}
		try{if(game.fieldState[x-1][y-2]==sign)move++;}catch(Exception e){}
		try{if(game.fieldState[x][y-2]==sign)move++;}catch(Exception e){}
		try{if(game.fieldState[x-2][y-1]==sign)move++;}catch(Exception e){}
		try{if(game.fieldState[x][y-1]==sign)move++;}catch(Exception e){}
		try{if(game.fieldState[x-2][y]==sign)move++;}catch(Exception e){}
		try{if(game.fieldState[x-1][y]==sign)move++;}catch(Exception e){}
		try{if(game.fieldState[x][y]==sign)move++;}catch(Exception e){}	
		if(move<2)return false;					
		
		return true;
	}

	public boolean isEndGame(Game game,int moveWin){	
		
		game.singleGameEngine.setTerrytoryState(game);
		int scoreWin=0;
		int empty=0;
		int scoreLos=0;
		
		if(moveWin==moveTypeBlue)scoreWin=game.singleGameEngine.getBlueScore();
		if(moveWin==moveTypeRed)scoreWin=game.singleGameEngine.getRedScore();
		                                                   
		SingleGameEngine singleGameEngine=game.singleGameEngine.clone();
		
		for(int x=1;x<=game.sizeX;x++){for(int y=1;y<=game.sizeY;y++){		
			if(game.fieldTerrytoryState[x-1][y-1]!=templateDotTypeNULL)continue;		
			if(moveWin==moveTypeBlue)singleGameEngine.makeMove(new Point(x,y),moveTypeRed);
			if(moveWin==moveTypeRed)singleGameEngine.makeMove(new Point(x,y),moveTypeBlue);
		}}
		
		if(scoreWin<=getScoreForSurroundSecurity(singleGameEngine,moveWin)){return false;}
		
		for(;;){
			empty=0;
			int[][] field=singleGameEngine.getFieldState();
			scoreLos=getScoreForSurroundSecurity(singleGameEngine,moveWin);
			for(int x=1;x<=game.sizeX;x++){for(int y=1;y<=game.sizeY;y++){
				if(empty>0){break;}
				if(field[x-1][y-1]==templateDotTypeNULL){
					empty++;
					
					if(moveWin==moveTypeBlue)singleGameEngine.makeMove(new Point(x,y),moveTypeRed);
					if(moveWin==moveTypeRed)singleGameEngine.makeMove(new Point(x,y),moveTypeBlue);
					
					if(scoreWin<=getScoreForSurroundSecurity(singleGameEngine,moveWin)){return false;}
				}		
			}}		
			if(moveWin==moveTypeBlue&&scoreLos==getScoreForSurroundSecurity(singleGameEngine,moveWin)&&scoreWin>getScoreForSurroundSecurity(singleGameEngine,moveWin)){
				field=singleGameEngine.getFieldState();
				for(int x=1;x<=game.sizeX;x++){for(int y=1;y<=game.sizeY;y++){		
					if(field[x-1][y-1]!=templateDotTypeNULL)continue;
					singleGameEngine.makeMove(new Point(x,y),moveTypeRed);
				}}				
				break;
			}
			if(moveWin==moveTypeRed&&scoreLos==getScoreForSurroundSecurity(singleGameEngine,moveWin)&&scoreWin>getScoreForSurroundSecurity(singleGameEngine,moveWin)){
				field=singleGameEngine.getFieldState();
				for(int x=1;x<=game.sizeX;x++){for(int y=1;y<=game.sizeY;y++){		
					if(field[x-1][y-1]!=templateDotTypeNULL)continue;
					singleGameEngine.makeMove(new Point(x,y),moveTypeBlue);
				}}				
				break;
			}
			if(empty==0){break;}
		}
		
		if(scoreWin>getScoreForSurroundSecurity(singleGameEngine,moveWin)){return true;}
		return false;		
	}

	private int getScoreForSurroundSecurity(SingleGameEngine singleGameEngine,int moveType){
		if(moveType==moveTypeBlue){return singleGameEngine.getRedScore();}
		else return singleGameEngine.getBlueScore();
	}
	
	public void wallsSearch(Game game){
		//System.out.println("==========================");
		
		game.fieldOfWalls=new ArrayList<String>();//[game.sizeX][game.sizeY];
		for(int i=0;i<game.sizeX;i++)for(int j=0;j<game.sizeY;j++)game.fieldOfWalls.add("N");
		
		game.wallRed=new ArrayList<Wall>();
		game.wallBlue=new ArrayList<Wall>();
		game.crossCount=0;
		
		//поиск скрестов и добавление стен
		for(int i=0;i<game.sizeX-1;i++){for(int j=0;j<game.sizeY-1;j++){
			if(game.fieldState[i][j]==templateDotTypeRED&&game.fieldState[i+1][j+1]==templateDotTypeRED&&game.fieldState[i+1][j]==templateDotTypeBLUE&&game.fieldState[i][j+1]==templateDotTypeBLUE){			
				game.crossCount++;			
				addNewWallPoints(game,i, j,"R");
				addNewWallPoints(game,i+1, j,"B");			
				addNewWallPoints(game,i+1, j+1,"R");
				addNewWallPoints(game,i, j+1,"B");			
			}else if((game.fieldState[i][j]==templateDotTypeBLUE&&game.fieldState[i+1][j+1]==templateDotTypeBLUE&&game.fieldState[i+1][j]==templateDotTypeRED&&game.fieldState[i][j+1]==templateDotTypeRED)){
				game.crossCount++;
				addNewWallPoints(game,i+1, j,"R");
				addNewWallPoints(game,i, j,"B");
				addNewWallPoints(game,i, j+1,"R");
				addNewWallPoints(game,i+1, j+1,"B");
			}
		}}
		
		if(game.crossCount==0){//добавление точек, если нет скрестов
			Point point=getCentralPointForWallSearch(game,templateDotTypeRED);
			if(point!=null)addNewWallPoints(game,point.x, point.y,"R");
			point=getCentralPointForWallSearch(game,templateDotTypeBLUE);
			if(point!=null)addNewWallPoints(game,point.x, point.y,"B");	
		}
		
		//искать близкие точки для стен и их добавление в WallPoints
		for(int i=0;i<game.wallBlue.size();i++){
			searchWallPoints2(game,game.wallBlue.get(i),templateDotTypeBLUE);
		}
		for(int i=0;i<game.wallRed.size();i++){
			searchWallPoints2(game,game.wallRed.get(i),templateDotTypeRED);
		}
		
		//искать отдаленные (abstract) blue точки
		for(int i=0;i<game.sizeX;i++)for(int j=0;j<game.sizeY;j++){
			if(game.fieldOfWalls.get(i*game.sizeY+j).equals("N")&&game.fieldState[i][j]==templateDotTypeBLUE){
				for(int l=3;l<20;l++){	
					if(!game.fieldOfWalls.get(i*game.sizeY+j).equals("N")){
						//System.out.println("abstract point added; koord="+(i+1)+";"+(j+1));
						break;
					}
					for(int c=i-l;c<=i+l;c++){
						try{if(game.fieldOfWalls.get(c*game.sizeY+j-l).startsWith("PB")||game.fieldOfWalls.get(c*game.sizeY+j-l).startsWith("AB")){
							int wallIndex=new Integer(game.fieldOfWalls.get(c*game.sizeY+j-l).substring(2)).intValue();
							if(isRelatedForWallSearch(game,i,j,c,j-l,game.wallRed.get(wallIndex))){
								game.wallBlue.get(wallIndex).addAbstract(game,i+1, j+1, game.fieldOfWalls);
								break;
							}
						}}catch(Exception e){}
						try{if(game.fieldOfWalls.get(c*game.sizeY+j+l).startsWith("PB")||game.fieldOfWalls.get(c*game.sizeY+j+l).startsWith("AB")){
							int wallIndex=new Integer(game.fieldOfWalls.get(c*game.sizeY+j+l).substring(2)).intValue();
							if(isRelatedForWallSearch(game,i,j,c,j+l,game.wallRed.get(wallIndex))){
								game.wallBlue.get(wallIndex).addAbstract(game,i+1, j+1, game.fieldOfWalls);
								break;
							}
						}}catch(Exception e){}
					}
					for(int c=j-l;c<=j+l;c++){
						try{if(game.fieldOfWalls.get((i-l)*game.sizeY+c).startsWith("PB")||game.fieldOfWalls.get((i-l)*game.sizeY+c).startsWith("AB")){
							int wallIndex=new Integer(game.fieldOfWalls.get((i-l)*game.sizeY+c).substring(2)).intValue();
							if(isRelatedForWallSearch(game,i,j,i-l,c,game.wallRed.get(wallIndex))){
								game.wallBlue.get(wallIndex).addAbstract(game,i+1, j+1, game.fieldOfWalls);
								break;
						}
						}}catch(Exception e){}
						try{if(game.fieldOfWalls.get((i+l)*game.sizeY+c).startsWith("PB")||game.fieldOfWalls.get((i+l)*game.sizeY+c).startsWith("AB")){
							int wallIndex=new Integer(game.fieldOfWalls.get((i+l)*game.sizeY+c).substring(2)).intValue();
							if(isRelatedForWallSearch(game,i,j,i+l,c,game.wallBlue.get(wallIndex))){
								game.wallBlue.get(wallIndex).addAbstract(game,i+1, j+1, game.fieldOfWalls);
								break;
							}
						}}catch(Exception e){}
					}
				}
			}
		}
		
		//искать отдаленные (abstract) red точки
		for(int i=0;i<game.sizeX;i++)for(int j=0;j<game.sizeY;j++){
			if(game.fieldOfWalls.get(i*game.sizeY+j).equals("N")&&game.fieldState[i][j]==templateDotTypeRED){
				for(int l=3;l<20;l++){	
					if(!game.fieldOfWalls.get(i*game.sizeY+j).equals("N")){
						//System.out.println("abstract point added; koord="+(i+1)+";"+(j+1));
						break;
					}
					for(int c=i-l;c<=i+l;c++){
						try{if(game.fieldOfWalls.get(c*game.sizeY+j-l).startsWith("PR")||game.fieldOfWalls.get(c*game.sizeY+j-l).startsWith("AR")){
							int wallIndex=new Integer(game.fieldOfWalls.get(c*game.sizeY+j-l).substring(2)).intValue();
							if(isRelatedForWallSearch(game,i,j,c,j-l,game.wallRed.get(wallIndex))){
								game.wallRed.get(wallIndex).addAbstract(game,i+1, j+1, game.fieldOfWalls);
								break;
							}
						}}catch(Exception e){}
						try{if(game.fieldOfWalls.get(c*game.sizeY+j+l).startsWith("PR")||game.fieldOfWalls.get(c*game.sizeY+j+l).startsWith("AR")){
							int wallIndex=new Integer(game.fieldOfWalls.get(c*game.sizeY+j+l).substring(2)).intValue();
							if(isRelatedForWallSearch(game,i,j,c,j+l,game.wallRed.get(wallIndex))){
								game.wallRed.get(wallIndex).addAbstract(game,i+1, j+1, game.fieldOfWalls);
								break;
							}
						}}catch(Exception e){}
					}
					for(int c=j-l;c<=j+l;c++){
						try{if(game.fieldOfWalls.get((i-l)*game.sizeY+c).startsWith("PR")||game.fieldOfWalls.get((i-l)*game.sizeY+c).startsWith("AR")){
							int wallIndex=new Integer(game.fieldOfWalls.get((i-l)*game.sizeY+c).substring(2)).intValue();
							if(isRelatedForWallSearch(game,i,j,i-l,c,game.wallRed.get(wallIndex))){
								game.wallRed.get(wallIndex).addAbstract(game,i+1, j+1, game.fieldOfWalls);
								break;
						}
						}}catch(Exception e){}
						try{if(game.fieldOfWalls.get((i+l)*game.sizeY+c).startsWith("PR")||game.fieldOfWalls.get((i+l)*game.sizeY+c).startsWith("AR")){
							int wallIndex=new Integer(game.fieldOfWalls.get((i+l)*game.sizeY+c).substring(2)).intValue();
							if(isRelatedForWallSearch(game,i,j,i+l,c,game.wallRed.get(wallIndex))){
								game.wallRed.get(wallIndex).addAbstract(game,i+1, j+1, game.fieldOfWalls);
								break;
							}
						}}catch(Exception e){}
					}
				}
			}
		}
		
		//искать концы стен
		for(int i=0;i<game.wallBlue.size();i++){
			game.wallBlue.get(i).searchWallEnd(game,game.fieldOfWalls);
		}
		for(int i=0;i<game.wallRed.size();i++){
			game.wallRed.get(i).searchWallEnd(game,game.fieldOfWalls);
		}
	}
	
	public int getSmallerRedWallIdx(Game game){
		
		int count=0;//число красных стен около края поля
		for(int i=0;i<game.wallRed.size();i++){
			if(game.wallRed.get(i).isAtSide(game))count++;
		}
		
		if(count==game.wallRed.size())return -1;//если все стенки около края поля
		if(count==game.wallRed.size()-1)//если все стенки кроме одной около края поля
			for(int i=0;i<game.wallRed.size();i++){
				if(!game.wallRed.get(i).isAtSide(game))return i;
			}

		int minLengthFromLastBlue=Integer.MAX_VALUE;//конец какой стены ближе всего к последнему ходу синих (для стен не у края поля)
		
		int smaller=0;
		for(int i=0;i<game.wallRed.size();i++){
			if(!game.wallRed.get(i).isAtSide(game)){
				if(game.wallRed.get(i).getLengthFromLastBlue(game.singleGameEngine.lastPoint)<=minLengthFromLastBlue){
					minLengthFromLastBlue=game.wallRed.get(i).getLengthFromLastBlue(game.singleGameEngine.lastPoint);
					smaller=i;
				}
			};
		}
		//System.out.println("smaller="+smaller);
		//System.out.println("last=("+game.singleGameEngine.lastX+";"+game.singleGameEngine.lastY+"); wall end=("+wallRed.get(smaller).wallEndX+";"+wallRed.get(smaller).wallEndY+"); minLengthFromLastBlue="+minLengthFromLastBlue);
		return smaller;
	}
	
	private void addNewWallPoints(Game game,int x,int y,String sign){	
		if(!game.fieldOfWalls.get(x*game.sizeY+y).equals("N")){return;}
		if(sign.equals("R")){
			game.wallRed.add(new Wall(game,x+1,y+1,game.wallRed.size(),"R",game.fieldOfWalls));
			searchWallPoints1(game,game.wallRed.get(game.wallRed.size()-1),templateDotTypeRED);
		}else{
			game.wallBlue.add(new Wall(game,x+1,y+1,game.wallBlue.size(),"B",game.fieldOfWalls));
			searchWallPoints1(game,game.wallBlue.get(game.wallBlue.size()-1),templateDotTypeBLUE);
		}
	}

	private void searchWallPoints1(Game game,Wall wall,int sign){
		int antiSign;
		if(sign==templateDotTypeRED)antiSign=templateDotTypeBLUE;else antiSign=templateDotTypeRED;
		//пройти существующие точки и добавить соседние
		for(int i=0;i<wall.points.size();i++){
			int x=wall.points.get(i).x-1,y=wall.points.get(i).y-1;
			//соседние точки			
			try{if(game.fieldState[x-1][y-1]==sign&&(game.fieldState[x-1][y]!=antiSign||game.fieldState[x][y-1]!=antiSign)){wall.addPoint(game,x,y,game.fieldOfWalls);}}catch(Exception e){}
			try{if(game.fieldState[x-1][y+1]==sign&&(game.fieldState[x-1][y]!=antiSign||game.fieldState[x][y+1]!=antiSign)){wall.addPoint(game,x,y+2,game.fieldOfWalls);}}catch(Exception e){}
			try{if(game.fieldState[x+1][y-1]==sign&&(game.fieldState[x+1][y]!=antiSign||game.fieldState[x][y-1]!=antiSign)){wall.addPoint(game,x+2,y,game.fieldOfWalls);}}catch(Exception e){}
			try{if(game.fieldState[x+1][y+1]==sign&&(game.fieldState[x+1][y]!=antiSign||game.fieldState[x][y+1]!=antiSign)){wall.addPoint(game,x+2,y+2,game.fieldOfWalls);}}catch(Exception e){}
			try{if(game.fieldState[x+1][y]==sign){wall.addPoint(game,x+2,y+1,game.fieldOfWalls);}}catch(Exception e){}
			try{if(game.fieldState[x][y+1]==sign){wall.addPoint(game,x+1,y+2,game.fieldOfWalls);}}catch(Exception e){}
			try{if(game.fieldState[x-1][y]==sign){wall.addPoint(game,x,y+1,game.fieldOfWalls);}}catch(Exception e){}
			try{if(game.fieldState[x][y-1]==sign){wall.addPoint(game,x+1,y,game.fieldOfWalls);}}catch(Exception e){}
		}
	}
	
	private void searchWallPoints2(Game game,Wall wall, int sign){
		int antiSign;
		if(sign==templateDotTypeRED)antiSign=templateDotTypeBLUE;else antiSign=templateDotTypeRED;
		//пройти существующие точки и добавить соседние
		for(int i=0;i<wall.points.size();i++){
			int x=wall.points.get(i).x-1,y=wall.points.get(i).y-1;
			
			//соседние точки			
			try{if(game.fieldState[x-1][y-1]==sign&&(game.fieldState[x-1][y]!=antiSign||game.fieldState[x][y-1]!=antiSign)){wall.addPoint(game,x,y,game.fieldOfWalls);}}catch(Exception e){}
			try{if(game.fieldState[x-1][y+1]==sign&&(game.fieldState[x-1][y]!=antiSign||game.fieldState[x][y+1]!=antiSign)){wall.addPoint(game,x,y+2,game.fieldOfWalls);}}catch(Exception e){}
			try{if(game.fieldState[x+1][y-1]==sign&&(game.fieldState[x+1][y]!=antiSign||game.fieldState[x][y-1]!=antiSign)){wall.addPoint(game,x+2,y,game.fieldOfWalls);}}catch(Exception e){}
			try{if(game.fieldState[x+1][y+1]==sign&&(game.fieldState[x+1][y]!=antiSign||game.fieldState[x][y+1]!=antiSign)){wall.addPoint(game,x+2,y+2,game.fieldOfWalls);}}catch(Exception e){}
			try{if(game.fieldState[x+1][y]==sign){wall.addPoint(game,x+2,y+1,game.fieldOfWalls);}}catch(Exception e){}
			try{if(game.fieldState[x][y+1]==sign){wall.addPoint(game,x+1,y+2,game.fieldOfWalls);}}catch(Exception e){}
			try{if(game.fieldState[x-1][y]==sign){wall.addPoint(game,x,y+1,game.fieldOfWalls);}}catch(Exception e){}
			try{if(game.fieldState[x][y-1]==sign){wall.addPoint(game,x+1,y,game.fieldOfWalls);}}catch(Exception e){}
			
			//дальние точки, удаленные на 2
			try{if(game.fieldState[x+1][y]==templateDotTypeNULL&&game.fieldState[x+2][y]==sign){wall.addPoint(game,x+3,y+1,game.fieldOfWalls);wall.addConnection(game,x+2,y+1,game.fieldOfWalls);}}catch(Exception e){}
			try{if(game.fieldState[x][y+1]==templateDotTypeNULL&&game.fieldState[x][y+2]==sign){wall.addPoint(game,x+1,y+3,game.fieldOfWalls);wall.addConnection(game,x+1,y+2,game.fieldOfWalls);}}catch(Exception e){}
			try{if(game.fieldState[x-1][y]==templateDotTypeNULL&&game.fieldState[x-2][y]==sign){wall.addPoint(game,x-1,y+1,game.fieldOfWalls);wall.addConnection(game,x,y+1,game.fieldOfWalls);}}catch(Exception e){}
			try{if(game.fieldState[x][y-1]==templateDotTypeNULL&&game.fieldState[x][y-2]==sign){wall.addPoint(game,x+1,y-1,game.fieldOfWalls);wall.addConnection(game,x+1,y,game.fieldOfWalls);}}catch(Exception e){}	
			//дальние точки, удаленные на 2 наискось
			try{if(game.fieldState[x+1][y+1]==templateDotTypeNULL&&game.fieldState[x+2][y+2]==sign){wall.addPoint(game,x+3,y+3,game.fieldOfWalls);wall.addConnection(game,x+2,y+2,game.fieldOfWalls);}}catch(Exception e){}
			try{if(game.fieldState[x-1][y+1]==templateDotTypeNULL&&game.fieldState[x-2][y+2]==sign){wall.addPoint(game,x-1,y+3,game.fieldOfWalls);wall.addConnection(game,x,y+2,game.fieldOfWalls);}}catch(Exception e){}
			try{if(game.fieldState[x+1][y-1]==templateDotTypeNULL&&game.fieldState[x+2][y-2]==sign){wall.addPoint(game,x+3,y-1,game.fieldOfWalls);wall.addConnection(game,x+2,y,game.fieldOfWalls);}}catch(Exception e){}
			try{if(game.fieldState[x-1][y-1]==templateDotTypeNULL&&game.fieldState[x-2][y-2]==sign){wall.addPoint(game,x-1,y-1,game.fieldOfWalls);wall.addConnection(game,x,y,game.fieldOfWalls);}}catch(Exception e){}
			//дальние точки, удаленные на 2 конем
			try{if((game.fieldState[x+1][y]==templateDotTypeNULL||game.fieldState[x+1][y-1]==templateDotTypeNULL)&&game.fieldState[x+2][y-1]==sign){wall.addPoint(game,x+3,y,game.fieldOfWalls);if(game.fieldState[x+1][y]==templateDotTypeNULL)wall.addConnection(game,x+2,y+1,game.fieldOfWalls);if(game.fieldState[x+1][y-1]==templateDotTypeNULL)wall.addConnection(game,x+2,y,game.fieldOfWalls);}}catch(Exception e){}
			try{if((game.fieldState[x+1][y]==templateDotTypeNULL||game.fieldState[x+1][y+1]==templateDotTypeNULL)&&game.fieldState[x+2][y+1]==sign){wall.addPoint(game,x+3,y+2,game.fieldOfWalls);if(game.fieldState[x+1][y]==templateDotTypeNULL)wall.addConnection(game,x+2,y+1,game.fieldOfWalls);if(game.fieldState[x+1][y+1]==templateDotTypeNULL)wall.addConnection(game,x+2,y+2,game.fieldOfWalls);}}catch(Exception e){}
			try{if((game.fieldState[x][y+1]==templateDotTypeNULL||game.fieldState[x+1][y+1]==templateDotTypeNULL)&&game.fieldState[x+1][y+2]==sign){wall.addPoint(game,x+2,y+3,game.fieldOfWalls);if(game.fieldState[x][y+1]==templateDotTypeNULL)wall.addConnection(game,x+1,y+2,game.fieldOfWalls);if(game.fieldState[x+1][y+1]==templateDotTypeNULL)wall.addConnection(game,x+2,y+2,game.fieldOfWalls);}}catch(Exception e){}
			try{if((game.fieldState[x][y+1]==templateDotTypeNULL||game.fieldState[x-1][y+1]==templateDotTypeNULL)&&game.fieldState[x-1][y+2]==sign){wall.addPoint(game,x,y+3,game.fieldOfWalls);if(game.fieldState[x][y+1]==templateDotTypeNULL)wall.addConnection(game,x+1,y+2,game.fieldOfWalls);if(game.fieldState[x-1][y+1]==templateDotTypeNULL)wall.addConnection(game,x,y+2,game.fieldOfWalls);}}catch(Exception e){}
			try{if((game.fieldState[x-1][y]==templateDotTypeNULL||game.fieldState[x-1][y+1]==templateDotTypeNULL)&&game.fieldState[x-2][y+1]==sign){wall.addPoint(game,x-1,y+2,game.fieldOfWalls);if(game.fieldState[x-1][y]==templateDotTypeNULL)wall.addConnection(game,x,y+1,game.fieldOfWalls);if(game.fieldState[x-1][y+1]==templateDotTypeNULL)wall.addConnection(game,x,y+2,game.fieldOfWalls);}}catch(Exception e){}
			try{if((game.fieldState[x-1][y]==templateDotTypeNULL||game.fieldState[x-1][y-1]==templateDotTypeNULL)&&game.fieldState[x-2][y-1]==sign){wall.addPoint(game,x-1,y,game.fieldOfWalls);if(game.fieldState[x-1][y]==templateDotTypeNULL)wall.addConnection(game,x,y+1,game.fieldOfWalls);if(game.fieldState[x-1][y-1]==templateDotTypeNULL)wall.addConnection(game,x,y,game.fieldOfWalls);}}catch(Exception e){}
			try{if((game.fieldState[x][y-1]==templateDotTypeNULL||game.fieldState[x-1][y-1]==templateDotTypeNULL)&&game.fieldState[x-1][y-2]==sign){wall.addPoint(game,x,y-1,game.fieldOfWalls);if(game.fieldState[x][y-1]==templateDotTypeNULL)wall.addConnection(game,x+1,y,game.fieldOfWalls);if(game.fieldState[x-1][y-1]==templateDotTypeNULL)wall.addConnection(game,x,y,game.fieldOfWalls);}}catch(Exception e){}
			try{if((game.fieldState[x][y-1]==templateDotTypeNULL||game.fieldState[x+1][y-1]==templateDotTypeNULL)&&game.fieldState[x+1][y-2]==sign){wall.addPoint(game,x+2,y-1,game.fieldOfWalls);if(game.fieldState[x][y-1]==templateDotTypeNULL)wall.addConnection(game,x+1,y,game.fieldOfWalls);if(game.fieldState[x+1][y-1]==templateDotTypeNULL)wall.addConnection(game,x+2,y,game.fieldOfWalls);}}catch(Exception e){}
		
			//дальние точки, удаленные на 3
			try{if(game.fieldState[x+1][y]==templateDotTypeNULL&&game.fieldState[x+2][y]==templateDotTypeNULL&&game.fieldState[x+3][y]==sign){wall.addPoint(game,x+4,y+1,game.fieldOfWalls);wall.addConnection(game,x+3,y+1,game.fieldOfWalls);wall.addConnection(game,x+2,y+1,game.fieldOfWalls);}}catch(Exception e){}
			try{if(game.fieldState[x][y+1]==templateDotTypeNULL&&game.fieldState[x][y+2]==templateDotTypeNULL&&game.fieldState[x][y+3]==sign){wall.addPoint(game,x+1,y+4,game.fieldOfWalls);wall.addConnection(game,x+1,y+3,game.fieldOfWalls);wall.addConnection(game,x+1,y+2,game.fieldOfWalls);}}catch(Exception e){}
			try{if(game.fieldState[x-1][y]==templateDotTypeNULL&&game.fieldState[x-2][y]==templateDotTypeNULL&&game.fieldState[x-3][y]==sign){wall.addPoint(game,x-2,y+1,game.fieldOfWalls);wall.addConnection(game,x-1,y+1,game.fieldOfWalls);wall.addConnection(game,x,y+1,game.fieldOfWalls);}}catch(Exception e){}
			try{if(game.fieldState[x][y-1]==templateDotTypeNULL&&game.fieldState[x][y-2]==templateDotTypeNULL&&game.fieldState[x][y-3]==sign){wall.addPoint(game,x+1,y-2,game.fieldOfWalls);wall.addConnection(game,x+1,y-1,game.fieldOfWalls);wall.addConnection(game,x+1,y,game.fieldOfWalls);}}catch(Exception e){}
			//дальние точки, удаленные на 4
			try{if(game.fieldState[x+1][y]==templateDotTypeNULL&&game.fieldState[x+2][y]==templateDotTypeNULL&&game.fieldState[x+3][y]==templateDotTypeNULL&&game.fieldState[x+4][y]==sign){wall.addPoint(game,x+5,y+1,game.fieldOfWalls);wall.addConnection(game,x+3,y+1,game.fieldOfWalls);wall.addConnection(game,x+2,y+1,game.fieldOfWalls);wall.addConnection(game,x+4,y+1,game.fieldOfWalls);}}catch(Exception e){}
			try{if(game.fieldState[x][y+1]==templateDotTypeNULL&&game.fieldState[x][y+2]==templateDotTypeNULL&&game.fieldState[x][y+3]==templateDotTypeNULL&&game.fieldState[x][y+4]==sign){wall.addPoint(game,x+1,y+5,game.fieldOfWalls);wall.addConnection(game,x+1,y+3,game.fieldOfWalls);wall.addConnection(game,x+1,y+2,game.fieldOfWalls);wall.addConnection(game,x+1,y+4,game.fieldOfWalls);}}catch(Exception e){}
			try{if(game.fieldState[x-1][y]==templateDotTypeNULL&&game.fieldState[x-2][y]==templateDotTypeNULL&&game.fieldState[x-3][y]==templateDotTypeNULL&&game.fieldState[x-4][y]==sign){wall.addPoint(game,x-3,y+1,game.fieldOfWalls);wall.addConnection(game,x-1,y+1,game.fieldOfWalls);wall.addConnection(game,x,y+1,game.fieldOfWalls);wall.addConnection(game,x-2,y+1,game.fieldOfWalls);}}catch(Exception e){}
			try{if(game.fieldState[x][y-1]==templateDotTypeNULL&&game.fieldState[x][y-2]==templateDotTypeNULL&&game.fieldState[x][y-3]==templateDotTypeNULL&&game.fieldState[x][y-4]==sign){wall.addPoint(game,x+1,y-3,game.fieldOfWalls);wall.addConnection(game,x+1,y-1,game.fieldOfWalls);wall.addConnection(game,x+1,y,game.fieldOfWalls);wall.addConnection(game,x+1,y-2,game.fieldOfWalls);}}catch(Exception e){}
			//дальние точки, удаленные на 3 конем
			try{if((game.fieldState[x+1][y]==templateDotTypeNULL||game.fieldState[x+1][y-1]==templateDotTypeNULL)&&(game.fieldState[x+2][y]==templateDotTypeNULL||game.fieldState[x+2][y-1]==templateDotTypeNULL)&&game.fieldState[x+3][y-1]==sign){
				wall.addPoint(game,x+4,y,game.fieldOfWalls);
				if(game.fieldState[x+1][y]==templateDotTypeNULL)wall.addConnection(game,x+2,y+1,game.fieldOfWalls);if(game.fieldState[x+1][y-1]==templateDotTypeNULL)wall.addConnection(game,x+2,y,game.fieldOfWalls);
				if(game.fieldState[x+2][y]==templateDotTypeNULL)wall.addConnection(game,x+3,y+1,game.fieldOfWalls);if(game.fieldState[x+2][y-1]==templateDotTypeNULL)wall.addConnection(game,x+3,y,game.fieldOfWalls);
			}}catch(Exception e){}
			try{if((game.fieldState[x+1][y]==templateDotTypeNULL||game.fieldState[x+1][y+1]==templateDotTypeNULL)&&(game.fieldState[x+2][y]==templateDotTypeNULL||game.fieldState[x+2][y+1]==templateDotTypeNULL)&&game.fieldState[x+3][y+1]==sign){
				wall.addPoint(game,x+4,y+2,game.fieldOfWalls);
				if(game.fieldState[x+1][y]==templateDotTypeNULL)wall.addConnection(game,x+2,y+1,game.fieldOfWalls);if(game.fieldState[x+1][y+1]==templateDotTypeNULL)wall.addConnection(game,x+2,y+2,game.fieldOfWalls);
				if(game.fieldState[x+2][y]==templateDotTypeNULL)wall.addConnection(game,x+3,y+1,game.fieldOfWalls);if(game.fieldState[x+2][y+1]==templateDotTypeNULL)wall.addConnection(game,x+3,y+2,game.fieldOfWalls);
			}}catch(Exception e){}
			try{if((game.fieldState[x][y+1]==templateDotTypeNULL||game.fieldState[x+1][y+1]==templateDotTypeNULL)&&(game.fieldState[x][y+2]==templateDotTypeNULL||game.fieldState[x+1][y+2]==templateDotTypeNULL)&&game.fieldState[x+1][y+3]==sign){
				wall.addPoint(game,x+2,y+4,game.fieldOfWalls);
				if(game.fieldState[x][y+1]==templateDotTypeNULL)wall.addConnection(game,x+1,y+2,game.fieldOfWalls);if(game.fieldState[x+1][y+1]==templateDotTypeNULL)wall.addConnection(game,x+2,y+2,game.fieldOfWalls);
				if(game.fieldState[x][y+2]==templateDotTypeNULL)wall.addConnection(game,x+1,y+3,game.fieldOfWalls);if(game.fieldState[x+1][y+2]==templateDotTypeNULL)wall.addConnection(game,x+2,y+3,game.fieldOfWalls);
			}}catch(Exception e){}
			try{if((game.fieldState[x][y+1]==templateDotTypeNULL||game.fieldState[x-1][y+1]==templateDotTypeNULL)&&(game.fieldState[x][y+2]==templateDotTypeNULL||game.fieldState[x-1][y+2]==templateDotTypeNULL)&&game.fieldState[x-1][y+3]==sign){
				wall.addPoint(game,x,y+4,game.fieldOfWalls);
				if(game.fieldState[x][y+1]==templateDotTypeNULL)wall.addConnection(game,x+1,y+2,game.fieldOfWalls);if(game.fieldState[x-1][y+1]==templateDotTypeNULL)wall.addConnection(game,x,y+2,game.fieldOfWalls);
				if(game.fieldState[x][y+2]==templateDotTypeNULL)wall.addConnection(game,x+1,y+3,game.fieldOfWalls);if(game.fieldState[x-1][y+2]==templateDotTypeNULL)wall.addConnection(game,x,y+3,game.fieldOfWalls);
			}}catch(Exception e){}
			try{if((game.fieldState[x-1][y]==templateDotTypeNULL||game.fieldState[x-1][y+1]==templateDotTypeNULL)&&(game.fieldState[x-2][y]==templateDotTypeNULL||game.fieldState[x-2][y+1]==templateDotTypeNULL)&&game.fieldState[x-3][y+1]==sign){
				wall.addPoint(game,x-2,y+2,game.fieldOfWalls);
				if(game.fieldState[x-1][y]==templateDotTypeNULL)wall.addConnection(game,x,y+1,game.fieldOfWalls);if(game.fieldState[x-1][y+1]==templateDotTypeNULL)wall.addConnection(game,x,y+2,game.fieldOfWalls);
				if(game.fieldState[x-2][y]==templateDotTypeNULL)wall.addConnection(game,x-1,y+1,game.fieldOfWalls);if(game.fieldState[x-2][y+1]==templateDotTypeNULL)wall.addConnection(game,x-1,y+2,game.fieldOfWalls);
			}}catch(Exception e){}
			try{if((game.fieldState[x-1][y]==templateDotTypeNULL||game.fieldState[x-1][y-1]==templateDotTypeNULL)&&(game.fieldState[x-2][y]==templateDotTypeNULL||game.fieldState[x-2][y-1]==templateDotTypeNULL)&&game.fieldState[x-3][y-1]==sign){
				wall.addPoint(game,x-2,y,game.fieldOfWalls);
				if(game.fieldState[x-1][y]==templateDotTypeNULL)wall.addConnection(game,x,y+1,game.fieldOfWalls);if(game.fieldState[x-1][y-1]==templateDotTypeNULL)wall.addConnection(game,x,y,game.fieldOfWalls);
				if(game.fieldState[x-2][y]==templateDotTypeNULL)wall.addConnection(game,x-1,y+1,game.fieldOfWalls);if(game.fieldState[x-2][y-1]==templateDotTypeNULL)wall.addConnection(game,x-1,y,game.fieldOfWalls);
			}}catch(Exception e){}
			try{if((game.fieldState[x][y-1]==templateDotTypeNULL||game.fieldState[x-1][y-1]==templateDotTypeNULL)&&(game.fieldState[x][y-2]==templateDotTypeNULL||game.fieldState[x-1][y-2]==templateDotTypeNULL)&&game.fieldState[x-1][y-3]==sign){
				wall.addPoint(game,x,y-2,game.fieldOfWalls);
				if(game.fieldState[x][y-1]==templateDotTypeNULL)wall.addConnection(game,x+1,y,game.fieldOfWalls);if(game.fieldState[x-1][y-1]==templateDotTypeNULL)wall.addConnection(game,x,y,game.fieldOfWalls);
				if(game.fieldState[x][y-2]==templateDotTypeNULL)wall.addConnection(game,x+1,y-1,game.fieldOfWalls);if(game.fieldState[x-1][y-2]==templateDotTypeNULL)wall.addConnection(game,x,y-1,game.fieldOfWalls);
			}}catch(Exception e){}
			try{if((game.fieldState[x][y-1]==templateDotTypeNULL||game.fieldState[x+1][y-1]==templateDotTypeNULL)&&(game.fieldState[x][y-2]==templateDotTypeNULL||game.fieldState[x+1][y-2]==templateDotTypeNULL)&&game.fieldState[x+1][y-3]==sign){
				wall.addPoint(game,x+2,y-2,game.fieldOfWalls);
				if(game.fieldState[x][y-1]==templateDotTypeNULL)wall.addConnection(game,x+1,y,game.fieldOfWalls);if(game.fieldState[x+1][y-1]==templateDotTypeNULL)wall.addConnection(game,x+2,y,game.fieldOfWalls);
				if(game.fieldState[x][y-2]==templateDotTypeNULL)wall.addConnection(game,x+1,y-1,game.fieldOfWalls);if(game.fieldState[x+1][y-2]==templateDotTypeNULL)wall.addConnection(game,x+2,y-1,game.fieldOfWalls);
			}}catch(Exception e){}
		}
	}
	
	private boolean isRelatedForWallSearch(Game game,int x1,int y1,int x2,int y2,Wall wall){
		
		if(x1==x2){//вертикально
			if(y1<y2){//сверху вниз
				for(int i=y1+1;i<y2;i++)if(game.fieldOfWalls.get(x1*game.sizeY+i).startsWith("P")||game.fieldOfWalls.get(x1*game.sizeY+i).startsWith("A")||game.fieldOfWalls.get(x1*game.sizeY+i).startsWith("C"))return false;
				for(int i=y1+1;i<y2;i++)wall.addLink(game,x1+1, i+1, game.fieldOfWalls);
				return true;
			}else{//снизу вверх
				for(int i=y1-1;i>y2;i--)if(game.fieldOfWalls.get(x1*game.sizeY+i).startsWith("P")||game.fieldOfWalls.get(x1*game.sizeY+i).startsWith("A")||game.fieldOfWalls.get(x1*game.sizeY+i).startsWith("C"))return false;
				for(int i=y1-1;i>y2;i--)wall.addLink(game,x1+1, i+1, game.fieldOfWalls);
				return true;
			}
		}
		else if(y1==y2){//горизонтально
			if(x1<x2){//слева направо
				for(int i=x1+1;i<x2;i++)if(game.fieldOfWalls.get(i*game.sizeY+y1).startsWith("P")||game.fieldOfWalls.get(i*game.sizeY+y1).startsWith("A")||game.fieldOfWalls.get(i*game.sizeY+y1).startsWith("C"))return false;
				for(int i=x1+1;i<x2;i++)wall.addLink(game,i+1, y1+1, game.fieldOfWalls);
				return true;
			}else{//справа налево
				for(int i=x1-1;i>x2;i--)if(game.fieldOfWalls.get(i*game.sizeY+y1).startsWith("P")||game.fieldOfWalls.get(i*game.sizeY+y1).startsWith("A")||game.fieldOfWalls.get(i*game.sizeY+y1).startsWith("C"))return false;
				for(int i=x1-1;i>x2;i--)wall.addLink(game,i+1, y1+1, game.fieldOfWalls);
				return true;
			}
		}
		
		double stepX=(double)Math.abs(x2-x1)/(double)Math.abs(y2-y1);
		double stepY=(double)Math.abs(y2-y1)/(double)Math.abs(x2-x1);
		//System.out.println("stepX="+stepX+"; stepY="+stepY);
		int max;
		if(stepY>stepX){		
			max=Math.abs(y2-y1);
			stepY=1;
		}else{
			max=Math.abs(x2-x1);
			stepX=1;
		}
		if(x2>x1&&y2>y1){
			for(int i=1;i<max;i++){
				if(game.fieldOfWalls.get((int)(x1+stepX*i)*game.sizeY+(int)(y1+stepY*i)).startsWith("P")||
						game.fieldOfWalls.get((int)(x1+stepX*i)*game.sizeY+(int)(y1+stepY*i)).startsWith("A")||
						game.fieldOfWalls.get((int)(x1+stepX*i)*game.sizeY+(int)(y1+stepY*i)).startsWith("C"))
				return false;
			}
			for(int i=1;i<max;i++){
				if(game.fieldState[(int)(x1+stepX*i)][(int)(y1+stepY*i)]==templateDotTypeNULL)wall.addLink(game,(int)(x1+stepX*i)+1,(int)(y1+stepY*i)+1, game.fieldOfWalls);
			}
			return true;
		}
		else if(x2<x1&&y2<y1){
			for(int i=1;i<max;i++){
				if(game.fieldOfWalls.get((int)(x1-stepX*i)*game.sizeY+(int)(y1-stepY*i)).startsWith("P")||
						game.fieldOfWalls.get((int)(x1-stepX*i)*game.sizeY+(int)(y1-stepY*i)).startsWith("A")||
						game.fieldOfWalls.get((int)(x1-stepX*i)*game.sizeY+(int)(y1-stepY*i)).startsWith("C"))
				return false;
			}
			for(int i=1;i<max;i++){
				if(game.fieldState[(int)(x1-stepX*i)][(int)(y1-stepY*i)]==templateDotTypeNULL)wall.addLink(game,(int)(x1-stepX*i)+1,(int)(y1-stepY*i)+1, game.fieldOfWalls);
			}
			return true;
		}
		else if(x2>x1&&y2<y1){
			for(int i=1;i<max;i++){
				if(game.fieldOfWalls.get((int)(x1+stepX*i)*game.sizeY+(int)(y1-stepY*i)).startsWith("P")||
						game.fieldOfWalls.get((int)(x1+stepX*i)*game.sizeY+(int)(y1-stepY*i)).startsWith("A")||
						game.fieldOfWalls.get((int)(x1+stepX*i)*game.sizeY+(int)(y1-stepY*i)).startsWith("C"))
				return false;
			}
			for(int i=1;i<max;i++){
				if(game.fieldState[(int)(x1+stepX*i)][(int)(y1-stepY*i)]==templateDotTypeNULL)wall.addLink(game,(int)(x1+stepX*i)+1,(int)(y1-stepY*i)+1, game.fieldOfWalls);
			}
			return true;
		}
		else if(x2<x1&&y2>y1){
			for(int i=1;i<max;i++){
				if(game.fieldOfWalls.get((int)(x1-stepX*i)*game.sizeY+(int)(y1+stepY*i)).startsWith("P")||
						game.fieldOfWalls.get((int)(x1-stepX*i)*game.sizeY+(int)(y1+stepY*i)).startsWith("A")||
						game.fieldOfWalls.get((int)(x1-stepX*i)*game.sizeY+(int)(y1+stepY*i)).startsWith("C"))
				return false;
			}
			for(int i=1;i<max;i++){
				if(game.fieldState[(int)(x1-stepX*i)][(int)(y1+stepY*i)]==templateDotTypeNULL)wall.addLink(game,(int)(x1-stepX*i)+1,(int)(y1+stepY*i)+1, game.fieldOfWalls);
			}
			return true;
		}
		
		return false;
	}
	
	private Point getCentralPointForWallSearch(Game game,int sign){
		int centralX=(int)(game.sizeX/2.0);
		int centralY=(int)(game.sizeY/2.0);
		
		int depth=(int)(Math.max(game.sizeX, game.sizeY)/2.0);
			
		for(int i=0;i<=depth;i++){//нахождение крайних красных точек на поле
			try{if(game.fieldState[centralX-1-i][centralY-1-i]==sign)return new Point(centralX-i,centralY-i);}catch(Exception e){}
			try{if(game.fieldState[centralX-1-i][centralY-1+i]==sign)return new Point(centralX-i,centralY+i);}catch(Exception e){}
			try{if(game.fieldState[centralX-1+i][centralY-1-i]==sign)return new Point(centralX+i,centralY-i);}catch(Exception e){}
			try{if(game.fieldState[centralX-1+i][centralY-1+i]==sign)return new Point(centralX+i,centralY+i);}catch(Exception e){}
			
			try{if(game.fieldState[centralX-1][centralY-1-i]==sign)return new Point(centralX,centralY-i);}catch(Exception e){}
			try{if(game.fieldState[centralX-1][centralY-1+i]==sign)return new Point(centralX,centralY+i);}catch(Exception e){}
			try{if(game.fieldState[centralX-1-i][centralY-1]==sign)return new Point(centralX-i,centralY);}catch(Exception e){}
			try{if(game.fieldState[centralX-1+i][centralY-1]==sign)return new Point(centralX+i,centralY);}catch(Exception e){}
			
			if(i>1){
				for(int j=1;j<=i-1;j++){
					try{if(game.fieldState[centralX-1-i+j][centralY-1-i]==sign)return new Point(centralX-i+j,centralY-i);}catch(Exception e){}
					try{if(game.fieldState[centralX-1-i][centralY-1+i-j]==sign)return new Point(centralX-i,centralY+i-j);}catch(Exception e){}
					try{if(game.fieldState[centralX-1+i][centralY-1-i+j]==sign)return new Point(centralX+i,centralY-i+j);}catch(Exception e){}
					try{if(game.fieldState[centralX-1+i-j][centralY-1+i]==sign)return new Point(centralX+i-j,centralY+i);}catch(Exception e){}
					
					try{if(game.fieldState[centralX-1+j][centralY-1-i]==sign)return new Point(centralX+j,centralY-i);}catch(Exception e){}
					try{if(game.fieldState[centralX-1-j][centralY-1+i]==sign)return new Point(centralX-j,centralY+i);}catch(Exception e){}
					try{if(game.fieldState[centralX-1-i][centralY-1-j]==sign)return new Point(centralX-i,centralY-j);}catch(Exception e){}
					try{if(game.fieldState[centralX-1+i][centralY-1+j]==sign)return new Point(centralX+i,centralY+j);}catch(Exception e){}
				}
			}
		}
		return null;
	}	
}
