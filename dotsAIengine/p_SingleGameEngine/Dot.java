package p_SingleGameEngine;

public class Dot{			
	public int x;
	public int y;
	public int type;//DotType
	public Surrounding masterSurrounding;		
	Dot(int x1, int y1, int dotTypeEmpty) {
		x = x1;
		y = y1;
		type = dotTypeEmpty;
	}
}