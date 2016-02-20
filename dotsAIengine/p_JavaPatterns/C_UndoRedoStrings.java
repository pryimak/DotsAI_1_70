package p_JavaPatterns;

public class C_UndoRedoStrings {
	private String changes[];
	private int index=0;
	
public C_UndoRedoStrings(){
	changes=new String[100];
	for(int i=0;i<changes.length;i++)changes[i]="";
}

/*public C_UndoRedoStrings(int undoRedoBaseSize){
	changes=new String[undoRedoBaseSize];
	for(int i=0;i<changes.length;i++)changes[i]="";
}*/

public void insert(byte[] arr){
	String content="";
	for(int i=0;i<arr.length;i++){
		content+=arr[i]+"";
	}
	
	if(changes[index].equalsIgnoreCase(content)){}//no inserted
	else{//inserted index
		if(index==(changes.length-1)){
			for(int i=0;i<changes.length-1;i++)changes[i]=changes[i+1];
			changes[index]=content;
		}
		else{
			for(int i=index+1;i<changes.length-1;i++)changes[i+1]="";
			index++;
			changes[index]=content;
		}
	}
}

public byte[] getUndo(){
	if(index>0&changes[index-1]!="")index--;
	
	byte[] arr=new byte[81];
	int idx=0;
	for(int i=0;i<arr.length;i++){
		if(changes[index].substring(idx,idx+1).equals("-")){
			arr[i]=new Byte(changes[index].substring(idx,idx+2)).byteValue();
			idx+=2;
		}else{
			arr[i]=new Byte(changes[index].substring(idx,idx+1)).byteValue();
			idx++;
		}
	}
	
	return arr;
}

public byte[] getRedo(){
	if(index<(changes.length-1)&changes[index+1]!="")index++;
	
	byte[] arr=new byte[81];
	int idx=0;
	for(int i=0;i<arr.length;i++){
		if(changes[index].substring(idx,idx+1).equals("-")){
			arr[i]=new Byte(changes[index].substring(idx,idx+2)).byteValue();
			idx+=2;
		}else{
			arr[i]=new Byte(changes[index].substring(idx,idx+1)).byteValue();
			idx++;
		}
	}
	
	return arr;
}

}
