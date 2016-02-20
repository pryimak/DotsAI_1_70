package p_TemplateEngine;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import p_DotsAI.Protocol;
import p_DotsAI.Protocol.Game;
import p_MakrosEngine.Makros;
import p_MakrosEngine.MakrosApplicationInGame;
import p_SingleGameEngine.SingleGameEngine;

public class TemplateView {
	
	public byte[] strTemplateWithoutTargets;
	public byte[] strTemplateWithTargets;
	public int templateRotationType;
	public int templateType;
	public int templateIndex;
	public ArrayList<Point> point_RED_ATTACK;
	public ArrayList<Point> point_BLUE_ATTACK;
	public ArrayList<Point> point_RED_NORMAL;
	public ArrayList<Point> point_RED_PROTECTION;
	
	public TemplateView(byte[] templateContent,byte[] templateLogic,int templateRotationType1,int templateType1,int templateIndex1){
		templateRotationType=templateRotationType1;
		templateType=templateType1;
		templateIndex=templateIndex1;
		
		strTemplateWithoutTargets=TemplateRotationType.getTransformArray(templateRotationType,templateContent);
		strTemplateWithTargets=TemplateRotationType.getTransformArray(templateRotationType,templateLogic);
		
		point_RED_NORMAL=getPointsCoordinates(strTemplateWithTargets,Protocol.templateDotTypeRED_NORMAL);
		point_RED_ATTACK=getPointsCoordinates(strTemplateWithTargets,Protocol.templateDotTypeRED_ATTACK);
		point_RED_PROTECTION=getPointsCoordinates(strTemplateWithTargets,Protocol.templateDotTypeRED_PROTECTION);
		point_BLUE_ATTACK=getPointsCoordinates(strTemplateWithTargets,Protocol.templateDotTypeBLUE_ATTACK);
	}
	
	/*private String getTemplateWithoutTargets(String str){
		str=str.replaceAll(Protocol.templateDotTypeRED_NORMAL,Protocol.templateDotTypeNULL);
		str=str.replaceAll(Protocol.templateDotTypeRED_ATTACK,Protocol.templateDotTypeNULL);
		str=str.replaceAll(Protocol.templateDotTypeRED_PROTECTION,Protocol.templateDotTypeNULL);
		str=str.replaceAll(Protocol.templateDotTypeBLUE_ATTACK,Protocol.templateDotTypeNULL);
		return str;
	}*/
	
	public ArrayList<Point> getPointsCoordinates(byte[] content,int dot){
		ArrayList<Point> point=new ArrayList<Point>();
		for(int i=0;i<content.length;i++)if(content[i]==dot){point.add(new Point(i%Protocol.sizeX_TE,i/Protocol.sizeX_TE));}
		return point;
	}
	
