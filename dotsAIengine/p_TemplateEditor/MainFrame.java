package p_TemplateEditor;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import p_DotsAI.Protocol;
import p_JavaPatterns.C_AddComponent;
import p_JavaPatterns.C_JFrame;
import p_JavaPatterns.C_Resource;
import p_TemplateEngine.TemplateType;

public class MainFrame extends JFrame implements Runnable,MouseListener, WindowListener, MouseMotionListener{
	
	Thread t=new Thread(this);
	JOptionPane msg=new JOptionPane();
	C_AddComponent add=new C_AddComponent(this.getContentPane());
	
	int otstup5=140;
	
	JButton butSave,butResave,butInfoPanel,butUndo,butRedo,butDelete,butRepaint;
	JButton butTransform,butType;//menu
	JButton butFirst,butLast,butPrevious,butNext,butIndex;//navigation
	JButton butEMPTY,butBLUE,butRED,butANY,butRED_EMPTY,butBLUE_EMPTY,butLAND;//кнопки дл€ точек
	JButton butL,butR,butT,butB,butLT,butLB,butRT,butRB;//стрелки сдвига пол€
	
	JLabel butTemplateSequenceInBase,labMove,labCoordinates,labType;
	
	JPopupMenu menu_transform=new JPopupMenu("ѕоворот"),menu_type=new JPopupMenu("“ип");
	JButton butOpen;//menu
	JMenuItem but90,but180,but270,butVert,butGor,butVert90,butGor90;//transform
	JButton butRedAttack,butRedProtection,butRedNormal,butBlueNormal;//таргетные точки
	JMenuItem templateTypeItems[];
	
