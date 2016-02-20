package p_JavaPatterns;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class C_OpenFile {

	JFileChooser fc;
	C_ReadAndWriteFile file=new C_ReadAndWriteFile();
	String strSave="";String fileType="";
	
public C_OpenFile(JFrame frame,String[]fileFilter){
	C_AddComponent add=new C_AddComponent(frame.getContentPane());
	fc=(JFileChooser)add.component("fileChooser",0,0,0,0,"",actionJFileChooser);
	if(fileFilter!=null)fc.setFileFilter(new C_ExtensionFileFilter(fileFilter));
	fc.setCurrentDirectory(new File(C_Resource.savedGames));
	fc.showOpenDialog(frame);
}	

ActionListener actionJFileChooser=new ActionListener(){
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("ApproveSelection")){
			try {strSave=file.ReadTxtFile(fc.getSelectedFile().getPath());
			} catch (Exception e1) {e1.printStackTrace();}
		}
	}
};

public String getFileContent(){return strSave;}

}
