Date: March 25th, 2021
To: Khoury College of Computer Sciences at Northeastern University  
From: Lynnsey Martin and Isabel Bolger  
Subject: Designing Adversary Interface

Dear Management at Growl Inc.,

This document is a proposal for designing the Adversary interface. At a high level, the adversaries will be similar to players in that it’ll interact with the game manager on every turn, however there will be several differences. First, an adversary will get the full level information, and second, an adversary will know where all the players are before it makes a turn. To handle Adversaries, we will create an abstract class like so: 

abstract class Adversary {

	...other methods not relevant to milestone requirements...

	public void updateController(GameManager gameManager) {
		AdversaryView view = new AdversaryView();
		this.controller.update(view);
	}
}

To distinguish the two views for the players and the adversaries, we will create two separate views: AdversaryView and PlayerView. The AdversaryView will allow the adversary to see the entire level, while the PlayerView will be restricted. The ensure that the adversary will only get updates of where the players are before it takes a turn, the GameManager will update the adversary’s controller before it makes a move. Sample code:

In GameManager class:
public void doTurn(Character currentCharacter) {
	currentCharacter.updateController(this);
...Rest of doTurn() method... 
}

If there are additional features/requirements/functionalities to the Adversary in the future, it’ll be very easy to do so in the abstract class. Furthermore, the abstract class makes it easy to customize different types of Adversaries, as they can simply extend from this abstract class. We hope this provides some further clarity on the design of the Adversary for the Snarl game. If you have any further questions, please feel free to follow up. Thank you.

Sincerely,
Isabel Bolger & Lynnsey Martin
