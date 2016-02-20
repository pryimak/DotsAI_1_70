package p_JavaPatterns;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class C_ExtensionFileFilter extends FileFilter{

	private String[] strFile;

public C_ExtensionFileFilter(String[] strFile){this.strFile=strFile;}
public String getDescription(){return "";}

public boolean accept(File f){//установить фильтр на файлы
	if (f.isDirectory()){
		return true;
	}
	for(int i=0;i<strFile.length;i++){
		if (f.getName().toLowerCase().endsWith(strFile[i])){
			return true;
		}
	}
	return false;
}

}