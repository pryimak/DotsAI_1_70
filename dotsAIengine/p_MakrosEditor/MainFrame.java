package p_MakrosEditor;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import p_JavaPatterns.C_AddComponent;
import p_JavaPatterns.C_JFrame;
import p_JavaPatterns.C_Resource;

public class MainFrame extends JFrame implements Runnable,MouseListener{
	
	Thread t=new Thread(this);
	JOptionPane msg=new JOptionPane();
	C_AddComponent add=new C_AddComponent(this.getContentPane());
	
	JLabel labMakros;
	JButton butAddMove,butDelMove,butEditMove,
		butType,butTypeRed,butTypeBlue,butTypeNull,butBlueDeleteForMakrosEdit,
		butExpand,butCollapse,butPaintChilds,
		//butSaveMakrosBase,
		butSetSymmetry;	
	
	int X=270,Y=-100;
	
	JTree tree;//=new JTree();
	
MainFrame(){

	this.setIconImage(C_Resource.icon);
	new C_JFrame(this,"Dots Makros Editor",false,500,750,new Color(245,245,245));
	this.move(getX()+470, getY());
	this.addMouseListener(this);
	
	labMakros=(JLabel)add.component("label",10+X,2,150,18,"",null);
	//butSaveMakrosBase=C_Resource.getButton(add, C_Resource.base+"resave.png", 10+X,220+Y, "");
	
	butType=(JButton)add.component("button",10+X,130+Y,70,20,"B",null);	
	butTypeBlue=C_Resource.getButton(add, C_Resource.dotTypes+"B.png", 10+X,160+Y, "");
	butBlueDeleteForMakrosEdit=C_Resource.getButton(add, C_Resource.dotTypes+"blueDeleteForMakrosEdit.png", 10+X,185+Y, "");
	butTypeRed=C_Resource.getButton(add, C_Resource.dotTypes+"R.png", 35+X,160+Y, "");
	butTypeNull=C_Resource.getButton(add, C_Resource.dotTypes+"N.png", 60+X,160+Y, "");
	
	butType.setBackground(Color.blue);
	butType.setForeground(Color.lightGray);
	butType.setEnabled(false);
	
	butTypeBlue.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			butType.setText("B");butType.setBackground(Color.blue);butType.setForeground(Color.lightGray);
		}
	});
	butBlueDeleteForMakrosEdit.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			butType.setText("E");butType.setBackground(Color.blue);butType.setForeground(Color.lightGray);
		}
	});
	butTypeRed.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			butType.setText("R");butType.setBackground(Color.red);butType.setForeground(Color.black);
		}
	});
	butTypeNull.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			butType.setText("N");butType.setBackground(Color.white);butType.setForeground(Color.black);
		}
	});
	
	butAddMove=(JButton)add.component("button",10+X,185,160,20,"<html>добавить ход/макрос",null);
	butDelMove=(JButton)add.component("button",10+X,215,160,20,"<html>удалить ход/макрос",null);
	butEditMove=(JButton)add.component("button",10+X,245,160,20,"<html>редактировать ход",null);
	
	butExpand=(JButton)add.component("button",88+X,30,138,20,"<html>раскрыть дерево",null);
	butCollapse=(JButton)add.component("button",88+X,55,138,20,"<html>свернуть дерево",null);
	
	butPaintChilds=(JButton)add.component("button",88+X,85,138,45,"<html>показать/скрыть вложенные точки",null);
	butSetSymmetry=(JButton)add.component("button",88+X,135,138,45,"<html>указать тип симметрии",null);
}

public void run(){}
public void mousePressed(MouseEvent me) {}
public void mouseClicked(MouseEvent me) {}
public void mouseEntered(MouseEvent me) {}
public void mouseExited(MouseEvent me) {}
public void mouseReleased(MouseEvent me) {}

}
