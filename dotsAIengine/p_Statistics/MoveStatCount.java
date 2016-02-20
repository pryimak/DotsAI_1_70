package p_Statistics;

public class MoveStatCount {

	public int moveStat[];
	public int moveStatMakros[];
	public int moveStatMax;
	public String lastAImoveString;
	public MoveStatType lastAImoveType;
	boolean isByMakros;
	
	public void addMoveStat(MoveStatType moveStatType,boolean isByMakros){	
		moveStat[MoveStatType.getMoveStatIndex(moveStatType)]++;
		
		this.isByMakros=isByMakros;
		if(isByMakros)moveStatMakros[MoveStatType.getMoveStatIndex(moveStatType)]++;
		
		lastAImoveString=MoveStatType.toString(moveStatType);
		lastAImoveType=moveStatType;
	}

	public int getMoveStatLength(){
		if(moveStat==null){
			moveStat=new int[MoveStatType.getMoveStatIndex(MoveStatType.ERROR)];
			moveStatMakros=new int[MoveStatType.getMoveStatIndex(MoveStatType.ERROR)];
		}
		return moveStat.length;
	}

	public int getMoveStatMax(){	
		moveStatMax=0;
		for(int i=0;i<moveStat.length;i++){
			if(moveStat[i]>moveStatMax)moveStatMax=moveStat[i];
		}
		return moveStatMax+1;
	}

	public void clearMoveStat(){	
		for(int i=0;i<moveStat.length;i++){
			moveStat[i]=0;
			moveStatMakros[i]=0;
		}
		lastAImoveString="";
	}
}
