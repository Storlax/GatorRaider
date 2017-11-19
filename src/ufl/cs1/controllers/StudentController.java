package ufl.cs1.controllers;

import game.controllers.DefenderController;
import game.models.Attacker;
import game.models.Defender;
import game.models.Game;
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
        /////////////////////////////////////
        /////////////////////////////////////
        ///////////////BLINKY////////////////
        /////////////////////////////////////
        /////////////////////////////////////

        Node defenderLoc = blinky.getLocation();
        Node attackerLoc = attacker.getLocation();

        //List<Node> powerPillList = this.getPowerPillList();

        int currentDir = attacker.getDirection();

        List<Node> lol = attacker.getPathTo(attackerLoc);

        int location = defenderLoc.getPathDistance(attackerLoc);

        int x = attackerLoc.getX();
        int y = attackerLoc.getY();

        Node xAttacker = attackerLoc.getNeighbor(x);
        Node yAttacker = attackerLoc.getNeighbor(y);

        actions[0] = blinky.getNextDir(xAttacker, true);
        actions[0] = blinky.getNextDir(yAttacker, true);

        ////////////////////////////////////
        ////////////////////////////////////
        //////////////BLINKY////////////////
        ////////////////////////////////////
        ////////////////////////////////////

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

		for (int i = 0; i < powerLocations.size(); i++) {
			if (game.checkPowerPill(powerLocations.get(i)) == true) {
				actions[2] = defender.getNextDir(powerLocations.get(i), true);

				//reverse direction , so it sits on it
				actions[2] = defender.getNextDir(powerLocations.get(i), false);

				//Chase Pacman if he gets close, based off of a prediciton of where he's going
				if(defender.getLocation().getPathDistance(attacker.getLocation()) < 10){
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





		return actions;
	}
}