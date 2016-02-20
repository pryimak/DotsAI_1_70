package p_Statistics;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;

import p_DotsAI.DotsAI;
import p_JavaPatterns.C_JFrame;
import p_JavaPatterns.C_Resource;

public class StatisticsFrame{
	
	public JFrame frame;
	int max=0;
	int sumOfMoveStat;//всего ходов
	int sumOfMoveStatTemplates;//всего ходов по шаблонам (включая макросы, исключая EXPRESS_SURROUND и RANDOM)
	int sumOfMoveStatMakros;//всего ходов по макросам
	public MoveStatCount moveStatMas=new MoveStatCount();
	int Y=30;
		
public StatisticsFrame(){
	frame=new JFrame();
	new C_JFrame(frame, "Статистика ИИ", false, 250, 70+moveStatMas.getMoveStatLength()*15, Color.white);
	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	frame.move(frame.getX()-469,frame.getY()+100);
	frame.setIconImage(C_Resource.icon);
	//frame.setAlwaysOnTop(true);
	//paintFrame(null);
}

public void clearStat(){
	max=0;
	sumOfMoveStat=0;
	sumOfMoveStatTemplates=0;
	sumOfMoveStatMakros=0;
	for(int i=0;i<moveStatMas.moveStat.length;i++){
		moveStatMas.moveStat[i]=0;
		moveStatMas.moveStatMakros[i]=0;
	}
	moveStatMas.moveStatMax=0;
}

public void paintFrame(DotsAI pointsAI){

	Graphics g=frame.getGraphics();	
	g.clearRect(0, 0, frame.getWidth(), frame.getHeight());	
	
	sumOfMoveStat=0;
	sumOfMoveStatMakros=0;
	for(int i=0;i<moveStatMas.getMoveStatLength();i++){
		//if(MoveStatType.isDisabled(MoveStatType.getMoveStatType(i)))g.setColor(Color.red);
		//else 
			g.setColor(Color.pink);
		g.fillRect(10, Y+15*i, 200, 11);
		sumOfMoveStat+=moveStatMas.moveStat[i];
		sumOfMoveStatMakros+=moveStatMas.moveStatMakros[i];
	}
	sumOfMoveStatTemplates=
			sumOfMoveStat
			-
			moveStatMas.moveStat[MoveStatType.getMoveStatIndex(MoveStatType.EXPRESS_SURROUND_SECURITY_1)]
			-
			moveStatMas.moveStat[MoveStatType.getMoveStatIndex(MoveStatType.EXPRESS_SURROUND_SECURITY_2)]
			-
			moveStatMas.moveStat[MoveStatType.getMoveStatIndex(MoveStatType.RANDOM)];
	
	for(int i=0;i<moveStatMas.getMoveStatLength();i++){
		g.setColor(Color.green);
		g.fillRect(10, Y+15*i, (int)((float)moveStatMas.moveStat[i]/sumOfMoveStat*200), 11);
		g.setColor(Color.cyan);
		g.fillRect(10, Y+15*i, (int)((float)moveStatMas.moveStatMakros[i]/sumOfMoveStat*200), 11);
	}
	
	g.setColor(Color.black);	
	for(int i=0;i<moveStatMas.getMoveStatLength();i++){
		g.drawString(MoveStatType.toString(MoveStatType.getMoveStatType(i)), 15, Y+10+15*i);
	}
	
	g.setColor(Color.yellow);
	g.fillRect(10, Y+15*moveStatMas.getMoveStatLength(), 240, 13);
	g.fillRect(10, Y+16+15*moveStatMas.getMoveStatLength(), 240, 13);
	g.setColor(Color.black);
	g.drawString("Last move: "+moveStatMas.lastAImoveString, 15, Y+11+15*moveStatMas.getMoveStatLength());	
	//if(pointsAI!=null)
		g.drawString(pointsAI.protocol.getGame(pointsAI.gameIdx).moveAI.x+";"+pointsAI.protocol.getGame(pointsAI.gameIdx).moveAI.y, 103, Y+27+15*moveStatMas.getMoveStatLength());
	
	//<пометка, если последний ход по макросу>
	if(moveStatMas.isByMakros){
		g.setColor(Color.black);
		g.drawString("(по макросу)", 175, Y+27+15*moveStatMas.getMoveStatLength());
	}
	//</пометка, если последний ход по макросу>
	
	max=moveStatMas.getMoveStatMax();
	
	g.setColor(Color.black);		
	for(int i=0;i<moveStatMas.getMoveStatLength();i++){		
		//g.setColor(new Color(255-(int)(moveStatMas.moveStat[i]/(double)max*255),255-(int)(moveStatMas.moveStat[i]/(double)max*255),255-(int)(moveStatMas.moveStat[i]/(double)max*255)));
		g.fillRect(215, Y+15*i, 35, 11);		
		//g.drawRect(215, 30+15*i, 35, 10);
	}
	
	
	for(int i=0;i<moveStatMas.getMoveStatLength();i++){
		//g.fillRect(10, 30+15*i, (int)((float)moveStatMas.moveStat[i]/sum*200), 10);
		if(moveStatMas.moveStat[i]>0)g.setColor(Color.green);
		else g.setColor(Color.pink);
		
		/*if(MoveStatType.isDisabled(MoveStatType.getMoveStatType(i))){
			g.setColor(Color.red);
			g.drawString("откл", 220, Y+10+15*i);
			continue;
		}*/
		
		g.drawString(""+moveStatMas.moveStat[i], 220, Y+10+15*i);
	}
	
	//<итоговые суммы ходов по макросам и шаблонам>
	g.setColor(Color.pink);
	g.fillRect(10, Y+32+15*moveStatMas.getMoveStatLength(), 240, 13);
	g.fillRect(10, Y+48+15*moveStatMas.getMoveStatLength(), 240, 13);
	
	g.setColor(Color.green);
	g.fillRect(10, Y+32+15*moveStatMas.getMoveStatLength(), (int)((float)sumOfMoveStatTemplates/sumOfMoveStat*240), 13);
	g.setColor(Color.cyan);
	g.fillRect(10, Y+48+15*moveStatMas.getMoveStatLength(), (int)((float)sumOfMoveStatMakros/sumOfMoveStatTemplates*240), 13);
	
	g.setColor(Color.black);
	g.drawString("Ходов по шаблонам: "+sumOfMoveStatTemplates+" из "+sumOfMoveStat, 15, Y+43+15*moveStatMas.getMoveStatLength());
	g.drawString("Ходов по макросам: "+sumOfMoveStatMakros+" из "+sumOfMoveStatTemplates, 15, Y+59+15*moveStatMas.getMoveStatLength());
	//</итоговые суммы ходов по макросам и шаблонам>
}

}
