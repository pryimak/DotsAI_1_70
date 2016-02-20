package p_TemplateEngine;

import java.awt.Point;
import java.util.ArrayList;

import p_DotsAI.Protocol;
import p_DotsAI.Protocol.Game;
import p_JavaPatterns.C_ReadAndWriteFile;

public class TemplateEngine{
	
public ArrayList<Template> baseOfTemplates;//база вариантов
public int maxIndex;
private C_ReadAndWriteFile file;
private String templateFileName;
public int[] templateTypeCount;

//параметры найденного шаблона
public int foundedNumber;//порядковый номер шаблона в базе
public int foundedIndex;//индекс шаблона
public int foundedTemplateType;

public TemplateEngine(){//загрузить базу из файлов
	templateFileName="dotsAIresources//templateBase.txt";
	
	file=new C_ReadAndWriteFile();
	maxIndex=0;
	
	//<load base>
	String str=file.ReadTxtFile(templateFileName);
	baseOfTemplates=new ArrayList<Template>();//создать массив для хранения базы
		
	int templateCount=0,makrosCount=0;
	templateTypeCount=new int[TemplateType.lastIndexOfTemplateType];
	for(int i=0;i<templateTypeCount.length;i++)templateTypeCount[i]=0;
	
	while(str.length()>0){//пройти все варианты в файле
		int idx=str.indexOf("</template>")+"</template>".length();
		String strTemplate=str.substring(0,idx);
		Template t=new Template(strTemplate);//загрузить вариант	
		
		templateCount++;
		if(t.makros!=null)makrosCount++;
		
		templateTypeCount[t.templateType]++;
		
		str=str.substring(idx);		
		baseOfTemplates.add(t);
	}	
	System.out.println("templateCount="+templateCount);
	System.out.println("makrosCount="+makrosCount);
	//</load base>
	
	foundedNumber=0;//порядковый номер шаблона в базе
	foundedIndex=0;//индекс шаблона
	foundedTemplateType=TemplateType.templateTypeBEGIN;
	
	for(int i=0;i<baseOfTemplates.size();i++){//search max template Index
		if(baseOfTemplates.get(i).templateIndex>maxIndex){
			maxIndex=baseOfTemplates.get(i).templateIndex;
		}			
	}
	System.out.println("maxTemplateIndex="+maxIndex);
	System.out.println("------------------------------------------------------------");
}

public void saveTemplateBase(){
	ArrayList<Template> newBase=new ArrayList<Template>();
	
	for(int k=0;k<baseOfTemplates.size();k++){//setANYDotsCount
		baseOfTemplates.get(k).setANYDotsCount();
	}
	String s="";
	for(int k=81;k>=0;k--){//setANYDotsCount
		for(int i=0;i<baseOfTemplates.size();i++){
			if(baseOfTemplates.get(i).ANYDotsCount!=k)continue;
			String newTemplate=templateToString(baseOfTemplates.get(i));
			s+=newTemplate;
			newBase.add(new Template(newTemplate));
		}
	}
	file.WriteTxtFile(templateFileName,s);
	baseOfTemplates=newBase;
	
	System.out.println("template base saved");
}

private String templateToString(Template t){
	String s="";
	s+="<template>";
	s+="<index:"+t.templateIndex+">";
	s+="<type:"+TemplateType.toString(t.templateType)+">";
	s+="<content:"+arrayToString(t.templateContent)+">";//ситуация на поле без логики
	s+="<logic:"+arrayToString(t.templateLogic)+">";//куда ходить и доп логика типа BSS
	
	if(t.makros!=null){
		s+="<makros>";
		if(t.makros.symmetryType!=-1)s=s+t.makros.getSymmetryTypeString();
		s=s+t.makros.node.getString();
		s+="</makros>";
	}
	s=s.replaceAll("<makros></makros>", "");//для удаленных макросов
	s+="</template>";
	return s;
}

public boolean isExistTemplateWhenNewSave(byte[] content,String type){
	if(isExistTemplateView(content/*,type*/))return true;
	
	String strTemplateWithoutTargets=getStrTemplateWithoutTargets(content);
	String strTemplateWithTargets=getStrTemplateWithTargets(content);
	
	maxIndex=maxIndex+1;
	
	String s="";
	s+="<template>";
	s+="<index:"+maxIndex+">";
	s+="<type:"+type+">";
	s+="<content:"+strTemplateWithoutTargets+">";//ситуация на поле без логики
	s+="<logic:"+strTemplateWithTargets+">";//куда ходить и доп логика типа BSS
	s+="</template>";
	
	baseOfTemplates.add(new Template(s));
	saveTemplateBase();	
	return false;
}	

public boolean isExistTemplateWhenResave(byte[] content,int i,String type){
	if(isExistTemplateView(content/*,type*/))return true;
	
	String strTemplateWithoutTargets=getStrTemplateWithoutTargets(content);
	String strTemplateWithTargets=getStrTemplateWithTargets(content);
	
	String s="";
	s+="<template>";
	s+="<index:"+baseOfTemplates.get(i).templateIndex+">";
	s+="<type:"+type+">";
	s+="<content:"+strTemplateWithoutTargets+">";//ситуация на поле без логики
	s+="<logic:"+strTemplateWithTargets+">";//куда ходить и доп логика типа BSS
	
	if(baseOfTemplates.get(i).makros!=null){
		s+="<makros>";
		if(baseOfTemplates.get(i).makros.symmetryType!=-1)s=s+baseOfTemplates.get(i).makros.getSymmetryTypeString();
		s=s+baseOfTemplates.get(i).makros.node.getString();
		s+="</makros>";
	}
	s+="</template>";
	
	baseOfTemplates.get(i).changeTemplate(s);
	
	saveTemplateBase();
	return false;
}

private boolean isExistTemplateView(byte[] content/*,String type*/){
	//int templateType=TemplateType.getTemplateType(type);
	for(int j=0;j<baseOfTemplates.size();j++){
		//if(baseOfTemplates.get(j).templateType!=templateType)continue;
		if(baseOfTemplates.get(j).getTemplateViewIfEqualsLikeAreaWithTargets(content)!=null)return true;
	}
	return false;
}

public Point getPointIfEqualsLikeAreaByPointList(Protocol protocol,Game game,int templateType,ArrayList<Point> point,byte[][] fieldState,Point recommendedMove){
	/*ArrayList<String> str=new ArrayList<String>();
	for(int j=0;j<point.size();j++){
		str.add(TemplateType.getContent(game,point.get(j),fieldState,templateType));
	}*/
	
	for(int i=0;i<baseOfTemplates.size();i++){try{
		int idx=TemplateType.isTemplateBaseSearchFromStartIdx(templateType)?i:(baseOfTemplates.size()-1-i);
		if(baseOfTemplates.get(idx).templateType!=templateType)continue;
		Point p=baseOfTemplates.get(idx).getPointIfEqualsLikeAreaByPointList(protocol,game,point,fieldState,templateType,recommendedMove);
		if(p.x!=-1){
			foundedNumber=idx;
			foundedIndex=baseOfTemplates.get(idx).templateIndex;					
			foundedTemplateType=templateType;
			return p;			
		}
	}catch(Exception e){}}
	return new Point(-1,-1);
}

public Point getPointIfEqualsLikeAreaByPoint(Protocol protocol,Game game,int templateType,Point point,byte[][] fieldState,Point recommendedMove){
	//String str=TemplateType.getContent(game, point,fieldState,templateType);
	
	for(int i=0;i<baseOfTemplates.size();i++){try{
		int idx=TemplateType.isTemplateBaseSearchFromStartIdx(templateType)?i:(baseOfTemplates.size()-1-i);
		if(baseOfTemplates.get(idx).templateType!=templateType)continue;
		Point p=baseOfTemplates.get(idx).getPointIfEqualsLikeAreaByPoint(protocol,game,point,fieldState,templateType,recommendedMove);
		if(p.x!=-1){
			foundedNumber=idx;
			foundedIndex=baseOfTemplates.get(idx).templateIndex;					
			foundedTemplateType=templateType;
			return p;			
		}
	}catch(Exception e){}}
	return new Point(-1,-1);
}

public void deleteTemplate(int index){
	baseOfTemplates.remove(index);
	saveTemplateBase();
}

public Template getTemplateByTemplateIndex(int index){
	for(int i=0;i<baseOfTemplates.size();i++){
		if(baseOfTemplates.get(i).templateIndex==index)return baseOfTemplates.get(i);
	}
	return null;
}

public int getBaseIndexByTemplateIndex(int index){
	for(int i=0;i<baseOfTemplates.size();i++){
		if(baseOfTemplates.get(i).templateIndex==index)return i;
	}
	return baseOfTemplates.size();
}

private String arrayToString(byte[] arr){//только для изменения движка при объединении шаблонов с макросами
	String s="";
	for(int i=0;i<arr.length;i++){
		s+=Math.abs(arr[i]);
	}
	return s;
}

private String getStrTemplateWithoutTargets(byte[] arr){//только для изменения движка при объединении шаблонов с макросами
	String s="";
	for(int i=0;i<arr.length;i++){
		if(arr[i]<0)s+=Protocol.templateDotTypeNULL;
		else s+=arr[i];
	}
	return s;
}

private String getStrTemplateWithTargets(byte[] arr){//только для изменения движка при объединении шаблонов с макросами
	String s="";
	for(int i=0;i<arr.length;i++){
		if(arr[i]>=0)s+=9;
		else s+=Math.abs(arr[i]);
	}
	return s;
}

}
