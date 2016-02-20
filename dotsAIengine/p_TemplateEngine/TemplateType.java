package p_TemplateEngine;

import java.awt.Point;

import javax.swing.ImageIcon;

import p_DotsAI.Protocol;
import p_DotsAI.Protocol.Game;
import p_JavaPatterns.C_Resource;
import p_Statistics.MoveStatType;

public class TemplateType{
	
	public static int templateTypeBEGIN=0;
	public static int templateTypeSQUARE_SIDE=1;
	public static int templateTypeSQUARE=2;
	public static int templateTypeWALL=3;
	public static int templateTypeWALL_SIDE=4;
	public static int templateTypeABSTRACT_DEFENSE_WALL=5;
	public static int templateTypeABSTRACT_ATTACK_WALL=6;
	public static int templateTypeBLUE_SURROUND=7;
	public static int templateTypeRED_SURROUND=8;		
	public static int templateTypeGLOBAL_ATTACK=9;
	public static int templateTypeGLOBAL_DESTROY=10;
	public static int templateTypeWALL_DESTROY=11;
	public static int templateTypeFINAL_RED_ATTACK=12;
	public static int templateTypeCONTINUED_RED_ATTACK=13;
	public static int templateTypeGROUND_SIDE=14;
	public static int templateTypeGROUND=15;	
	public static int lastIndexOfTemplateType=16;

public static String toString(int type) {//аббревиатура шаблона
	if(type==templateTypeSQUARE_SIDE)return "sst";
	if(type==templateTypeSQUARE)return "sqt";
	if(type==templateTypeWALL)return "wlt";
	if(type==templateTypeWALL_SIDE)return "wst";
	if(type==templateTypeBLUE_SURROUND)return "bst";
	if(type==templateTypeRED_SURROUND)return "rst";
	if(type==templateTypeFINAL_RED_ATTACK)return "fra";
	if(type==templateTypeCONTINUED_RED_ATTACK)return "cra";
	if(type==templateTypeGROUND_SIDE)return "grs";
	if(type==templateTypeGROUND)return "grt";		
	if(type==templateTypeWALL_DESTROY)return "wdt";
	if(type==templateTypeBEGIN)return "bgt";
	if(type==templateTypeGLOBAL_ATTACK)return "gat";
	if(type==templateTypeGLOBAL_DESTROY)return "gdt";
	if(type==templateTypeABSTRACT_DEFENSE_WALL)return "adw";
	if(type==templateTypeABSTRACT_ATTACK_WALL)return "aaw";
	return "err";
}

public static String getTemplateTypeName(int type) {//имя шаблона
	if(type==templateTypeSQUARE_SIDE)return "Квадрат у края";
	if(type==templateTypeSQUARE)return "Квадратный шаблон";
	if(type==templateTypeWALL)return "Стенка";
	if(type==templateTypeWALL_SIDE)return "Стенка у края";
	if(type==templateTypeBLUE_SURROUND)return "Синие хотят окружить";
	if(type==templateTypeRED_SURROUND)return "Красные хотят окружить";
	if(type==templateTypeWALL_DESTROY)return "Разрыв стен синих";
	if(type==templateTypeFINAL_RED_ATTACK)return "Атака красных - завершение";
	if(type==templateTypeCONTINUED_RED_ATTACK)return "Атака красных - в прогрессе";
	if(type==templateTypeGROUND_SIDE)return "Заземление у края";
	if(type==templateTypeGROUND)return "Заземление красных";
	if(type==templateTypeBEGIN)return "Начало игры";
	if(type==templateTypeGLOBAL_ATTACK)return "Глобальная атака";
	if(type==templateTypeGLOBAL_DESTROY)return "Глобальная защита";
	if(type==templateTypeABSTRACT_DEFENSE_WALL)return "Абстрактная защитная стена";
	if(type==templateTypeABSTRACT_ATTACK_WALL)return "Абстрактная атакующая стена";
	return "err";
}

public static MoveStatType getMoveStatType(int type){//тип хода в статистике игры
	if(type==templateTypeSQUARE_SIDE)return MoveStatType.SQUARE_SIDE;			
	if(type==templateTypeBLUE_SURROUND)return MoveStatType.BLUE_SURROUND;			
	if(type==templateTypeRED_SURROUND)return MoveStatType.RED_SURROUND;
	if(type==templateTypeSQUARE)return MoveStatType.SQUARE;
	if(type==templateTypeWALL_DESTROY)return MoveStatType.WALL_DESTROY;
	if(type==templateTypeFINAL_RED_ATTACK)return MoveStatType.FINAL_RED_ATTACK;
	if(type==templateTypeCONTINUED_RED_ATTACK)return MoveStatType.CONTINUED_RED_ATTACK;
	if(type==templateTypeWALL_SIDE)return MoveStatType.WALL_SIDE;
	if(type==templateTypeWALL)return MoveStatType.WALL;
	if(type==templateTypeGROUND_SIDE)return MoveStatType.GROUND_SIDE;
	if(type==templateTypeGROUND)return MoveStatType.GROUND;
	if(type==templateTypeBEGIN)return MoveStatType.BEGIN;	
	if(type==templateTypeGLOBAL_ATTACK)return MoveStatType.GLOBAL_ATTACK;
	if(type==templateTypeGLOBAL_DESTROY)return MoveStatType.GLOBAL_DESTROY;
	if(type==templateTypeABSTRACT_DEFENSE_WALL)return MoveStatType.ABSTRACT_DEFENSE_WALL;
	if(type==templateTypeABSTRACT_ATTACK_WALL)return MoveStatType.ABSTRACT_ATTACK_WALL;
	return null;
}

public static ImageIcon getImageIcon(int type) {return new ImageIcon(C_Resource.templateTypes+toString(type)+".png");}

public static boolean isUseTargetPoints(int type){//использует ли тип шаблона целевые точки
	if(type==templateTypeBEGIN)return false;
	if(type==templateTypeSQUARE_SIDE)return false;		
	if(type==templateTypeSQUARE)return false;
	if(type==templateTypeWALL)return false;
	if(type==templateTypeWALL_SIDE)return false;
	if(type==templateTypeBLUE_SURROUND)return true;
	if(type==templateTypeRED_SURROUND)return true;
	if(type==templateTypeWALL_DESTROY)return false;
	if(type==templateTypeFINAL_RED_ATTACK)return true;
	if(type==templateTypeCONTINUED_RED_ATTACK)return false;
	if(type==templateTypeGROUND)return false;
	if(type==templateTypeGROUND_SIDE)return false;
	if(type==templateTypeGLOBAL_ATTACK)return false;
	if(type==templateTypeGLOBAL_DESTROY)return false;
	if(type==lastIndexOfTemplateType)return false;
	if(type==templateTypeABSTRACT_DEFENSE_WALL)return false;
	if(type==templateTypeABSTRACT_ATTACK_WALL)return false;
	return true;
}

public static boolean isSide(int type){//боковой тип шаблона
	if(type==templateTypeBEGIN)return false;
	if(type==templateTypeSQUARE_SIDE)return true;		
	if(type==templateTypeSQUARE)return false;
	if(type==templateTypeWALL)return false;
	if(type==templateTypeWALL_SIDE)return true;
	if(type==templateTypeBLUE_SURROUND)return false;
	if(type==templateTypeRED_SURROUND)return false;
	if(type==templateTypeWALL_DESTROY)return false;
	if(type==templateTypeFINAL_RED_ATTACK)return false;
	if(type==templateTypeCONTINUED_RED_ATTACK)return false;
	if(type==templateTypeGROUND)return false;
	if(type==templateTypeGROUND_SIDE)return true;
	if(type==templateTypeGLOBAL_ATTACK)return false;
	if(type==templateTypeGLOBAL_DESTROY)return false;
	if(type==lastIndexOfTemplateType)return false;
	if(type==templateTypeABSTRACT_DEFENSE_WALL)return false;
	if(type==templateTypeABSTRACT_ATTACK_WALL)return false;
	return true;
}

public static boolean isTemplateBaseSearchFromStartIdx(int type){//проход по базе шаблонов с начала или с конца
	if(type==templateTypeBEGIN)return false;
	if(type==templateTypeSQUARE_SIDE)return false;		
	if(type==templateTypeSQUARE)return false;
	if(type==templateTypeWALL)return false;
	if(type==templateTypeWALL_SIDE)return false;
	if(type==templateTypeBLUE_SURROUND)return false;
	if(type==templateTypeRED_SURROUND)return false;
	if(type==templateTypeWALL_DESTROY)return false;
	if(type==templateTypeFINAL_RED_ATTACK)return true;
	if(type==templateTypeCONTINUED_RED_ATTACK)return false;
	if(type==templateTypeGROUND)return false;
	if(type==templateTypeGROUND_SIDE)return false;
	if(type==templateTypeGLOBAL_ATTACK)return false;
	if(type==templateTypeGLOBAL_DESTROY)return false;
	if(type==lastIndexOfTemplateType)return false;
	if(type==templateTypeABSTRACT_DEFENSE_WALL)return false;
	if(type==templateTypeABSTRACT_ATTACK_WALL)return false;
	return true;
}

public static int getTemplateType(String str){//получить тип шаблона
	if(str.equals(toString(templateTypeBEGIN)))return templateTypeBEGIN;
	if(str.equals(toString(templateTypeSQUARE_SIDE)))return templateTypeSQUARE_SIDE;
	if(str.equals(toString(templateTypeSQUARE)))return templateTypeSQUARE;
	if(str.equals(toString(templateTypeWALL)))return templateTypeWALL;
	if(str.equals(toString(templateTypeWALL_SIDE)))return templateTypeWALL_SIDE;
	if(str.equals(toString(templateTypeBLUE_SURROUND)))return templateTypeBLUE_SURROUND;
	if(str.equals(toString(templateTypeRED_SURROUND)))return templateTypeRED_SURROUND;
	if(str.equals(toString(templateTypeWALL_DESTROY)))return templateTypeWALL_DESTROY;
	if(str.equals(toString(templateTypeFINAL_RED_ATTACK)))return templateTypeFINAL_RED_ATTACK;
	if(str.equals(toString(templateTypeCONTINUED_RED_ATTACK)))return templateTypeCONTINUED_RED_ATTACK;
	if(str.equals(toString(templateTypeGROUND)))return templateTypeGROUND;
	if(str.equals(toString(templateTypeGROUND_SIDE)))return templateTypeGROUND_SIDE;
	if(str.equals(toString(templateTypeGLOBAL_ATTACK)))return templateTypeGLOBAL_ATTACK;
	if(str.equals(toString(templateTypeGLOBAL_DESTROY)))return templateTypeGLOBAL_DESTROY;
	if(str.equals(toString(templateTypeABSTRACT_DEFENSE_WALL)))return templateTypeABSTRACT_DEFENSE_WALL;
	if(str.equals(toString(templateTypeABSTRACT_ATTACK_WALL)))return templateTypeABSTRACT_ATTACK_WALL;
	return lastIndexOfTemplateType;
}

public static byte[] getContentForTemplateCreation(Game game,Point p,byte[][] fieldState,int type){try{//получить содержимое шаблона
	byte[] content=new byte[81];	
	if(!isSide(type)){
		int idx=0;
		for(int j=0;j<9;j++){
			for(int i=0;i<9;i++){
				try{content[idx]=fieldState[i+p.x-5][j+p.y-5];idx++;}catch(Exception e){content[idx]=Protocol.templateDotTypeNULL;idx++;}
			}
		}
		return content;
	}else{
		int s=TemplateFieldSideType.getFieldSideType(game, new Point(p.x, p.y));
		if(s==TemplateFieldSideType.templateFieldSideTypeRIGHT){
			int idx=0;
			for(int j=p.y-5;j<p.y+4;j++){
				for(int i=game.sizeX-8;i<game.sizeX;i++){
					try{content[idx]=fieldState[i][j];idx++;}catch(Exception e){content[idx]=Protocol.templateDotTypeNULL;idx++;}
				}					
				content[idx]=Protocol.templateDotTypeLAND;idx++;
			}
			return content;
		}
		if(s==TemplateFieldSideType.templateFieldSideTypeLEFT){
			int idx=0;
			for(int j=p.y-5;j<p.y+4;j++){
				content[idx]=Protocol.templateDotTypeLAND;idx++;
				for(int i=0;i<8;i++){
					try{content[i*j+i]=fieldState[i][j];idx++;}catch(Exception e){content[i*j+i]=Protocol.templateDotTypeNULL;idx++;}
				}						
			}
			return content;
		}
		if(s==TemplateFieldSideType.templateFieldSideTypeBOTTOM){
			int idx=0;
			for(int j=(game.sizeY-8);j<game.sizeY;j++){
				for(int i=p.x-5;i<p.x+4;i++){
					try{content[idx]=fieldState[i][j];idx++;}catch(Exception e){content[idx]=Protocol.templateDotTypeNULL;idx++;}
				}
			}
			for(int i=0;i<9;i++){
				content[idx]=Protocol.templateDotTypeLAND;
				idx++;
			}
			return content;
		}
		if(s==TemplateFieldSideType.templateFieldSideTypeTOP){
			int idx=0;
			for(int i=0;i<9;i++){
				content[idx]=Protocol.templateDotTypeLAND;
				idx++;
			}
			for(int j=0;j<8;j++){
				for(int i=p.x-5;i<p.x+4;i++){
					try{content[idx]=fieldState[i][j];idx++;}catch(Exception e){content[idx]=Protocol.templateDotTypeNULL;idx++;}
				}
			}
			return content;
		}
		return content;
	}
	}catch(Exception e){return null;}
}

public static int getContentSymbolNotSide(Game game,Point p,byte[][] fieldState,int i){
	try{return fieldState[i%9+p.x-5][i/9+p.y-5];}catch(Exception e){return Protocol.templateDotTypeNULL;}
}

public static int getContentSymbolRight(Game game,Point p,byte[][] fieldState,int i){		
	if(i%9==8)return Protocol.templateDotTypeLAND;
	try{return fieldState[game.sizeX-8+i%9][i/9+p.y-5];}catch(Exception e){return Protocol.templateDotTypeNULL;}
}

public static int getContentSymbolLeft(Game game,Point p,byte[][] fieldState,int i){	
	if(i%9==0)return Protocol.templateDotTypeLAND;
	try{return fieldState[i%9-1][i/9+p.y-5];}catch(Exception e){return Protocol.templateDotTypeNULL;}
}

public static int getContentSymbolBottom(Game game,Point p,byte[][] fieldState,int i){	
	if(i/9==8)return Protocol.templateDotTypeLAND;
	try{return fieldState[i%9+p.x-5][game.sizeY-8+i/9];}catch(Exception e){return Protocol.templateDotTypeNULL;}
}

public static int getContentSymbolTop(Game game,Point p,byte[][] fieldState,int i){
	if(i/9==0)return Protocol.templateDotTypeLAND;
	try{return fieldState[i%9+p.x-5][i/9-1];}catch(Exception e){return Protocol.templateDotTypeNULL;}
}
	
}
