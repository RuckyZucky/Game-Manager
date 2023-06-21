# Game Manager

This repository contains a broken down variant of a game manager concept. It implements a simple workflow to use for a 
game logic. Because of the use of Spring Beans and dependency injection it is highly customizable and can easily be
expanded.

## Game

The game class in this repository is a placeholder and contains everything that is necessary to use the game manager.
That includes a Workflow, a State and a progress variable. The game object is altered by various actions in the 
workflow. 

## Actions

The actions are the fundamental parts of the workflow to alter the game object. Actions are divided into two kinds: 
SimpleActions and ComplexActions. Both can be used inside a workflow by adding the class object to the actions list 
inside the Workflow class. Therefor every action has to be a Spring Bean (`@Component`). This also allows to use 
dependency injection inside the actions to access other services or JPA repositories;

### Simple Actions
A simple action always contains a `doAction` method. This can alter the game object it gets as a parameter. After the
execution the action is finished and the next action in the workflow can be executed. 

### Complex Actions
A complex action can be described as a phase of the game, where it waits for user input. Because of that, complex 
actions are not immediately finished. The game remains in this phase until the `isFinished` condition is met. To alter
the game in complex actions, new public methods have to be implemented in the class. Additionally, a method has to be 
implemented in the GameService to call the method on the current action. Therefor the method `executeComplexAction`
has to be used. This checks if the game is currently in the correct action, loads the Bean with the Spring 
ApplicationContext and executes the method. If the game is not currently in the correct action, the method throws an
exception. The method in the game service can then be called from other parts of the application like a REST controller. 
