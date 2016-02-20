package p_SingleGameEngine;

import java.util.ArrayList;

public class Surrounding{
	public int type;//SurroundingType
	public ArrayList<Dot> path;
	public ArrayList<Dot> capturedPoints;
	public Surrounding() {
		path = new ArrayList<Dot>();
		capturedPoints = new ArrayList<Dot>();
	}
}