	public ImageIcon[] templateTypeIcons;
	
MainFrame(){

	this.setIconImage(C_Resource.icon);
	new C_JFrame(this,"Dots Template Editor",false,325,170,new Color(245,245,245));
	this.addMouseListener(this);
	this.addWindowListener(this);
	this.addMouseMotionListener(this);
		
	labCoordinates=(JLabel)add.component("label",15,0,40,20,"0:0",null);labCoordinates.setBackground(Color.white);
	labType=(JLabel)add.component("label",235,150,20,18,"sqt",null);
	labType.setIcon(new ImageIcon(C_Resource.templateTypes+TemplateType.toString(TemplateType.templateTypeBEGIN)+".png"));
	
	//butRefreshTemplates=C_Resource.getButton(add,C_Resource.base+"refresh_te.png",265,149,"обновить базу шаблонов и макросов");//=getButton("refresh_te",265,149,20,20,null,"");	
	butUndo=C_Resource.getButton(add,C_Resource.navigation+"undo.png",otstup5+80,5,"отменить изменени€");//=getButton("undo",otstup5+80,5,20,20,null,"");
	butRedo=C_Resource.getButton(add,C_Resource.navigation+"redo.png",otstup5+105,5,"вернуть изменени€");//=getButton("redo",otstup5+105,5,20,20,null,"вернуть изменени€");
	butRepaint=C_Resource.getButton(add,C_Resource.navigation+"refresh.png",otstup5+130,5,"обновить");//=getButton("refresh",otstup5+130,5,20,20,null,"обновить");
	
	int otstup1=80;
	butDelete=C_Resource.getButton(add,C_Resource.base+"delete.png",otstup1-17,150,"удалить шаблон");//=getButton("delete",otstup1-17,150,20,18,null,"удалить шаблон");
	butFirst=C_Resource.getButton(add,C_Resource.navigation+"first.png",6+otstup1,150,"");//=getButton("first",6+otstup1,150,20,18,null,"");
	butPrevious=C_Resource.getButton(add,C_Resource.navigation+"previous.png",29+otstup1,150,"");//=getButton("previous",29+otstup1,150,20,18,null,"");
	butTemplateSequenceInBase=(JLabel)add.component("label",54+otstup1,150,40,18,"<HTML>0",null);
	butNext=C_Resource.getButton(add,C_Resource.navigation+"next.png",89+otstup1,150,"");//=getButton("next",89+otstup1,150,20,18,null,"");
	butLast=C_Resource.getButton(add,C_Resource.navigation+"last.png",112+otstup1,150,"");//=getButton("last",112+otstup1,150,20,18,null,"");

	butOpen=C_Resource.getButton(add,C_Resource.base+"open.png",otstup5+155,84,"перейти к шаблону");//=getButton("open",otstup5+155,84,30,25,null,"перейти к шаблону");
	butSave=C_Resource.getButton(add,C_Resource.base+"save.png",otstup5+155,113,"сохранить как новый");//=getButton("save",otstup5+155,113,30,25,null,"сохранить как новый");
	butResave=C_Resource.getButton(add,C_Resource.base+"resave.png",otstup5+155,142,"перезаписать шаблон");//=getButton("resave",otstup5+155,142,30,25,null,"перезаписать шаблон");
	
	but90=C_Resource.getMenuItem(C_Resource.rotates+"90.png", "Ќа 90 градусов");//but90.setIcon(new ImageIcon(buttons.class.getResource("90.png")));
	but180=C_Resource.getMenuItem(C_Resource.rotates+"180.png", "Ќа 180 градусов");//but180.setIcon(new ImageIcon(buttons.class.getResource("180.png")));
	but270=new JMenuItem("Ќа 270 градусов");
	butVert=C_Resource.getMenuItem(C_Resource.rotates+"vert.png", "ѕо вертикали");//butVert.setIcon(new ImageIcon(buttons.class.getResource("vert.png")));
	butGor=C_Resource.getMenuItem(C_Resource.rotates+"gor.png", "ѕо горизонтали");//butGor.setIcon(new ImageIcon(buttons.class.getResource("gor.png")));
	butVert90=new JMenuItem("ѕо вертикали и на 90");
	butGor90=new JMenuItem("ѕо горизонтали и на 90");
	
	menu_transform.add(but90);menu_transform.add(but180);menu_transform.add(but270);menu_transform.addSeparator();
	menu_transform.add(butVert);menu_transform.add(butGor);menu_transform.addSeparator();
	menu_transform.add(butVert90);menu_transform.add(butGor90);
	butTransform=C_Resource.getButton(add,C_Resource.rotates+"transform.png",otstup5+80,127,"поворот шаблона");//=getButton("transform",otstup5+80,127,70,20,null,"transform");
	menu_transform.setPopupSize(180,180);
	butTransform.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0){
		menu_transform.setLocation(MainFrame.this.getX()+330,MainFrame.this.getY()+30);
		if(menu_transform.isVisible())menu_transform.setVisible(false);else menu_transform.setVisible(true);
	}});
	
	templateTypeItems=new JMenuItem[TemplateType.lastIndexOfTemplateType];
	templateTypeIcons=new ImageIcon[TemplateType.lastIndexOfTemplateType];
	for(int i=0;i<templateTypeItems.length;i++){
		JMenuItem item=new JMenuItem(TemplateType.getTemplateTypeName(i)+" ("+TemplateType.toString(i)+")");
		item.setIcon(TemplateType.getImageIcon(i));
		templateTypeIcons[i]=TemplateType.getImageIcon(i);
		templateTypeItems[i]=item;
		menu_type.add(templateTypeItems[i]);
	}
		
	butType=C_Resource.getButton(add,C_Resource.templateTypes+"type.png",otstup5+155,5,"");////=getButton("type",otstup5+155,5,30,20,null,"type");menu_type.setPopupSize(230, 250);
	butType.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0){
		menu_type.setLocation(MainFrame.this.getX()+330,MainFrame.this.getY()+30);
		if(menu_type.isVisible())menu_type.setVisible(false);else menu_type.setVisible(true);
	}});
	
	int otstup2=18;
	butIndex=(JButton)add.component("button",5,otstup2,45,18,Protocol.templateDotTypeRED_NORMAL+"",null);//=getButton(Protocol.RED_NORMAL.toString(),5,otstup2,45,18,null,null);
	butIndex.setEnabled(false);
	butIndex.setText("");
	
	butEMPTY=C_Resource.getButton(add,C_Resource.dotTypes+"N.png",5,otstup2+22,Protocol.templateDotTypeNULL);//=getButton(Protocol.NULL.toString(),5,otstup2+22,20,18,Color.white,Protocol.NULL.toString());
	butANY=C_Resource.getButton(add,C_Resource.dotTypes+"A.png",30,otstup2+22,Protocol.templateDotTypeANY);//=getButton(Protocol.ANY.toString(),30,otstup2+22,20,18,Color.green,Protocol.ANY.toString());
	butBLUE=C_Resource.getButton(add,C_Resource.dotTypes+"B.png",5,otstup2+44,Protocol.templateDotTypeBLUE);//=getButton(Protocol.BLUE.toString(),5,otstup2+44,20,18,Color.blue,Protocol.BLUE.toString());
	butBLUE_EMPTY=C_Resource.getButton(add,C_Resource.dotTypes+"C.png",30,otstup2+44,Protocol.templateDotTypeBLUE_EMPTY);//=getButton(Protocol.BLUE_EMPTY.toString(),30,otstup2+44,20,18,Color.cyan,Protocol.BLUE_EMPTY.toString());
	butRED=C_Resource.getButton(add,C_Resource.dotTypes+"R.png",5,otstup2+66,Protocol.templateDotTypeRED);//=getButton(Protocol.RED.toString(),5,otstup2+66,20,18,Color.red,Protocol.RED.toString());
	butRED_EMPTY=C_Resource.getButton(add,C_Resource.dotTypes+"P.png",30,otstup2+66,Protocol.templateDotTypeRED_EMPTY);//=getButton(Protocol.RED_EMPTY.toString(),30,otstup2+66,20,18,Color.pink,Protocol.RED_EMPTY.toString());
	butLAND=C_Resource.getButton(add,C_Resource.dotTypes+"L.png",5,otstup2+88,Protocol.templateDotTypeLAND);//=getButton(Protocol.LAND.toString(),5,otstup2+88,20,18,Color.black,Protocol.LAND.toString());
	
	butRedNormal=C_Resource.getButton(add,C_Resource.targetDotTypes+Math.abs(Protocol.templateDotTypeRED_NORMAL)+".png",5,otstup2+110,Protocol.templateDotTypeRED_NORMAL);//=getButton(Protocol.RED_NORMAL.toString(),5,otstup2+110,20,18,Color.red,Protocol.RED_NORMAL.toString());
	butRedAttack=C_Resource.getButton(add,C_Resource.targetDotTypes+Math.abs(Protocol.templateDotTypeRED_ATTACK)+".png",5,otstup2+132,Protocol.templateDotTypeRED_ATTACK);//=getButton(Protocol.RED_ATTACK.toString(),5,otstup2+132,20,18,Color.red,Protocol.RED_ATTACK.toString());
	butRedProtection=C_Resource.getButton(add,C_Resource.targetDotTypes+Math.abs(Protocol.templateDotTypeRED_PROTECTION)+".png",30,otstup2+132,Protocol.templateDotTypeRED_PROTECTION);//=getButton(Protocol.RED_PROTECTION.toString(),30,otstup2+132,20,18,Color.red,Protocol.RED_PROTECTION.toString());
	butBlueNormal=C_Resource.getButton(add,C_Resource.targetDotTypes+Math.abs(Protocol.templateDotTypeBLUE_ATTACK)+".png",30,otstup2+110,Protocol.templateDotTypeBLUE_ATTACK);//=getButton(Protocol.BLUE_ATTACK.toString(),30,otstup2+110,20,18,Color.blue,Protocol.BLUE_ATTACK.toString());
	
	butRED.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0){repaintButIndex(butRED);}});
	butRED_EMPTY.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0){repaintButIndex(butRED_EMPTY);}});
	butBLUE.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0){repaintButIndex(butBLUE);}});
	butBLUE_EMPTY.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0){repaintButIndex(butBLUE_EMPTY);}});
	butEMPTY.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0){repaintButIndex(butEMPTY);}});
	butANY.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0){repaintButIndex(butANY);}});
	butLAND.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0){repaintButIndex(butLAND);}});
	butRedNormal.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0){repaintButIndex(butRedNormal);}});repaintButIndex(butRedNormal);
	butRedAttack.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0){repaintButIndex(butRedAttack);}});
	butRedProtection.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0){repaintButIndex(butRedProtection);}});
	butBlueNormal.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent arg0){repaintButIndex(butBlueNormal);}});
	
	int otstupX=otstup5+80,otstupY=55;
	butLT=C_Resource.getButton(add,C_Resource.arrows+"lt.png",otstupX,otstupY,"");
	butT=C_Resource.getButton(add,C_Resource.arrows+"top.png",otstupX+25,otstupY,"");//=getButton("top",otstupX+25,otstupY,20,20,null,"");
	butRT=C_Resource.getButton(add,C_Resource.arrows+"rt.png",otstupX+50,otstupY,"");//=getButton("rt",otstupX+50,otstupY,20,20,null,"");
	butL=C_Resource.getButton(add,C_Resource.arrows+"left.png",otstupX,otstupY+25,"");//=getButton("left",otstupX,otstupY+25,20,20,null,"");
	labMove=(JLabel)add.component("label",otstupX+30,otstupY+25,20,20,"",null);labMove.setIcon(new ImageIcon(C_Resource.arrows+"move.png"));
	butR=C_Resource.getButton(add,C_Resource.arrows+"right.png",otstupX+50,otstupY+25,"");//=getButton("right",otstupX+50,otstupY+25,20,20,null,"");
	butLB=C_Resource.getButton(add,C_Resource.arrows+"lb.png",otstupX,otstupY+50,"");//=getButton("lb",otstupX,otstupY+50,20,20,null,"");
	butB=C_Resource.getButton(add,C_Resource.arrows+"bottom.png",otstupX+25,otstupY+50,"");//=getButton("bottom",otstupX+25,otstupY+50,20,20,null,"");
	butRB=C_Resource.getButton(add,C_Resource.arrows+"rb.png",otstupX+50,otstupY+50,"");//=getButton("rb",otstupX+50,otstupY+50,20,20,null,"");
	
	butInfoPanel=(JButton)add.component("button",otstup5+80,30,105,20,"панель",null);//=getButton("панель",otstup5+80,30,105,20,null,null);
	butInfoPanel.setEnabled(false);
}

void repaintButIndex(JButton button){butIndex.setText(button.getName());butIndex.setBackground(button.getBackground());}

public void run(){}
public void mousePressed(MouseEvent me) {}
public void windowActivated(WindowEvent e) {}
public void windowClosed(WindowEvent e) {}
public void windowClosing(WindowEvent e) {}
public void windowDeactivated(WindowEvent e) {}
public void windowDeiconified(WindowEvent e) {}
public void windowIconified(WindowEvent e) {}
public void windowOpened(WindowEvent e) {}
public void mouseClicked(MouseEvent me) {}
public void mouseEntered(MouseEvent me) {}
public void mouseExited(MouseEvent me) {}
public void mouseReleased(MouseEvent me) {}
public void mouseDragged(MouseEvent arg0) {}
public void mouseMoved(MouseEvent me) {}

}
