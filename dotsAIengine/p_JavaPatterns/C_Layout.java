/*Этот класс обеспечивает установку размеров для создаваемого окна JFrame.
 * Конструктор класса принимает требуемые размеры окна в виде ширины и высоты окна:
 *
 * 		int width,int height
 *
 *Для установки размеров окна необходимо в вызывающем классе прописать код:
 *
 *		frame.getContentPane().setLayout(new p_GUI.C_Layout(width,height));
 */

package p_JavaPatterns;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

public class C_Layout implements LayoutManager {//класс

	int width;//ширина окна
	int height;//высота окна

public C_Layout(int width,int height){//конструктор класса
		this.width=width;//установить ширину, равную входному значению
		this.height=height;//установить высоту, равную входному значению
		}

public void addLayoutComponent(String name, Component comp) {  }
public void removeLayoutComponent(Component comp) { }
public Dimension preferredLayoutSize(Container parent) {
        return new Dimension(width, height);        }
public Dimension minimumLayoutSize(Container parent) {
    	return new Dimension(width, height);        }
public void layoutContainer(Container parent) {  }
}

