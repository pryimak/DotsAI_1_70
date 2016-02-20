package p_GUI;

import java.awt.*;

import javax.swing.*;

import p_DotsAI.Protocol;
import p_JavaPatterns.C_AddComponent;
import p_JavaPatterns.C_JFrame;
import p_JavaPatterns.C_Resource;

public class AboutGameFrame{//о программе

	public JFrame frame=new JFrame();
	C_AddComponent add=new C_AddComponent(frame.getContentPane());
	
	JLabel appImage=new JLabel(new ImageIcon(C_Resource.gui+"dotsAI.png"));
	
public AboutGameFrame(){	

	new C_JFrame(frame, "О программе "+Protocol.appName+" версия "+Protocol.appVersion, false, 550, 199, null);
	frame.setIconImage(C_Resource.icon);
	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	frame.setAlwaysOnTop(true);
	frame.getContentPane().add(appImage);//фоновый рисунок
	appImage.setBounds(0,-5, 250, 205);//размеры фонового рисунка	
	
	JLabel info=(JLabel)add.component("label",255, -1, 300, 195,"<html><font size=3>Программа для игры в точки против ИИ<br><br><font size=5>"+
			Protocol.appName+" <font size=3>(PointsAI), 2011-2016<br><br>(Dots Artificial Intelligence)<br><br>версия " +
			Protocol.appVersion+" ("+Protocol.appDate+")<br><br>автор Алексей Приймак, лицензия - GPL v2"+
			"<br><br><font color=blue><u>http://playdots.ru/ai</u></font>",null);
	info.setBackground(Color.white);
}

}


