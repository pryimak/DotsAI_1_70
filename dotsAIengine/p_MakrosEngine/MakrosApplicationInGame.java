package p_MakrosEngine;

import java.awt.Point;
import java.util.Random;

import p_DotsAI.Protocol;
import p_DotsAI.Protocol.Game;
import p_TemplateEngine.TemplateRotationType;
import p_TemplateEngine.TemplateType;
import p_TemplateEngine.TemplateView;

public class MakrosApplicationInGame {

	public Point center;
	public Makros makros;
	public Point transformAIPoint;
	private TreeNodeMakros currentNode;
	private Random rand;
	private int isSearchBySymmetry;//0-еще не найдено, 1-не по симметрии, 2-по симметрии
	public int templateRotationType;
	public int templateType;
	public int templateIndex;
	//byte[] initialContent;
	
public MakrosApplicationInGame(Game game,Makros makros,int templateType,int templateIndex,int templateRotationType,int x,int y){
	this.makros=makros;
	isSearchBySymmetry=0;
	currentNode=makros.node;
	this.templateRotationType=templateRotationType;
	this.templateType=templateType;
	this.templateIndex=templateIndex;
	
	rand=new Random();
	
	center=new Point();//центр шаблона, в котором находится макрос
	if(TemplateType.isSide(this.templateType)){
		if(x<9){center.x=4;center.y=y;}
		else if(x>game.sizeX-9){center.x=game.sizeX-3;center.y=y;}
		else if(y<9){center.y=4;center.x=x;}
		else if(y>game.sizeY-9){center.y=game.sizeY-3;center.x=x;}
	}
	else {center.x=x;center.y=y;}
	
	//System.out.println("add makros, centerPoint="+center.toString());
	/*initialContent=TemplateType.getContentForTemplateCreation(game, center, game.fieldState, templateType);
	initialContent=TemplateRotationType.getTransformArray(templateRotationType, initialContent);
	System.out.println("add makros, initialContent="+arrayToString(initialContent));
	System.out.println("-----------------------------------");*/
}	

private String arrayToString(byte[] arr){//только для изменения движка при объединении шаблонов с макросами
	String s="";
	for(int i=0;i<arr.length;i++){
		s+=Math.abs(arr[i]);
	}
	return s;
}

public boolean isExistsLevelMove(Point lastBluePoint,boolean isMoveOnlyByDefault,Point recommendedMove){
	//System.out.println("===================================================== templ idx="+templateIndex+"; isSearchBySymmetry="+isSearchBySymmetry+"; templateRotationType="+templateRotationType);
	//System.out.println("lastBluePoint.x="+lastBluePoint.x+", lastBluePoint.y="+lastBluePoint.y+", center.x="+center.x+", center.y="+center.y);
	if((lastBluePoint.x!=center.x||lastBluePoint.y!=center.y)&&!isMoveOnlyByDefault){//если совпадают, то переходить сразу к поиску default хода
		//System.out.println("search single move");
		//найти в дереве lastBluePoint, в котором затем искать ответный красный ход
		for(int i=0;i<currentNode.childNodes.size();i++){
			//System.out.println("--------search iteration="+i+"; isSearchBySymmetry="+isSearchBySymmetry);
			TreeNodeMakros parent=currentNode.childNodes.get(i);
			if(parent.blueIsDefault)continue;			
			if(parent.moveType==Protocol.moveTypeRed)continue;
			
			for(int j=0;j<parent.points.size();j++){
				Point p=TemplateRotationType.getTransformPoint(templateRotationType, parent.points.get(j));						
				
				if(isSearchBySymmetry==0||isSearchBySymmetry==1){
					if(p.x+center.x==lastBluePoint.x&&p.y+center.y==lastBluePoint.y){
						TreeNodeMakros n=null;
						
						if(recommendedMove==null){
							n=getRandomChildNode(parent,Protocol.moveTypeRed);
						}else{//рекомендованный ход - при повторе игры делать ход, который был, т.к. в макромах может быть случайный выбор хода
							for(int k=0;k<parent.childNodes.size();k++){
								if(parent.childNodes.get(k).moveType==Protocol.moveTypeRed){
									n=parent.childNodes.get(k);
									Point newPoint=TemplateRotationType.getTransformPoint(templateRotationType, n.points.get(0));
									if(recommendedMove.x!=center.x+newPoint.x|recommendedMove.y!=center.y+newPoint.y){
										//System.out.println("not a recommendedMove");
										continue;
									}
								}
							}
						}
						
						if(n==null)continue;
						Point newPoint=TemplateRotationType.getTransformPoint(templateRotationType, n.points.get(0));
						transformAIPoint=new Point(center.x+newPoint.x,center.y+newPoint.y);	
						//System.out.println("find no symmetry");						
						setMarkerOnCurrentNode(n);
						currentNode=n;
						isSearchBySymmetry=1;
						return true;
					}
				}
				
				if(isSearchBySymmetry==0||isSearchBySymmetry==2){
					if(makros.symmetryType==-1)continue;
					Point pSymmetry=makros.getSymmetryPoint(p,templateRotationType);
					/*System.out.println("originalPoint="+parent.points.get(j).toString());
					System.out.println("transformPoint="+p.toString());
					System.out.println("pSymmetry="+pSymmetry.toString());
					System.out.println("pSymmetry.x+center.x="+(pSymmetry.x+center.x));
					System.out.println("pSymmetry.y+center.y="+(pSymmetry.y+center.y));*/
					
					if(pSymmetry.x+center.x==lastBluePoint.x&&pSymmetry.y+center.y==lastBluePoint.y){
						
						TreeNodeMakros n=null;
						
						if(recommendedMove==null){
							n=getRandomChildNode(parent,Protocol.moveTypeRed);
						}else{//рекомендованный ход - при повторе игры делать ход, который был, т.к. в макромах может быть случайный выбор хода
							for(int k=0;k<parent.childNodes.size();k++){
								if(parent.childNodes.get(k).moveType==Protocol.moveTypeRed){
									n=parent.childNodes.get(k);
									Point newPoint=TemplateRotationType.getTransformPoint(templateRotationType, n.points.get(0));
									Point newPointSymmetry=makros.getSymmetryPoint(newPoint,templateRotationType);
									if(recommendedMove.x!=center.x+newPointSymmetry.x|recommendedMove.y!=center.y+newPointSymmetry.y){
										//System.out.println("not a recommendedMove");
										continue;
									}
								}
							}
						}
						
						if(n==null)continue;
						Point newPoint=TemplateRotationType.getTransformPoint(templateRotationType, n.points.get(0));
						Point newPointSymmetry=makros.getSymmetryPoint(newPoint,templateRotationType);
						
						transformAIPoint=new Point(center.x+newPointSymmetry.x,center.y+newPointSymmetry.y);
						/*System.out.println("newPoint="+newPoint.toString());
						System.out.println("newPointSymmetry="+newPointSymmetry.toString());
						System.out.println("transformAIPoint="+transformAIPoint.toString());
						System.out.println("find symmetry");*/					
						setMarkerOnCurrentNode(n);
						currentNode=n;
						isSearchBySymmetry=2;
						//System.out.println("--------set isSearchBySymmetry=2");
						return true;
					}
				}
			}
		}
	}
	
	//если lastBluePoint не найден или если первый ход в игре красный, то поиск default хода
	//System.out.println("search default move");
	for(int i=0;i<currentNode.childNodes.size();i++){		
		TreeNodeMakros parent=currentNode.childNodes.get(i);
		if(!parent.blueIsDefault)continue;
		if(parent.moveType==Protocol.moveTypeRed)continue;
		
		TreeNodeMakros n=null;
		double randomDouble=0;
		if(recommendedMove==null){
			randomDouble=rand.nextDouble();
			n=getRandomChildNode(parent,Protocol.moveTypeRed);
			
			//if(templateIndex==10999){randomDouble=0.6;n=parent.childNodes.get(2);}//для отладки
			
			if(makros.symmetryType==-1)randomDouble=0.6;
			
		}else{//рекомендованный ход - при повторе игры делать ход, который был, т.к. в макромах может быть случайный выбор хода
			for(int k=0;k<parent.childNodes.size();k++){
				if(parent.childNodes.get(k).moveType==Protocol.moveTypeRed){
					n=parent.childNodes.get(k);
					
					Point newPoint=TemplateRotationType.getTransformPoint(templateRotationType, n.points.get(0));
					if(recommendedMove.x==center.x+newPoint.x&recommendedMove.y==center.y+newPoint.y){
						//System.out.println("find recommendedMove");
						randomDouble=0.6;
						break;
					}
					
					Point pSymmetry=makros.getSymmetryPoint(newPoint,templateRotationType);
					if(recommendedMove.x==center.x+pSymmetry.x&recommendedMove.y==center.y+pSymmetry.y&makros.symmetryType!=-1){
						//System.out.println("find recommendedMove");
						randomDouble=0.4;
						break;
					}
				}
			}
		}
		if(n==null)continue;			
		Point newPoint=TemplateRotationType.getTransformPoint(templateRotationType, n.points.get(0));
				
		if(isSearchBySymmetry==0){
			if(randomDouble>0.5){
				
				if(recommendedMove!=null){//рекомендованный ход - при повторе игры делать ход, который был, т.к. в макромах может быть случайный выбор хода
					if(recommendedMove.x!=center.x+newPoint.x|recommendedMove.y!=center.y+newPoint.y){
						//System.out.println("not a recommendedMove");
						continue;
					}							
				}
				
				transformAIPoint=new Point(center.x+newPoint.x,center.y+newPoint.y);
				isSearchBySymmetry=1;
				//System.out.println("--------set isSearchBySymmetry=1");
				/*System.out.println("--------n.points.get(0)="+(n.points.get(0).x)+","+(n.points.get(0).y));
				System.out.println("--------newPoint (non symmetry)="+(newPoint.x)+","+(newPoint.y));
				System.out.println("--------set isSearchBySymmetry=1");
				System.out.println("--------AIPoint="+(center.x+newPoint.x)+","+(center.y+newPoint.y));
				System.out.println("--------transformAIPoint="+transformAIPoint.x+","+transformAIPoint.y);*/
			}else{
				Point pSymmetry=makros.getSymmetryPoint(newPoint,templateRotationType);
				
				if(recommendedMove!=null){//рекомендованный ход - при повторе игры делать ход, который был, т.к. в макромах может быть случайный выбор хода
					if(recommendedMove.x!=center.x+pSymmetry.x|recommendedMove.y!=center.y+pSymmetry.y){
						//System.out.println("not a recommendedMove");
						continue;
					}							
				}
				
				transformAIPoint=new Point(center.x+pSymmetry.x,center.y+pSymmetry.y);
				isSearchBySymmetry=2;
				/*System.out.println("--------n.points.get(0)="+(n.points.get(0).x)+","+(n.points.get(0).y));
				System.out.println("--------newPoint (non symmetry)="+(newPoint.x)+","+(newPoint.y));
				System.out.println("--------pSymmetryPoint="+(pSymmetry.x)+","+(pSymmetry.y));				
				System.out.println("--------set isSearchBySymmetry=2");
				System.out.println("--------AIPoint="+(center.x+newPoint.x)+","+(center.y+newPoint.y));
				System.out.println("--------transformAIPoint="+transformAIPoint.x+","+transformAIPoint.y);*/
				//templateRotationType=4;
			}
		}else if(isSearchBySymmetry==1){							
			transformAIPoint=new Point(center.x+newPoint.x,center.y+newPoint.y);
		}else if(isSearchBySymmetry==2){
			Point pSymmetry=makros.getSymmetryPoint(newPoint,templateRotationType);
			transformAIPoint=new Point(center.x+pSymmetry.x,center.y+pSymmetry.y);				
		}
		
		setMarkerOnCurrentNode(n);
		currentNode=n;
		return true;
	}
		
	//Если ходы в районе макроса делаются в any точки, то не отключать макрос (isEnabled=false) 
	/*int newX=x-center.x+5,newY=y-center.y+5;
	int index=newX-1+(newY-1)*9;
	String dotToString=templateView.strTemplateWithoutTargets.substring(index,index+1);
	if(!dotToString.equals(TemplateDotType.ANY.toString())){
		isEnabled=false;
	}*/
	return false;
}

private void setMarkerOnCurrentNode(TreeNodeMakros n){
	currentNode.setUserObject(currentNode.toString().replaceAll("<font color=green>&#9679;</font>", ""));
	n.setUserObject(n.toString().replaceAll("<font color=green>&#9679;</font>", ""));
	if(n.moveType==Protocol.moveTypeBlue)n.setUserObject(n.toString().replaceAll("<font color=blue>","<font color=green>&#9679;</font><font color=blue>"));
	else n.setUserObject(n.toString().replaceAll("<font color=red>","<font color=green>&#9679;</font><font color=red>"));
	makros.treeModel.reload(currentNode);
	makros.treeModel.reload(n);
}

private TreeNodeMakros getRandomChildNode(TreeNodeMakros parentNode,int type){
	int childCount=0;
	for(int i=0;i<parentNode.childNodes.size();i++){
		if(parentNode.childNodes.get(i).moveType==type)childCount++;
	}
	if(childCount==0){System.out.println("error childCount=0");return null;}
	else if(childCount==1){
		for(int i=0;i<parentNode.childNodes.size();i++){
			if(parentNode.childNodes.get(i).moveType==type)return parentNode.childNodes.get(i);
		}
	}else{
		int childIdx=rand.nextInt(childCount);
		int findedIdx=0;
		for(int i=0;i<parentNode.childNodes.size();i++){
			if(parentNode.childNodes.get(i).moveType==type){
				if(childIdx==findedIdx){
					return parentNode.childNodes.get(i);
				}else{
					findedIdx++;
				}
			}
		}
	}
	System.out.println("error childCount not found");
	return null;
}

}
