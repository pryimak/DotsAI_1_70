package p_GUI;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Graphics;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;

import p_DotsAI.DotsAI;
import p_DotsAI.Protocol;
import p_JavaPatterns.C_Resource;

public class GameGUI{

	public DrawSingleGameEngine draw=new DrawSingleGameEngine();//рисование игрового поля
	private Graphics graphics;
	String spc="&nbsp;&nbsp;&nbsp;";
	AudioClip sound=null;
	public static int squareSize=16;//размеры клетки поля в пикселях
	public static int offsetX=1;
	public static int offsetY=3;
	public static int pointSize=(int)(squareSize/2);//отступы (размерность - клеток игрового поля) и размер точки
	
public GameGUI(){
	File f=new File(C_Resource.gui+"move.wav");
	try{sound=Applet.newAudioClip(f.toURL());}catch(Exception e){System.out.println("sound fail");}
}

public void newGame(DotsAI pointsAI){
	
	pointsAI.label.setText(getLabelText(pointsAI));	
	pointsAI.AImovesCount=0;
	pointsAI.AItotalTime=0;
	pointsAI.protocol.getGame(pointsAI.gameIdx).singleGameEngine.setFieldState(pointsAI.protocol.getGame(pointsAI.gameIdx));
	pointsAI.protocol.wallsSearch(pointsAI.protocol.getGame(pointsAI.gameIdx));
	
	if(pointsAI.isPause)pointsAI.isPause=false;
	
	gameRepaint(pointsAI,true);
}

public void gameRepaint(DotsAI pointsAI,boolean isRepaintTE){
	//System.out.println(pointsAI.protocol.movesCount);
	sound.play();
	
	pointsAI.label.setText(getLabelText(pointsAI));	
	
	graphics=pointsAI.getGraphics();
	graphics.setColor(new Color(254,254,254));
	graphics.fillRect(0, 50, squareSize*(pointsAI.protocol.getGame(pointsAI.gameIdx).sizeX+1+offsetX), pointsAI.protocol.getGame(pointsAI.gameIdx).sizeY*(squareSize+1));
	draw.paint(graphics, pointsAI.protocol.getGame(pointsAI.gameIdx).singleGameEngine,0,0,null,null);
	
	try{pointsAI.analytics.repaint(pointsAI.protocol.getGame(pointsAI.gameIdx));}catch(Exception e){System.out.println("error repaint analytics frame");}
	pointsAI.protocol.stat.paintFrame(pointsAI);
	
	try{if(pointsAI.protocol.getGame(pointsAI.gameIdx).getMovesCount()>0&isRepaintTE&pointsAI.protocol.getGame(pointsAI.gameIdx).singleGameEngine.lastMoveType==Protocol.moveTypeRed)pointsAI.dotsTE.moveFromAI(
			pointsAI.protocol.templateEngine.getTemplateByTemplateIndex(pointsAI.protocol.templateEngine.foundedIndex).templateContent,
			pointsAI.protocol.templateEngine.getTemplateByTemplateIndex(pointsAI.protocol.templateEngine.foundedIndex).templateLogic,
			pointsAI.protocol.templateEngine.foundedNumber,
			pointsAI.protocol.templateEngine.foundedTemplateType
		);}catch(Exception e){}
	//System.out.println(pointsAI.protocol.templateEngine.foundedNumber);
	//pointsAI.pointsTE.engine.paint();
	
	paintWallPoints(pointsAI);//}catch(Exception e){}
}

/*private String arrayToString(int[] arr){//только для изменения движка при объединении шаблонов с макросами
	String s="";
	for(int i=0;i<arr.length;i++){
		s+=arr[i];
	}
	return s;
}*/

public String getLabelText(DotsAI dotsAI){try{
	return "<HTML><FONT color=blue>"+spc+dotsAI.protocol.getGame(dotsAI.gameIdx).enemyType.toString()+"</FONT><FONT color=black> "+dotsAI.protocol.getGame(dotsAI.gameIdx).singleGameEngine.getBlueScore()+" - "+dotsAI.protocol.getGame(dotsAI.gameIdx).singleGameEngine.getRedScore()+
		"</FONT><FONT color=red> "+dotsAI.protocol.appName+" "+dotsAI.protocol.appVersion+"</FONT>"+spc+"moves <FONT color=green>"+(dotsAI.protocol.getGame(dotsAI.gameIdx).getMovesCount()+4)+"</FONT>"+spc+
		"AI time"+"<FONT color=green> "+
		new BigDecimal((double)(dotsAI.AItime)/1000.0).setScale(2,RoundingMode.HALF_UP).doubleValue()+
		"</FONT>"+spc+
		"avg AI time"+"<FONT color=green> "+dotsAI.AIavgTime+"</FONT></HTML>";
}catch(Exception e){
	return "<HTML><FONT color=blue>"+spc+spc+dotsAI.protocol.getGame(dotsAI.gameIdx).enemyType.toString()+"</FONT><FONT color=black> 0 - 0</FONT><FONT color=red> "+Protocol.appName+" "+Protocol.appVersion+"</FONT>"+spc+"ход <FONT color=green>8</FONT></HTML>";
}}

public void paintWallPoints(DotsAI pointsAI){try{	
	Graphics g=pointsAI.getGraphics();
	for(int i=0;i<pointsAI.protocol.getGame(pointsAI.gameIdx).wallRed.size();i++){
		draw.fillPoint(g, pointsAI.protocol.getGame(pointsAI.gameIdx).wallRed.get(i).wallEndX, pointsAI.protocol.getGame(pointsAI.gameIdx).wallRed.get(i).wallEndY,Color.black);
	}
	
	if(pointsAI.protocol.getGame(pointsAI.gameIdx).smallerRedWallIdx!=-1)draw.fillPoint(g, pointsAI.protocol.getGame(pointsAI.gameIdx).wallRed.get(pointsAI.protocol.getGame(pointsAI.gameIdx).smallerRedWallIdx).wallEndX, pointsAI.protocol.getGame(pointsAI.gameIdx).wallRed.get(pointsAI.protocol.getGame(pointsAI.gameIdx).smallerRedWallIdx).wallEndY,Color.yellow);
	
	for(int i=0;i<pointsAI.protocol.getGame(pointsAI.gameIdx).wallRed.size();i++){
		if(pointsAI.protocol.getGame(pointsAI.gameIdx).wallRed.get(i).isAtSide(pointsAI.protocol.getGame(pointsAI.gameIdx)))
			draw.fillPoint(g, pointsAI.protocol.getGame(pointsAI.gameIdx).wallRed.get(i).wallEndX, pointsAI.protocol.getGame(pointsAI.gameIdx).wallRed.get(i).wallEndY,Color.green);
	}
}catch(Exception e){}}

}
