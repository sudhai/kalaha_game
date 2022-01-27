# Kalaha Game API Implementation

This application implements Kalaha or Mancala Game using Java Spring boot.
To know about the game visit this link https://en.wikipedia.org/wiki/Kalah

#Installation

##Pre-requisites

- JDK 11
- Apache Maven

##Build 

- Build the artifacts and download all the dependencies run `mvn clean install`
- To run test cases `mvn test`

##Run the application

- run spring boot application command `mvn spring-boot:run`

##Access the application

- Also for API documentation use the below swagger link
http://localhost:8080/swagger-ui.html#/
- Application runs http://localhost:8080/

##Getting Started

- There are 3 end points to this API:
    - Create game: This api to create the game and board setup
    - Get Game: This api is for getting the updated scoreboard on the pits and stones for the particular game
    - Play game: This api is to play game by specifying pit id of the player

- To start the game(create game) : `curl --header "Content-Type: application/json" --request POST http://<host>:<port>/games`
- To get the created game(get game) : `curl --header "Content-Type: application/json" --request GET http://<host>:<port>/games/{gameId}`
- To make a move(play game) : `curl --header "Content-Type: application/json" --request POST http://<host>:<port>/games/{gameId}/pits/{pitId}`

#Author
    Sudha Iyer
