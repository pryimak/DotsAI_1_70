package p_JavaPatterns;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

public class C_ReadAndWriteFile {

public C_ReadAndWriteFile(){}

public String ReadTxtFile(String strInputFile){
	StringBuffer s=new StringBuffer();
	try {
		FileReader r=new FileReader(strInputFile);
		CharArrayWriter w=new CharArrayWriter();
		int c=0;
		while(true){c=r.read();	if(c!=-1)w.write(c);else break;	}
		s.append(w.toCharArray());
	}catch(Exception e){}
	return s.substring(0, s.length());
}

public void WriteTxtFile(String strOutputFile, String strOutputText){try {
	File f=new File(strOutputFile);
	f.delete();
	FileOutputStream fout=new FileOutputStream(strOutputFile);
	fout.write(strOutputText.getBytes());
}catch(Exception e){}}

}
