package p_Analytics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import p_DotsAI.Protocol;
import p_DotsAI.Protocol.Game;
import p_JavaPatterns.C_JFrame;
import p_JavaPatterns.C_Resource;

public class AnalyticsFrame{
	
	JFrame frame=new JFrame();
	int highLightWallRed=-1,highLightWallBlue=-1;
	boolean isRedHighLightWall;
	JButton bBlue,bRed;	
	Game game;
	
public AnalyticsFrame(Game game){
	
	new C_JFrame(frame, "Анализ игры", false, 200, 670, Color.white);
	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	frame.move(frame.getX()-445,frame.getY()-85);
	frame.setIconImage(C_Resource.icon);
	
	bBlue=new JButton("");
	bBlue.setBounds(105, 225, 20, 20);
	frame.add(bBlue);
	bBlue.setBackground(Color.blue);
	bBlue.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			isRedHighLightWall=false;
			if(highLightWallBlue>=getWallIndexBlue()-1)highLightWallBlue=0;
			else highLightWallBlue++;
			repaint(getGame());
		}
	});
	
	bRed=new JButton("");
	bRed.setBounds(130, 225, 20, 20);
	frame.add(bRed);
	bRed.setBackground(Color.red);	
	bRed.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			isRedHighLightWall=true;
			if(highLightWallRed>=getWallIndexRed()-1)highLightWallRed=0;
			else highLightWallRed++;
			repaint(getGame());
		}
	});
}

int getWallIndexRed(){return game.wallRed.size();}
int getWallIndexBlue(){return game.wallBlue.size();}
Game getGame(){return game;}

