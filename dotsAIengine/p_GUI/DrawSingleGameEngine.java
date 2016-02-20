package p_GUI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;

import p_DotsAI.Protocol;
import p_SingleGameEngine.SingleGameEngine;
import p_SingleGameEngine.Surrounding;

public class DrawSingleGameEngine{
	
	int sizeX;//=Protocol.sizeX;
	int sizeY;//=Protocol.sizeY;
	int pointSize=GameGUI.pointSize;
	int offsetY=-1;
	Font font9=new Font("Verdana",9,9);
	
public void setSize(int x,int y){sizeX=x;sizeY=y;}
public void setOffsetY(int offsetY){this.offsetY=offsetY;}

public void fillPoint(Graphics graphics,int x,int y,Color color){
	graphics.setColor(color);
	graphics.fillOval(getPixel(x, y).x - pointSize/2, getPixel(x, y).y - pointSize/2, pointSize-2, pointSize-2);
}

public void drawPoint(Graphics graphics,int x,int y,Color color){
	graphics.setColor(color);
	graphics.drawOval(getPixel(x, y).x - pointSize/2, getPixel(x, y).y - pointSize/2, pointSize, pointSize);
}

public void fillSquare(Graphics graphics,int x,int y,Color color,int level){
	graphics.setColor(color);
	graphics.fillRect(getPixel(x, y).x - pointSize/2-2, getPixel(x, y).y - pointSize/2-2, pointSize+4, pointSize+4);
	graphics.setColor(Color.white);
	graphics.setFont(font9);
	graphics.drawString(level+"", getPixel(x, y).x - pointSize/2 + (((level+"").length()==1)?1:-3), getPixel(x, y).y - pointSize/2 + pointSize);
}

public void drawSquare(Graphics graphics,int x,int y,Color color,int level){
	graphics.setColor(Color.white);
	graphics.fillRect(getPixel(x, y).x - pointSize/2-2, getPixel(x, y).y - pointSize/2-2, pointSize+4, pointSize+4);
	graphics.setColor(color);
	graphics.drawRect(getPixel(x, y).x - pointSize/2-2, getPixel(x, y).y - pointSize/2-2, pointSize+4, pointSize+4);
	graphics.setFont(font9);
	graphics.drawString(level+"", getPixel(x, y).x - pointSize/2 + (((level+"").length()==1)?1:-3), getPixel(x, y).y - pointSize/2 + pointSize);
}

public void paint(Graphics graphics,SingleGameEngine singleGameEngine,int startCoordinateX,int startCoordinateY,ArrayList<Point> lastAI,ArrayList<Point> lastHuman){
	sizeX=singleGameEngine.sizeX-2;
	sizeY=singleGameEngine.sizeY-2;
	
	// <drawing-grid>
	if(lastHuman==null&lastAI==null){
		graphics.setColor(new Color(225,225,225));
		for (int x = 1; x <= sizeX; x++) 
			graphics.drawLine(getPixel(x, 0.5).x, getPixel(x, 0.5).y, getPixel(x,sizeY + 0.5).x, getPixel(x, sizeY + 0.5).y);
		for (int y = 1; y <= sizeY; y++)
			graphics.drawLine(getPixel(0.5, y).x, getPixel(0.5, y).y, getPixel(sizeX + 0.5, y).x, getPixel(sizeX + 0.5, y).y);
	}
	// </drawing-grid>
	
	((Graphics2D)graphics).setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
	
	for (int x = 1; x <= sizeX; x++) {
		for (int y = 1; y <= sizeY; y++) {
			// drawing circles
			int pointDiameter = pointSize-1;
			int drawX = getPixel(x, y).x - pointSize/2;
			int drawY = getPixel(x, y).y - pointSize/2;
			int dotType=singleGameEngine.field[x][y].type;
			if(dotType==singleGameEngine.dotTypeBlue){
				graphics.setColor(new Color(21, 96, 189, 255));
				graphics.fillOval(drawX, drawY, pointDiameter, pointDiameter);
			}else if(dotType==singleGameEngine.dotTypeBlueTired){
				graphics.setColor(new Color(0, 0, 255, 128));
				graphics.fillOval(drawX, drawY, pointDiameter, pointDiameter);
			}else if(dotType==singleGameEngine.dotTypeRedEatedBlue){
				graphics.setColor(new Color(21, 96, 189, 255));
				graphics.fillOval(drawX, drawY, pointDiameter, pointDiameter);
			}else if(dotType==singleGameEngine.dotTypeRed){
				graphics.setColor(new Color(255, 0, 0, 255));
				graphics.fillOval(drawX, drawY, pointDiameter, pointDiameter);
			}else if(dotType==singleGameEngine.dotTypeRedTired){
				graphics.setColor(new Color(255, 0, 0, 128));
				graphics.fillOval(drawX, drawY, pointDiameter, pointDiameter);
			}else if(dotType==singleGameEngine.dotTypeBlueEatedRed){
				graphics.setColor(new Color(255, 0, 0, 255));
				graphics.fillOval(drawX, drawY, pointDiameter, pointDiameter);					
			}else if(dotType==singleGameEngine.dotTypeBlueCtrl){
			}else if(dotType==singleGameEngine.dotTypeRedCtrl){}
		}
	}
	
	//int pointDiameter = pointSize;
	
	// <drawing-last-move>
	/*if(lastHuman==null|lastAI==null)*/try{		
		if(singleGameEngine.lastMoveType==Protocol.moveTypeRed)graphics.setColor(new Color(255, 0, 0, 100));
		else graphics.setColor(new Color(21, 96, 189, 100));//синий
		int pixelX = getPixel(singleGameEngine.lastPoint.x, singleGameEngine.lastPoint.y).x;
		int pixelY = getPixel(singleGameEngine.lastPoint.x, singleGameEngine.lastPoint.y).y;
		int drawX = pixelX - pointSize/2;
		int drawY = pixelY - pointSize/2;
		graphics.drawOval(drawX-2, drawY-2, pointSize+2, pointSize+2);//нарисовать последний ход
		graphics.drawOval(drawX-3, drawY-3, pointSize+4, pointSize+4);//нарисовать последний ход
	}catch(Exception e){}
	
	// <drawing-last-move (for makros)>
	if(lastHuman!=null)if(lastHuman.size()>0){
		graphics.setColor(new Color(21, 96, 189, 100));//синий
		for(int i=0;i<lastHuman.size();i++){
			int pixelX = getPixel(lastHuman.get(i).x, lastHuman.get(i).y).x;
			int pixelY = getPixel(lastHuman.get(i).x, lastHuman.get(i).y).y;
			int drawX = pixelX - pointSize/2;
			int drawY = pixelY - pointSize/2;
			graphics.drawOval(drawX-2, drawY-2, pointSize+2, pointSize+2);//нарисовать последний ход
			graphics.drawOval(drawX-3, drawY-3, pointSize+4, pointSize+4);//нарисовать последний ход
		}
	}
	if(lastAI!=null)if(lastAI.size()>0){
		graphics.setColor(new Color(255, 0, 0, 100));
		for(int i=0;i<lastAI.size();i++){
			int pixelX = getPixel(lastAI.get(i).x, lastAI.get(i).y).x;
			int pixelY = getPixel(lastAI.get(i).x, lastAI.get(i).y).y;
			int drawX = pixelX - pointSize/2;
			int drawY = pixelY - pointSize/2;
			graphics.drawOval(drawX-2, drawY-2, pointSize+2, pointSize+2);//нарисовать последний ход
			graphics.drawOval(drawX-3, drawY-3, pointSize+4, pointSize+4);//нарисовать последний ход
		}
	}
	
	{
		// <drawing-surroundings>
		ArrayList<Surrounding> surroundingsList = singleGameEngine.getSurroundings();
		for (int surroundingIndex = 0; surroundingIndex < surroundingsList.size(); surroundingIndex++) {
			Surrounding surrounding=surroundingsList.get(surroundingIndex);
			if((surrounding.type==singleGameEngine.surroundingTypeBlueCtrl)||(surrounding.type==singleGameEngine.surroundingTypeRedCtrl))continue;			
			
			Polygon polygon = new Polygon();
			for (int pathIndex = 0; pathIndex < surrounding.path.size(); pathIndex++) {
				int dotX = surrounding.path.get(pathIndex).x;
				int dotY = surrounding.path.get(pathIndex).y;
				polygon.addPoint(getPixel(dotX, dotY).x, getPixel(dotX, dotY).y);
			}
			if(surrounding.type==singleGameEngine.surroundingTypeBlue){graphics.setColor(new Color(21, 96, 189, 90));}
			if(surrounding.type==singleGameEngine.surroundingTypeRed){graphics.setColor(new Color(255, 0, 0, 90));}
			if(surrounding.type==singleGameEngine.surroundingTypeBlueCtrl){graphics.setColor(new Color(21, 96, 189, 185));}
			if(surrounding.type==singleGameEngine.surroundingTypeRedCtrl){graphics.setColor(new Color(255, 0, 0, 20));}

			graphics.fillPolygon(polygon);

			if(surrounding.type==singleGameEngine.surroundingTypeBlue){graphics.setColor(new Color(21, 96, 189, 185));}
			if(surrounding.type==singleGameEngine.surroundingTypeRed){graphics.setColor(new Color(255, 0, 0, 255));}
			if(surrounding.type==singleGameEngine.surroundingTypeBlueCtrl){graphics.setColor(new Color(21, 96, 189, 185));}
			if(surrounding.type==singleGameEngine.surroundingTypeRedCtrl){graphics.setColor(new Color(255, 0, 0, 0));}

			Graphics2D graphics2d = (Graphics2D) graphics;
			graphics2d.setStroke(new BasicStroke((float) (3),BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

			graphics.drawPolygon(polygon);
		}
		// </drawing-surroundings>
	}

	{
		// <drawing-coordinates>
		Font font = graphics.getFont();
		font = font.deriveFont((GameGUI.squareSize * 0.5f));
		graphics.setFont(font);
		graphics.setColor(Color.BLACK);
		for (int x = 1; x <= sizeX; x++) {
			Point pixel = getPixel(x, sizeY + 1.1);
			pixel.translate(-graphics.getFontMetrics().stringWidth(""+x) / 2, 0);
			graphics.drawString(""+(x-startCoordinateX), pixel.x, pixel.y);
		}
		for (int y = 1; y <= sizeY; y++) {
			Point pixel = getPixel(.25, y);
			pixel.translate(-graphics.getFontMetrics().stringWidth(""+y), +graphics.getFontMetrics().getHeight()/ 2);
			graphics.drawString(""+(y-startCoordinateY), pixel.x, pixel.y);
		}
		// </drawing-coordinates>
	}
}	
// </drawing-area>

private Point getPixel(double x, double y) {
	Point result = new Point();
	result.x = (int) (GameGUI.squareSize * (x + GameGUI.offsetX - 0.25));
	result.y = (int) (GameGUI.squareSize * (y + getOffsetY()- 0.25));
	return result;
}

private int getOffsetY(){
	if(offsetY!=-1)return offsetY;
	return GameGUI.offsetY;
}

}
