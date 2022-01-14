package com.test.kalahagame.gameService;

import com.test.kalahagame.dataModels.KalahaGame;
import com.test.kalahagame.exception.ResourceNotFoundException;

public interface KalahaService {

    public Integer createGame();

    KalahaGame getGame(Integer gameId) throws ResourceNotFoundException;

    KalahaGame playGame(Integer gameId, Integer pitId) throws Exception;
}
