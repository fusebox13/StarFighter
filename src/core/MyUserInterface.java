package core;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;

import enumerations.Player;
import gameNet.GameNet_UserInterface;
import gameNet.GamePlayer;
import geometry.HealthBar;
import geometry.Projectile;
import geometry.Quad;
import geometry.Point;
import geometry.Ship;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;


import camera.FPSCamera;




public class MyUserInterface implements GameNet_UserInterface {
	
	float lastTime = 0.0f;
	float time = 0.0f;
	float dx = 0.0f;
	float dy = 0.0f;
	float dt = 0.0f;
	
	
	float sensitivity = .2f;
	float movementSpeed = 20f;
	
	float laneWidth = 1.0f;
	double angle = 0;
	double rX = 10;
	
	ArrayList<Quad> lanes = new ArrayList<Quad>(); 
	int numLanes = 36;
	
	int clientIndex;
	
	Ship player1;
	Ship player2;
	
	public int player2Index = 0;
	public int player1Index = 0;
	
	boolean s = false;
	
	//volatile ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	
	//Immutable arraylist used to avoid concurrency issues.  The arraylist will be accessed
	//by multiple threads, so concurrency was an issue.  Because the arraylist is immutable
	//concurrency problems are a non issue
	CopyOnWriteArrayList<Projectile> projectiles = new CopyOnWriteArrayList<Projectile>();
	Point[] points = new Point[10000];
	
	HealthBar p1HealthBar;
	HealthBar p2HealthBar;
	
	private Texture p1Wins;
	private Texture p2Wins;
	
	FPSCamera camera = new FPSCamera(0,0,0);

	
	//Gamenet stuff:
	
	GamePlayer myGamePlayer;
	String myName;
	MyGameInput myGameInput = new MyGameInput();
	

	public void glInit()
	{
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective((float)30, 800f / 600f, 1f, 7f);
		glMatrixMode(GL_MODELVIEW);
		glShadeModel(GL_SMOOTH);
		
	}
	public void start()
	{
		try 
		{
			Display.setDisplayMode(new DisplayMode(800, 600));
			Display.setTitle("STARFIGHTER IV: QUEST FOR GALACTIC HUMILIATION");
			Display.setVSyncEnabled(true);
			Display.create();
		} 
		catch (LWJGLException e) 
		{
			e.printStackTrace();
			System.exit(0);
		}

		
		//Initializes open GL
		glInit();
		setUpLighting();
		createStars();
		
		p1Wins = loadTexture("p1wins.png");
		p2Wins = loadTexture("p2wins.png");
		
		
		while (!Display.isCloseRequested())
		{
			//clears screen and depth buffer
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			
		
			
			
			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			gluPerspective((float)30, 800f / 600f, 1f, 10);
			
			
			glMatrixMode(GL_MODELVIEW);
			glLoadIdentity();
			glEnable(GL_DEPTH_TEST);
			glEnable(GL_LIGHTING);
			glDisable(GL_TEXTURE_2D);
			
			glPushMatrix();
			setPerspective();
			processInput();
			
			for (Point stars: points) {
				if (stars.z < 0.001f)
					stars.z += .2f;
				else
					stars.z = -200f;
				
				glColor3f(stars.red, stars.green, stars.blue);
				glBegin(GL_POINTS);	
					glVertex3f(stars.x, stars.y, stars.z);
				glEnd();
			}
			
			
			
			/*Why draw the lanes like this?  Well because if I iterate through the lanes,
			they end up drawing over the lane when I change the lane color, this method
			fixes it.*/
			if (lanes.size() > 0)
			{
				glColor3f(1,0,0);
				for (int i = 0; i < lanes.size(); i++)
				{
					lanes.get(i).draw();
				}
			}
			
			
			
			
			
			if (projectiles.size() > 0)
			{
				synchronized(projectiles)
				{
					for(Projectile p: projectiles)
						p.draw();
				}
			}
			if (player1 != null)
				player1.draw();
			
			if (player2 != null)
				player2.draw();
			
			glPopMatrix();
			
			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			glOrtho(0.0f, Display.getWidth(), 0.0f, Display.getHeight(), -1.0f, 1.0f);
			
			glMatrixMode(GL_MODELVIEW);
			glLoadIdentity();
			glDisable(GL_DEPTH_TEST);
			glDisable(GL_LIGHTING);
			
			if (player1 != null)
				p1HealthBar.draw();
			
			if (player2 != null)
				p2HealthBar.draw();
			
			if (p2HealthBar.alive == false)
			{
				glEnable(GL_TEXTURE_2D);
				glEnable(GL_BLEND);
		        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
				p1Wins.bind();
				glBegin(GL_QUADS);
					glTexCoord2f(0,0);
					glVertex2f(272,364);
					glTexCoord2f(1,0);
					glVertex2f(528,364);
					glTexCoord2f(1,1);
					glVertex2f(528,236);
					glTexCoord2f(0,1);
					glVertex2f(272,236);
					
				glEnd();
				glDisable(GL_BLEND);
			}
			
			if(p1HealthBar.alive == false)
			{
				glEnable(GL_TEXTURE_2D);
				glEnable(GL_BLEND);
		        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
				p2Wins.bind();
				glBegin(GL_QUADS);
					glTexCoord2f(0,0);
					glVertex2f(272,364);
					glTexCoord2f(1,0);
					glVertex2f(528,364);
					glTexCoord2f(1,1);
					glVertex2f(528,236);
					glTexCoord2f(0,1);
					glVertex2f(272,236);
					
				glEnd();
				glDisable(GL_BLEND);
			}
			
			if (p1HealthBar.alive == false && p2HealthBar.alive == false)
				System.out.println("TIE!");
				
				
			
		
			Display.update();
			Display.sync(60);
		}
		
		exitProgram();
		Display.destroy();
	}
	