public void repaint(Game game){
	this.game=game;
	
	Graphics g=frame.getGraphics();	
	g.clearRect(0, 0, frame.getWidth(), frame.getHeight());	
	
	//рисование поля
	for(int i=0;i<game.sizeX;i++){for(int j=0;j<game.sizeY;j++){
		if(game.fieldState[i][j]==Protocol.templateDotTypeRED){g.setColor(Color.red);}
		else if(game.fieldState[i][j]==Protocol.templateDotTypeBLUE){g.setColor(Color.blue);}
		else if(game.fieldState[i][j]==Protocol.templateDotTypeNULL){g.setColor(Color.white);}
		g.fillRect(7+i*5, 35+j*5, 5, 5);
	}}
	g.setColor(Color.black);
	g.drawRect(6, 34, 196, 161);
	
	//рисование точек, принадлежащих стенам
	for(int i=0;i<game.sizeX;i++)for(int j=0;j<game.sizeY;j++){
		if(game.fieldOfWalls.get(i*game.sizeY+j).startsWith("PR")){//рисовать точки
			g.setColor(Color.pink);
			g.fillRect(9+i*5, 37+j*5, 1, 1);
			if(game.fieldOfWalls.get(i*game.sizeY+j).endsWith(""+highLightWallRed)&isRedHighLightWall){				
				g.setColor(Color.green);
				g.drawRect(7+i*5, 35+j*5, 4, 4);
			}
		}
		else if(game.fieldOfWalls.get(i*game.sizeY+j).startsWith("PB")){//рисовать точки
			g.setColor(Color.cyan);
			g.fillRect(9+i*5, 37+j*5, 1, 1);
			if(game.fieldOfWalls.get(i*game.sizeY+j).endsWith(""+highLightWallBlue)&!isRedHighLightWall){				
				g.setColor(Color.green);
				g.drawRect(7+i*5, 35+j*5, 4, 4);
			}
		}
		else if(game.fieldOfWalls.get(i*game.sizeY+j).startsWith("CR")){//рисовать связи
			if(game.fieldOfWalls.get(i*game.sizeY+j).endsWith(""+highLightWallRed)&isRedHighLightWall)g.setColor(Color.green);			
			else g.setColor(Color.red);	
			g.drawLine(8+i*5, 37+j*5, 10+i*5, 37+j*5);
			g.drawLine(9+i*5, 36+j*5, 9+i*5, 38+j*5);
		}
		else if(game.fieldOfWalls.get(i*game.sizeY+j).startsWith("CB")){//рисовать связи
			if(game.fieldOfWalls.get(i*game.sizeY+j).endsWith(""+highLightWallBlue)&!isRedHighLightWall)g.setColor(Color.green);			
			else g.setColor(Color.blue);
			g.drawLine(8+i*5, 37+j*5, 10+i*5, 37+j*5);
			g.drawLine(9+i*5, 36+j*5, 9+i*5, 38+j*5);
		}
		else if(game.fieldOfWalls.get(i*game.sizeY+j).startsWith("AR")){//рисовать абстрактные точки
			if(game.fieldOfWalls.get(i*game.sizeY+j).endsWith(""+highLightWallRed)&isRedHighLightWall){
				g.setColor(Color.green);
				g.drawRect(5+i*5, 35+j*5, 4, 4);
			}else g.setColor(Color.white);
			g.drawRect(9+i*5, 36+j*5, 2, 2);
		}
		else if(game.fieldOfWalls.get(i*game.sizeY+j).startsWith("AB")){//рисовать абстрактные точки
			if(game.fieldOfWalls.get(i*game.sizeY+j).endsWith(""+highLightWallBlue)&!isRedHighLightWall){
				g.setColor(Color.green);
				g.drawRect(5+i*5, 35+j*5, 4, 4);
			}else g.setColor(Color.white);
			g.drawRect(8+i*5, 36+j*5, 2, 2);
		}
		else if(game.fieldOfWalls.get(i*game.sizeY+j).startsWith("LR")){//рисовать абстрактные связи
			if(game.fieldOfWalls.get(i*game.sizeY+j).endsWith(""+highLightWallRed)&isRedHighLightWall)g.setColor(Color.green);	
			else g.setColor(Color.red);				
			g.fillRect(9+i*5, 37+j*5, 1, 1);
		}
		else if(game.fieldOfWalls.get(i*game.sizeY+j).startsWith("LB")){//рисовать абстрактные связи			
			if(game.fieldOfWalls.get(i*game.sizeY+j).endsWith(""+highLightWallBlue)&!isRedHighLightWall)g.setColor(Color.green);
			else g.setColor(Color.blue);				
			g.fillRect(9+i*5, 37+j*5, 1, 1);
		}		
	}
	
	//рисование заземленных точек
	/*g.setColor(Color.black);
	for(int i=0;i<game.sizeX;i++)for(int j=0;j<game.sizeY;j++){
		if(game.fieldGroundedAIPoints[i][j].equals("R")){//рисовать точки
			g.drawRect(7+i*5, 35+j*5, 5, 5);
		}
	}*/
	
	//рисование абстрактных точек, но не из wall, а из game
	g.setColor(Color.black);
	for(int i=0;i<game.abstractPoints.size();i++){
		g.drawRect(7+(game.abstractPoints.get(i).x-1)*5, 35+(game.abstractPoints.get(i).y-1)*5, 5, 5);
	}
		
	g.setColor(Color.white);
	for(int i=0;i<game.wallBlue.size();i++){		
		//g.fillRect(6+(wallBlue[i].endX-1)*5, 36+(wallBlue[i].endY-1)*5, 3, 3);
		g.drawLine(8+(game.wallBlue.get(i).wallEndX-1)*5, 37+(game.wallBlue.get(i).wallEndY-1)*5, 10+(game.wallBlue.get(i).wallEndX-1)*5, 37+(game.wallBlue.get(i).wallEndY-1)*5);
		g.drawLine(9+(game.wallBlue.get(i).wallEndX-1)*5, 36+(game.wallBlue.get(i).wallEndY-1)*5, 9+(game.wallBlue.get(i).wallEndX-1)*5, 38+(game.wallBlue.get(i).wallEndY-1)*5);
	}
	g.setColor(Color.white);
	for(int i=0;i<game.wallRed.size();i++){
		//g.fillRect(6+(wallRed[i].endX-1)*5, 36+(wallRed[i].endY-1)*5, 3, 3);
		g.drawLine(8+(game.wallRed.get(i).wallEndX-1)*5, 37+(game.wallRed.get(i).wallEndY-1)*5, 10+(game.wallRed.get(i).wallEndX-1)*5, 37+(game.wallRed.get(i).wallEndY-1)*5);
		g.drawLine(9+(game.wallRed.get(i).wallEndX-1)*5, 36+(game.wallRed.get(i).wallEndY-1)*5, 9+(game.wallRed.get(i).wallEndX-1)*5, 38+(game.wallRed.get(i).wallEndY-1)*5);
	}
	
	g.setColor(Color.black);
	g.drawString("Число скрестов="+game.crossCount, 10, 220);
	g.drawString("Число синих стен="+game.wallBlue.size(), 10, 232);
	g.drawString("Число красных стен="+game.wallRed.size(), 10, 244);
	g.drawString("Выделить стену:", 10, 264);
	g.drawString("- точки", 30, 300);
	g.drawString("- абстрактные точки", 30, 312);
	g.drawString("- связи", 30, 324);
	g.drawString("- абстрактные связи", 30, 336);
	g.drawString("- концы стен", 30, 348);
	
	//точки
	g.setColor(Color.blue);g.fillRect(12, 294, 5, 5);
	g.setColor(Color.cyan);g.fillRect(14, 296, 1, 1);
	g.setColor(Color.red);g.fillRect(22, 294, 5, 5);
	g.setColor(Color.pink);g.fillRect(24, 296, 1, 1);	
	//абстрактные точки
	g.setColor(Color.blue);g.fillRect(12, 306, 5, 5);
	g.setColor(Color.white);g.drawRect(13, 307, 2, 2);
	g.setColor(Color.red);g.fillRect(22, 306, 5, 5);
	g.setColor(Color.white);g.drawRect(23, 307, 2, 2);	
	//связи
	g.setColor(Color.blue);g.drawLine(13, 320, 15, 320);g.drawLine(14, 319, 14, 321);
	g.setColor(Color.red);g.drawLine(23, 320, 25, 320);g.drawLine(24, 319, 24, 321);
	//абстрактные связи
	g.setColor(Color.blue);g.fillRect(14, 332, 1, 1);
	g.setColor(Color.red);g.fillRect(24, 332, 1, 1);
	//концы стен
	g.setColor(Color.blue);g.fillRect(12, 342, 5, 5);
	g.setColor(Color.white);g.drawLine(13, 344, 15, 344);g.drawLine(14, 343, 14, 345);
	g.setColor(Color.red);g.fillRect(22, 342, 5, 5);
	g.setColor(Color.white);g.drawLine(23, 344, 25, 344);g.drawLine(24, 343, 24, 345);
	
	bBlue.repaint();bRed.repaint();
	
	//спектральная диаграмма========================================================================================================
	
	//рисование поля - спектральная диаграмма
	g.setColor(Color.white);
	for(int i=0;i<game.sizeX;i++){for(int j=0;j<game.sizeY;j++){
		if(game.fieldSpectrumBlue[i][j]>=Integer.MAX_VALUE)continue;
		if(game.fieldSpectrumRed[i][j]>=Integer.MAX_VALUE)continue;
		g.setColor(new Color(0,0,255,(int)(game.fieldSpectrumBlue[i][j]/game.fieldSpectrumBlueMax*255.0)));		
		g.fillRect(7+i*5, 370+j*5, 5, 5);
		g.setColor(new Color(255,0,0,(int)(game.fieldSpectrumRed[i][j]/game.fieldSpectrumRedMax*255.0)));
		g.fillRect(7+i*5, 370+j*5, 5, 5);
	
		if(game.fieldSpectrumBlue[i][j]==0&game.fieldSpectrumRed[i][j]==0){g.setColor(Color.white);g.fillRect(7+i*5, 370+j*5, 5, 5);}		
	}}
	//g.drawRect(6, 534, 196, 161);
	
	//рисование поля - места с точками	
	for(int i=0;i<game.sizeX;i++){for(int j=0;j<game.sizeY;j++){
		if(game.fieldSpectrumBlue[i][j]>=Integer.MAX_VALUE){g.setColor(Color.blue);g.fillRect(7+i*5, /*535*/370+j*5, 5, 5);}
		if(game.fieldSpectrumRed[i][j]>=Integer.MAX_VALUE){g.setColor(Color.red);g.fillRect(7+i*5, /*535*/370+j*5, 5, 5);}	
	}}
	g.setColor(Color.black);
	g.drawRect(6, 369, 196, 161);
}

