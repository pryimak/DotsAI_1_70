package p_TemplateEditor;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;

import p_DotsAI.DotsAI;
import p_GUI.GameGUI;
import p_JavaPatterns.C_Resource;
import p_JavaPatterns.C_UndoRedoStrings;
import p_MakrosEditor.DotsME;
import p_TemplateEngine.Template;
import p_TemplateEngine.TemplateEngine;
import p_TemplateEngine.TemplateRotationType;
import p_TemplateEngine.TemplateType;

public class DotsTE extends MainFrame{

	private Thread t=new Thread(this);
	public TemplateEngine base;
	
	SingleGameEngine engine;
	int preX=-1,preY=-1;
	int baseTemplateIndex=0;
	C_UndoRedoStrings changes=new C_UndoRedoStrings();
	JFileChooser fcExport,fcImport;
	Template template=null;//=new Template();
	public DotsME pointsME;
	private DotsAI pointsAI;
	
public DotsTE(TemplateEngine base,int moveX,int moveY,DotsAI pointsAI){
	this.pointsAI=pointsAI;
	this.move(moveX+pointsAI.protocol.getGame(pointsAI.gameIdx).sizeX*2+40,moveY+10);
	
	engine=new SingleGameEngine(this);
	this.base=base;
	pointsME=new DotsME(base);

	for(int i=0;i<templateTypeItems.length;i++){
		templateTypeItems[i].setText(base.templateTypeCount[i]+" - "+templateTypeItems[i].getText());
		templateTypeItems[i].addActionListener(getActionListener(templateTypeItems[i].getIcon(),i));
	}

	//getNewTitle();
	baseTemplateIndex=0;//base.baseOfTemplates.get(TemplateType.BEGIN).size()-1;
	getBaseTemplate(0);	
	buttonActions();
	engine.paint();
	//pointsME.engine.drawField();
}

public ActionListener getActionListener(final Icon icon,final int n){
	return new ActionListener(){public void actionPerformed(ActionEvent arg0){
		labType.setIcon(icon);
		labType.setText(TemplateType.toString(n));
	}};
}

public void moveFromAI(byte[] content,byte[] logic,int foundedNumber,int templateType){	
	if(foundedNumber!=0){
		//System.out.println("moveFromAI");
		getBaseTemplate(foundedNumber);
		changes.insert(engine.getContent());
	}
	else{
		engine.moveByContent(content,logic);
		changes.insert(engine.getContent());
	}

	/*labType.setText(TemplateType.toString(templateType));
	labType.setIcon(new ImageIcon(C_Resource.templateTypes+TemplateType.toString(templateType)+".png"));
	*/
};

private void getBaseTemplate(int templateNumber){
	//System.out.println("get template");
	while(templateNumber<0)templateNumber++;
	while(templateNumber>base.baseOfTemplates.size()-1)templateNumber--;
	template=base.baseOfTemplates.get(templateNumber);
	
	engine.moveByContent(template.templateContent,template.templateLogic);	

	butInfoPanelChange(""+template.templateIndex);
	butTemplateSequenceInBase.setText("<HTML>"+(templateNumber+1));
	
	baseTemplateIndex=templateNumber;
	changes.insert(engine.getContent());
	labType.setText(TemplateType.toString(template.templateType));
	labType.setIcon(templateTypeIcons[template.templateType]);
	
	pointsME.setMakros(template);
}

/*private String arrayToString(int[] arr){//только для изменения движка при объединении шаблонов с макросами
	String s="";
	for(int i=0;i<arr.length;i++){
		s+=arr[i];
	}
	return s;
}*/

public void mousePressed(MouseEvent me) {
	int x=engine.getMouseClickX(me);
	int y=engine.getMouseClickY(me);
	
	try{if(x<pointsAI.protocol.getGame(pointsAI.gameIdx).sizeX&x>=0&y<pointsAI.protocol.getGame(pointsAI.gameIdx).sizeY&y>=0){
		engine.move(x,y,new Byte(butIndex.getText().substring(butIndex.getText().lastIndexOf(">")+1)).byteValue());
		changes.insert(engine.getContent());
		engine.paint();
	}}catch(Exception e){}
}

public void windowClosing(WindowEvent e){}
public void windowActivated(WindowEvent e) {t=new Thread(this);t.start();}
public void run(){
	try{t.sleep(500);}catch(Exception e){}
	engine.paint();
	try{pointsME.engine.drawField(null,null);}catch(Exception e){}
	t.stop();
}

void butInfoPanelChange(String str){
	butInfoPanel.setText(str);
	if(str.length()<40&str.length()>5){
		Font f=new Font("Tahoma", 14, 14);
		Graphics g=this.getGraphics();
		g.setFont(f);
		g.drawString(str, 63, 120);
	}
};

void buttonActions(){
	/*butRefreshTemplates.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0) {
		//base.sortTemplateBase();
		pointsAI.pointsTE.dispose();
		pointsAI.pointsTE.pointsME.dispose();
		pointsAI.protocol.templateEngine=null;
		
		System.gc();
		pointsAI.protocol.templateEngine=new TemplateEngine();
		pointsAI.pointsTE=new PointsTE(base,pointsAI.getX(),pointsAI.getY()+pointsAI.getHeight(),pointsAI);
	}});*/
	butRepaint.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0) {engine.paint();}});
	butDelete.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0) {
		getBaseTemplate(baseTemplateIndex);
		if(msg.showConfirmDialog(DotsTE.this,"удалить шаблон?","удалить шаблон?",0)==0){
			butInfoPanelChange("удален");
			changes.insert(engine.getContent());
			base.deleteTemplate(baseTemplateIndex);
			getBaseTemplate(baseTemplateIndex);
		}
	}});
	butSave.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0) {
			if(base.isExistTemplateWhenNewSave(engine.getContent(),labType.getText()))butInfoPanelChange("уже есть");
			else {
				butTemplateSequenceInBase.setText("<HTML>"+base.baseOfTemplates./*get(TemplateType.getTemplateType(labType.getText())).*/size());
				baseTemplateIndex=base.baseOfTemplates./*get(TemplateType.getTemplateType(labType.getText())).*/size()-1;
				t=new Thread(DotsTE.this);t.start();
				getBaseTemplate(base.getBaseIndexByTemplateIndex(base.maxIndex));
				butInfoPanelChange("добавлен");
			}
	}});
	
	butResave.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0) {
		int templateIdx=base.baseOfTemplates.get(baseTemplateIndex).templateIndex;
		if(base.isExistTemplateWhenResave(engine.getContent(),baseTemplateIndex,labType.getText()))butInfoPanelChange("уже есть");
		else{
			getBaseTemplate(base.getBaseIndexByTemplateIndex(templateIdx));
			butInfoPanelChange("пересохранено");			
		}
	}});	
	
	butUndo.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0){engine.moveByContent(changes.getUndo(),null);}});
	butRedo.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0){engine.moveByContent(changes.getRedo(),null);}});
	butR.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0){changes.insert(engine.getContent());engine.moveInSide("r");}});
	butL.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0){changes.insert(engine.getContent());engine.moveInSide("l");}});
	butT.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0){changes.insert(engine.getContent());engine.moveInSide("t");}});
	butRT.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0){changes.insert(engine.getContent());engine.moveInSide("rt");}});
	butLT.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0){changes.insert(engine.getContent());engine.moveInSide("lt");}});
	butB.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0){changes.insert(engine.getContent());engine.moveInSide("b");}});
	butRB.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0){changes.insert(engine.getContent());engine.moveInSide("rb");}});
	butLB.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0){changes.insert(engine.getContent());engine.moveInSide("lb");}});
	
	butPrevious.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0){try{
		while(!TemplateType.toString(base.baseOfTemplates.get(baseTemplateIndex-1).templateType).equals(labType.getText())){baseTemplateIndex--;}
		getBaseTemplate(baseTemplateIndex-1);
	}catch(Exception e){}}});
	butNext.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0){try{
		while(!TemplateType.toString(base.baseOfTemplates.get(baseTemplateIndex+1).templateType).equals(labType.getText())){baseTemplateIndex++;}
		getBaseTemplate(baseTemplateIndex+1);
	}catch(Exception e){}}});
	butLast.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0){
		int idx=base.baseOfTemplates.size()-1;
		while(!TemplateType.toString(base.baseOfTemplates.get(idx).templateType).equals(labType.getText())){idx--;}
		baseTemplateIndex=idx;
		getBaseTemplate(baseTemplateIndex);		
	}});
	butFirst.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0){
		int idx=0;
		while(!TemplateType.toString(base.baseOfTemplates.get(idx).templateType).equals(labType.getText())){idx++;}
		baseTemplateIndex=idx;
		getBaseTemplate(baseTemplateIndex);
	}});	
	butOpen.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0){try{
		int i=new Integer(msg.showInputDialog("Перейти к шаблону с номером (например 10788)","")).intValue();
		//int t=base.getTemplateTypeByTemplateIndex(i);
		getBaseTemplate(base.getBaseIndexByTemplateIndex(i));
		changes.insert(engine.getContent());
	}catch(Exception e){}}});
	
	but90.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0) {try{
		engine.moveByContent(TemplateRotationType.getTransformArray(TemplateRotationType.templateRotationTypeR90, engine.getContent()));changes.insert(engine.getContent());
	}catch(Exception e){}}});
	but180.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0) {try{
		engine.moveByContent(TemplateRotationType.getTransformArray(TemplateRotationType.templateRotationTypeR180, engine.getContent()));changes.insert(engine.getContent());
	}catch(Exception e){}}});
	but270.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0) {try{
		engine.moveByContent(TemplateRotationType.getTransformArray(TemplateRotationType.templateRotationTypeR270, engine.getContent()));changes.insert(engine.getContent());
	}catch(Exception e){}}});
	butVert.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0) {try{
		engine.moveByContent(TemplateRotationType.getTransformArray(TemplateRotationType.templateRotationTypeVERTICAL, engine.getContent()));changes.insert(engine.getContent());
	}catch(Exception e){}}});
	butGor.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0) {try{
		engine.moveByContent(TemplateRotationType.getTransformArray(TemplateRotationType.templateRotationTypeGORIZONTAL, engine.getContent()));changes.insert(engine.getContent());
	}catch(Exception e){}}});
	butVert90.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0) {try{
		engine.moveByContent(TemplateRotationType.getTransformArray(TemplateRotationType.templateRotationTypeVERTICAL90, engine.getContent()));changes.insert(engine.getContent());
	}catch(Exception e){}}});
	butGor90.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0) {try{
		engine.moveByContent(TemplateRotationType.getTransformArray(TemplateRotationType.templateRotationTypeGORIZONTAL90, engine.getContent()));changes.insert(engine.getContent());
	}catch(Exception e){}}});
}

