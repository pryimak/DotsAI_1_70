package p_DotsAI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import p_Analytics.AnalyticsFrame;
import p_GUI.AboutGameFrame;
import p_GUI.GameGUI;
import p_GUI.GameManagement;
import p_JavaPatterns.C_JFrame;
import p_JavaPatterns.C_OpenFile;
import p_JavaPatterns.C_ReadAndWriteFile;
import p_JavaPatterns.C_Resource;
import p_TemplateEditor.DotsTE;

public class DotsAI extends JFrame implements Runnable,MouseListener, WindowListener, MouseMotionListener{//PointsAIFrame{

	/*

	 - ����� ������
	 	- ������� � templateLogic �������� �� �����
	 	- ��������� ����� �������� ����� �������� ������ � ������� � ����� �������� ������ ��������
	 	- ������ ������� - ������, ��������� �� ���������� ����������, �� ������������ �� �������� (�� ����� ANY �����)
	 	- �������������� default ���� � ��������, � ������� �� ������������ (������� ����������� � ����������),
	 		�������������� ����� ����������� ������, ���� ����� ������� �� �������� ������� � ��������
	 	- � ������� ��� ������� �������� ������� (mark) ��� ��������� ���� ��� ������� �������� �� ����
	 		- ��� ����� �������� ����� ��������� ��� ������������
	 		- ��� ������ �������� ����� ��������� ��� ���������
	 		- ������� � ������ �������� ��� ���� ��� ���������� ��� ��� ����������� ����� ��� ���������

	 - ���������� �������� makrosApplication, ���� ��� ��� �� ����� �� ����� ���� 
	 	(����� ����� ��������� templateView ��� makrosApplication � ������ ��������� ����� � �������, �����
	 	 �������� � ������ makrosApplication ���������� ���������� ��� �������)
	 - ����������� ��������� Link ��� Wall - ����� ��������� �� ����� �����, ��������, ������� ���� � ����� �����
	 - ������ ��������� ������ ��� �������������� ������-�� ������� � ���� ������ ������������� ���������� ������� � ���� (�� ������ ������ ��������� ������ - 11016)
	 - ������ � pointsTE ��� ��������� �� ���� �������� ��� ���������� �� ������ ��� ��������� ������� ����� �������� ������ ���� ������ ���� �� ��� ���� �������� ������/�����, ����� ������� �� ���� ������� � ����
	 - ����� �� �������� ��������
	 - ������� ������� global attack and global destroy	 
	 - �������� � bss - ���� ����� ������ � ����� BLUE_ATTACK	 	
	 - ���������� ���������
	 	- ������� �������� ����������� ����� �� ���� PointsTE
	 	- ���������� ������� 9�9
	 - ������ �������� �� ����
	 	- ����� surround ������������ ������ ������� walls,wall ��� ����������� ��������� ���������	 	
	 - ����������
	 	- ��� �������� ��������� �� ����� ������ �����, ������� ����� ���������
	 	- ����� ������� �� ������ ����� - ��� ���������� �� ���������, ��� ��������� �� ������� �����, � ���������

	*/
	
