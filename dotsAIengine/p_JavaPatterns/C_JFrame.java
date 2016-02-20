/*Ётот класс обеспечивает создание JFrame окна дл€ класса,
 * создающего экземпл€р данного класса, а также создание заставки
 *
 * ƒл€ этого необходимо в вызывающем классе прописать код:
 *
 *
 *		JFrame frame=new JFrame();
 *
 *		public Main(){
 * 			new C_JFrame(frame,"LYRICS v.1.1",false,600,800,Color.cyan, new int[]{5,5,2009});
 * 		}
 *
 * где "frame" - объект в главном классе, содержащий окно JFrame дл€ главного класса
 * где "LYRICS v.1.1" - заголовок окна с версией программы
 * где "false" - возможность изменени€ границ окна типа boolean
 * где 600,800 - ширина и высота окна
 * где Color.cyan - фоновый цвет окна
 * где new int[]{5,5,2009} - массив с параметрами заставки
 * 		(5,5 - координаты отображени€ заставки, 2009 - год)
 *
 */

package p_JavaPatterns;

import java.awt.Color;
import java.awt.Toolkit;
import javax.swing.JFrame;

public class C_JFrame {//класс

	//конструктор с параметрами
public C_JFrame(JFrame frame, String title, boolean resizable,int width,int height,Color background){
	frame.setTitle(title);//установить заголовок
	frame.setResizable(resizable);//установить возможность изменени€ границ окна
	//установить компоновку с шириной и высотой окна
	frame.getContentPane().setLayout(new p_JavaPatterns.C_Layout(width,height));
	frame.getContentPane().setBackground(background);//установить цвет фона
	frame.pack();//дл€ вывода окна на экран
	frame.show();//дл€ вывода окна на экран
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//дл€ корректного закрыти€ окна
	setCenterAlign(frame);
}

public void setCenterAlign(JFrame frame){
	frame.move((int)(Toolkit.getDefaultToolkit().getScreenSize().width*0.5f-frame.getWidth()/2), 
			(int)(Toolkit.getDefaultToolkit().getScreenSize().height*0.5f-frame.getHeight()/2));
}

}
