package core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.util.vector.Vector3f;

import enumerations.Player;

import gameNet.GameControl;
import gameNet.GameNet_CoreGame;
import geometry.HealthBar;
import geometry.Projectile;
import geometry.Quad;
import geometry.Ship;


public class MyGame extends GameNet_CoreGame implements Runnable, Serializable {

	private ArrayList<String> clients = new ArrayList<String>();
	transient GameControl gameControl;
	
	CopyOnWriteArrayList<Projectile> projectiles = new CopyOnWriteArrayList<Projectile>();
	ArrayList<Quad> lanes = new ArrayList<Quad>(); 
	float laneWidth = 1.0f;
	int numLanes = 36;
	double angle = 0;
	
	
	int player1LaneIndex = 0;
	int player2LaneIndex = 0;
	Ship ship1 = null;
	Ship ship2 = null;
	
	HealthBar p1HealthBar = new HealthBar(20,580, 300, 0);
	HealthBar p2HealthBar = new HealthBar(800-20-300, 580, 300, 1);
	
	@Override
	public void run() {
		
		while (true)
		{
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(projectiles.size() > 0)
			{
					for(Projectile p: projectiles)
					{
						if (p.z < -4.9 && p.clientIndex == 0)	
							projectiles.remove(p);
						if(p.z > -1.1 && p.clientIndex == 1)
							projectiles.remove(p);
						
						if (ship2 != null )
						{
							if (p.z < -4.6 && p.clientIndex == 0 && ship2.laneIndex == p.laneIndex)
							{
									
									projectiles.remove(p);
									p2HealthBar.updateWidth();
									
							}
						}
						
						if (p.z > -1.4 && p.clientIndex == 1 && ship1.laneIndex == p.laneIndex)
						{	
							
							projectiles.remove(p);
							p1HealthBar.updateWidth();
							
			
						
						}
						
						p.update(0.1f);
					}
					
					//Send back to the client
				MyGameOutput mo = new MyGameOutput(this);
				gameControl.putMsgs(mo);
			}
		
		}
		
	}

	@Override
	public Object process(Object ob) {
		MyGameInput myGameInput = (MyGameInput)ob;
		int clientIndex;
		
		if(myGameInput.command == MyGameInput.CONNECTING && clients.size() < 4)
		{
			clients.add(myGameInput.name);
			clientIndex = getMyIndex(myGameInput.name);
			if (clientIndex == 0)
				ship1 = new Ship(lanes.get(player1LaneIndex), clientIndex);
			else if (clientIndex == 1)
				ship2 = new Ship(lanes.get(player2LaneIndex), clientIndex);
		}
		
		switch(myGameInput.command)
		{
		case MyGameInput.CONNECTING:
			break;
		case MyGameInput.DISCONNECTING:
			clientIndex = getMyIndex(myGameInput.name);
			if (clientIndex == 0)
				ship1 = null;
			else if (clientIndex == 1)
				ship2 = null;
			clients.remove(myGameInput.name);
			break;
		case MyGameInput.MOVELEFT:
			clientIndex = getMyIndex(myGameInput.name);
			if(clientIndex == 0)
			{
				player1LaneIndex--;
				if (player1LaneIndex < 0)
					player1LaneIndex = numLanes -1;
				ship1.update(lanes.get(player1LaneIndex));
			}
			else if (clientIndex == 1)
			{ 
				player2LaneIndex++;
				player2LaneIndex %= numLanes;
				ship2.update(lanes.get(player2LaneIndex));
			}
			//TODO
			break;
		case MyGameInput.MOVERIGHT:
			clientIndex = getMyIndex(myGameInput.name);
			if(clientIndex == 0)
			{
				player1LaneIndex++;
				player1LaneIndex %= numLanes;
				ship1.update(lanes.get(player1LaneIndex));
			}
			else if (clientIndex == 1)
			{
				
				player2LaneIndex--;
				if (player2LaneIndex < 0)
					player2LaneIndex = numLanes -1;
				ship2.update(lanes.get(player2LaneIndex));
			}
			//TODO
			break;
		case MyGameInput.FIRE:
			clientIndex = getMyIndex(myGameInput.name);
			
			if (p1HealthBar.alive == false || p2HealthBar.alive == false)
			{
				resetGame();
			}
			Projectile p = null;
			if (clientIndex == 0)
				p = new Projectile(lanes.get(player1LaneIndex), .1f, clientIndex);
			else if (clientIndex == 1)
			{
				p = new Projectile(lanes.get(player2LaneIndex), .1f, clientIndex);
			}
				
				
			synchronized(projectiles)
			{
				projectiles.add(p);
			}
			break;
		}
		// TODO Auto-generated method stub
		MyGameOutput myGameOutput = new MyGameOutput(this);
		return myGameOutput;
	}
	
	public int getMyIndex(String name)
	{
		return clients.indexOf(name);
	}
	
	private void createLanes()
	{
		Vector3f origin = new Vector3f(0,-2f,-1f);
		Quad q = null;
		q = new Quad(origin, laneWidth, 4, 0, 0);
		
		
		lanes.add(q);
		
		for (int i = 1; i < numLanes; i++)
		{
			Vector3f rightVertice = lanes.get(i - 1).rightVertex;
			//angle = Math.abs((Math.random() * 180) - 90);
			angle += 10f;
			q = new Quad(rightVertice, laneWidth, 4, angle, i);
			lanes.add(q);
			
		}
	}
	
	
	public void startGame(GameControl g)
	{
		gameControl = g;
		System.out.println("Creating Lanes");
		if (clients.size() < 2)
			createLanes();
		Thread t = new Thread(this);
		t.start();
		
	}
	
	public void resetGame()
	{
		p1HealthBar.resetBar();
		p2HealthBar.resetBar();
		
	}
	
	

}