	public GameGUI gameGUI=new GameGUI();
	public AnalyticsFrame analytics;
	public DotsTE dotsTE=null;
	int preX=0,preY=0,x,y;
	//EnemyType enemyType=EnemyType.HUMAN;//0-human, 1-AI, 2-random
	String blueMoves;
	int moveAIx=99,moveAIy=99;
	String strFile="";
	Point point;
	public Protocol protocol;
	long start;
	public long AItime;public int AImovesCount,AItotalTime;public double AIavgTime;
	public boolean isPause=false;
	Random rand=new Random();
	public JLabel label=new JLabel();
	public JLabel labelCoordinates=new JLabel();
	public JMenuItem itemNewAIvsHuman,itemNewAIvsRandom,itemOpenGame,itemSaveGame,itemAboutGame;
	public Thread t=new Thread(this);
	public int sizeX=39,sizeY=32;	
	int redX1=19,redY1=17,blueX1=19,blueY1=16,redX2=20,redY2=16,blueX2=20,blueY2=17;
	public int gameIdx=0;
	
public DotsAI(){
	
	AboutGameFrame aboutGameFrame=new AboutGameFrame();
	aboutGameFrame.frame.setTitle("��������. ��������� ����� 10 ������...");
	
	protocol=new Protocol();
	
	//setStartPosition(isStartPosition2X);
	protocol.addNewGame(++gameIdx,EnemyType.HUMAN,sizeX,sizeY,redX1,redY1,blueX1,blueY1,redX2,redY2,blueX2,blueY2);
	
	analytics=new AnalyticsFrame(protocol.getGame(gameIdx));
			
	new C_JFrame(this,Protocol.appName+" "+Protocol.appVersion,false,GameGUI.squareSize*(protocol.getGame(gameIdx).sizeX+1+GameGUI.offsetX),
			GameGUI.squareSize*(protocol.getGame(gameIdx).sizeY+1+GameGUI.offsetY)-20,new Color(255,255,255));
	this.setIconImage(C_Resource.icon);
	this.addMouseListener(this);
	this.addWindowListener(this);
	this.addMouseMotionListener(this);
	
	JMenuBar menu=new JMenuBar();
	JMenu item1=new JMenu("File");
	itemNewAIvsHuman=new JMenuItem("New game AI vs Human");
	itemNewAIvsRandom=new JMenuItem("New game AI vs Random");
	itemOpenGame=new JMenuItem("Open game");
	itemSaveGame=new JMenuItem("Save game");
	itemAboutGame=new JMenuItem("About");
	
	menu.add(item1);
	item1.add(itemNewAIvsHuman);
	item1.add(itemNewAIvsRandom);
	item1.addSeparator();
	item1.add(itemOpenGame);
	item1.add(itemSaveGame);
	item1.addSeparator();
	item1.add(itemAboutGame);
	menu.add(label);
	menu.add(labelCoordinates);
	this.setJMenuBar(menu);
	
	aboutGameFrame.frame.dispose();
	
	this.move(DotsAI.this.getX(), DotsAI.this.getY()-100);
	
	dotsTE=new DotsTE(protocol.templateEngine,DotsAI.this.getX(),DotsAI.this.getY()+DotsAI.this.getHeight(),this);
	dotsTE.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	gameGUI.newGame(this);
	
	GameManagement gameManagementFrame=new GameManagement(this);
	menu.add(gameManagementFrame.butPause);
	menu.add(gameManagementFrame.butBackLength1);
	menu.add(gameManagementFrame.butBackLength5);
	menu.add(gameManagementFrame.butCreateTemplate);
	
	aiMove(null);
	
	itemAboutGame.addMouseListener(new MouseListener() {
		public void mouseClicked(MouseEvent arg0) {}
		public void mouseEntered(MouseEvent arg0) {}
		public void mouseExited(MouseEvent arg0) {}
		public void mousePressed(MouseEvent arg0) {new AboutGameFrame();}
		public void mouseReleased(MouseEvent arg0) {}
	});
	
	itemOpenGame.addMouseListener(new MouseListener() {
		public void mouseClicked(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {
			strFile=new C_OpenFile(DotsAI.this,new String[]{".pai"}).getFileContent();
			t.stop();
			
			//try{protocol.stat.clearStat();}catch(Exception e1){}
			protocol.addNewGame(++gameIdx,EnemyType.HUMAN,sizeX,sizeY,redX1,redY1,blueX1,blueY1,redX2,redY2,blueX2,blueY2);
			gameGUI.newGame(DotsAI.this);
			blueMoves=strFile;
			
			int move=0;
			boolean isLastAiMove=false;
			for(int i=0;i<strFile.length();i++){
				if(strFile.substring(i, i+1).equals(";"))continue;
				
				int x=0,y=0;
				if(strFile.substring(i+1, i+2).equals("-")){
					x=new Integer(strFile.substring(i, i+1));i+=2;
					if(strFile.substring(i+1, i+2).equals(";")){
						y=new Integer(strFile.substring(i, i+1));i+=1;
					}
					else {y=new Integer(strFile.substring(i, i+2));i+=2;}
				}else{
					x=new Integer(strFile.substring(i, i+2));i+=3;
					if(strFile.substring(i+1, i+2).equals(";")){
						y=new Integer(strFile.substring(i, i+1));i+=1;
					}
					else {y=new Integer(strFile.substring(i, i+2));i+=2;}
				}
				if(move%2==0){
					aiMove(new Point(x,y));
					isLastAiMove=true;
					//System.out.println("aiMove "+x+","+y);
				}else{
					AIenemyMove(x,y);
					isLastAiMove=false;
					//System.out.println("AIenemyMove "+x+","+y);
				}
				move++;
			}
			if(!isLastAiMove)aiMove(null);
			
			t=new Thread(DotsAI.this);
			t.start();
		}
		public void mouseExited(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
	});
	
	itemSaveGame.addMouseListener(new MouseListener() {
		public void mouseClicked(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {
			blueMoves="";
			for(int i=0;i<protocol.getGame(gameIdx).movesMas.length;i++){
				//if(protocol.getGame(gameIdx).movesMas[i][2]==0)continue;
				if(protocol.getGame(gameIdx).movesMas[i][0]==0&protocol.getGame(gameIdx).movesMas[i][1]==0)break;
				//if(protocol.getGame(gameIdx).movesMas[i][2]==1){
					blueMoves+=protocol.getGame(gameIdx).movesMas[i][0]+"-"+protocol.getGame(gameIdx).movesMas[i][1]+";";
				//}
			}
			//new C_SaveFile(PointsAI.this,blueMoves,"game",".pai");
			new C_ReadAndWriteFile().WriteTxtFile(new File(C_Resource.savedGames+"game ("+new Date().toLocaleString().replaceAll(":", "-")+").pai").toString(),blueMoves);
		}
		public void mouseExited(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
	});
	
	itemNewAIvsHuman.addMouseListener(new MouseListener() {	
		public void mouseClicked(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {
			t.stop();
			//try{protocol.getGame(gameIdx).stat.clearStat();}catch(Exception e1){}
			protocol.addNewGame(++gameIdx,EnemyType.HUMAN,sizeX,sizeY,redX1,redY1,blueX1,blueY1,redX2,redY2,blueX2,blueY2);
			gameGUI.newGame(DotsAI.this);
			//if(isFirstMoveByAI)
				aiMove(null);
			t=new Thread(DotsAI.this);
			t.start();
		}
		public void mouseExited(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}});

	itemNewAIvsRandom.addMouseListener(new MouseListener(){	
		public void mouseClicked(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {
			t.stop();
			//try{protocol.getGame(gameIdx).stat.clearStat();}catch(Exception e1){}
			protocol.addNewGame(++gameIdx,EnemyType.RANDOM,sizeX,sizeY,redX1,redY1,blueX1,blueY1,redX2,redY2,blueX2,blueY2);
			gameGUI.newGame(DotsAI.this);
			//if(isFirstMoveByAI)aiMove();
			//gameRandom=gameGUI.newGameRotated();			
			t=new Thread(DotsAI.this);
			t.start();
		}
		public void mouseExited(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}});	
}
	
public void mousePressed(MouseEvent me){
	x=getMouseClickX(me);
	y=getMouseClickY(me);
	//System.out.println(x+";"+y);
	if(protocol.getGame(gameIdx).singleGameEngine.canMakeMove(x,y)&protocol.getGame(gameIdx).enemyType==EnemyType.HUMAN&(protocol.getGame(gameIdx).singleGameEngine.lastMoveType==Protocol.moveTypeRed|protocol.getGame(gameIdx).getMovesCount()==0)){
		AIenemyMove(x,y);
		aiMove(null);
	}
}

public void windowActivated(WindowEvent e) {t.stop();t=new Thread(this);t.start();}
public void windowClosing(WindowEvent e){protocol.templateEngine.saveTemplateBase();}

public static void main(String[] args){new DotsAI();}

public void run(){try{
	
	try{t.sleep(500);}catch(Exception e){}

	if(protocol.getGame(gameIdx).enemyType==EnemyType.RANDOM){
		
		for(;;){try{
			
			if(isPause){try{t.sleep(100);}catch(Exception e){}continue;}
			
			aiMove(null);
			if(protocol.getGame(gameIdx).enemyType==EnemyType.HUMAN)break;
			//try{t.sleep(100);}catch(Exception e){}
			
			while(!protocol.getGame(gameIdx).singleGameEngine.canMakeMove(moveAIx,moveAIy)){	
				moveAIx=rand.nextInt(protocol.getGame(gameIdx).sizeX)+1;
				moveAIy=rand.nextInt(protocol.getGame(gameIdx).sizeY)+1;
			}
			AIenemyMove(moveAIx,moveAIy);

			//try{t.sleep(100);}catch(Exception e){}
			//if(!isFirstMoveByAI)aiMove();
			if((protocol.getGame(gameIdx).lastAimove.x==-2&protocol.getGame(gameIdx).lastAimove.y==-2)|(protocol.getGame(gameIdx).lastAimove.x==-3&protocol.getGame(gameIdx).lastAimove.y==-3)|(protocol.getGame(gameIdx).lastAimove.x==-4&protocol.getGame(gameIdx).lastAimove.y==-4)){break;}
			if(protocol.getGame(gameIdx).getMovesCount()>(protocol.getGame(gameIdx).sizeX*protocol.getGame(gameIdx).sizeY-100))break;
		}catch(Exception exc){break;}}
	}

	t.stop();
}catch(Exception e){}}

public void aiMove(Point recommendedMove){
	
	start=System.currentTimeMillis();
	point=protocol.getAImove(gameIdx,10,recommendedMove);
	AItime=System.currentTimeMillis()-start;
	AItotalTime+=AItime;
	AImovesCount++;
	AIavgTime = new BigDecimal(AItotalTime/(double)AImovesCount/1000.0).setScale(2, RoundingMode.HALF_UP).doubleValue();
	
	boolean isGameEnd=false;
	if((point.x==-2&point.y==-2)|//�� ����������, �� ����������� ����
			(point.x==-3&point.y==-3)|//������� ����������, �� �������
			(point.x==-4&point.y==-4)//�� �������, �.�. ������� ����� �����
		){
		isGameEnd=true;
	}
	
	if(isGameEnd){
		protocol.getGame(gameIdx).singleGameEngine.setTerrytoryState(protocol.getGame(gameIdx));
		for(int moveAIx1=1;moveAIx1<=protocol.getGame(gameIdx).sizeX;moveAIx1++){for(int moveAIy1=1;moveAIy1<=protocol.getGame(gameIdx).sizeY;moveAIy1++){
			if(protocol.getGame(gameIdx).fieldTerrytoryState[moveAIx1-1][moveAIy1-1]==Protocol.templateDotTypeNULL){
				if((point.x==-2&point.y==-2)|(point.x==-4&point.y==-4)){
					protocol.getGame(gameIdx).singleGameEngine.makeMove(new Point(moveAIx1,moveAIy1),Protocol.moveTypeBlue);
				}
				if(point.x==-3&point.y==-3){
					protocol.getGame(gameIdx).singleGameEngine.makeMove(new Point(moveAIx1,moveAIy1),Protocol.moveTypeRed);
				}
			}
		}}
		DotsAI.this.label.setText(gameGUI.getLabelText(DotsAI.this));
		
	}
	
	gameGUI.gameRepaint(DotsAI.this,true);
	
	if(isGameEnd){
		protocol.deleteGame(gameIdx);
		System.out.println("delete game "+gameIdx);
	}
}

public void AIenemyMove(int x,int y){
	protocol.addAIenemyMove(gameIdx,x,y);
	gameGUI.gameRepaint(DotsAI.this,true);
}

public void mouseMoved(MouseEvent me){try{//System.out.println("+++");
	if(getMouseClickX(me)!=preX|getMouseClickY(me)!=preY){
		Graphics graphics=this.getGraphics();
		if(protocol.getGame(gameIdx).singleGameEngine.canMakeMove(preX, preY)){
			graphics.setColor(Color.white);
			graphics.drawOval(preX*GameGUI.squareSize+9, preY*GameGUI.squareSize+41, 6, 6);
			graphics.setColor(new Color(225,225,225));
			graphics.drawLine(preX*GameGUI.squareSize+9, preY*GameGUI.squareSize+44, preX*GameGUI.squareSize+15, preY*GameGUI.squareSize+44);
			graphics.drawLine(preX*GameGUI.squareSize+12, preY*GameGUI.squareSize+41, preX*GameGUI.squareSize+12, preY*GameGUI.squareSize+47);
		}		
		preX=getMouseClickX(me);preY=getMouseClickY(me);
		if(preX>0&preX<(protocol.getGame(gameIdx).sizeX+1)&preY>0&preY<(protocol.getGame(gameIdx).sizeY+1))labelCoordinates.setText("<HTML><font color=gray>"+preX+":"+preY);
		if(protocol.getGame(gameIdx).singleGameEngine.canMakeMove(preX, preY)){
			graphics.setColor(Color.blue);
			graphics.drawOval(preX*GameGUI.squareSize+9, preY*GameGUI.squareSize+41, 6, 6);
		}
	}
}catch(Exception e){}}

int getMouseClickX(MouseEvent me){
	return (int)(((double)me.getX()-5-(double)((GameGUI.offsetX-1)*GameGUI.squareSize))/(double)GameGUI.squareSize);
};
int getMouseClickY(MouseEvent me){
	return (int)(((double)me.getY()-5-(double)((GameGUI.offsetY-1)*GameGUI.squareSize))/(double)GameGUI.squareSize);
};

public void windowClosed(WindowEvent e) {}
public void windowDeactivated(WindowEvent e) {}
public void windowDeiconified(WindowEvent e) {}
public void windowIconified(WindowEvent e) {}
public void windowOpened(WindowEvent e) {}
public void mouseClicked(MouseEvent me) {}
public void mouseEntered(MouseEvent me) {}
public void mouseExited(MouseEvent me) {}
public void mouseReleased(MouseEvent me) {}
public void mouseDragged(MouseEvent arg0) {}

}