package p_GUI;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

import p_DotsAI.EnemyType;
import p_DotsAI.DotsAI;
import p_DotsAI.Protocol;
import p_DotsAI.Protocol.Game;
import p_JavaPatterns.C_AddComponent;
import p_JavaPatterns.C_Resource;
import p_TemplateEngine.TemplateType;

public class GameManagement{//заставка

	private C_AddComponent add;
	public JMenuItem butBackLength1,butBackLength5,butCreateTemplate,butPause;
	private DotsAI pointsAI;
	private int[][] movesMas;
	
public GameManagement(DotsAI pointsAI){
	
	this.pointsAI=pointsAI;

	add=new C_AddComponent(pointsAI.getContentPane());
	
	butBackLength1=(JMenuItem)add.component("jmenuitem",30, 15, 20, 20,"",new ActionListener(){
		public void actionPerformed(ActionEvent arg0) {
			back(1);
	}});
	butBackLength5=(JMenuItem)add.component("jmenuitem",55, 15, 20, 20,"",new ActionListener(){
		public void actionPerformed(ActionEvent arg0) {
			back(5);
	}});
	butCreateTemplate=(JMenuItem)add.component("jmenuitem",110, 15, 30, 25,"",new ActionListener(){
		public void actionPerformed(ActionEvent arg0) {
			createTemplate();			
	}});
	butPause=(JMenuItem)add.component("jmenuitem",150, 15, 70, 20,"",new ActionListener(){
		public void actionPerformed(ActionEvent arg0) {
			pauseGameWithRandom();			
	}});
	
	butBackLength1.setIcon(new ImageIcon(C_Resource.navigation+"undo1.png"));
	butBackLength5.setIcon(new ImageIcon(C_Resource.navigation+"undo5.png"));
	butCreateTemplate.setIcon(new ImageIcon(C_Resource.base+"save_small.png"));
	butPause.setIcon(new ImageIcon(C_Resource.navigation+"pause.png"));
	
	butBackLength1.setToolTipText("1 ход назад");
	butBackLength5.setToolTipText("5 ходов назад");
	butCreateTemplate.setToolTipText("Создать шаблон");
	butPause.setToolTipText("Пауза при игре с рандомом");
}

private void createTemplate(){
	JOptionPane msg=new JOptionPane();
	try{
		int x=new Integer(msg.showInputDialog("input X", "0"));
		int y=new Integer(msg.showInputDialog("input Y", "0"));
		pointsAI.protocol.getGame(pointsAI.gameIdx).singleGameEngine.setFieldState(pointsAI.protocol.getGame(pointsAI.gameIdx));
		pointsAI.dotsTE.moveFromAI(
				TemplateType.getContentForTemplateCreation(pointsAI.protocol.getGame(pointsAI.gameIdx),new Point(x, y),pointsAI.protocol.getGame(pointsAI.gameIdx).fieldState,TemplateType.templateTypeWALL),
				null,
				0,
				TemplateType.templateTypeWALL);
	}catch(Exception exc){}
}

private void pauseGameWithRandom(){
	if(pointsAI.isPause)pointsAI.isPause=false;
	else pointsAI.isPause=true;
}

private void back(int backLength){
	if(this.pointsAI.protocol.getGame(this.pointsAI.gameIdx).enemyType==EnemyType.RANDOM){
		this.pointsAI.protocol.getGame(this.pointsAI.gameIdx).enemyType=EnemyType.HUMAN;
		return;
	}
	//System.out.println("+++++++++++++++++++++++++++++++++ moves "+pointsAI.protocol.getGame(pointsAI.gameIdx).getMovesCount());
	if(pointsAI.protocol.getGame(pointsAI.gameIdx).singleGameEngine.lastMoveType!=Protocol.moveTypeRed)return;
	else if(pointsAI.protocol.getGame(pointsAI.gameIdx).getMovesCount()==0){
		return;
	}else if(pointsAI.protocol.getGame(pointsAI.gameIdx).getMovesCount()==1){
		/*Point lastAimove=pointsAI.protocol.getGame(pointsAI.gameIdx).lastAimove;
		pointsAI.protocol.addNewGame(++pointsAI.gameIdx,EnemyType.HUMAN,pointsAI.sizeX,pointsAI.sizeY,19,17,19,16,20,16,20,17);
		pointsAI.gameGUI.newGame(pointsAI);
		pointsAI.aiMove(lastAimove);*/
		return;
	}else{
		movesMas=new int[pointsAI.protocol.getGame(pointsAI.gameIdx).getMovesCount()][3];
		for(int i=0;i<movesMas.length;i++){
			movesMas[i][0]=pointsAI.protocol.getGame(pointsAI.gameIdx).movesMas[i][0];
			movesMas[i][1]=pointsAI.protocol.getGame(pointsAI.gameIdx).movesMas[i][1];
			movesMas[i][2]=pointsAI.protocol.getGame(pointsAI.gameIdx).movesMas[i][2];
		}
		
		//try{pointsAI.protocol.getGame(pointsAI.gameIdx).stat.clearStat();}catch(Exception e1){}
		pointsAI.protocol.addNewGame(++pointsAI.gameIdx,EnemyType.HUMAN,pointsAI.sizeX,pointsAI.sizeY,19,17,19,16,20,16,20,17);
		pointsAI.gameGUI.newGame(pointsAI);
		
		//int move=0;
		boolean isLastAiMove=false;
		for(int i=0;i<movesMas.length;i++){
			if(i+1+backLength*2>movesMas.length){
				if(!isLastAiMove)pointsAI.aiMove(new Point(movesMas[i][0],movesMas[i][1]));
				break;			
			}
			
			if(movesMas[i][2]==Protocol.moveTypeRed){
				pointsAI.aiMove(new Point(movesMas[i][0],movesMas[i][1]));
				isLastAiMove=true;
				//System.out.println("aiMove "+x+","+y);
			}else{
				pointsAI.AIenemyMove(movesMas[i][0],movesMas[i][1]);
				isLastAiMove=false;
				//System.out.println("AIenemyMove "+x+","+y);
			}
			//move++;
		}
		//if(!isLastAiMove)pointsAI.aiMove(null);
	}
}

}