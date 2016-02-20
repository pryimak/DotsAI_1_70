/*Ётот класс добавл€ет в контейнер элементы интерфейса, чтобы их потом можно было отображать
 * в окне JFrame
 *
 *  онструктор класса принимает в качестве параметра котейнер, в котором
 * будут отображены создаваемые элементы интерфейса
 *
 *
 *¬ вызывающем классе необходимо прописать код
 *
 *C_AddComponent add=new C_AddComponent(frame.getContentPane());
 *
 *ѕримеры элементов:
 *	JButton butAdd=add.buttonSpecial("butAdd",10,765,actionAdd);
	JButton butDelete=add.buttonSpecial("butDelete",120,765,actionDelete);
	JButton butClose=add.buttonSpecial("butClose",500,765,null);
	List listAvtors=(List)add.component("list",10,40,200,410,"",itemListenerListAvtors_1);
	List listSongs=(List)add.component("list",10,460,200,300,"",itemListenerListSongs_1);
	JLabel labSong=(JLabel)add.component("label",220,5,370,30,"",null);
	JTextArea textArea=(JTextArea)add.componentInScroll("textArea",220,40,370,720,"",null);
 *
 */

package p_JavaPatterns;

import java.awt.Checkbox;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.util.EventListener;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.CaretListener;

public class C_AddComponent {//класс

	Container con;//контейнер
	JOptionPane msg=new JOptionPane();//создать форму сообщени€

public C_AddComponent(Container con){//конструктор
		this.con=con;//установить контейнер
	}

//создать элемент интерфейса
public Component component(String strCom,int x,int y,int width,int height,
		String strText,EventListener listener){

	//объ€вить элемент
	Component com=null;

	List list = null;//объ€вить список
	JLabel label=null;//объ€вить метку
	JButton button=null;//объ€вить кнопку
	JTextField field=null;//объ€вить текстовое поле
	JFileChooser fc=null;//объ€вить окно выбора файла
	Checkbox chk=null;//объ€вить радиокнопку
	JMenuItem jMenuItem=null;

	if(strCom.equalsIgnoreCase("checkbox")){//если необходима радиокнопка
		chk=new Checkbox();chk.addItemListener((ItemListener)listener);chk.setLabel(strText);
		com=chk;com.setBounds(x, y, width, height);//добавить и установить размеры элемента
		}
	if(strCom.equalsIgnoreCase("list")){//если необходим список
		list=new List();list.addItemListener((ItemListener)listener);com=list;
		com.setBounds(x, y, width, height);//добавить и установить размеры элемента
		}
	//если необходима метка
	if(strCom.equalsIgnoreCase("label")){label=new JLabel();label.setText(strText);com=label;
		com.setBounds(x, y, width, height);//добавить и установить размеры элемента
		}
	//если необходима кнопка
	if(strCom.equalsIgnoreCase("button")){button=new JButton();button.setText(strText);
		button.setCursor(new Cursor(12));button.addActionListener((ActionListener)listener);
		com=button;com.setBounds(x, y, width, height);//добавить и установить размеры элемента
		}
	//если необходим jmenuitem
		if(strCom.equalsIgnoreCase("jmenuitem")){jMenuItem=new JMenuItem();jMenuItem.setText(strText);
			jMenuItem.setCursor(new Cursor(12));jMenuItem.addActionListener((ActionListener)listener);
			com=jMenuItem;//добавить элемент
			}
	//если необходимо текстовое поле
	if(strCom.equalsIgnoreCase("field")){field=new JTextField();field.setText(strText);
		field.addCaretListener((CaretListener)listener);com=field;
		com.setBounds(x, y, width, height);//добавить и установить размеры элемента
		}
	//если необходимо окно выбора файла
	if(strCom.equalsIgnoreCase("fileChooser")){fc=new JFileChooser();
		fc.addActionListener((ActionListener)listener);com=fc;
		}

	//если такой strCom не предусмотрен, вывести собщение об ошибке
	if(com==null){msg.showMessageDialog(con,"<HTML><FONT size=5 color=red>" +
			"ќшибка создани€ компонента "+strCom+"!<BR>ћетод не предусматривает" +
					"<BR>создание данного компонента");return null;}//ничего не вернуть

	con.add(com);//com.setBounds(x, y, width, height);//добавить и установить размеры элемента
	com.requestFocus();//установить фокус, чтобы элемент выводилс€ на форму, а не скрывалс€

	return com;//вернуть элемент
}

}
