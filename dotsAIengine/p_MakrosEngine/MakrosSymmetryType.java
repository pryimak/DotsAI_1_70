package p_MakrosEngine;

public class MakrosSymmetryType {
	
	public static int makrosSymmetryTypeGORIZONTAL=0;
	public static int makrosSymmetryTypeVERTICAL=1;
	public static int makrosSymmetryTypeMAIN_DIAGONAL=2;
	public static int makrosSymmetryTypeSECOND_DIAGONAL=3;	
	
	public static String toStringMakrosSymmetryType(int symmetryType){
		if(symmetryType==MakrosSymmetryType.makrosSymmetryTypeGORIZONTAL)return "gor";
		if(symmetryType==MakrosSymmetryType.makrosSymmetryTypeVERTICAL)return "vert";
		if(symmetryType==MakrosSymmetryType.makrosSymmetryTypeMAIN_DIAGONAL)return "mdiag";
		if(symmetryType==MakrosSymmetryType.makrosSymmetryTypeSECOND_DIAGONAL)return "sdiag";
		return "error";
	}

	public static String getMakrosSymmetryTypeDescription(int symmetryType){
		if(symmetryType==MakrosSymmetryType.makrosSymmetryTypeGORIZONTAL)return "�����������";
		if(symmetryType==MakrosSymmetryType.makrosSymmetryTypeVERTICAL)return "���������";
		if(symmetryType==MakrosSymmetryType.makrosSymmetryTypeMAIN_DIAGONAL)return "������� ���������";
		if(symmetryType==MakrosSymmetryType.makrosSymmetryTypeSECOND_DIAGONAL)return "�������� ���������";
		return "error";
	}
	
}
