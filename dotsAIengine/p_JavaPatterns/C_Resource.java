package p_JavaPatterns;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;

public class C_Resource {

	public static String directory="dotsAIresources//";
	
	public static String buttons=directory+"buttons//";
	public static String gui=directory+"gui//";
	//public static String makrosBase=directory+"makrosBase//";
	public static String savedGames=directory+"savedGames//";
	//public static String templateBase=directory+"templateBase//";
	
	public static String arrows=buttons+"arrows//";
	public static String base=buttons+"base//";
	public static String dotTypes=buttons+"dotTypes//";
	public static String navigation=buttons+"navigation//";
	public static String rotates=buttons+"rotates//";
	public static String targetDotTypes=buttons+"targetDotTypes//";
	public static String templateTypes=buttons+"templateTypes//";
	
	public static IconImage icon=new IconImage(C_Resource.gui+"icon.png");
	
	public static JButton getButton(C_AddComponent add,String icon,int X,int Y,int buttonName){
		ImageIcon i=new ImageIcon(icon);
		JButton b=(JButton)add.component("button",X,Y,i.getIconWidth(),i.getIconHeight(),"",null);
		b.setName(buttonName+"");
		b.setIcon(i);
		return b;
	}
	
	public static JButton getButton(C_AddComponent add,String icon,int X,int Y,String buttonName){
		ImageIcon i=new ImageIcon(icon);
		JButton b=(JButton)add.component("button",X,Y,i.getIconWidth(),i.getIconHeight(),"",null);
		b.setName(buttonName);
		b.setIcon(i);
		return b;
	}
	
	public static JMenuItem getMenuItem(String icon,String itemName){
		ImageIcon i=new ImageIcon(icon);		
		JMenuItem b=new JMenuItem(itemName);
		b.setIcon(i);
		return b;
	}
	
	private static class IconImage extends BufferedImage{
		public IconImage(String src){
			super(20,20,BufferedImage.TYPE_INT_RGB);
			Graphics2D g=(Graphics2D)super.getGraphics();

			File f=new File(src);
			BufferedImage image=null;
			try{image = ImageIO.read(f);}catch(Exception e){}
			g.drawImage(image, 0, 0, null);
		}
	}
}