	public boolean isEquals(Game game,Point point,byte[][] fieldState,boolean isSide,int fieldSideType){try{
		if(!isSide){//шаблон не для края поля
			
			/*boolean isAtSide=false;//но шаблон может применяться на краю поля
			if(point.x>(game.sizeX-4)|point.x<5|point.y>(game.sizeY-4)|point.y<5){
				isAtSide=true;			
			}*/
			/*String s1="",s2="";
			if(templateIndex==11104){
				for(int i=0;i<81;i++){
					s1+=strTemplateWithoutTargets[i];
				}
				for(int j=0;j<9;j++){
					for(int i=0;i<9;i++){
						s2+=fieldState[i+point.x-5][j+point.y-5];
					}
				}
				System.out.println("strTemplateWithoutTargets = "+s1);
				System.out.println("fieldState = "+s2);
			}*/
			
			for(int i=36;i<=44;i++)if(!isEqualsBySymbol(TemplateType.getContentSymbolNotSide(game,point,fieldState,i),strTemplateWithoutTargets[i]))return false;
			for(int i=27;i<=35;i++)if(!isEqualsBySymbol(TemplateType.getContentSymbolNotSide(game,point,fieldState,i),strTemplateWithoutTargets[i]))return false;
			for(int i=45;i<=53;i++)if(!isEqualsBySymbol(TemplateType.getContentSymbolNotSide(game,point,fieldState,i),strTemplateWithoutTargets[i]))return false;
			for(int i=18;i<=26;i++)if(!isEqualsBySymbol(TemplateType.getContentSymbolNotSide(game,point,fieldState,i),strTemplateWithoutTargets[i]))return false;
			for(int i=54;i<=62;i++)if(!isEqualsBySymbol(TemplateType.getContentSymbolNotSide(game,point,fieldState,i),strTemplateWithoutTargets[i]))return false;
			for(int i=9;i<=17;i++)if(!isEqualsBySymbol(TemplateType.getContentSymbolNotSide(game,point,fieldState,i),strTemplateWithoutTargets[i]))return false;
			for(int i=63;i<=71;i++)if(!isEqualsBySymbol(TemplateType.getContentSymbolNotSide(game,point,fieldState,i),strTemplateWithoutTargets[i]))return false;
			for(int i=0;i<=8;i++)if(!isEqualsBySymbol(TemplateType.getContentSymbolNotSide(game,point,fieldState,i),strTemplateWithoutTargets[i]))return false;
			for(int i=72;i<=80;i++)if(!isEqualsBySymbol(TemplateType.getContentSymbolNotSide(game,point,fieldState,i),strTemplateWithoutTargets[i]))return false;
		}else if(fieldSideType==TemplateFieldSideType.templateFieldSideTypeLEFT){
			for(int i=36;i<=44;i++)if(!isEqualsBySymbol(TemplateType.getContentSymbolLeft(game,point,fieldState,i),strTemplateWithoutTargets[i]))return false;
			for(int i=27;i<=35;i++)if(!isEqualsBySymbol(TemplateType.getContentSymbolLeft(game,point,fieldState,i),strTemplateWithoutTargets[i]))return false;
			for(int i=45;i<=53;i++)if(!isEqualsBySymbol(TemplateType.getContentSymbolLeft(game,point,fieldState,i),strTemplateWithoutTargets[i]))return false;
			for(int i=18;i<=26;i++)if(!isEqualsBySymbol(TemplateType.getContentSymbolLeft(game,point,fieldState,i),strTemplateWithoutTargets[i]))return false;
			for(int i=54;i<=62;i++)if(!isEqualsBySymbol(TemplateType.getContentSymbolLeft(game,point,fieldState,i),strTemplateWithoutTargets[i]))return false;
			for(int i=9;i<=17;i++)if(!isEqualsBySymbol(TemplateType.getContentSymbolLeft(game,point,fieldState,i),strTemplateWithoutTargets[i]))return false;
			for(int i=63;i<=71;i++)if(!isEqualsBySymbol(TemplateType.getContentSymbolLeft(game,point,fieldState,i),strTemplateWithoutTargets[i]))return false;
			for(int i=0;i<=8;i++)if(!isEqualsBySymbol(TemplateType.getContentSymbolLeft(game,point,fieldState,i),strTemplateWithoutTargets[i]))return false;
			for(int i=72;i<=80;i++)if(!isEqualsBySymbol(TemplateType.getContentSymbolLeft(game,point,fieldState,i),strTemplateWithoutTargets[i]))return false;
		}else if(fieldSideType==TemplateFieldSideType.templateFieldSideTypeRIGHT){
			for(int i=36;i<=44;i++)if(!isEqualsBySymbol(TemplateType.getContentSymbolRight(game,point,fieldState,i),strTemplateWithoutTargets[i]))return false;
			for(int i=27;i<=35;i++)if(!isEqualsBySymbol(TemplateType.getContentSymbolRight(game,point,fieldState,i),strTemplateWithoutTargets[i]))return false;
			for(int i=45;i<=53;i++)if(!isEqualsBySymbol(TemplateType.getContentSymbolRight(game,point,fieldState,i),strTemplateWithoutTargets[i]))return false;
			for(int i=18;i<=26;i++)if(!isEqualsBySymbol(TemplateType.getContentSymbolRight(game,point,fieldState,i),strTemplateWithoutTargets[i]))return false;
			for(int i=54;i<=62;i++)if(!isEqualsBySymbol(TemplateType.getContentSymbolRight(game,point,fieldState,i),strTemplateWithoutTargets[i]))return false;
			for(int i=9;i<=17;i++)if(!isEqualsBySymbol(TemplateType.getContentSymbolRight(game,point,fieldState,i),strTemplateWithoutTargets[i]))return false;
			for(int i=63;i<=71;i++)if(!isEqualsBySymbol(TemplateType.getContentSymbolRight(game,point,fieldState,i),strTemplateWithoutTargets[i]))return false;
			for(int i=0;i<=8;i++)if(!isEqualsBySymbol(TemplateType.getContentSymbolRight(game,point,fieldState,i),strTemplateWithoutTargets[i]))return false;
			for(int i=72;i<=80;i++)if(!isEqualsBySymbol(TemplateType.getContentSymbolRight(game,point,fieldState,i),strTemplateWithoutTargets[i]))return false;
		}else if(fieldSideType==TemplateFieldSideType.templateFieldSideTypeTOP){
			for(int i=36;i<=44;i++)if(!isEqualsBySymbol(TemplateType.getContentSymbolTop(game,point,fieldState,i),strTemplateWithoutTargets[i]))return false;
			for(int i=27;i<=35;i++)if(!isEqualsBySymbol(TemplateType.getContentSymbolTop(game,point,fieldState,i),strTemplateWithoutTargets[i]))return false;
			for(int i=45;i<=53;i++)if(!isEqualsBySymbol(TemplateType.getContentSymbolTop(game,point,fieldState,i),strTemplateWithoutTargets[i]))return false;
			for(int i=18;i<=26;i++)if(!isEqualsBySymbol(TemplateType.getContentSymbolTop(game,point,fieldState,i),strTemplateWithoutTargets[i]))return false;
			for(int i=54;i<=62;i++)if(!isEqualsBySymbol(TemplateType.getContentSymbolTop(game,point,fieldState,i),strTemplateWithoutTargets[i]))return false;
			for(int i=9;i<=17;i++)if(!isEqualsBySymbol(TemplateType.getContentSymbolTop(game,point,fieldState,i),strTemplateWithoutTargets[i]))return false;
			for(int i=63;i<=71;i++)if(!isEqualsBySymbol(TemplateType.getContentSymbolTop(game,point,fieldState,i),strTemplateWithoutTargets[i]))return false;
			for(int i=0;i<=8;i++)if(!isEqualsBySymbol(TemplateType.getContentSymbolTop(game,point,fieldState,i),strTemplateWithoutTargets[i]))return false;
			for(int i=72;i<=80;i++)if(!isEqualsBySymbol(TemplateType.getContentSymbolTop(game,point,fieldState,i),strTemplateWithoutTargets[i]))return false;
		}else if(fieldSideType==TemplateFieldSideType.templateFieldSideTypeBOTTOM){
			for(int i=36;i<=44;i++)if(!isEqualsBySymbol(TemplateType.getContentSymbolBottom(game,point,fieldState,i),strTemplateWithoutTargets[i]))return false;
			for(int i=27;i<=35;i++)if(!isEqualsBySymbol(TemplateType.getContentSymbolBottom(game,point,fieldState,i),strTemplateWithoutTargets[i]))return false;
			for(int i=45;i<=53;i++)if(!isEqualsBySymbol(TemplateType.getContentSymbolBottom(game,point,fieldState,i),strTemplateWithoutTargets[i]))return false;
			for(int i=18;i<=26;i++)if(!isEqualsBySymbol(TemplateType.getContentSymbolBottom(game,point,fieldState,i),strTemplateWithoutTargets[i]))return false;
			for(int i=54;i<=62;i++)if(!isEqualsBySymbol(TemplateType.getContentSymbolBottom(game,point,fieldState,i),strTemplateWithoutTargets[i]))return false;
			for(int i=9;i<=17;i++)if(!isEqualsBySymbol(TemplateType.getContentSymbolBottom(game,point,fieldState,i),strTemplateWithoutTargets[i]))return false;
			for(int i=63;i<=71;i++)if(!isEqualsBySymbol(TemplateType.getContentSymbolBottom(game,point,fieldState,i),strTemplateWithoutTargets[i]))return false;
			for(int i=0;i<=8;i++)if(!isEqualsBySymbol(TemplateType.getContentSymbolBottom(game,point,fieldState,i),strTemplateWithoutTargets[i]))return false;
			for(int i=72;i<=80;i++)if(!isEqualsBySymbol(TemplateType.getContentSymbolBottom(game,point,fieldState,i),strTemplateWithoutTargets[i]))return false;
		}
		//System.out.println("found "+templateIndex);
		
		/*byte[] b=new byte[81];
		for(int i=0;i<81;i++){
			try{b[i]=fieldState[i%9+point.x-5][i/9+point.y-5];}catch(Exception e){b[i]=Protocol.templateDotTypeNULL;}
		}
		
		if(templateIndex==10052){
			System.out.println("point_RED_NORMAL="+point_RED_NORMAL.toString());
			System.out.println("fieldState="+arrayToString(b));
			System.out.println("strTemplateWithoutTargets="+arrayToString(strTemplateWithoutTargets));
			System.out.println("strTemplateWithTargets="+arrayToString(strTemplateWithTargets));
		}*/
		return true;
	}catch(Exception e){return false;}}

