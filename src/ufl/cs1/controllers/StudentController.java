package ufl.cs1.controllers;

import game.controllers.DefenderController;
import game.models.Defender;
import game.models.Game;
import game.models.Attacker;
import game.models.Node;

import java.util.List;

/*
THINGS CHANGED:

Game.java: DELAY from 40 to 10


 */


public final class StudentController implements DefenderController
{
	//Test comment
	//Test comment "Hi Michael"
	//If we want Scatter, Chase, or Frightened behavior, go to the original defenders class.
	public void init(Game game) {

	}

	public void shutdown(Game game) { }

	public int[] update(Game game,long timeDue)
	{
		int[] actions = new int[Game.NUM_DEFENDER];
		List<Defender> enemies = game.getDefenders();
		Attacker attacker = game.getAttacker();
		Defender defender = enemies.get(1);


		//The controller must use information from the attacker’s state, board state, and maze in making decisions.

		//////////////////////////////RED GUY///////////////////////////////////

		Defender blinky = enemies.get(0);

		Node defenderLoc = blinky.getLocation();
		Node attackerLoc = attacker.getLocation();
		Node attackerNode = defenderLoc.getNeighbor(blinky.getDirection());
		Node devastatorNode = attackerLoc.getNeighbor(attacker.getDirection());

		int distanceFromDefenderToDevastator = defenderLoc.getPathDistance(attackerLoc);

		if (distanceFromDefenderToDevastator < 25) {
			if (devastatorNode != null) {
				actions[0] = blinky.getNextDir(devastatorNode, true);
			} else {
				actions[0] = blinky.getNextDir(attackerLoc, false);
			}
		}

		boolean devastatorJunction = attackerLoc.isJunction();
		Node devastatorNextMovePrediction = attacker.getLocation().getNeighbor(attacker.getDirection());


		if (devastatorJunction == true && attacker.getLocation().getNeighbor(attacker.getDirection()) != null)
		{
			actions[0] = blinky.getNextDir(devastatorNextMovePrediction, true);
		}







		//////////////////////////////PINK GUY///////////////////////////////////
		//Pink's normal attack = go to the node Pacman is heading towards, i.e. chase one step ahead of him
		defender = enemies.get(1);
		if (attacker.getLocation().getNeighbor(attacker.getDirection()) != null){
			Node prediction = attacker.getLocation().getNeighbor(attacker.getDirection());
			actions[1] = defender.getNextDir(prediction, true);
		}
		else{
			actions[1] = defender.getNextDir(attacker.getLocation(), true);
		}

		//If edible, gtfo
		if(defender.getVulnerableTime() > 0)
			actions[1]= defender.getNextDir(attacker.getLocation(),false);

		//////////////////////////////ORANGE GUY///////////////////////////////////
		//Orange Normal attack = sit on an existing pill

		//Get the list of power pills
		List<Node> powerLocations = game.getPowerPillList();
		defender = enemies.get(2);
		boolean atPill = false;

		for (int i = 0; i < powerLocations.size(); i++) {
			if (game.checkPowerPill(powerLocations.get(i)) == true) {
				actions[2] = defender.getNextDir(powerLocations.get(i), true);

				//can't reverse direction, so it loops to guard the pill
				int nextDirection = defender.getNextDir(powerLocations.get(i), atPill);
				atPill = !atPill;


				//Chase Pacman if he gets close, based off of a prediciton of where he's going
				if(defender.getLocation().getPathDistance(attacker.getLocation()) < 40){
					if (attacker.getLocation().getNeighbor(attacker.getDirection()) != null){
						Node prediction = attacker.getLocation().getNeighbor(attacker.getDirection());
						actions[1] = defender.getNextDir(prediction, true);
					}
					else{
						actions[1] = defender.getNextDir(attacker.getLocation(), true);
					}
				}
			}
		}
		if(powerLocations.size() == 0){
			actions[2] = defender.getNextDir(attacker.getLocation(), true);
		}


		//////////////////////////////BLUE GUY A.K.A Jesus///////////////////////////////////
		defender = enemies.get(3);

		//Determine if Pac man is close to a power pill, so blue can know to run away
		boolean pacmanCloseToSuper = false;
		int [] powerPills = new int[powerLocations.size()];
		for (int i = 0; i < powerLocations.size(); i++) {
			if (game.checkPowerPill(powerLocations.get(i)) == true) {
				if(attacker.getLocation().getPathDistance(powerLocations.get(i)) < 10) {
					pacmanCloseToSuper = true;
					break;
				}
				else{
					pacmanCloseToSuper = false;
				}
			}
		}


		//if pac man about to go god-mode, gtfo
		if(defender.getVulnerableTime()>0 || pacmanCloseToSuper) {
			//if edible or Ms Pac-Man is close to power pill, move away from Ms Pac-Man
			actions[3] = defender.getNextDir(attacker.getLocation(), false);
		}
		else {
			//Default behavior is go towards Pacman
			if (attacker.getLocation().getNeighbor(attacker.getDirection()) != null){
				Node prediction = attacker.getLocation().getNeighbor(attacker.getDirection());
				actions[3] = defender.getNextDir(prediction, true);
			}
			else{
				actions[3] = defender.getNextDir(attacker.getLocation(), true);
			}
		}



		return actions;
	}
}