	private Texture loadTexture(String fileName)
	{
		try {
			return TextureLoader.getTexture("PNG", new FileInputStream(new File(fileName)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void setPerspective()
	{
		
		
		Vector3f mid;
		switch(clientIndex)
		{
		case 0:
			if (lanes.size() > 0)
			{
				mid = lanes.get(player1Index).midLane;
				glTranslatef(-mid.x, -mid.y -.4f, -1);
			//glRotatef(-5, 1.0f, 0, 0);
			}
			break;
		case 1:
			mid = lanes.get(player2Index).midLane;
			
			glTranslatef(mid.x, -mid.y -.4f, -7f);
			glRotatef(180, 0, 1.0f, 0);
			//glRotatef(-5, 1.0f, 0, 0);
			setUpLighting();
			break;
		default:
			camera.updateCameraPosition();
			break;
		}
		
		

	}
	
	public void processInput()
	{
		
		if (Mouse.isButtonDown(1)) {
			dx = Mouse.getDX();
			dy = Mouse.getDY();
		
			camera.yaw(dx * sensitivity);
			camera.pitch(dy * sensitivity);
		}
		while (Keyboard.next())
		{
			if (Keyboard.getEventKeyState())
			{
				if (Keyboard.getEventKey() == Keyboard.KEY_RIGHT)
				{
					myGameInput.setCommand(MyGameInput.MOVERIGHT);
					if(myGamePlayer != null)
					{
						try{
						myGamePlayer.sendMessage(myGameInput);
						} catch (RuntimeException e)
						{
							System.out.println("Disconnected from the server");
							System.exit(0);
						}
					}
					
				}
				
				if (Keyboard.getEventKey() == Keyboard.KEY_LEFT)
				{
					myGameInput.setCommand(MyGameInput.MOVELEFT);
					if(myGamePlayer != null)
					{
						try{
						myGamePlayer.sendMessage(myGameInput);
						} catch (RuntimeException e)
						{
							System.out.println("Disconnected from the server");
							System.exit(0);
						}
					}
				}
			}
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_F))
		{
			myGameInput.setCommand(MyGameInput.FIRE);
			if(myGamePlayer != null)
			{
				try{
				myGamePlayer.sendMessage(myGameInput);
				} catch (RuntimeException e)
				{
					System.out.println("Disconnected from the server");
					System.exit(0);
				}
			}
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_W))
			camera.forward(movementSpeed *0.002f);
		if (Keyboard.isKeyDown(Keyboard.KEY_S))
			camera.backwards(movementSpeed * 0.002f);
		if (Keyboard.isKeyDown(Keyboard.KEY_A))
			camera.strafeLeft(movementSpeed * 0.002f);
		if (Keyboard.isKeyDown(Keyboard.KEY_D))
			camera.strafeRight(movementSpeed * 0.002f);	
		if (Keyboard.isKeyDown(Keyboard.KEY_E))
		{
			
			glLight(GL_LIGHT0, GL_POSITION, asFloatBuffer(new float[]{camera.position.x,camera.position.y,camera.position.z,1}));
			System.out.println(camera.position.x + "," + camera.position.y + "," + camera.position.z);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE))
			camera.up(movementSpeed);
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
			camera.down(movementSpeed);
	}
	
	private void createStars()
	{
		Random random = new Random();
		
		for (int i = 0; i < points.length; i++) {
			
			float x = (random.nextFloat() * 4) - 2f;
			float y = (random.nextFloat() * 4) - 2f;
			int z = random.nextInt(200) - 200;
			
			points[i] = new Point(x,y,z);
		}
	}
	
	private static FloatBuffer asFloatBuffer(float[] values) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(values.length);
		buffer.put(values);
		buffer.flip();
		return buffer;
	}
	
