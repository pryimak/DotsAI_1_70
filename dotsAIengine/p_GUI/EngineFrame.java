package p_GUI;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;

import p_JavaPatterns.C_JFrame;
import p_SingleGameEngine.SingleGameEngine;

public class EngineFrame implements Runnable{
	
	JFrame frame=new JFrame();
	SingleGameEngine engine;
	Thread t=new Thread(this);
	DrawSingleGameEngine draw=new DrawSingleGameEngine();//рисование игрового поля
	
public EngineFrame(SingleGameEngine engine){
	this.engine=engine;
	
	new C_JFrame(frame,"",false,GameGUI.squareSize*(engine.sizeX-2+1+GameGUI.offsetX),
			GameGUI.squareSize*(engine.sizeY-2+1+GameGUI.offsetY*1)-20,new Color(255,255,255));
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	t.start();
}
	
public void paint(Graphics g){
	g.setColor(new Color(254,254,254));
	g.fillRect(0, 50, GameGUI.squareSize*(engine.sizeX-2+1+GameGUI.offsetX), engine.sizeY-2*(GameGUI.squareSize+1));
	draw.paint(g, engine,0,0,null,null);
}

public void run(){
	for(;;){
		try{t.sleep(1000);}
		catch(Exception e){}
		paint(frame.getGraphics());
	}
}

}