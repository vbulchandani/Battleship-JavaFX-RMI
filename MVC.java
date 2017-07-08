package application;

/**
*
*  version 1.0
*/
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * 
 * @author Vishal
 */

class playBattleshipGame {

	/**
	 * This function is called to pass the players and will loop to play
	 *
	 * @param playerOne
	 *            BattleshipPlayer one info and fleet
	 * @param oceanOne
	 *            BattleshipPlayer one ocean
	 * @param playerTwo
	 *            BattleshipPlayer Two info and fleet
	 * @param oceanTwo
	 *            BattleshipPlayer Two ocean
	 */

	playerInterface PlayerA;
	playerInterface PlayerB;

	playBattleshipGame(playerInterface PlayerA, playerInterface PlayerB) {
		this.PlayerA = PlayerA;
		this.PlayerB = PlayerB;
	}

	public void play(BattleshipPlayer playerOne, Ocean oceanOne, BattleshipPlayer playerTwo, Ocean oceanTwo) {
		try {
			while (gameNotOver(playerOne, playerTwo)) { // Looping for player
														// one
														// and two to play game
				PlayerA.displayText("Turn : \tPlayer A,\n Please Enter Co-ordinates to hit  ");
				while (gameNotOver(playerOne, playerTwo) && playerTwo.takeAShot(playerOne.shoot())) {
					oceanTwo.printGrid(PlayerA);
				}
				oceanTwo.printGrid(PlayerA);
				// System.out.println();
				PlayerB.displayText("Turn : \tPlayer B,\n Please Enter Co-ordinates to hit  ");
				while (gameNotOver(playerOne, playerTwo) && playerOne.takeAShot(playerTwo.shoot())) {
					oceanOne.printGrid(PlayerB);
				}
				oceanOne.printGrid(PlayerB);
			}
			// System.out.println("You Won!!");
			if (playerTwo.allShipsDead()) {
				PlayerA.displayText("You Won!!" + playerOne.getPlayerName());
				PlayerB.displayText("You Won!!" + playerOne.getPlayerName());
			} else {
				PlayerA.displayText("You Won!!" + playerTwo.getPlayerName());
				PlayerB.displayText("You Won!!" + playerTwo.getPlayerName());
			}
			PlayerA.displayText("EXIT");
			PlayerB.displayText("EXIT");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * Checks whether the game is finished or is it not.
	 *
	 * @param playerOne
	 *            object of player one with info
	 * @param playerTwo
	 *            object of player two with info
	 * @return True if game is finished, else false.
	 */
	public boolean gameNotOver(BattleshipPlayer playerOne, BattleshipPlayer playerTwo) {
		return !(playerOne.allShipsDead() || playerTwo.allShipsDead());
	}

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates and open the template
 * in the editor.
 */

/*
 * .java
 * 
 * Version: V-1.0
 * 
 * Revisions:
 */

/**
 * this class keeps track of all the fleets
 *
 * @author Vishal 
 *
 *
 * @version 1.0
 */
class Fleet {

	private final String[] shipType = { "Carrier" };// {"Carrier", "Battleship",
													// "Cruiser", "Destroyer"};
	public final int[] shipSize = { 5 };// {5, 4, 3, 2};
	// Scanner scan = new Scanner(System.in);
	Ocean ocean;
	playerInterface Player;

	public Ship[] myFleet = new Ship[shipSize.length];

	/**
	 * this is the constructor to initialize the variables
	 *
	 * @param passedOcean
	 *            Ocean for the fleet
	 */
	Fleet(Ocean passedOcean, playerInterface Player) throws Exception {
		this.Player = Player;

		ocean = passedOcean;
		for (int i = 0; i < myFleet.length; i++) {
			myFleet[i] = new Ship(i + 1, shipType[i], shipSize[i], Player);
		}
		// positionShips();
	}

	/**
	 * this function positions all the ships into the ocean
	 *
	 */
	public void positionShips() throws Exception {

		for (int i = 0; i < myFleet.length; i++) {
			placeShip(myFleet[i]);
		}

	}

	/**
	 * this function places individual ships into the ocean
	 *
	 * @param ship
	 *            Ship object to be placed into the ocean
	 */
	private void placeShip(Ship ship) throws Exception {
		int[] loc;
		int orientation = 1;
		boolean flag = true;
		do {
			loc = getCoordinates(); // gets the input
			orientation = getOrientation(); // input orientation
			if (ocean.canBePlaced(loc[0], loc[1], orientation, ship)) {
				ocean.placeShip(loc, orientation, ship);
				flag = false;
			} else {
				Player.displayText("Not available. Please Enter different coordinates");
			}
		} while (flag);
	}

	/**
	 * takes the input and sends the coordinates to function
	 *
	 * @return array with X Y coordinates
	 */
	public int[] getCoordinates() throws Exception {
		int[] loc = new int[2];

		loc[0] = getInput(Player); // function to get input from keyboard
		loc[1] = getInput(Player); // function to get input from keyboard
		return loc;
	}

	/**
	 * function to get the input from keyboard
	 *
	 * @return integer coordinate
	 */
	public int getInput(playerInterface Player) throws Exception {
		String input = "xyz";

		while (!input.matches("[0-9]+")) { // grammar to restrict to only digits
			Player.displayText("Please Enter an integer between 0 to " + (ocean.getGridSize() - 1));
			// dout.writeUTF("$");
			input = Player.getData();
			if (input.matches("[0-9]+")) { // error handling
				if (!(Integer.parseInt(input) < (ocean.getGridSize() - 1))) {
					input = "xyz";
				}
			}
		}
		return Integer.parseInt(input);
	}

	/**
	 * takes the orientation of the ship from the user
	 *
	 * @return integer representation of orientation
	 */
	private int getOrientation() throws Exception {
		String input = "nothing";

		while (!input.matches("[0-1]")) { // grammar to restrict to only digits
			Player.displayText("Please Enter orientation of ship between 0 or 1");
			// dout.writeUTF("$");
			input = Player.getData();
		}
		return Integer.parseInt(input);
	}

	/**
	 * check if all the ships are dead or not
	 *
	 * @return true if all are dead
	 */
	boolean areAllDead() {
		for (int i = 0; i < myFleet.length; i++) {
			if (myFleet[i].isShipDestroyed() == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * reduces the health of a ship and notes the ship
	 *
	 * @param shipNumber
	 *            the ship which is hit by the attack
	 */
	void aShipIsHit(int shipNumber) throws Exception {
		myFleet[shipNumber - 1].reduceHealth(); // health reduction
	}

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates and open the template
 * in the editor.
 */

/**
 * ocean class implements grid and executes all the functions in it
 *
 * @author Vishal
 */
class Ocean extends Observable implements Grid {

	private int[][] theOcean;

	/**
	 * constructor to create the ocean
	 *
	 * @param size
	 *            size of the ocean, integer
	 */
	public Ocean(int size) {
		theOcean = new int[size][size];
		setGrid();
	}

	/**
	 * sets the grid to default values
	 */
	@Override
	public void setGridtoDefault() {
		for (int i = 0; i < theOcean.length; i++) {
			for (int j = 0; j < theOcean.length; j++) {
				theOcean[i][j] = 0;
			}
		}
	}

	/**
	 * returns the size of ship
	 *
	 * @return size of ship integer
	 */
	@Override
	public int getGridSize() {
		if (theOcean != null) {
			return theOcean.length;
		} else {
			return 0;
		}
	}

	/**
	 * checks whether the ship can be placed or not
	 *
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @param orientation
	 *            orientation of ship
	 * @param ship
	 *            ship type with number (object)
	 * @return returns true if can be placed
	 */
	boolean canBePlaced(int x, int y, int orientation, Ship ship) {

		boolean flag = true;
		int i = 0;
		int[] loc = new int[2];
		loc[0] = x;
		loc[1] = y;
		while (flag && (i < ship.getLength())) {
			if (!checkIfInAndAvilable(loc)) {
				flag = false;
			}
			if (orientation == 1) {
				loc[0] += 1;
			} else {
				loc[1] += 1;
			}
			i++;
		}
		return flag;
	}

	/**
	 * function to print the grid
	 *
	 */
	@Override
	public void printGrid() {
	}

	public void printGrid(playerInterface Player) throws Exception {
		String str = "";
		str += " ";
		for (int j = 0; j < theOcean.length; j++) {
			str += "   " + j + "  ";
		}
		str += "\n";
		for (int i = 0; i < theOcean.length; i++) {
			str += "" + i;
			for (int j = 0; j < theOcean.length; j++) {
				str += "|";
				switch (theOcean[i][j]) {
				case -1:
					str += "  " + "X" + "  ";
					break;
				case -2:
					str += "  " + "$" + "  ";
					break;
				default:
					str += "  " + "." + "  ";
					break;
				}
			}
			str += "|";

			str += "\n";
			str += " ";
			for (int j = 0; j < theOcean.length; j++) {
				str += "  ___ ";
			}
			str += "\n";
		}
		Player.displayText(str);
	}

	/**
	 * function to check if the space is available on grid
	 * 
	 * @param loc
	 *            integer array with x and y coordinates
	 * @return returns true if it can be placed
	 */
	public boolean checkIfInAndAvilable(int[] loc) {

		if ((loc[0] < 0) || (loc[0] >= theOcean.length)) {
			return false;
		}
		if (theOcean[loc[0]][loc[1]] != 0) {
			return false;
		}
		if (!(loc[0] - 1 < 0) && theOcean[loc[0] - 1][loc[1]] != 0) {
			return false;
		}
		if (!(loc[0] + 1 >= theOcean.length) && theOcean[loc[0] + 1][loc[1]] != 0) {
			return false;
		}
		if (!(loc[1] - 1 < 0) && theOcean[loc[0]][loc[1] - 1] != 0) {
			return false;
		}
		return !(!(loc[1] + 1 >= theOcean.length) && theOcean[loc[0]][loc[1] + 1] != 0);
	}

	/**
	 * sets the position in the grid with a hit on ship
	 * 
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 */
	public void setPositionInOcean(int x, int y) {
		if (theOcean[x][y] > 0) {
			theOcean[x][y] = -2;
		} else {
			theOcean[x][y] = -1;
		}
		setChanged();
		notifyObservers();

	}

	/**
	 * gets the value from grid
	 * 
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @return returns character value of the grid data
	 */
	@Override
	public char getGridLocation(int x, int y) {
		return (char) theOcean[x][y];
	}

	/**
	 * gets the integer value from grids
	 * 
	 * @param x
	 *            array with x coordinate and y coordinate
	 * @return returns the value in grid
	 */
	public int getGridLocation(int[] x) {
		return theOcean[x[0]][x[1]];

	}

	/**
	 * places the ship in ocean
	 * 
	 * @param loc
	 *            coordinates x and y
	 * @param orientation
	 *            orientation of ship
	 * @param ship
	 *            ship to be placed
	 */
	void placeShip(int[] loc, int orientation, Ship ship) {
		int i = 0;
		while (i < ship.getLength()) {
			theOcean[loc[0]][loc[1]] = ship.getShipType();
			if (orientation == 1) {
				loc[0] += 1;
			} else {
				loc[1] += 1;
			}
			i++;
		}
	}

	/**
	 * this function sets the value with the character integer value
	 * 
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @param ch
	 *            character value to be placed
	 */
	@Override
	public void setGridLocation(int x, int y, char ch) {
		theOcean[x][y] = Character.getNumericValue(ch);
	}

	/**
	 * sets grid to default value
	 */
	private void setGrid() {
		setGridtoDefault();
	}

}

/*
 * .java
 * 
 * Version: V-1.0
 * 
 * Revisions:
 */

/**
 * This class contains details and features of players
 *
 * @author Vishal
 *
 *
 * @version 1.0
 */
class BattleshipPlayer implements Player {

	private String myName;
	public Fleet myFleet;
	private Ocean myOcean;
	playerInterface PlayerA;
	playerInterface PlayerB;

	BattleshipPlayer otherPlayer = null;

	/**
	 * Constructor to set the player properties
	 *
	 * @param playerName
	 *            Name of the player
	 * @param ocean
	 *            The ocean in which he is going to play
	 */
	void setPlayer(BattleshipPlayer p) {
		this.otherPlayer = p;
	}

	public boolean IsThisHitBefore(int[] loc) {
		boolean value = false;
		if (myOcean.getGridLocation(loc) < 0)
			value = true;
		return value;
	}

	BattleshipPlayer(String playerName, Ocean ocean, playerInterface PlayerA, playerInterface PlayerB)
			throws Exception {
		myName = playerName;
		this.PlayerA = PlayerA;
		this.PlayerB = PlayerB;
		myFleet = new Fleet(ocean, PlayerA);
		myOcean = ocean;
	}

	void placeFleet() {
		try {
			myFleet.positionShips();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * function to shoot the other player
	 * 
	 * @return array with X Y coordinates
	 */
	int[] shoot() throws Exception {
		int[] loc;// = myFleet.getCoordinates();
		do {
			PlayerA.displayText("Enter fresh co-ordinates to fire.");
			loc = myFleet.getCoordinates(); // get the coordinates
		} while (otherPlayer.IsThisHitBefore(loc));
		return loc;
		// return myFleet.getCoordinates();
	}

	/**
	 * it checks if all ships are dead for a particular player
	 *
	 * @return true if they are dead
	 */
	boolean allShipsDead() {
		return myFleet.areAllDead(); // function call in Fleet
	}

	/**
	 * It takes coordinates and checks if it is hit by a ship or just a miss
	 * 
	 * @param loc
	 *            X Y coordinates where shot is fired
	 * @return returns true if a ship is hit
	 */
	boolean takeAShot(int[] loc) throws Exception {
		boolean hit = false;
		if (myOcean.getGridLocation(loc) > 0) {
			hit = true;
			PlayerA.displayText("Ship Is Hit!!");
			PlayerB.displayText("Ship Is Hit!!");
			myFleet.aShipIsHit(myOcean.getGridLocation(loc));
			myOcean.setPositionInOcean(loc[0], loc[1]);

		} else if (myOcean.getGridLocation(loc) == 0) {
			myOcean.setPositionInOcean(loc[0], loc[1]);
		}

		return hit; // returns the hit or miss signal
	}

	/**
	 * returns the name of player
	 * 
	 * @return name of the player
	 */
	@Override
	public String getPlayerName() {
		return myName;
	}

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates and open the template
 * in the editor.
 */

/**
 *
 * @author Vishal
 */

class Visitor implements Observer {

	private Ocean myOcean;

	@Override
	public void update(Observable arg0, Object arg1) {

		myOcean = (Ocean) arg0;
		myOcean.printGrid();
	}

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates and open the template
 * in the editor.
 */

/**
 *
 * @author Vishal
 */
/**
 * class to hold the ship data
 *
 * @author Vishal 
 *
 *
 * @version 1.0
 */
class Ship {

	private String myName;
	private int myType, mySize, health;
	private boolean destroyed = false;
	playerInterface Player;

	/**
	 * constructor to initialize a ship
	 *
	 * @param shipType
	 *            number given to ship
	 * @param shipName
	 *            name of the ship
	 * @param shipSize
	 *            size of the ship
	 */
	public Ship(int shipType, String shipName, int shipSize, playerInterface Player) {
		myType = shipType;
		this.Player = Player;
		myName = shipName;
		mySize = shipSize;
		health = shipSize;
	}

	/**
	 * return the status of ship
	 *
	 * @return true if destroyed
	 */
	public boolean isShipDestroyed() {
		if (destroyed == true) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * length of the ship
	 *
	 * @return integer length
	 */
	int getLength() {
		return mySize;
	}

	/**
	 * type of ship
	 *
	 * @return integer ship number
	 */
	int getShipType() {
		return myType;
	}

	/**
	 * function which reduces the ship health if hit
	 *
	 */
	void reduceHealth() throws Exception {
		health--;
		if (health == 0) {
			destroyed = true;
			Player.displayText("Ship -> " + myName + " <- is DESTROYED.");
		}
	}

}

/**
 * interface to implement by all players
 * 
 * @author Vishal
 */

interface Player {

	String getPlayerName();

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates and open the template
 * in the editor.
 */

/**
 * interface to implement all the common functions related to grid
 * 
 * @author Vishal
 */
interface Grid {

	void printGrid();

	void setGridtoDefault();

	int getGridSize();

	char getGridLocation(int x, int y);

	void setGridLocation(int x, int y, char ch);

}

class HoldPlayers {
	playerInterface Playerinterf;
	BattleshipPlayer player;

	HoldPlayers(playerInterface Playerinterf, BattleshipPlayer player) {
		this.player = player;
		this.Playerinterf = Playerinterf;
	}

	public HoldPlayers() {
		// TODO Auto-generated constructor stub
	}
}

public class MVC implements ServerInterface {

	static String[] loc = new String[2];
	static String[] Pname = new String[2];
	static int count = 0;
	static int count2 = 0;

	static playBattleshipGame Game = null;
	static Ocean oceanOne;
	static Ocean oceanTwo;
	static BattleshipPlayer playerOne;
	static BattleshipPlayer playerTwo;
	static playerInterface PlayerA;
	static playerInterface PlayerB;
	int noofships1 = 0;
	int noofships2 = 0;

	static HoldPlayers currentplayer = new HoldPlayers();
	static HoldPlayers waitingplayer = new HoldPlayers();

	/**
	 * Main function to initialize and pass all the objects to play the game
	 * 
	 * @param args
	 *            the command line arguments
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		try {
			MVC obj = new MVC();
			ServerInterface stubS = (ServerInterface) UnicastRemoteObject.exportObject(obj, 0);
			LocateRegistry.createRegistry(1099);
			Registry registry = LocateRegistry.getRegistry("127.0.0.1");
			registry.bind("Server", stubS);

			while (count != 2) {
				Thread.sleep(1000);
				System.out.println("sleeping!" + count + Pname[0] + Pname[1]);
			}

			Registry registry1 = LocateRegistry.getRegistry(loc[0]);
			Registry registry2 = LocateRegistry.getRegistry(loc[1]);
			PlayerA = (playerInterface) registry1.lookup("Player" + Pname[0]);
			PlayerB = (playerInterface) registry2.lookup("Player" + Pname[1]);

			oceanOne = new Ocean(10); // create players
			oceanTwo = new Ocean(10);
			Visitor PlayerOneVisitor = new Visitor();
			Visitor PlayerTwoVisitor = new Visitor();
			oceanOne.addObserver(PlayerOneVisitor);
			oceanTwo.addObserver(PlayerTwoVisitor);
			Game = new playBattleshipGame(PlayerA, PlayerB); // create
			playerOne = new BattleshipPlayer(Pname[0], oceanOne, PlayerA, PlayerB); //// game
			playerTwo = new BattleshipPlayer(Pname[1], oceanTwo, PlayerB, PlayerA);
			System.out.println(playerOne.myFleet.myFleet.length);
			// PlayerA.displayText("Let's play the game!");
			// oceanOne.printGrid(); //create ocean
			// PlayerB.displayText("Let's play the game!");
			// oceanTwo.printGrid();

			while (count2 != 2) {
				Thread.sleep(1000);
				System.out.println("sleeping!" + count + Pname[0] + Pname[1]);
			}
			currentplayer.player = playerTwo;
			currentplayer.Playerinterf = PlayerB;
			waitingplayer.player = playerOne;
			waitingplayer.Playerinterf = PlayerA;
			PlayWithRMI();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}
	
	private static void PlayWithRMI() throws Exception {

		if (Game.gameNotOver(playerOne, playerTwo)) {
			swap(currentplayer, waitingplayer);
			waitingplayer.Playerinterf.Disable();
			currentplayer.Playerinterf.Enable();

			

		} else {
			currentplayer.Playerinterf.Disable();
			waitingplayer.Playerinterf.Disable();
		}

	}

	private static void swap(HoldPlayers currentplayer2, HoldPlayers waitingplayer2) {
		HoldPlayers temp;
		temp = currentplayer;
		currentplayer = waitingplayer;
		waitingplayer = temp;
	}

	public void PlayGame() {
		playerOne.setPlayer(playerTwo);
		playerTwo.setPlayer(playerOne);
		Game.play(playerOne, oceanOne, playerTwo, oceanTwo); // play game
	}

	public void initPA() throws Exception {
		// PlayerA.displayText("Enter coordinates to place ship for player
		// one\n");
		playerOne = new BattleshipPlayer(Pname[0], oceanOne, PlayerA, PlayerB); //
	}

	public void initPB() throws Exception {
		// PlayerB.displayText("Enter coordinates to place ship for player
		// two\n");
		playerTwo = new BattleshipPlayer(Pname[1], oceanTwo, PlayerB, PlayerA);
	}

	@Override
	public void setClient(String s, String Name) throws RemoteException {
		loc[count] = s;
		Pname[count] = Name;
		count++;
	}

	@Override
	public void setPlay() throws RemoteException {
		count2++;
	}

	@Override
	public Boolean canBePlaced(String loc, String ori, String p) throws RemoteException {

		Boolean flag = false;
		int x, y, temp;
		temp = Integer.parseInt(loc);
		x = temp / 10;
		y = temp % 10;
		System.out.println(" " + flag + " " + x + " " + y + " " + temp + " ");
		if (p.equals(Pname[0])
				&& oceanOne.canBePlaced(x, y, Integer.parseInt(ori), playerOne.myFleet.myFleet[noofships1])) {
			flag = true;
		}

		if (p.equals(Pname[1])
				&& oceanTwo.canBePlaced(x, y, Integer.parseInt(ori), playerTwo.myFleet.myFleet[noofships2])) {
			flag = true;
		}
		return flag;
	}

	@Override
	public void Place(String loc, String ori, String p) throws RemoteException {
		int temp;
		int[] locarr = new int[2];
		temp = Integer.parseInt(loc);
		locarr[0] = temp / 10;
		locarr[1] = temp % 10;
		if (p.equals(Pname[0])) {
			oceanOne.placeShip(locarr, Integer.parseInt(ori), playerOne.myFleet.myFleet[noofships1++]);
		}

		if (p.equals(Pname[1])) {
			oceanTwo.placeShip(locarr, Integer.parseInt(ori), playerTwo.myFleet.myFleet[noofships2++]);
		}
	}

	@Override
	public int getTotalShips() throws RemoteException {
		try {
			// initPA();
			// initPB();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("in total ships");

		return playerOne.myFleet.myFleet.length;
	}

	@Override
	public Boolean isHit1(String s) {
		boolean flag = false;
		int temp;
		int[] locarr = new int[2];
		temp = Integer.parseInt(s);
		locarr[0] = temp / 10;
		locarr[1] = temp % 10;
		
		try {
			flag = waitingplayer.player.takeAShot(locarr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	public void PlayWithRMI1() {
		try {
			PlayWithRMI();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}