package com.test.kalahagame.gameService;

import com.test.kalahagame.dataModels.KalahaGame;
import com.test.kalahagame.exception.ResourceNotFoundException;

public interface KalahaGameService {

    /**
     * Creates a new Game
     * @return new Game
     */
    KalahaGame createGame();

    /**
     * Returns game on given gameId if present
     * @param gameId
     *         is unique id for a game
     * @return game stored in database
     * @throws ResourceNotFoundException
     *          if gameId is not present
     */
    KalahaGame getGame(Integer gameId);

    /**
     * Allows player to play
     * @param gameId
     *         is unique id for a game
     * @param pitId
     *        is the id or number of the selected pit in the game board
     * @return updated Game after the player has played
     * @throws Exception
     *      if there are no stones in the selected pit
     *      or if the player has selected opposite player's pit
     */
    KalahaGame playGame(Integer gameId, Integer pitId) ;
}
