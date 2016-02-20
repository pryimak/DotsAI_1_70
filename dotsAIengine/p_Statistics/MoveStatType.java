package p_Statistics;

public enum MoveStatType{
	
	BEGIN,
	FINAL_RED_ATTACK,
	CONTINUED_RED_ATTACK,
	SQUARE_SIDE,
	SQUARE,
	WALL_DESTROY,
	WALL,
	WALL_SIDE,
	ABSTRACT_DEFENSE_WALL,
	ABSTRACT_ATTACK_WALL,
	BLUE_SURROUND,
	BLUE_SURROUND_SECURITY,
	RED_SURROUND,
	GLOBAL_ATTACK,
	GLOBAL_DESTROY,
	EXPRESS_SURROUND_SECURITY_1,
	EXPRESS_SURROUND_SECURITY_2,
	GROUND,
	GROUND_SIDE,
	RANDOM,
	//MAKROS,
	ERROR;
	
	public static MoveStatType getMoveStatType(int index){	
		switch (index) {
			case 0: return BEGIN;
			//case 1: return MAKROS;
			case 1: return BLUE_SURROUND_SECURITY;			
			case 2:	return SQUARE_SIDE;
			case 3:	return BLUE_SURROUND;			
			case 4: return RED_SURROUND;
			case 5: return SQUARE;
			case 6: return WALL_DESTROY;
			case 7: return FINAL_RED_ATTACK;
			case 8: return ABSTRACT_DEFENSE_WALL;
			case 9: return ABSTRACT_ATTACK_WALL;
			case 10:return WALL_SIDE;
			case 11:return WALL;
			case 12:return GLOBAL_ATTACK;
			case 13:return GLOBAL_DESTROY;
			case 14:return CONTINUED_RED_ATTACK;
			case 15:return GROUND_SIDE;
			case 16:return GROUND;
			case 17:return RANDOM;	
			case 18:return EXPRESS_SURROUND_SECURITY_1;
			case 19:return EXPRESS_SURROUND_SECURITY_2;
			default:return ERROR;
		}
	}
	
	public static int getMoveStatIndex(MoveStatType moveStatType) {
		switch (moveStatType){
			case BEGIN: 				return 0;
			//case MAKROS:				return 1;
			case BLUE_SURROUND_SECURITY:return 1;			
			case SQUARE_SIDE:			return 2;			
			case BLUE_SURROUND:			return 3;			
			case RED_SURROUND: 			return 4;
			case SQUARE:				return 5;			
			case WALL_DESTROY:			return 6;
			case FINAL_RED_ATTACK:		return 7;
			case ABSTRACT_DEFENSE_WALL:	return 8;
			case ABSTRACT_ATTACK_WALL:	return 9;
			case WALL_SIDE:				return 10;
			case WALL:					return 11;
			case GLOBAL_ATTACK:			return 12;
			case GLOBAL_DESTROY:		return 13;
			case CONTINUED_RED_ATTACK:	return 14;
			case GROUND_SIDE:			return 15;
			case GROUND:				return 16;
			case RANDOM:				return 17;
			case EXPRESS_SURROUND_SECURITY_1:return 18;
			case EXPRESS_SURROUND_SECURITY_2:return 19;
			default:					return 20;
		}
	}
	
public static String toString(MoveStatType moveStatType) {
	switch (moveStatType) {
		case BEGIN:					return "begin template (bgt)";
		case SQUARE_SIDE:			return "square side template (sst)";
		case SQUARE:				return "square template (sqt)";
		case WALL:					return "wall template (wlt)";
		case WALL_SIDE:				return "wall side template (wst)";
		case BLUE_SURROUND:			return "blue surround template (bst)";
		case BLUE_SURROUND_SECURITY:return "blue surround security (bss)";
		case RED_SURROUND: 			return "red surround template (rst)";
		case GROUND:				return "ground template (grt)";
		case GROUND_SIDE:			return "ground at side template (grs)";
		case RANDOM:				return "random";
		//case MAKROS:				return "makros";
		case EXPRESS_SURROUND_SECURITY_1:return "express surround security 1";
		case EXPRESS_SURROUND_SECURITY_2:return "express sur sec 2 (max terrytory)";
		case FINAL_RED_ATTACK:		return "final red attack (fra)";
		case CONTINUED_RED_ATTACK:	return "continued red attack (cra)";
		case WALL_DESTROY:			return "wall destroy template (wdt)";
		case GLOBAL_ATTACK:			return "global attack template (gat)";
		case GLOBAL_DESTROY:		return "global destroy template (gdt)";
		case ABSTRACT_DEFENSE_WALL:	return "abstract defense wall (adw)";
		case ABSTRACT_ATTACK_WALL:	return "abstract attack wall (aaw)";
		default:return "-";
	}
}
	
}
