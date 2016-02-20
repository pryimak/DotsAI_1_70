package p_TemplateEditor;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;

import p_DotsAI.Protocol;
import p_GUI.GameGUI;

public class SingleGameEngine{

	private byte[][] field;
	DotsTE pointsTE;
	Graphics g;
	public int offsetX_TE=4;
	public int offsetY_TE=1;//отступы (размерность - клеток игрового поля)
	int squareSize=GameGUI.squareSize;
	int pointSize=GameGUI.pointSize;
	
public SingleGameEngine(DotsTE pointsTE){
	this.pointsTE=pointsTE;
	field=new byte[Protocol.sizeX_TE][Protocol.sizeY_TE];
	for(int x=0;x<Protocol.sizeX_TE;x++)for(int y=0;y<Protocol.sizeY_TE;y++)field[x][y]=Protocol.templateDotTypeNULL;
}
	
public boolean isCanMove(int x, int y){
	if((x<0)|(y<0)|(x>=Protocol.sizeX_TE)|(y>=Protocol.sizeY_TE)) {return false;}
	if(field[x][y]==Protocol.templateDotTypeNULL){return true;}
	return false;
}
		
public void paint(){try{
	//System.out.println(getContent());
	int otstupX=48,otstupY=26;
	
	g=pointsTE.getGraphics();
	g.setColor(new Color(254,254,254));//}catch(Exception e){}
	g.fillRect(9+otstupX, otstupY+3, squareSize*(Protocol.sizeX_TE+1)-3, Protocol.sizeY_TE*(squareSize+1)-7);
	g.setColor(Color.gray);
	g.drawRect(17+otstupX, otstupY+5, squareSize*(Protocol.sizeX_TE+1)-11, Protocol.sizeY_TE*(squareSize+1)-12);
	
	g.setColor(new Color(225,225,225));
	for(int x=0;x<Protocol.sizeX_TE;x++)for(int y=0;y<Protocol.sizeY_TE;y++){// <drawing-grid>
		g.drawLine(28+otstupX+squareSize*x, otstupY+2, 28+otstupX+squareSize*x, otstupY+145);
		g.drawLine(20+otstupX,otstupY+10+squareSize*y,164+otstupX, otstupY+10+squareSize*y);
	}
	
	for(int x=0;x<Protocol.sizeX_TE;x++)for(int y=0;y<Protocol.sizeY_TE;y++){		
		int drawX = (int) (squareSize * (x + offsetX_TE - 0.25+1))-pointSize/2;
		int drawY = (int) (squareSize * (y + offsetY_TE - 0.25+1))-pointSize/2+8;
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		/*blue*/
		if(field[x][y]==Protocol.templateDotTypeBLUE){
			g.setColor(Color.blue);
			g.fillOval(drawX, drawY, pointSize, pointSize);
		}
		/*red*/
		else if(field[x][y]==Protocol.templateDotTypeRED){
			g.setColor(Color.red);
			g.fillOval(drawX, drawY, pointSize, pointSize);
		}
		/*red null*/
		else if(field[x][y]==Protocol.templateDotTypeRED_EMPTY){
			g.setColor(Color.pink);
			g.fillOval(drawX, drawY, pointSize, pointSize);
		}
		/*blue null*/
		else if(field[x][y]==Protocol.templateDotTypeBLUE_EMPTY){
			g.setColor(Color.cyan);
			g.fillOval(drawX, drawY, pointSize, pointSize);
		}
		/*land*/
		else if(field[x][y]==Protocol.templateDotTypeLAND){
			g.setColor(Color.black);
			g.fillRect(drawX-1, drawY-1, pointSize+7, pointSize+7);
		}
		/*red normal target*/
		else if(field[x][y]==Protocol.templateDotTypeRED_NORMAL){
			g.setColor(Color.red);
			g.drawOval(drawX-2, drawY-2, pointSize+4, pointSize+4);
			g.drawOval(drawX-1, drawY-1, pointSize+2, pointSize+2);
			g.setColor(Color.black);
			g.drawString("N",drawX, drawY+9);
		}
		/*red attack target*/
		else if(field[x][y]==Protocol.templateDotTypeRED_ATTACK){
			g.setColor(Color.red);
			g.drawOval(drawX-2, drawY-2, pointSize+4, pointSize+4);
			g.drawOval(drawX-1, drawY-1, pointSize+2, pointSize+2);
			g.setColor(Color.black);
			g.drawString("A",drawX+1, drawY+9);
		}
		/*red protection target*/
		else if(field[x][y]==Protocol.templateDotTypeRED_PROTECTION){
			g.setColor(Color.red);
			g.drawOval(drawX-2, drawY-2, pointSize+4, pointSize+4);
			g.drawOval(drawX-1, drawY-1, pointSize+2, pointSize+2);
			g.setColor(Color.black);
			g.drawString("P",drawX+1, drawY+10);
		}
		/*blue normal target*/
		else if(field[x][y]==Protocol.templateDotTypeBLUE_ATTACK){
			g.setColor(Color.blue);
			g.drawOval(drawX-2, drawY-2, pointSize+4, pointSize+4);
			g.drawOval(drawX-1, drawY-1, pointSize+2, pointSize+2);
			g.setColor(Color.black);
			g.drawString("N",drawX, drawY+10);
		}
		/*any*/
		else if(field[x][y]==Protocol.templateDotTypeANY){
			g.setColor(Color.green);
			g.fillOval(drawX, drawY, pointSize, pointSize);
		}
		/*null*/
		else if(field[x][y]==Protocol.templateDotTypeNULL){
			g.setColor(Color.white);
			g.fillOval(drawX, drawY, pointSize, pointSize);
		}
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
		/*enemy blue target*/
	}
	g.setColor(Color.BLACK);
	g.setFont(new Font("tahoma", 8, 8));
	// <drawing-coordinates>
	for(int x=0;x<Protocol.sizeX_TE;x++){
		for(int y=0;y<Protocol.sizeY_TE;y++){
			g.drawString(""+(x+1),25+squareSize*x+otstupX,otstupY+5);
			g.drawString(""+(y+1),12+otstupX,otstupY+14+squareSize*y);
		}
	}
}catch(Exception e){}}
	
void moveByContent(byte[] content,byte[] logic){
	for(int i=0;i<content.length;i++){
		move(i%Protocol.sizeX_TE,i/Protocol.sizeX_TE,content[i]);
		if(logic!=null){
			if(logic[i]!=9){
				move(i%Protocol.sizeX_TE,i/Protocol.sizeX_TE,logic[i]);
			}
		}
	}
	paint();
}

void moveByContent(byte[] content){
	for(int i=0;i<content.length;i++){
		move(i%Protocol.sizeX_TE,i/Protocol.sizeX_TE,content[i]);
	}
	paint();
}

void moveByField(byte content[][]){
	for(int i=0;i<Protocol.sizeX_TE;i++){
		for(int j=0;j<Protocol.sizeY_TE;j++){
			move(i,j,content[i][j]);
		}
	}
	paint();
}	

public void move(int x, int y, byte moveType){
	if((x<0)|(y<0)|(x>=Protocol.sizeX_TE)|(y>=Protocol.sizeY_TE)){}
	else if(moveType==Protocol.templateDotTypeRED_NORMAL|
			moveType==Protocol.templateDotTypeRED_PROTECTION){clearField(moveType);field[x][y]=moveType;}
	else field[x][y]=moveType;	
}

/*String getContent(){
	String content="";
	for(int j=0;j<Protocol.sizeY_TE;j++){
		for(int i=0;i<Protocol.sizeX_TE;i++){
			content+=field[i][j];
		}
	}
	return content;
}*/

byte[] getContent(){
	byte[] content=new byte[81];
	int idx=0;
	for(int j=0;j<Protocol.sizeY_TE;j++){
		for(int i=0;i<Protocol.sizeX_TE;i++){
			content[idx]=field[i][j];
			idx++;
		}
	}
	return content;
}

int getMouseClickX(MouseEvent me){return (int)(((double)me.getX()-4-(double)(offsetX_TE*squareSize))/(double)squareSize);};
int getMouseClickY(MouseEvent me){return (int)(((double)me.getY()-12-(double)(offsetY_TE*squareSize))/(double)squareSize);};
//public String[][] getField(){return field;}
private void clearField(int moveType){for(int i=0;i<Protocol.sizeX_TE;i++)for(int j=0;j<Protocol.sizeY_TE;j++)if(field[i][j]==moveType)field[i][j]=Protocol.templateDotTypeNULL;}

void moveInSide(String sideName){
	
	//String content[][]=getField();
	
	//<move and clearSide>
	if(sideName.equalsIgnoreCase("lt")){
		for(int i=0;i<Protocol.sizeX_TE-1;i++)for(int j=0;j<Protocol.sizeY_TE-1;j++)field[i][j]=field[i+1][j+1];
		for(int i=0;i<Protocol.sizeX_TE;i++)field[i][Protocol.sizeY_TE-1]=Protocol.templateDotTypeNULL;for(int j=0;j<Protocol.sizeY_TE;j++)field[Protocol.sizeX_TE-1][j]=Protocol.templateDotTypeNULL;
	}
	if(sideName.equalsIgnoreCase("rt")){
		for(int i=Protocol.sizeX_TE-1;i>0;i--)for(int j=0;j<Protocol.sizeY_TE-1;j++)field[i][j]=field[i-1][j+1];
		for(int i=0;i<Protocol.sizeX_TE;i++)field[i][Protocol.sizeY_TE-1]=Protocol.templateDotTypeNULL;for(int j=0;j<Protocol.sizeY_TE;j++)field[0][j]=Protocol.templateDotTypeNULL;
	}
	if(sideName.equalsIgnoreCase("t")){
		for(int i=0;i<Protocol.sizeX_TE;i++)for(int j=0;j<Protocol.sizeY_TE-1;j++)field[i][j]=field[i][j+1];
		for(int i=0;i<Protocol.sizeX_TE;i++)field[i][Protocol.sizeY_TE-1]=Protocol.templateDotTypeNULL;
	}
	if(sideName.equalsIgnoreCase("l")){
		for(int j=0;j<Protocol.sizeY_TE;j++)for(int i=0;i<Protocol.sizeX_TE-1;i++)field[i][j]=field[i+1][j];
		for(int j=0;j<Protocol.sizeY_TE;j++)field[Protocol.sizeX_TE-1][j]=Protocol.templateDotTypeNULL;
	}
	if(sideName.equalsIgnoreCase("r")){
		for(int i=Protocol.sizeX_TE-1;i>0;i--)for(int j=0;j<Protocol.sizeY_TE;j++)field[i][j]=field[i-1][j];
		for(int j=0;j<Protocol.sizeY_TE;j++)field[0][j]=Protocol.templateDotTypeNULL;
	}
	if(sideName.equalsIgnoreCase("lb")){
		for(int i=0;i<Protocol.sizeX_TE-1;i++)for(int j=Protocol.sizeY_TE-1;j>0;j--)field[i][j]=field[i+1][j-1];
		for(int i=0;i<Protocol.sizeX_TE;i++)field[i][0]=Protocol.templateDotTypeNULL;for(int j=0;j<Protocol.sizeY_TE;j++)field[Protocol.sizeX_TE-1][j]=Protocol.templateDotTypeNULL;
	}
	if(sideName.equalsIgnoreCase("rb")){
		for(int i=Protocol.sizeY_TE-1;i>0;i--)for(int j=Protocol.sizeX_TE-1;j>0;j--)field[j][i]=field[j-1][i-1];
		for(int i=0;i<Protocol.sizeX_TE;i++)field[i][0]=Protocol.templateDotTypeNULL;for(int j=0;j<Protocol.sizeY_TE;j++)field[0][j]=Protocol.templateDotTypeNULL;
	}
	if(sideName.equalsIgnoreCase("b")){
		for(int i=Protocol.sizeY_TE-1;i>0;i--)for(int j=0;j<Protocol.sizeX_TE;j++)field[j][i]=field[j][i-1];
		for(int i=0;i<Protocol.sizeX_TE;i++)field[i][0]=Protocol.templateDotTypeNULL;
	}
	//</clearSide>
	
	moveByField(field);
}
	
}
