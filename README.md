# spaceBattleGame

*Created by Jialin Liu on 4 Oct 2016*

*CSEE, University of Essex, UK*

*Email: jialin.liu.cn@gmail.com*

## Game
Single- and 2-player space battle game, derived from the original Spacewar. The framework is adapted to the [GVG-AI competition](http://www.gvgai.net/index.php).
### Spaceship 
Each player/agent controllers a spaceship which has a maximal speed (units distance/game tick), and slows down over time. At each game tick, the player can choose to *do nothing* or to make an action among {*RotateClockwise, RotateAnticlockwise, Thrust, Shoot*}. A missile is launched while the *Shoot* action is chosen and its cooldown period is finished, otherwise, no action will be made (like *do nothing*). The spaceship is affected by a random recoil force when launching a missile.
### Missile
A missile has a maximal speed (units distance/game tick), and vanishes into nothing later (e.g. after 30 game ticks). It never damages its mother ship.
### Score
Every time a player hits its opponent, it obtains **r** points (reward). Every time a player launches a missile, it is penalized by a predefined **c** points (cost). Given a game state **s**, the player **i** has a **score** calculated using the number of lives subtracted from the opponent and the number of launched missiles by player **i**.
### End condition
The end consition is playable, the current one is detailed here.
A game ends after **T** game ticks.
A player wins the game if it has higher score than its opponent after **T** game ticks, and it's a loss of the other player. If both players have the same score, it's a draw for both.
Another common end codition is the game ends if one of the player loses a number of lives.
### Visualisation
Every spaceship has a radius of 20 pixels and every missile has a radius of 4 pixels in a layout of size 640\*480.

## Parameter space (game space)
Once a parameter setting is chosen, a new game instance is created.
The playable parameters are listed in [*ontology/Constants.java*](https://github.com/ljialin/spaceBattleGame/blob/master/src/ontology/Constants.java). Examples are, but not limited to, the speed of spaceship/missile, the reward of hitting the opponent, the cost of firing, the radius of spaceship, etc.

## Structure of The Repository
### [ontology] (https://github.com/ljialin/spaceBattleGame/tree/master/src/ontology)	
#### physics
The physics in the game, for instance, the gravity, the random repulsive force, etc. The use of physics increases the stochasticity of the game. 
#### asteroids
The main game objects in the game.
### [core](https://github.com/ljialin/spaceBattleGame/tree/master/src/core)
The main game and abstract class of players.
### [controllers](https://github.com/ljialin/spaceBattleGame/tree/master/src/controllers)
Human controllers and deterministic controllers, and some sample AI controllers from the GVG-AI.
The heuristics are also placed here, as they are parts of AI controllers.
### [competition](https://github.com/ljialin/spaceBattleGame/tree/master/src/competition)
This is the same as the one in the [GVG-AI competition](http://www.gvgai.net/index.php).
The total game ticks of a game and the legal time for making a decision at every game tick are set in [*competition/CompetitionParameters.java*](https://github.com/ljialin/spaceBattleGame/blob/master/src/competition/CompetitionParameters.java), for the purpose of being adapted to the controllers submitted to the GVG-AI competition.
### [bandits](https://github.com/ljialin/spaceBattleGame/tree/master/src/bandits)
Our optimiser BanditEA. The simpler version for binary problem is introduced in our arxiv paper [Bandit-Based Random Mutation Hill-Climbing](https://arxiv.org/pdf/1606.06041.pdf).
### [test](https://github.com/ljialin/spaceBattleGame/tree/master/src/test)
Tests. 
Please refer to [test/GameTest.java](https://github.com/ljialin/spaceBattleGame/blob/master/src/test/GameTest.java) for an example of using this framework.
### [tools](https://github.com/ljialin/spaceBattleGame/tree/master/src/tools)