	/*private String arrayToString(byte[] arr){//только для изменения движка при объединении шаблонов с макросами
		String s="";
		for(int i=0;i<arr.length;i++){
			s+=Math.abs(arr[i]);
		}
		return s;
	}*/
	
	private boolean isEqualsBySymbol(int field,int template){try{
		//System.out.println("compare "+field+","+template);
		if(field==template){//новый алгоритм, его правильность под вопросом
			return true;
		}else if(template==Protocol.templateDotTypeANY){
			return true;
		}else if(field==Protocol.templateDotTypeRED){
			if(template==Protocol.templateDotTypeRED_EMPTY){return true;}else{return false;}
		}else if(field==Protocol.templateDotTypeBLUE){
			if(template==Protocol.templateDotTypeBLUE_EMPTY){return true;}else{return false;}
		}else if(field==Protocol.templateDotTypeNULL){
			if(template==Protocol.templateDotTypeRED_EMPTY||template==Protocol.templateDotTypeBLUE_EMPTY){return true;}else{return false;}
		}
		return false;
		/*if(str.equals(Protocol.RED.toString())){
			if(template.equals(Protocol.ANY.toString())||template.equals(Protocol.RED_EMPTY.toString())||
					template.equals(Protocol.RED.toString())){return true;}else return false;
		}
		if(str.equals(Protocol.BLUE.toString())){
			if(template.equals(Protocol.ANY.toString())||template.equals(Protocol.BLUE.toString())||
					template.equals(Protocol.BLUE_EMPTY.toString())
				){return true;}else return false;
		}		
		if(str.equals(Protocol.NULL_DOT.toString())){
			if(template.equals(Protocol.NULL_DOT.toString())||template.equals(Protocol.ANY.toString())||
				template.equals(Protocol.RED_EMPTY.toString())||template.equals(Protocol.BLUE_EMPTY.toString())){return true;}else return false;
		}
		if(str.equals(Protocol.LAND.toString())){if(template.equals(Protocol.LAND.toString())){return true;}else return false;}
		return false;*/
	}catch(Exception e){return false;}}

