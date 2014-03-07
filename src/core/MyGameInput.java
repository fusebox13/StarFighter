package core;

import java.io.Serializable;

public class MyGameInput implements Serializable {
	
	static final int CONNECTING=1;
	static final int DISCONNECTING=2;
	static final int MOVELEFT=3;
	static final int MOVERIGHT=4;
	static final int FIRE=5;
	
	String name;
	int laneIndex;
	int command;
	
	void setName(String name) {
		this.name = name;
	}
	
	void setCommand(int command){
		this.command=command;
	}
	
	

}
