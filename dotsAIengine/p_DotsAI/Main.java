package p_DotsAI;

import java.awt.Point;
import java.util.Random;

import p_DotsAI.Protocol.Game;
import p_GUI.EngineFrame;

public class Main implements Runnable{
	public Protocol protocol;
	Thread t1,t2,t3,t4,t5;
	int gameIdx=1;
	Random rand=new Random();
	public int sizeX=39,sizeY=32;
	int redX1=19,redY1=17,blueX1=19,blueY1=16,redX2=20,redY2=16,blueX2=20,blueY2=17;
	
	Main(){
		
		protocol=new Protocol();		
		
		protocol.addNewGame(1,EnemyType.RANDOM,sizeX,sizeY,redX1,redY1,blueX1,blueY1,redX2,redY2,blueX2,blueY2);
		protocol.addNewGame(2,EnemyType.RANDOM,sizeX,sizeY,redX1,redY1,blueX1,blueY1,redX2,redY2,blueX2,blueY2);
		protocol.addNewGame(3,EnemyType.RANDOM,sizeX,sizeY,redX1,redY1,blueX1,blueY1,redX2,redY2,blueX2,blueY2);
		protocol.addNewGame(4,EnemyType.RANDOM,sizeX,sizeY,redX1,redY1,blueX1,blueY1,redX2,redY2,blueX2,blueY2);
		protocol.addNewGame(5,EnemyType.RANDOM,sizeX,sizeY,redX1,redY1,blueX1,blueY1,redX2,redY2,blueX2,blueY2);/**/

		new EngineFrame(protocol.getGame(1).singleGameEngine);
		new EngineFrame(protocol.getGame(2).singleGameEngine);
		new EngineFrame(protocol.getGame(3).singleGameEngine);
		new EngineFrame(protocol.getGame(4).singleGameEngine);
		new EngineFrame(protocol.getGame(5).singleGameEngine);/**/
		
		t1=new Thread(this);
		t2=new Thread(this);
		t3=new Thread(this);
		t4=new Thread(this);
		t5=new Thread(this);/**/
		
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();/**/
	}
		
	public void run(){
		int gameIdx0=gameIdx;
		System.out.println("start game "+gameIdx0);
		
		//Game game=null;
		if(gameIdx0==1){/*game=game1*/;gameIdx++;}
		else if(gameIdx0==2){/*game=game2*/;gameIdx++;try{t2.sleep(1000);}catch(InterruptedException e){}}
		else if(gameIdx0==3){/*game=game3*/;gameIdx++;try{t3.sleep(2000);}catch(InterruptedException e){}}
		else if(gameIdx0==4){/*game=game4*/;gameIdx++;try{t4.sleep(3000);}catch(InterruptedException e){}}
		else if(gameIdx0==5){/*game=game5*/;gameIdx++;try{t5.sleep(4000);}catch(InterruptedException e){}}		
		
		Point point=protocol.getAImove(gameIdx0,10,null);
		
		Game game=protocol.getGame(gameIdx0);
		
		for(;;){
			//if(f!=null)f.frame.dispose();
			int x=-1,y=-1;
			while(!game.singleGameEngine.canMakeMove(x,y)){
				x=Math.abs(rand.nextInt(game.sizeX))+1;
				y=Math.abs(rand.nextInt(game.sizeY))+1;
			}
			protocol.addAIenemyMove(gameIdx0,x,y);
			System.out.println("random move "+x+","+y);
			
			point=protocol.getAImove(gameIdx0,10,null);
			if((point.x==-2&point.y==-2)|//ИИ заземлился, ИИ заканчивает игру
					(point.x==-3&point.y==-3)|//человек заземлился, ИИ сдается
					(point.x==-4&point.y==-4)//ИИ сдается, т.к. потерял много точек
				){
				break;
			}
			System.out.println("AI move "+point.x+","+point.y);
			System.out.println("score AI:random "+protocol.getGame(gameIdx0).singleGameEngine.getRedScore()+","+protocol.getGame(gameIdx0).singleGameEngine.getBlueScore());
			System.out.println("moves "+protocol.getGame(gameIdx0).movesCount);
			
			System.out.println("======================================="+(int)(Runtime.getRuntime().freeMemory()/1000000.0));
		}
		System.out.println("end game"+gameIdx0);
	}
	
	public static void main(String[] args){new Main();}

}
