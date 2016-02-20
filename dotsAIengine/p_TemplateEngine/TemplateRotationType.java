package p_TemplateEngine;

import java.awt.Point;

import p_DotsAI.Protocol;

public class TemplateRotationType{

	public static int templateRotationTypeR0=0;
	public static int templateRotationTypeR90=1;
	public static int templateRotationTypeR180=2;
	public static int templateRotationTypeR270=3;
	public static int templateRotationTypeGORIZONTAL=4;
	public static int templateRotationTypeVERTICAL=5;
	public static int templateRotationTypeGORIZONTAL90=6;
	public static int templateRotationTypeVERTICAL90=7;
	
	public static byte[] getTransformArray(int templateRotationType,byte[] arr) {
		byte[] str1=new byte[81];
		if(templateRotationType==templateRotationTypeR0){//чтобы создался новый объект
			for(int i=0;i<81;i++){
				str1[i]=arr[i];
			}
			return arr;
		}if(templateRotationType==templateRotationTypeR90){
			int idx=0;
			for(int i=0;i<9;i++){for(int j=9-1;j>=0;j--){str1[idx]=arr[9*j+i];idx++;}}
			return str1;
		}
		if(templateRotationType==templateRotationTypeR180){arr=getTransformArray(templateRotationTypeR90,arr);return(getTransformArray(templateRotationTypeR90,arr));}
		if(templateRotationType==templateRotationTypeR270){arr=getTransformArray(templateRotationTypeR180,arr);return(getTransformArray(templateRotationTypeR90,arr));}
		if(templateRotationType==templateRotationTypeGORIZONTAL){arr=getTransformArray(templateRotationTypeR180,arr);return(getTransformArray(templateRotationTypeVERTICAL,arr));}
		if(templateRotationType==templateRotationTypeVERTICAL){
			int idx=0;
			for(int i=Protocol.sizeY_TE-1;i>=0;i--){
				for(int j=0;j<9;j++){
					str1[idx]=arr[Protocol.sizeX_TE*i+j];
					idx++;
				}
			}
			return str1;
		}if(templateRotationType==templateRotationTypeGORIZONTAL90){arr=getTransformArray(templateRotationTypeGORIZONTAL,arr);return(getTransformArray(templateRotationTypeR90,arr));}
		if(templateRotationType==templateRotationTypeVERTICAL90){arr=getTransformArray(templateRotationTypeVERTICAL,arr);return(getTransformArray(templateRotationTypeR90,arr));}
		return arr;
	}
	
	public static Point getTransformPoint(int type,Point p) {
		if(type==templateRotationTypeR0)return p;
		if(type==templateRotationTypeR90)return new Point(-p.y,p.x);
		if(type==templateRotationTypeR180)return new Point(-p.x,-p.y);
		if(type==templateRotationTypeR270)return new Point(p.y,-p.x);
		if(type==templateRotationTypeGORIZONTAL)return new Point(-p.x,p.y);
		if(type==templateRotationTypeVERTICAL)return new Point(p.x,-p.y);
		if(type==templateRotationTypeGORIZONTAL90)return new Point(-p.y,-p.x);
		if(type==templateRotationTypeVERTICAL90)return new Point(p.y,p.x);
		return p;
	}
	
}
