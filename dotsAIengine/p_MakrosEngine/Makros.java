package p_MakrosEngine;

import java.awt.Point;
import java.util.ArrayList;

import javax.swing.tree.DefaultTreeModel;

import p_TemplateEngine.TemplateRotationType;

public class Makros{
	
	public int templateIndex;//=0;
	public ArrayList<Integer> nodeIndexes;//максимальный индекс нода, увеличивается при добавлении нода (уникальный для каждого нода для навигации по нодам)
	public TreeNodeMakros node;//Корень дерева
	public DefaultTreeModel treeModel;//для обновления главного node, чтобы не создавать новый объект JTree
	//public boolean isWasEdited;	
	//public MakrosSymmetry makrosSymmetry;
	public int symmetryType;
	public double symmetryBorder;
	
public Makros(String str,int index){
	//isWasEdited=false;
	changeMakros(str,index);
}

public void changeMakros(String str,int index){//изменяет текущий объект на новый
	templateIndex=index;
	
	if(str.contains("<symmetry:")){
		setMakrosSymmetry(str.substring(0,str.indexOf(">")));
		str=str.substring(str.indexOf(">")+1);
	}else{
		symmetryType=-1;
	}
	
	nodeIndexes=new ArrayList<Integer>();
	nodeIndexes.add(new Integer(0));
	node=new TreeNodeMakros("",str,0,nodeIndexes,null);	
	setNodeText();
	treeModel=new DefaultTreeModel(node);
}

public void setNodeText(){
	node.setUserObject("<html><id=0><font>Дерево ходов"+((symmetryType!=-1)?getDescription():"")+"</font></html>");
}

public String getDescription(){
	return ". Симметрия макроса: "+MakrosSymmetryType.getMakrosSymmetryTypeDescription(symmetryType)+" ("+MakrosSymmetryType.toStringMakrosSymmetryType(symmetryType)+"), смещение: "+symmetryBorder;
}

void setMakrosSymmetry(String str){
	//<symmetry:gor,-0.5>//System.out.println(str);		
	if(str.contains("gor")){
		symmetryType=MakrosSymmetryType.makrosSymmetryTypeGORIZONTAL;
	}else if(str.contains("vert")){
		symmetryType=MakrosSymmetryType.makrosSymmetryTypeVERTICAL;
	}else if(str.contains("mdiag")){
		symmetryType=MakrosSymmetryType.makrosSymmetryTypeMAIN_DIAGONAL;
	}else if(str.contains("sdiag")){
		symmetryType=MakrosSymmetryType.makrosSymmetryTypeSECOND_DIAGONAL;
	}
			
	symmetryBorder=new Double(str.substring(str.indexOf(",")+1)).doubleValue();
}

public String getSymmetryTypeString(){
	return "<symmetry:"+MakrosSymmetryType.toStringMakrosSymmetryType(symmetryType)+","+symmetryBorder+">";
}

public Point getSymmetryPoint(Point point,int templateRotationType){try{
	
	Point p=TemplateRotationType.getTransformPoint(templateRotationType, point);
	
	if(symmetryType==MakrosSymmetryType.makrosSymmetryTypeGORIZONTAL){
		double d=Math.abs(p.x-symmetryBorder);
		if(symmetryBorder>p.x)return TemplateRotationType.getTransformPoint(templateRotationType,new Point((int)(symmetryBorder+d),p.y));
		if(symmetryBorder<p.x)return TemplateRotationType.getTransformPoint(templateRotationType,new Point((int)(symmetryBorder-d),p.y));
		if(symmetryBorder==p.x){System.out.println("error GORIZONTAL symmetryBorder==p.x");return p;}
	}
	if(symmetryType==MakrosSymmetryType.makrosSymmetryTypeVERTICAL){
		double d=Math.abs(p.y-symmetryBorder);
		if(symmetryBorder>p.y)return TemplateRotationType.getTransformPoint(templateRotationType,new Point(p.x,(int)(symmetryBorder+d)));
		if(symmetryBorder<p.y)return TemplateRotationType.getTransformPoint(templateRotationType,new Point(p.x,(int)(symmetryBorder-d)));
		if(symmetryBorder==p.y){System.out.println("error VERTICAL symmetryBorder==p.y");return p;}
	}
	//if(symmetryType==MakrosSymmetryType.MAIN_DIAGONAL)return new Point(-p.y,-p.x);
	//if(symmetryType==MakrosSymmetryType.SECOND_DIAGONAL)return new Point(-p.y,-p.x);
	return p;
}catch(Exception e){return point;}}

}
