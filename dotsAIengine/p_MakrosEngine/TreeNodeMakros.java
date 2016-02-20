package p_MakrosEngine;

import java.awt.Point;
import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

import p_DotsAI.Protocol;

public class TreeNodeMakros extends DefaultMutableTreeNode /*implements TreeNode*/{

	public int level;	
	public int nodeId;
	public int moveType;	
	public ArrayList<Point> points;
	public ArrayList<TreeNodeMakros> childNodes;
	public TreeNodeMakros parentNode;
	public boolean blueIsDefault;
	
public TreeNodeMakros(String name,String str,int level1,ArrayList<Integer> nodeIndexes,TreeNodeMakros parentNode1){	
	super(name);
	blueIsDefault=false;	
	level=level1;
	parentNode=parentNode1;
	nodeId=nodeIndexes.get(nodeIndexes.size()-1);
	points=new ArrayList<Point>();
	childNodes=new ArrayList<TreeNodeMakros>();
	
	addRedMoves(str,nodeIndexes);
	addBlueMoves(str,nodeIndexes);			
}

public void addBlueMoves(String str,ArrayList<Integer> nodeIndexes){
	int idx=0;
	String beginB="<"+(level+1)+"b:";
	String endB="</"+(level+1)+"b>";	
	for(;;){
		idx=str.indexOf(beginB);
		if(idx==-1)break;
		
		String s="";
		try{s=str.substring(idx,str.indexOf(endB)+endB.length());}catch(Exception e){break;}
		str=str.replaceFirst(s, "");		
		s=s.replaceFirst(beginB, "");
		s=s.replaceFirst(endB, "");
		
		ArrayList<Point> points=getMovesFromString(s.substring(0, s.indexOf(">")),Protocol.moveTypeBlue);
		nodeIndexes.add(new Integer(nodeIndexes.size()));
		TreeNodeMakros n=null;
		if(points.size()>0){
			String blueMoves="";
			for(int i=0;i<points.size();i++){
				blueMoves+="("+points.get(i).x+","+points.get(i).y+"); ";
			}
			n=new TreeNodeMakros("<html><id="+(nodeIndexes.size()-1)+"><font color=blue>"+blueMoves+"</font></html>",s,level+1,nodeIndexes,this);
			n.points=points;
		}else{
			n=new TreeNodeMakros("<html><id="+(nodeIndexes.size()-1)+"><font color=blue>default</font></html>",s,level+1,nodeIndexes,this);
			n.blueIsDefault=true;
		}
		childNodes.add(n);
		this.add(n);
		n.moveType=Protocol.moveTypeBlue;
	}
}

public void addRedMoves(String str,ArrayList<Integer> nodeIndexes){
	int idx=0;
	String beginR="<"+(level+1)+"r:";	
	String endR="</"+(level+1)+"r>";
	for(;;){
		idx=str.indexOf(beginR);
		if(idx==-1)break;
		
		String s="";
		try{s=str.substring(idx,str.indexOf(endR)+endR.length());}catch(Exception e){break;}
		str=str.replaceFirst(s, "");		
		s=s.replaceFirst(beginR, "");
		s=s.replaceFirst(endR, "");

		nodeIndexes.add(new Integer(nodeIndexes.size()));
		ArrayList<Point> points=getMovesFromString(s.substring(0, s.indexOf(">")),Protocol.moveTypeRed);
		TreeNodeMakros n=new TreeNodeMakros("<html><id="+(nodeIndexes.size()-1)+"><font color=red>("+points.get(0).x+","+points.get(0).y+")</font></html>",s,level+1,nodeIndexes,this);
		childNodes.add(n);
		this.add(n);
		n.points=points;
		n.moveType=Protocol.moveTypeRed;
	}
}

public ArrayList<Point> getMovesFromString(String str,int type){
	ArrayList<Point> points=new ArrayList<Point>();
	if(type==Protocol.moveTypeRed){
		int x=new Integer(str.substring(0, str.indexOf(",")));
		int y=new Integer(str.substring(str.indexOf(",")+1));
		points.add(new Point(x, y));
		//System.out.println("add move r="+x+","+y);
		return points;
	}
	if(type==Protocol.moveTypeBlue){
		//points.add(new ArrayList<Point>());
		if(str.equals("default")){
			return points;
		}
		//System.out.println("search blue="+str);
		for(;;){
			int idx=str.indexOf(";");
			String s="";
			if(idx==-1)s=str;
			else s=str.substring(0, idx);
			
			int x=new Integer(s.substring(0, str.indexOf(",")));
			int y=new Integer(s.substring(str.indexOf(",")+1));
			points.add(new Point(x, y));
			
			str=str.replaceFirst(s+";", "");
			
			//System.out.println("add move b="+new Point(x, y).toString());
			if(idx==-1)return points;
		}		
	}
	return points;
}

public TreeNodeMakros getTreeNodeMakrosByID(int nodeId){
	if(this.nodeId==nodeId)return this;
	else{
		for(int i=0;i<childNodes.size();i++){
			if(childNodes.get(i).getTreeNodeMakrosByID(nodeId)!=null){
				if(childNodes.get(i).getTreeNodeMakrosByID(nodeId).nodeId==nodeId)return childNodes.get(i).getTreeNodeMakrosByID(nodeId);
			}
		}
	}
	return null;
}

public String getString(){
	String s="";
	for(int i=0;i<childNodes.size();i++){
		if(childNodes.get(i)==null)continue;
		if(childNodes.get(i).moveType==Protocol.moveTypeRed){
			s+="<"+childNodes.get(i).level+"r:"+childNodes.get(i).points.get(0).x+","+childNodes.get(i).points.get(0).y+">";
			s+=childNodes.get(i).getString();
			s+="</"+childNodes.get(i).level+"r>";
		}
	}
	for(int i=0;i<childNodes.size();i++){
		if(childNodes.get(i)==null)continue;
		if(childNodes.get(i).moveType==Protocol.moveTypeBlue){
			s+="<"+childNodes.get(i).level+"b:";
			if(!childNodes.get(i).blueIsDefault){
				for(int j=0;j<childNodes.get(i).points.size();j++){
					s+=childNodes.get(i).points.get(j).x+","+childNodes.get(i).points.get(j).y+";";
				}
				s=s.substring(0, s.length()-1);//чтобы удалить последнюю ;
			}else{
				s+="default";
			}			
			s+=">";
			s+=childNodes.get(i).getString();
			s+="</"+childNodes.get(i).level+"b>";
		}
	}
	return s;
}

public void addPoints(ArrayList<Point> p){//перевести и добавить точки формата (0;15) в формат (-7;7)
	for(int i=0;i<p.size();i++){
		points.add(new Point(p.get(i).x-Protocol.sizeX_ME_half,p.get(i).y-Protocol.sizeY_ME_half));
	}
}

public void addPoint(Point p){//перевести и добавить точки формата (0;15) в формат (-7;7)
	points.add(new Point(p.x-Protocol.sizeX_ME_half,p.y-Protocol.sizeY_ME_half));
}

public void changePoints(ArrayList<Point> p){//перевести и добавить точки формата (0;15) в формат (-7;7)
	points.clear();
	for(int i=0;i<p.size();i++){
		points.add(new Point(p.get(i).x-Protocol.sizeX_ME_half,p.get(i).y-Protocol.sizeY_ME_half));
	}
}

public void changePoint(Point p){//перевести и добавить точки формата (0;15) в формат (-7;7)
	points.clear();
	points.add(new Point(p.x-Protocol.sizeX_ME_half,p.y-Protocol.sizeY_ME_half));
}

}