public void mouseMoved(MouseEvent me) {try{
	
	int x=engine.getMouseClickX(me),y=engine.getMouseClickY(me);
	
	if(x>=0&y>=0&x<pointsAI.protocol.getGame(pointsAI.gameIdx).sizeX&y<pointsAI.protocol.getGame(pointsAI.gameIdx).sizeY){/*this.setCursor(Cursor.HAND_CURSOR);*/labCoordinates.setText("<HTML>"+(x+1)+":"+(y+1));
	}else this.setCursor(Cursor.DEFAULT_CURSOR);
	
	if(x!=preX|y!=preY){//произошло перемещение ползунка
		Graphics graphics=this.getGraphics();
		if(engine.isCanMove(preX, preY)){
			graphics.setColor(Color.white);
			graphics.drawOval((preX+1)*GameGUI.squareSize+57,(preY+1)*GameGUI.squareSize+17, 6, 6);
			graphics.setColor(new Color(225,225,225));
			graphics.drawLine((preX+1)*GameGUI.squareSize+57,(preY+1)*GameGUI.squareSize+20, (preX+1)*GameGUI.squareSize+63, (preY+1)*GameGUI.squareSize+20);
			graphics.drawLine((preX+1)*GameGUI.squareSize+60,(preY+1)*GameGUI.squareSize+15, (preX+1)*GameGUI.squareSize+60, (preY+1)*GameGUI.squareSize+21);
		}		
		preX=x;preY=y;		
		if(engine.isCanMove(x, y)){//new
			graphics.setColor(Color.black);
			graphics.drawOval((x+1)*GameGUI.squareSize+57, (y+1)*GameGUI.squareSize+17, 6, 6);
		}
	}
}catch(Exception e){if(engine==null)engine=new SingleGameEngine(this);}}

}