package p_DotsAI;

public enum EnemyType{
	
RANDOM,
HUMAN;
//SAVE,
//REPLAY,
//END;

public String toString() {//������������ �������
	switch (this) {
		case RANDOM:	return "Random";
		case HUMAN:		return "Human";
		//case SAVE:	return "����";
		//case REPLAY:	return "������";
		//case END:		return "�����";//���� ���������
		default:return "err";
	}
}
	
}
