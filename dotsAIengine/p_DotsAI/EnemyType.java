package p_DotsAI;

public enum EnemyType{
	
RANDOM,
HUMAN;
//SAVE,
//REPLAY,
//END;

public String toString() {//аббревиатура шаблона
	switch (this) {
		case RANDOM:	return "Random";
		case HUMAN:		return "Human";
		//case SAVE:	return "Сейв";
		//case REPLAY:	return "Повтор";
		//case END:		return "Конец";//игра закончена
		default:return "err";
	}
}
	
}
