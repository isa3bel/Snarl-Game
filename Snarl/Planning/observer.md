Date: March 11th, 2021  
To: Khoury College of Computer Sciences at Northeastern University    
From: Lynnsey Martin and Isabel Bolger  
Subject: Presenting a Game in progress for stakeholders  

Dear Management at Growl Inc.,  

This document is a proposal for designing how to present a game in progress for stakeholders. At a high level, certain stakeholders will need to “observe” the game state, and the “publisher” will need to provide an update for these observers.  For the sake of reducing work for the Game Manager class, it should contain a Publisher object, which will keep track of all the observers and update them accordingly. According to the current design of the Game Manager, much of the game state is mutated during a player’s turn. This includes moving a player to a new tile, and handling any interactions, both which changes a game state. Therefore, the publisher should update any observers at the end of each turn, particularly at the end of the doTurn()method. For anything that needs to “observe” the game state, it should implement the following interface:  

```java
interface Observer {  
	// updates this observer with a view of the updated game state
	public void update(View view);
}
```

Likewise, the publisher that is responsible for keeping track of and updating observers should implement the following interface:  

```java
class Publisher {
	List<Observer> observers;
	// sends a view of the updated game state to every observer
	public void update(GameManager gameManager) {
		View view = new ObserverView();
		gameManager.buildView(gameManager);
		this.observers.forEach(observer ->  observer.update(view));
	}
}
```

The concept of having a publisher and observers for viewing an intermediate game state can be used locally, as well as over a network. In the future, certain components that would like to observe any updates of the game state should be able to connect to a network, so this API can be used for local and online play.  

We hope this provides some further clarity on how certain stakeholders of the game will be able to see an intermediate game state of the Snarl game. If you have any further questions, please feel free to follow up. Thank you.  

Sincerely,  
Isabel Bolger & Lynnsey Martin  
