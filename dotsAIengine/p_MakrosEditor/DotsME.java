package p_MakrosEditor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;

import p_DotsAI.Protocol;
import p_MakrosEngine.MakrosSymmetryType;
import p_MakrosEngine.TreeNodeMakros;
import p_MakrosEngine.Makros;
import p_TemplateEngine.Template;
import p_TemplateEngine.TemplateEngine;
import p_TemplateEngine.TemplateType;

public class DotsME extends MainFrame{
	
	JScrollPane scroll;
	TemplateEngine base;
	Template template;
	Graphics graphics;
	
	public SingleGameEngine engine=new SingleGameEngine();
	ArrayList<Point> newPointsBlue;//добавляемые в макрос ходы человека
	Point newPointRed;//добавляемые в макрос ходы ИИ
		//newBoth;//ходы ИИ и человека возможны в одну и ту же точку (совпадение, т.к. есть возможность ходить в одно из несокльких мест)
	//TreeNodeMakros selectedTreeNode;
	int selectedNodeId;
	
	Makros makros;
	TreeNodeMakros node;
	DefaultTreeModel treeModel;
	
	boolean isAlwaysExpand=true;//раскрывать дерево ходов
	boolean isShowChilds=false;//рисовать вложеные хода в node
	
public DotsME(TemplateEngine base){
	this.base=base;
	addButtons();
	
	ArrayList<Integer> nodeIndexes=new ArrayList<Integer>();
	nodeIndexes.add(new Integer(0));
	node=new TreeNodeMakros("<html><id=0><font>Дерево ходов</font></html>","",0,nodeIndexes,null);	
	treeModel=new DefaultTreeModel(node);
	tree=new JTree(treeModel);
	
	scroll=new JScrollPane(tree);
	scroll.setBounds(10, 280, 480, 470);
	super.getContentPane().add(scroll);
	scroll.updateUI();
	
	tree.addTreeSelectionListener(new TreeSelectionListener() {
		public void valueChanged(TreeSelectionEvent s) {
			try{updateOnTreeSelection();}catch(Exception e){}
		}
	});
}

public void setMakros(Template template){
	selectedNodeId=0;
	this.template=template;	
	makros=template.makros;
	
	if(makros==null){labMakros.setText("макрос отсутствует");labMakros.setForeground(Color.red);
	}else{labMakros.setText("редактирование макроса");labMakros.setForeground(Color.green);}
		
	/*engine.draw.setLastHuman(0,0);
	engine.draw.setLastAI(0,0);
	labLevel.setText(""+level);
	labMove.setText(""+move);
	maxLevel=level;
	maxMove=move;*/
	engine.drawTemplate(getGraphics(), template,null,null);
	
	if(makros!=null){
		tree.setModel(makros.treeModel);		
		if(isAlwaysExpand)for(int i = 0; i < tree.getRowCount(); i ++)tree.expandRow(i);
	}else{
		tree.setModel(treeModel);
	}
	
	newPointsBlue=new ArrayList<Point>();
	newPointRed=null;
	//newBoth=new ArrayList<Point>();
}

void updateOnTreeSelection(){
	if(makros==null)return;
	
	String str=tree.getLastSelectedPathComponent().toString();
	String id=str.substring(str.indexOf("<id=")+4, str.indexOf("><font"));
	selectedNodeId=new Integer(id);	
	
	//System.out.println("node id="+selectedNodeId+"; level="+makros.node.getTreeNodeMakrosByID(selectedNodeId).level);
	//System.out.println(tree.getLastSelectedPathComponent().toString());
	
	TreeNodeMakros selectedTreeNode=makros.node.getTreeNodeMakrosByID(selectedNodeId);
	if(selectedTreeNode.moveType!=-1){
		if(selectedTreeNode.moveType==Protocol.moveTypeRed){
			butType.setText("B");butType.setBackground(Color.blue);butType.setForeground(Color.lightGray);
		}else if(selectedTreeNode.moveType==Protocol.moveTypeBlue){
			butType.setText("R");butType.setBackground(Color.red);butType.setForeground(Color.black);
		}
	}
	repaintField();
}

public void mousePressed(MouseEvent me) {
	int x=engine.getMouseClickX(me);
	int y=engine.getMouseClickY(me);
	if(butType.getText().equals("B")){
		if(engine.isCanMakeMove(x,y)){
			//engine.draw.setLastHuman(x, y);
			//engine.draw.setLastAI(0, 0);
			newPointsBlue.add(new Point(x,y));
			/*for(int i=0;i<newAI.size();i++){
				if(newAI.get(i).x==x&newAI.get(i).y==y){//ходы ИИ и человека в одну и ту же точку
					newBoth.add(new Point(x,y));
				}
			}*/
			if(newPointRed!=null){
				if(newPointRed.x==x&newPointRed.y==y){//удалить ход ИИ при ходе человека в одну и ту же точку
					newPointRed=null;
				}
			}
			for(int i=0;i<newPointsBlue.size();i++){
				engine.draw.drawPoint(getGraphics(), newPointsBlue.get(i).x, newPointsBlue.get(i).y, Color.blue);
			}
		}
	}if(butType.getText().equals("E")){//удалить синие точки при редактировании синего node
		//engine.draw.setLastHuman(x, y);
		//engine.draw.setLastAI(0, 0);

		TreeNodeMakros selectedTreeNode=makros.node.getTreeNodeMakrosByID(selectedNodeId);
		if(selectedTreeNode.moveType==Protocol.moveTypeRed)return;

		boolean isNeedToClear=false;//для проверки - принадлежит ли точка к выделеному node
		for(int i=0;i<selectedTreeNode.points.size();i++){
			if(selectedTreeNode.points.get(i).x==(x-Protocol.sizeX_ME_half)&selectedTreeNode.points.get(i).y==(y-Protocol.sizeY_ME_half)){//удалить ход человека при ходе ИИ в одну и ту же точку
				selectedTreeNode.points.remove(i);
				isNeedToClear=true;
				break;
			}
		}
		if(!isNeedToClear)return;

		if(selectedTreeNode.points.size()>0){
			String blueMoves="";
			for(int i=0;i<selectedTreeNode.points.size();i++){
				blueMoves+="("+selectedTreeNode.points.get(i).x+","+selectedTreeNode.points.get(i).y+"); ";
			}
			selectedTreeNode.blueIsDefault=false;
			selectedTreeNode.setUserObject("<html><id="+selectedTreeNode.nodeId+"><font color=blue>"+blueMoves+"</font></html>");
		}else if(selectedTreeNode.points.size()==0){
			selectedTreeNode.blueIsDefault=true;
			selectedTreeNode.setUserObject("<html><id="+selectedTreeNode.nodeId+"><font color=blue>default</font></html>");
		}
	
		repaintField();
		makros.treeModel.reload(selectedTreeNode);
		//makros.isWasEdited=true;
		if(isAlwaysExpand)for(int i = 0; i < tree.getRowCount(); i ++)tree.expandRow(i);		
	}else if(butType.getText().equals("R")){ 
		if(engine.isCanMakeMove(x,y)){
			//engine.draw.setLastHuman(0, 0);
			//engine.draw.setLastAI(x, y);
			newPointRed=new Point(x,y);
			/*for(int i=0;i<newHuman.size();i++){
				if(newHuman.get(i).x==x&newHuman.get(i).y==y){//ходы ИИ и человека в одну и ту же точку
					newBoth.add(new Point(x,y));
				}
			}*/
			for(int i=0;i<newPointsBlue.size();i++){
				if(newPointsBlue.get(i).x==x&newPointsBlue.get(i).y==y){//удалить ход человека при ходе ИИ в одну и ту же точку
					newPointsBlue.remove(i);
				}
			}
			//for(int i=0;i<newPointsRed.size();i++){
				engine.draw.drawPoint(getGraphics(), newPointRed.x, newPointRed.y, Color.red);
			//}
		}
	}else if(butType.getText().equals("N")){ 
		if(engine.isCanMakeMove(x,y)){
			if(newPointRed!=null){
				if(newPointRed.x==x&newPointRed.y==y){//очистить точку - удалить ход ИИ
					newPointRed=null;
					engine.draw.drawPoint(getGraphics(), x, y, Color.white);
				}
			}
			for(int i=0;i<newPointsBlue.size();i++){
				if(newPointsBlue.get(i).x==x&newPointsBlue.get(i).y==y){//очистить точку - удалить ход человека
					newPointsBlue.remove(i);
					engine.draw.drawPoint(getGraphics(), x, y, Color.white);
				}
			}			
		}
	}
	/*for(int i=0;i<newBoth.size();i++){//ходы ИИ и человека в одну и ту же точку
		engine.draw.drawPoint(getGraphics(), newBoth.get(i).x, newBoth.get(i).y, Color.green);
	}*/
}

void repaintField(){
	
	if(selectedNodeId==0){
		//engine.draw.setLastHuman(0, 0);
		//engine.draw.setLastAI(0, 0);
		engine.drawTemplate(getGraphics(), template,null,null);
	}else{
		//engine.setLastAI(99,99);
		//engine.setLastHuman(99,99);
		engine.drawTemplate(getGraphics(),template,null,null);
		
		TreeNodeMakros selectedTreeNode=makros.node.getTreeNodeMakrosByID(selectedNodeId);
		ArrayList<TreeNodeMakros> listOfNodes=new ArrayList<TreeNodeMakros>();		
		listOfNodes.add(selectedTreeNode);
		//System.out.println("add TreeNodeMakros="+selectedTreeNode.nodeId);
		
		while(listOfNodes.get(listOfNodes.size()-1).parentNode!=null){
			TreeNodeMakros n=listOfNodes.get(listOfNodes.size()-1).parentNode;			
			if(n.level>0){
				listOfNodes.add(n);
				//System.out.println("add TreeNodeMakros="+n.nodeId);
			}else{
				break;
			}
		}
		
		//<сделать ходы в engine для нодов>
		for(int i=listOfNodes.size()-1;i>=0;i--){
			if(listOfNodes.get(i).moveType==Protocol.moveTypeRed){
				Point p=new Point();
				p.x=listOfNodes.get(i).points.get(0).x+Protocol.sizeX_ME_half;
				p.y=listOfNodes.get(i).points.get(0).y+Protocol.sizeY_ME_half;
				if(engine.isCanMakeMove(p.x,p.y))engine.makeMove(p.x,p.y, Protocol.moveTypeRed);
			}else{
				if(listOfNodes.get(i).blueIsDefault){
					continue;
				}else{
					for(int j=0;j<listOfNodes.get(i).points.size();j++){
						Point p=new Point();
						p.x=listOfNodes.get(i).points.get(j).x+Protocol.sizeX_ME_half;
						p.y=listOfNodes.get(i).points.get(j).y+Protocol.sizeY_ME_half;
						if(engine.isCanMakeMove(p.x,p.y))engine.makeMove(p.x,p.y, Protocol.moveTypeBlue);
					}
				}
			}
		}
		//</сделать ходы в engine для нодов>
		
		engine.drawField(null,null);
		
		for(int j=0;j<listOfNodes.size();j++){//нарисовать синие квадраты когда выбор из нескольких синих ходов
			if(listOfNodes.get(j).moveType==Protocol.moveTypeBlue&listOfNodes.get(j).points.size()>1){
				for(int i=0;i<listOfNodes.get(j).points.size();i++){
					engine.draw.fillSquare(getGraphics(), listOfNodes.get(j).points.get(i).x+Protocol.sizeX_ME_half, listOfNodes.get(j).points.get(i).y+Protocol.sizeX_ME_half, Color.blue, listOfNodes.get(j).level);
				}
			}
		}
	}
	
	if(isShowChilds){
		TreeNodeMakros selectedTreeNode=makros.node.getTreeNodeMakrosByID(selectedNodeId);
		for(int i=0;i<selectedTreeNode.childNodes.size();i++){
			TreeNodeMakros child=selectedTreeNode.childNodes.get(i);
			if(child.moveType==Protocol.moveTypeRed){
				engine.draw.drawSquare(getGraphics(), child.points.get(0).x+Protocol.sizeX_ME_half, child.points.get(0).y+Protocol.sizeX_ME_half, Color.red, i);
			}else if(child.moveType==Protocol.moveTypeBlue){
				for(int j=0;j<child.points.size();j++){
					engine.draw.drawSquare(getGraphics(), child.points.get(j).x+Protocol.sizeX_ME_half, child.points.get(j).y+Protocol.sizeX_ME_half, Color.blue, i);
				}
			}
		}
	}
}

void addButtons(){
	
	/*butSaveMakrosBase.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			//System.out.println("makros id="+makros.templateIndex);
			//System.out.println(makros.node.getString());
			base.resaveMakrosBase();
		}
	});*/
	
	butAddMove.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			
			if(makros==null){
				makros=new Makros("<html><id=0><font>Дерево ходов</font></html>",template.templateIndex);
				template.makros=makros;
				tree.setModel(makros.treeModel);
				if(isAlwaysExpand)for(int i = 0; i < tree.getRowCount(); i ++)tree.expandRow(i);
			}
			
			TreeNodeMakros selectedTreeNode=makros.node.getTreeNodeMakrosByID(selectedNodeId);
			TreeNodeMakros n=null;
			if(newPointsBlue.size()>0&(selectedTreeNode.moveType==Protocol.moveTypeRed|selectedTreeNode.level==0)){//add blue
				makros.nodeIndexes.add(new Integer(makros.nodeIndexes.size()));
				String blueMoves="";
				for(int i=0;i<newPointsBlue.size();i++){
					blueMoves+="("+(newPointsBlue.get(i).x-Protocol.sizeX_ME_half)+","+(newPointsBlue.get(i).y-Protocol.sizeY_ME_half)+"); ";
					//try{engine.makeMove(newPointsBlue.get(i).x,newPointsBlue.get(i).y, Protocol.BLUE);}catch(Exception exc){}
				}
				n=new TreeNodeMakros("<html><id="+(makros.nodeIndexes.size()-1)+"><font color=blue>"+blueMoves+"</font></html>","",selectedTreeNode.level+1,makros.nodeIndexes,selectedTreeNode);
				n.addPoints(newPointsBlue);				
				selectedTreeNode.childNodes.add(n);
				selectedTreeNode.add(n);
				n.moveType=Protocol.moveTypeBlue;				
			}else if(newPointRed!=null){if(selectedTreeNode.moveType==Protocol.moveTypeBlue|selectedTreeNode.level==0){//add red
				//for(int i=0;i<newPointsRed.size();i++){
					//try{engine.makeMove(newPointsRed.x,newPointsRed.y, Protocol.RED);}catch(Exception exc){}					
				//}				
				makros.nodeIndexes.add(new Integer(makros.nodeIndexes.size()));
				n=new TreeNodeMakros("<html><id="+(makros.nodeIndexes.size()-1)+"><font color=red>("+(newPointRed.x-Protocol.sizeX_ME_half)+","+(newPointRed.y-Protocol.sizeY_ME_half)+")</font></html>","",selectedTreeNode.level+1,makros.nodeIndexes,selectedTreeNode);
				selectedTreeNode.childNodes.add(n);
				selectedTreeNode.add(n);
				n.addPoint(newPointRed);
				n.moveType=Protocol.moveTypeRed;
			}}else if(newPointRed==null){if(newPointsBlue.size()==0&(selectedTreeNode.moveType==Protocol.moveTypeRed|selectedTreeNode.level==0)){//add blue default				
				makros.nodeIndexes.add(new Integer(makros.nodeIndexes.size()));
				n=new TreeNodeMakros("<html><id="+(makros.nodeIndexes.size()-1)+"><font color=blue>default</font></html>","",selectedTreeNode.level+1,makros.nodeIndexes,selectedTreeNode);
				n.blueIsDefault=true;
				selectedTreeNode.childNodes.add(n);
				selectedTreeNode.add(n);
				n.moveType=Protocol.moveTypeBlue;
			}}
			if(n==null){
				newPointsBlue.clear();
				newPointRed=null;
				return;
			}
			
			//engine.drawField(null,null);
			repaintField();
			makros.treeModel.reload(selectedTreeNode);
			//makros.isWasEdited=true;
			if(isAlwaysExpand)for(int i = 0; i < tree.getRowCount(); i ++)tree.expandRow(i);
			
			newPointsBlue.clear();
			newPointRed=null;
		}
	});
	
	butEditMove.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			
			TreeNodeMakros selectedTreeNode=makros.node.getTreeNodeMakrosByID(selectedNodeId);
			if(selectedTreeNode.level==0)return;
			
			if(newPointRed!=null&selectedTreeNode.moveType==Protocol.moveTypeRed){//change red
				System.out.println("try change red");
				selectedTreeNode.setUserObject("<html><id="+selectedTreeNode.nodeId+"><font color=red>("+(newPointRed.x-Protocol.sizeX_ME_half)+","+(newPointRed.y-Protocol.sizeY_ME_half)+")</font></html>");
				selectedTreeNode.changePoint(newPointRed);
			}else if(selectedTreeNode.moveType==Protocol.moveTypeBlue){
				if(newPointsBlue.size()>0){//change blue
					System.out.println("try change blue");
					String blueMoves="";
					for(int i=0;i<newPointsBlue.size();i++){
						blueMoves+="("+(newPointsBlue.get(i).x-Protocol.sizeX_ME_half)+","+(newPointsBlue.get(i).y-Protocol.sizeY_ME_half)+"); ";
					}
					selectedTreeNode.blueIsDefault=false;
					selectedTreeNode.setUserObject("<html><id="+selectedTreeNode.nodeId+"><font color=blue>"+blueMoves+"</font></html>");
					selectedTreeNode.changePoints(newPointsBlue);
				}else if(newPointRed==null&newPointsBlue.size()==0){//change blue to default
					System.out.println("try change blue default");
					selectedTreeNode.blueIsDefault=true;
					selectedTreeNode.setUserObject("<html><id="+selectedTreeNode.nodeId+"><font color=blue>default</font></html>");
					//selectedTreeNode.points.clear();
				}
			}else{
				newPointsBlue.clear();
				newPointRed=null;
				return;
			}
			
			//engine.drawField(null,null);	
			repaintField();
			makros.treeModel.reload(selectedTreeNode);
			//makros.isWasEdited=true;
			if(isAlwaysExpand)for(int i = 0; i < tree.getRowCount(); i ++)tree.expandRow(i);
			
			newPointsBlue.clear();
			newPointRed=null;
		}
	});
	
	butDelMove.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			TreeNodeMakros selectedTreeNode=makros.node.getTreeNodeMakrosByID(selectedNodeId);
			if(selectedTreeNode.level==0)return;
			
			TreeNodeMakros parent=selectedTreeNode.parentNode;
			for(int i=0;i<parent.childNodes.size();i++){
				if(parent.childNodes.get(i).nodeId==selectedNodeId){
					parent.childNodes.remove(i);
					parent.remove(i);
					break;
				}
			}
			selectedNodeId=parent.nodeId;
			repaintField();
			//engine.drawField(null,null);			
			makros.treeModel.reload(parent);
			//makros.isWasEdited=true;
		}
	});
	
	butExpand.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			isAlwaysExpand=true;
			for(int i = 0; i < tree.getRowCount(); i ++)tree.expandRow(i);
		}
	});
	
	butCollapse.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			isAlwaysExpand=false;
			for(int i = 1; i < tree.getRowCount(); i ++)tree.collapseRow(i);
		}
	});
	
	butPaintChilds.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if(isShowChilds){
				isShowChilds=false;
				repaintField();
			}else{
				isShowChilds=true;
				repaintField();
			}
		}
	});
	
	butSetSymmetry.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			JOptionPane msg=new JOptionPane();
			int symmetryType=MakrosSymmetryType.makrosSymmetryTypeGORIZONTAL;
			double symmetryBorder=0;
			try{
				int x=new Integer(msg.showInputDialog("<html>Введите число 0,1,2,3 - тип симметрии:<br>"
						+ MakrosSymmetryType.makrosSymmetryTypeGORIZONTAL+" - "+MakrosSymmetryType.getMakrosSymmetryTypeDescription(MakrosSymmetryType.makrosSymmetryTypeGORIZONTAL)+"<br>"
						+ MakrosSymmetryType.makrosSymmetryTypeVERTICAL+" - "+MakrosSymmetryType.getMakrosSymmetryTypeDescription(MakrosSymmetryType.makrosSymmetryTypeVERTICAL)+"<br>"
						+ MakrosSymmetryType.makrosSymmetryTypeMAIN_DIAGONAL+" - "+MakrosSymmetryType.getMakrosSymmetryTypeDescription(MakrosSymmetryType.makrosSymmetryTypeMAIN_DIAGONAL)+"<br>"
						+ MakrosSymmetryType.makrosSymmetryTypeSECOND_DIAGONAL+" - "+MakrosSymmetryType.getMakrosSymmetryTypeDescription(MakrosSymmetryType.makrosSymmetryTypeSECOND_DIAGONAL)+"</html>", "0"));
				if(x==0)symmetryType=MakrosSymmetryType.makrosSymmetryTypeGORIZONTAL;
				else if(x==1)symmetryType=MakrosSymmetryType.makrosSymmetryTypeVERTICAL;
				else if(x==2)symmetryType=MakrosSymmetryType.makrosSymmetryTypeMAIN_DIAGONAL;
				else if(x==3)symmetryType=MakrosSymmetryType.makrosSymmetryTypeSECOND_DIAGONAL;
				
				symmetryBorder=new Double(msg.showInputDialog("Введите смещение - число типа double", "0")).doubleValue();
				
				makros.symmetryType=symmetryType;
				makros.symmetryBorder=symmetryBorder;
				makros.setNodeText();
				//makros.isWasEdited=true;
				makros.treeModel.reload(makros.node);
				
			}catch(Exception exc){}
		}
	});
}
	
}
