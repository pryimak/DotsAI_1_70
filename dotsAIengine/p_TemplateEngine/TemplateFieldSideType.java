package p_TemplateEngine;

import java.awt.Point;

import p_DotsAI.Protocol.Game;

public class TemplateFieldSideType{
	
public static int templateFieldSideTypeTOP=0;
public static int templateFieldSideTypeBOTTOM=1;
public static int templateFieldSideTypeLEFT=2;
public static int templateFieldSideTypeRIGHT=3;
public static int templateFieldSideTypeINSIDE=4;

public static int getFieldSideType(Game game,Point p){	
	if(p.x>(game.sizeX-8)){return templateFieldSideTypeRIGHT;}
	if(p.x<9){return templateFieldSideTypeLEFT;}
	if(p.y>(game.sizeY-8)){return templateFieldSideTypeBOTTOM;}
	if(p.y<9){return templateFieldSideTypeTOP;}
	return templateFieldSideTypeINSIDE;
}

public static Point getPointByFieldSideType(Game game,Point initialPoint,Point foundedPoint,boolean isSide){try{
	if(isSide){
		int s=getFieldSideType(game, initialPoint);
		if(s==templateFieldSideTypeTOP)return new Point(initialPoint.x+foundedPoint.x-4,foundedPoint.y);
		if(s==templateFieldSideTypeBOTTOM)return new Point(initialPoint.x+foundedPoint.x-4,game.sizeY+foundedPoint.y-7);			
		if(s==templateFieldSideTypeLEFT)return new Point(foundedPoint.x,initialPoint.y+foundedPoint.y-4);			
		if(s==templateFieldSideTypeRIGHT)return new Point(game.sizeX+foundedPoint.x-7,initialPoint.y+foundedPoint.y-4);			
		return new Point(foundedPoint.x+initialPoint.x-4,foundedPoint.y+initialPoint.y-4);
	}else return new Point(foundedPoint.x+initialPoint.x-4,foundedPoint.y+initialPoint.y-4);
}catch(Exception e){return null;}}

}
