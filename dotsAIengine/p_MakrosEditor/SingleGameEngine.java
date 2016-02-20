package p_MakrosEditor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import p_DotsAI.Protocol;
import p_GUI.DrawSingleGameEngine;
import p_GUI.GameGUI;
import p_TemplateEngine.Template;
import p_TemplateEngine.TemplateType;

public class SingleGameEngine{
	
	p_SingleGameEngine.SingleGameEngine singleGameEngine;
	DrawSingleGameEngine draw=new DrawSingleGameEngine();//рисование игрового поля
	Graphics graphics;
	Template templat;
	int Y=30;//точка начала рисования поля
	int offsetY=GameGUI.offsetY-1;
	
void drawTemplate(Graphics graphics, Template template,ArrayList<Point> lastAI,ArrayList<Point> lastHuman){
	
	this.graphics=graphics;
	this.templat=template;
	
	singleGameEngine=new p_SingleGameEngine.SingleGameEngine(Protocol.sizeX_ME,Protocol.sizeY_ME);
	draw.setSize(Protocol.sizeX_ME,Protocol.sizeY_ME);
	draw.setOffsetY(offsetY);
	
	graphics.setColor(new Color(254,254,254));
	graphics.fillRect(0, Y, GameGUI.squareSize*(Protocol.sizeX_ME+1+GameGUI.offsetX), Protocol.sizeY_ME*(GameGUI.squareSize+1)+5);
	// <drawing-grid>
	graphics.setColor(new Color(225,225,225));
	for (int x = 1; x <= Protocol.sizeX_ME; x++)//vertical line
		graphics.drawLine(GameGUI.squareSize*x+12, Y, GameGUI.squareSize*x+12, Y+GameGUI.squareSize*Protocol.sizeY_ME);
	for (int y = 1; y <= Protocol.sizeY_ME; y++)//gorizontal line
		graphics.drawLine(20, Y-2+y*GameGUI.squareSize, Protocol.sizeX_ME*GameGUI.squareSize+20,Y-2+y*GameGUI.squareSize);
	// </drawing-grid>
	
	((Graphics2D)graphics).setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
	for(int i=0;i<template.templateContent.length;i++){
		move(i%Protocol.sizeX_TE+4,i/Protocol.sizeX_TE+4,template.templateContent[i]);
		move(i%Protocol.sizeX_TE+4,i/Protocol.sizeX_TE+4,template.templateLogic[i]);
	}
	
	drawField(lastAI,lastHuman);
}

public void move(int x, int y, int moveType){
	
	if((x<1)|(y<1)|(x>Protocol.sizeX_ME)|(y>Protocol.sizeY_ME)){}
	else if(moveType==Protocol.templateDotTypeRED_NORMAL){
		if(singleGameEngine.canMakeMove(x, y)){
			singleGameEngine.makeMove(new Point(x,y), Protocol.moveTypeRed);
			//if(draw.getLastAI()==null)
			//	draw.setLastAI(x, y);
		}
	}else if(moveType==Protocol.templateDotTypeRED){
		if(singleGameEngine.canMakeMove(x, y))
			singleGameEngine.makeMove(new Point(x,y), Protocol.moveTypeRed);
	}else if(moveType==Protocol.templateDotTypeBLUE){
		if(singleGameEngine.canMakeMove(x, y))
			singleGameEngine.makeMove(new Point(x,y), Protocol.moveTypeBlue);
	}else if(moveType==Protocol.templateDotTypeANY){
		graphics.setColor(Color.green);
		graphics.fillOval(GameGUI.squareSize*(x+GameGUI.offsetX)-8, y*GameGUI.squareSize+Y-6, GameGUI.pointSize, GameGUI.pointSize);
	}else if(moveType==Protocol.templateDotTypeRED_EMPTY){
		graphics.setColor(Color.pink);
		graphics.fillOval(GameGUI.squareSize*(x+GameGUI.offsetX)-8, y*GameGUI.squareSize+Y-6, GameGUI.pointSize, GameGUI.pointSize);
	}else if(moveType==Protocol.templateDotTypeBLUE_EMPTY){
		graphics.setColor(Color.cyan);
		graphics.fillOval(GameGUI.squareSize*(x+GameGUI.offsetX)-8, y*GameGUI.squareSize+Y-6, GameGUI.pointSize, GameGUI.pointSize);
	}
}

public void drawField(ArrayList<Point> lastAI,ArrayList<Point> lastHuman){
	draw.paint(graphics, singleGameEngine,Protocol.sizeX_ME_half,Protocol.sizeY_ME_half, lastAI, lastHuman);
	if(TemplateType.isSide(templat.templateType)&templat.templateType==TemplateType.templateTypeSQUARE_SIDE|templat.templateType==TemplateType.templateTypeWALL_SIDE){
		graphics.setColor(Color.black);
		graphics.fillRect(GameGUI.squareSize*(Protocol.sizeX_ME+GameGUI.offsetX-3)-8, Y, GameGUI.squareSize*4, Protocol.sizeY_ME*(GameGUI.squareSize+1)+5);
	}
}

int getMouseClickX(MouseEvent me){return (int)(((double)me.getX()-5-(double)((GameGUI.offsetX-1)*GameGUI.squareSize))/(double)GameGUI.squareSize);};
int getMouseClickY(MouseEvent me){return (int)(((double)me.getY()-5-(double)((offsetY-1)*GameGUI.squareSize))/(double)GameGUI.squareSize);};
boolean isCanMakeMove(int x, int y){return singleGameEngine.canMakeMove(x,y);}
void makeMove(int x, int y,int moveType){singleGameEngine.makeMove(new Point(x,y),moveType);}

}