	public Point getMove(Protocol protocol,Game game,Point point,Makros makros,Point recommendedMove){
		Point moveAI=null;
		if(TemplateType.isUseTargetPoints(templateType)){
			SingleGameEngine e=game.singleGameEngine.clone();
			
			if(templateType==TemplateType.templateTypeBLUE_SURROUND){
				makeMove(game,e,point,point_RED_PROTECTION,Protocol.moveTypeBlue);
				makeMove(game,e,point,point_BLUE_ATTACK,Protocol.moveTypeBlue);
				if(e.getBlueSurroundingCount()>game.singleGameEngine.getBlueSurroundingCount())
					moveAI=getPointFromFieldSideType(
							game, 
							point, 
							point_RED_PROTECTION.get(0),
							TemplateType.isSide(templateType)
						);
				else{
					//System.out.println("add bss to "+templateView.getPoint_RED_PROTECTION().x+","+templateView.getPoint_RED_PROTECTION().y);
					game.BSSpoints.add(getPointFromFieldSideType(					
							game, 
							point, 
							point_RED_PROTECTION.get(0),
							TemplateType.isSide(templateType)
						));
					//System.out.println("err in "+template.templateIndex);
					if(point_RED_NORMAL.size()>0){//в BSS шаблонах возможно наличие RED_PROTECTION при остутствии RED_NORMAL
						moveAI=getPointFromFieldSideType(
							game, 
							point, 
							point_RED_NORMAL.get(0),
							TemplateType.isSide(templateType)
						);
					}
				}
			}else if(templateType==TemplateType.templateTypeFINAL_RED_ATTACK||templateType==TemplateType.templateTypeRED_SURROUND){
				makeMove(game,e,point,point_RED_NORMAL,Protocol.moveTypeRed);
				makeMove(game,e,point,point_RED_ATTACK,Protocol.moveTypeRed);
				if(e.getRedSurroundingCount()>game.singleGameEngine.getRedSurroundingCount())
					moveAI=getPointFromFieldSideType(
							game, 
							point, 
							point_RED_NORMAL.get(0),
							TemplateType.isSide(templateType)
						);
				else return new Point(-1,-1);
			}
		}else if(templateType==TemplateType.templateTypeGLOBAL_ATTACK||templateType==TemplateType.templateTypeGLOBAL_DESTROY){
			if(point_RED_ATTACK.size()==0){//шаблон 11016 - первый шаблон при атаке				
				if(makros!=null){					
					MakrosApplicationInGame ma=new MakrosApplicationInGame(game,makros,templateType,templateIndex,templateRotationType,point.x,point.y);					
					if(ma.center.x<7||ma.center.y<7||ma.center.x>game.sizeX-6||ma.center.y>game.sizeY-6){return new Point(-1,-1);}//не применять возле края поля
					game.makrosApplicationInGame.add(ma);
					if(ma.isExistsLevelMove(point,true,recommendedMove)){	
						moveAI=ma.transformAIPoint;
						if(protocol.isAICanMakeMove(moveAI.x,moveAI.y,game,templateType)){
							protocol.stat.moveStatMas.addMoveStat(TemplateType.getMoveStatType(templateType),true);	
							return moveAI;
						}
					}
				}
				//System.out.println("no find GLOBAL_ATTACK makros "+template.templateIndex);
				//return null;
			}else{
				//System.out.println("find GLOBAL_ATTACK template "+template.templateIndex);
				int pointIdx=0;			
				if(point_RED_ATTACK.size()>1)pointIdx=new Random().nextInt(point_RED_ATTACK.size());				
				moveAI=getPointFromFieldSideType(
						game, 
						point, 
						point_RED_ATTACK.get(pointIdx),
						TemplateType.isSide(templateType)
					);
			}
		}else if(templateType==TemplateType.templateTypeBEGIN){//цель BEGIN шаблона только найти макрос и добавить его в список используемых в игре
			/*moveAI=getPointFromFieldSideType(
					game, 
					point, 
					(point_RED_NORMAL!=null)?point_RED_NORMAL.get(0):point_RED_PROTECTION.get(0),
					TemplateType.isSide(templateType)
				);*/
			if(makros!=null){
				//System.out.println("find makros for TemplateType.BEGIN");
				game.makrosApplicationInGame.add(new MakrosApplicationInGame(game,makros,templateType,templateIndex,templateRotationType,point.x,point.y));
				return new Point(-5,-5);
			}
			return new Point(-1,-1);
		}else if(templateType==TemplateType.templateTypeABSTRACT_ATTACK_WALL){//чтобы по шаблону небыло хода на краю поля
			moveAI=getPointFromFieldSideType(
					game, 
					point, 
					(point_RED_NORMAL!=null)?point_RED_NORMAL.get(0):point_RED_PROTECTION.get(0),
					TemplateType.isSide(templateType)
				);
			if(moveAI.x<4||moveAI.y<4||moveAI.x>game.sizeX-3||moveAI.y>game.sizeY-3){
				//System.out.println("abstract move so close to the field border "+moveAI.toString());
				return new Point(-1,-1);//чтобы по шаблону небыло хода на краю поля
			}
			
		}else{
			moveAI=getPointFromFieldSideType(
					game, 
					point, 
					(point_RED_NORMAL!=null)?point_RED_NORMAL.get(0):point_RED_PROTECTION.get(0),
					TemplateType.isSide(templateType)
				);
			/*if(templateIndex==10052){
				System.out.println("point_RED_NORMAL="+point_RED_NORMAL.toString());
				System.out.println("moveAI="+moveAI);
			}*/
		}
		if(moveAI==null)return new Point(-1,-1);
		if(protocol.isAICanMakeMove(moveAI.x,moveAI.y,game,templateType)){
			
			//isForMakrosIntersept - перехват макросов, когда один кончается, а другой начинается
			//if(!isForMakrosIntersept)
			protocol.stat.moveStatMas.addMoveStat(TemplateType.getMoveStatType(templateType),false);			
			
			if(makros!=null){
				//System.out.println("find makros "+template.templateIndex);
				game.makrosApplicationInGame.add(new MakrosApplicationInGame(game,makros,templateType,templateIndex,templateRotationType,point.x,point.y));
			}
			return moveAI;
		}else return new Point(-1,-1);
	}
	
	private Point getPointFromFieldSideType(Game game,Point initialPoint,Point foundedPoint,boolean isSide){
		return TemplateFieldSideType.getPointByFieldSideType(					
				game, 
				initialPoint, 
				foundedPoint,
				isSide
			);
	}
	
	private void makeMove(Game game,SingleGameEngine e,Point initialPoint,ArrayList<Point> foundedPoint,int moveType){
		for(int it=0;it<foundedPoint.size();it++){
			Point p1=getPointFromFieldSideType(game, initialPoint, foundedPoint.get(it), TemplateType.isSide(templateType));
			e.makeMove(p1, moveType);
		}
	}
}