public Color getSmoothedColor(double value,double maxValue,double middle){
	
	if(value>middle*0.95&value<middle*1.05)return Color.white;
	
	if(value>=(maxValue-middle)/2.0+middle){
		return Color.red;
	}else if(value>=middle&value<(maxValue-middle)/2.0+middle){
		return Color.pink;
	}else if(value<middle/2.0){
		return Color.blue;
	}else if(value>=middle/2.0&value<middle){
		return Color.cyan;
	}
	
	/*g.setColor(new Color(139,0,255));//purple		
	g.setColor(new Color(0,0,255));//blue
	g.setColor(new Color(0,91,255));//cyan
	g.setColor(new Color(0,255,0));//green
	g.setColor(new Color(255,255,0));//yellow
	g.setColor(new Color(255,165,0));//orange
	g.setColor(new Color(255,0,0));//red
	*/		
	double spectrumBlockSize=maxValue/8.0;
	try{if(value<spectrumBlockSize)return(new Color(70+(int)(value*69.0/spectrumBlockSize),0,100+(int)(value*155.0/spectrumBlockSize)));}catch(Exception e){System.out.println("purple error");}//purple
	try{if(value>=spectrumBlockSize&value<spectrumBlockSize*2)return(new Color(139-(int)((value-spectrumBlockSize)*139.0/spectrumBlockSize),0,255));}catch(Exception e){System.out.println("purple-blue error");}//purple-blue
	try{if(value>=spectrumBlockSize*2&value<spectrumBlockSize*3)return(new Color(0,(int)((value-spectrumBlockSize*2)*91.0/spectrumBlockSize),255));}catch(Exception e){System.out.println("blue-cyan error");}//blue-cyan		
	try{if(value>=spectrumBlockSize*3&value<spectrumBlockSize*4)return(new Color(0,91+(int)((value-spectrumBlockSize*3)*164.0/spectrumBlockSize),255-(int)((value-spectrumBlockSize*3)*255.0/spectrumBlockSize)));}catch(Exception e){System.out.println("cyan-green error");}//cyan-green
	try{if(value>=spectrumBlockSize*4&value<spectrumBlockSize*5)return(new Color(0+(int)((value-spectrumBlockSize*4)*255.0/spectrumBlockSize),255,0));}catch(Exception e){System.out.println("green-yellow error");}//green-yellow
	try{if(value>=spectrumBlockSize*5&value<spectrumBlockSize*6)return(new Color(255,255-(int)((value-spectrumBlockSize*5)*90.0/spectrumBlockSize),0));}catch(Exception e){System.out.println("yellow-orange error");}//yellow-orange
	try{if(value>=spectrumBlockSize*6&value<spectrumBlockSize*7)return(new Color(255,165-(int)((value-spectrumBlockSize*6)*165.0/spectrumBlockSize),0));}catch(Exception e){System.out.println("orange-red error");}//orange-red
	try{if(value>=spectrumBlockSize*7&value<spectrumBlockSize*8)return(new Color(255-(int)((value-spectrumBlockSize*7)*100.0/spectrumBlockSize),0,0));}catch(Exception e){System.out.println("red error");}//red
	return Color.black;
}

}