	private static void setUpLighting() {
		glShadeModel(GL_SMOOTH);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_LIGHTING);
		glEnable(GL_LIGHT0);
		glEnable(GL_LIGHT1);
		glLightModel(GL_LIGHT_MODEL_AMBIENT, asFloatBuffer(new float[]{0.05f, 0.05f, 0.05f, 1f}));
		glLight(GL_LIGHT0, GL_POSITION, asFloatBuffer(new float[]{0,0,-.5f,1}));
		glLight(GL_LIGHT1, GL_POSITION, asFloatBuffer(new float[]{0,0,-4,1}));
		glEnable(GL_COLOR_MATERIAL);
		glColorMaterial(GL_FRONT, GL_DIFFUSE);
	}
	
	
	public static void main(String[] args)
	{
		MyUserInterface mui = new MyUserInterface();
		mui.start();
	}
	@Override
	public void receivedMessage(Object ob) {
		MyGameOutput myGameOutput = (MyGameOutput)ob;
		
		if(myGamePlayer !=null)
		{
			if (myGameOutput.myGame.getMyIndex(myName) < 0)
			{
				System.out.println("Not allowed to connect to the game");
				exitProgram();
			}
			else
			{
				if (lanes.size() == 0)
					lanes = myGameOutput.myGame.lanes;
				
				projectiles = myGameOutput.myGame.projectiles;
				player1 = myGameOutput.myGame.ship1;
				player2 = myGameOutput.myGame.ship2;
				player1Index = myGameOutput.myGame.player1LaneIndex;
				player2Index = myGameOutput.myGame.player2LaneIndex;
				clientIndex = myGameOutput.myGame.getMyIndex(myGameInput.name);
				p1HealthBar = myGameOutput.myGame.p1HealthBar;
				p2HealthBar = myGameOutput.myGame.p2HealthBar;
				
			}
			
		}
		else
			System.out.println("Getting outputs before we are ready");
		// TODO Auto-generated method stub
		
	}
	
	private void exitProgram()
	{
		if (myGamePlayer != null)
		{
			myGameInput.setCommand(MyGameInput.DISCONNECTING);
			
			try{
				myGamePlayer.sendMessage(myGameInput);
				} catch (RuntimeException e)
				{
					System.out.println("Disconnected from the server");
					System.exit(0);
				} // Let the game know that we are leaving

			myGamePlayer.doneWithGame(); // clean up sockets
		}
		System.exit(0);
	}
	
	@Override
	public void startUserInterface(GamePlayer player) {
		myGamePlayer = player;
		myName = myGamePlayer.getPlayerName();
		myGameInput.setName(myName);
		myGameInput.setCommand(MyGameInput.CONNECTING);
		try{
			myGamePlayer.sendMessage(myGameInput);
			} catch (RuntimeException e)
			{
				System.out.println("Disconnected from the server");
				System.exit(0);
			}
		start();
		// TODO Auto-generated method stub
		
	}
}
