package p_TemplateEngine;

import java.awt.Point;
import java.util.ArrayList;

import p_DotsAI.Protocol;
import p_DotsAI.Protocol.Game;
import p_MakrosEngine.Makros;

public class Template{
	
	public int templateIndex;
	public int templateType;
	public byte[] templateContent;
	public byte[] templateLogic;
	private ArrayList<TemplateView> templateView;//0,90,180,270,Vert,Gor,Vert90,ViewGor90
	public Makros makros;
	public int ANYDotsCount;//для сортировки базы шаблонов перед ее сохранением

public Template(String strTemplate){
	ANYDotsCount=0;
	makros=null;	
	changeTemplate(strTemplate);
}

public void changeTemplate(String strTemplate){//изменяет текущий объект на новый при пересохранении	
	templateIndex=new Integer(strTemplate.substring(strTemplate.indexOf("<index:")+7,strTemplate.indexOf("<index:")+12)).intValue();
	templateType=TemplateType.getTemplateType(strTemplate.substring(strTemplate.indexOf("<type:")+6,strTemplate.indexOf("<type:")+9));
	
	String content=strTemplate.substring(strTemplate.indexOf("<content:")+9,strTemplate.indexOf("<content:")+90);
	templateContent=new byte[81];
	for(int i=0;i<81;i++){
		templateContent[i]=new Byte(content.substring(i, i+1)).byteValue();
	}
	
	String logic=strTemplate.substring(strTemplate.indexOf("<logic:")+7,strTemplate.indexOf("<logic:")+88);
	templateLogic=new byte[81];
	for(int i=0;i<81;i++){
		templateLogic[i]=new Byte(logic.substring(i, i+1)).byteValue();
		if(templateLogic[i]!=9)templateLogic[i]=(byte)(0-templateLogic[i]);
	}
	
	if(strTemplate.contains("<makros>")&strTemplate.contains("</makros>")){
		makros=new Makros(strTemplate.substring(strTemplate.indexOf("<makros>")+8,strTemplate.indexOf("</makros>")),templateIndex);
	}
	
	templateView=new ArrayList<TemplateView>();
	templateView.add(new TemplateView(templateContent,templateLogic,TemplateRotationType.templateRotationTypeR0,templateType,templateIndex));
	
	/**/byte[] strTemplateWithTargets180=TemplateRotationType.getTransformArray(TemplateRotationType.templateRotationTypeR180,templateContent);
	if(isEqualsAsArray(strTemplateWithTargets180,templateContent)){//чтобы не добавлять одинаковые TemplateView
		//System.out.println("find symmetric 180");
	}else{
		templateView.add(new TemplateView(templateContent,templateLogic,TemplateRotationType.templateRotationTypeR180,templateType,templateIndex));
	}
	
	/**/byte[] strTemplateWithTargetsGor=TemplateRotationType.getTransformArray(TemplateRotationType.templateRotationTypeGORIZONTAL,templateContent);
	if(isEqualsAsArray(strTemplateWithTargetsGor,templateContent)//чтобы не добавлять одинаковые TemplateView
			||isEqualsAsArray(strTemplateWithTargetsGor,strTemplateWithTargets180)){
		//System.out.println("find symmetric gor");
	}else{
		templateView.add(new TemplateView(templateContent,templateLogic,TemplateRotationType.templateRotationTypeGORIZONTAL,templateType,templateIndex));
	}
	
	/**/byte[] strTemplateWithTargetsVer=TemplateRotationType.getTransformArray(TemplateRotationType.templateRotationTypeVERTICAL,templateContent);
	if(isEqualsAsArray(strTemplateWithTargetsVer,templateContent)//чтобы не добавлять одинаковые TemplateView
			||isEqualsAsArray(strTemplateWithTargetsVer,strTemplateWithTargets180)
			||isEqualsAsArray(strTemplateWithTargetsVer,strTemplateWithTargetsGor)){
		//System.out.println("find symmetric ver");
	}else{
		templateView.add(new TemplateView(templateContent,templateLogic,TemplateRotationType.templateRotationTypeVERTICAL,templateType,templateIndex));
	}
	
	/**/byte[] strTemplateWithTargets90=TemplateRotationType.getTransformArray(TemplateRotationType.templateRotationTypeR90,templateContent);
	if(isEqualsAsArray(strTemplateWithTargets90,templateContent)//чтобы не добавлять одинаковые TemplateView
			||isEqualsAsArray(strTemplateWithTargets90,strTemplateWithTargets180)
			||isEqualsAsArray(strTemplateWithTargets90,strTemplateWithTargetsGor)
			||isEqualsAsArray(strTemplateWithTargets90,strTemplateWithTargetsVer)){
		//System.out.println("find symmetric 90");
	}else{
		templateView.add(new TemplateView(templateContent,templateLogic,TemplateRotationType.templateRotationTypeR90,templateType,templateIndex));
	}

	/**/byte[] strTemplateWithTargets270=TemplateRotationType.getTransformArray(TemplateRotationType.templateRotationTypeR270,templateContent);
	if(isEqualsAsArray(strTemplateWithTargets270,templateContent)//чтобы не добавлять одинаковые TemplateView
			||isEqualsAsArray(strTemplateWithTargets270,strTemplateWithTargets180)
			||isEqualsAsArray(strTemplateWithTargets270,strTemplateWithTargetsGor)
			||isEqualsAsArray(strTemplateWithTargets270,strTemplateWithTargetsVer)
			||isEqualsAsArray(strTemplateWithTargets270,strTemplateWithTargets90)){
		//System.out.println("find symmetric 270");
	}else{
		templateView.add(new TemplateView(templateContent,templateLogic,TemplateRotationType.templateRotationTypeR270,templateType,templateIndex));
	}
	
	/**/byte[] strTemplateWithTargetsGor90=TemplateRotationType.getTransformArray(TemplateRotationType.templateRotationTypeGORIZONTAL90,templateContent);
	if(isEqualsAsArray(strTemplateWithTargetsGor90,templateContent)//чтобы не добавлять одинаковые TemplateView
			||isEqualsAsArray(strTemplateWithTargetsGor90,strTemplateWithTargets180)
			||isEqualsAsArray(strTemplateWithTargetsGor90,strTemplateWithTargetsGor)
			||isEqualsAsArray(strTemplateWithTargetsGor90,strTemplateWithTargetsVer)
			||isEqualsAsArray(strTemplateWithTargetsGor90,strTemplateWithTargets90)
			||isEqualsAsArray(strTemplateWithTargetsGor90,strTemplateWithTargets270)){
		//System.out.println("find symmetric GORIZONTAL90");
	}else{
		templateView.add(new TemplateView(templateContent,templateLogic,TemplateRotationType.templateRotationTypeGORIZONTAL90,templateType,templateIndex));
	}
	
	/**/byte[] strTemplateWithTargetsVer90=TemplateRotationType.getTransformArray(TemplateRotationType.templateRotationTypeVERTICAL90,templateContent);
	if(isEqualsAsArray(strTemplateWithTargetsVer90,templateContent)//чтобы не добавлять одинаковые TemplateView
			||isEqualsAsArray(strTemplateWithTargetsVer90,strTemplateWithTargets180)
			||isEqualsAsArray(strTemplateWithTargetsVer90,strTemplateWithTargetsGor)
			||isEqualsAsArray(strTemplateWithTargetsVer90,strTemplateWithTargetsVer)
			||isEqualsAsArray(strTemplateWithTargetsVer90,strTemplateWithTargets90)
			||isEqualsAsArray(strTemplateWithTargetsVer90,strTemplateWithTargets270)
			||isEqualsAsArray(strTemplateWithTargetsVer90,strTemplateWithTargetsGor90)){
		//System.out.println("find symmetric VERTICAL90");
	}else{
		templateView.add(new TemplateView(templateContent,templateLogic,TemplateRotationType.templateRotationTypeVERTICAL90,templateType,templateIndex));
	}
}

boolean isEqualsAsArray(byte[] arr1,byte[] arr2){
	for(int i=0;i<arr1.length;i++){
		if(arr1[i]!=arr2[i])return false;
	}
	return true;
}

public void setANYDotsCount(){//для сортировки базы шаблонов перед ее сохранением
	ANYDotsCount=0;
	for(int i=0;i<templateContent.length;i++){if(templateContent[i]==Protocol.templateDotTypeANY)ANYDotsCount++;}
}

//public String toStringTemplate(){return templateContent+templateIndex+TemplateType.toString(templateType);}

//используется в PointsTE для поиска совпадений в базе при сохранении новых или пересохранении
public TemplateView getTemplateViewIfEqualsLikeAreaWithTargets(byte[] str){
	for(int i=0;i<templateView.size();i++){
		int count=0;
		for(int j=0;j<81;j++){
			if(templateView.get(i).strTemplateWithTargets[j]!=str[j]){break;}
			count++;
		}
		if(count==81)return templateView.get(i);//вариант в базе есть
	}	
	return null;
}

//используется в игре при поиске шаблона для хода
public TemplateView getTemplateViewIfEqualsLikeAreaWithoutTargets(Game game,Point point,byte[][] fieldState,int templateType){
	boolean isSide=TemplateType.isSide(templateType);
	int fieldSideType=TemplateFieldSideType.getFieldSideType(game, point);
	for(int i=0;i<templateView.size();i++){
		if(templateView.get(i).isEquals(game,point,fieldState,isSide,fieldSideType)){return templateView.get(i);}//вариант в базе есть
	}	
	return null;
}

//возвращает список поворотов симметричных TemplateView для поиска хода в макросах
/*public ArrayList<TemplateRotationType> getSymmetricalTemplateRotationType(TemplateView v){
	ArrayList<TemplateRotationType> symmetrical=new ArrayList<TemplateRotationType>();
	for(int i=0;i<templateView.size();i++){
		if(templateView.get(i).isEquals(v.strTemplateWithoutTargets)){symmetrical.add(templateView.get(i).templateRotationType);}//вариант в базе есть
	}	
	return symmetrical;
}*/

public Point getPointIfEqualsLikeAreaByPointList(Protocol protocol,Game game,ArrayList<Point> point,byte[][] fieldState,int templateType,Point recommendedMove){	
	for(int j=0;j<point.size();j++){	
		TemplateView t=getTemplateViewIfEqualsLikeAreaWithoutTargets(game,point.get(j),fieldState,templateType);
		if(t==null)continue;
		try{if(t.getMove(protocol,game,point.get(j),makros,recommendedMove).x!=-1){
			return point.get(j);				
		}}catch(Exception e){continue;}
	}
	return new Point(-1,-1);
}

public Point getPointIfEqualsLikeAreaByPoint(Protocol protocol,Game game,Point point,byte[][] fieldState,int templateType,Point recommendedMove){
	TemplateView t=getTemplateViewIfEqualsLikeAreaWithoutTargets(game,point,fieldState,templateType);
	if(t==null)return new Point(-1,-1);
	try{if(t.getMove(protocol,game,point,makros,recommendedMove).x!=-1){
		return point;				
	}}catch(Exception e){return new Point(-1,-1);}
	return new Point(-1,-1);
}

}
