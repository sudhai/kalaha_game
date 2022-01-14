package com.test.kalahagame.gameService;

import com.test.kalahagame.dataModels.GamePit;
import com.test.kalahagame.dataModels.KalahaGame;
import com.test.kalahagame.dataModels.Player;
import com.test.kalahagame.exception.KalahaBadRequestException;
import com.test.kalahagame.exception.KalahaException;
import com.test.kalahagame.exception.ResourceNotFoundException;
import com.test.kalahagame.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KalahaServiceImpl implements KalahaService{

    @Autowired
    GameRepository repository;

    @Override
    public Integer createGame() {

        KalahaGame game = new KalahaGame(6);//Default is 6 stones per pit
        game.setPlayersTurn(Player.PLAYER_A);
        repository.save(game);
        return game.getId();
    }

    @Override
    public KalahaGame getGame(Integer gameId) throws ResourceNotFoundException {
        Optional<KalahaGame> result = repository.findById(gameId);
        if(!result.isPresent())
            throw new ResourceNotFoundException("Game id is not present");
        return result.get();
    }

    @Override
    public KalahaGame playGame(Integer gameId, Integer requestedPitId) throws Exception {

        KalahaGame game = getGame(gameId);

        /**
         * If Player has selected other player's pit, then return
         */
        if((game.getPlayersTurn() == Player.PLAYER_A && requestedPitId > 6 )
        || (game.getPlayersTurn() == Player.PLAYER_B && requestedPitId < 8)){
            throw new KalahaBadRequestException("You have to select your own pit. It is "+game.getPlayersTurn() + " turn");
        }

        //Get Pit details - stones in the pit
        GamePit selectedPit = game.getPitDetailsById(requestedPitId);

        Integer stonesInPit = selectedPit.getStones();

        if(stonesInPit == 0){
            throw new KalahaException("No stones in the pid. Please select different Pid.");
        }

      //  game.setCurrentPitIndex(requestedPitId);

        List<GamePit> allPits = game.getPits();
        allPits.get(requestedPitId - 1).clear();

        boolean houseIndex = false;

        for (int i=1; i <= stonesInPit; i++) {

            if(requestedPitId == 14){
                requestedPitId = 0;
            }

            if((requestedPitId == 5 && game.getPlayersTurn() != Player.PLAYER_A) || (requestedPitId == 13 && game.getPlayersTurn() != Player.PLAYER_B))
                continue;

            allPits.get(requestedPitId).addStones();

            if(i == stonesInPit && ( (game.getPlayersTurn() == Player.PLAYER_A &&  requestedPitId == 5)
                    || (game.getPlayersTurn() == Player.PLAYER_B &&  requestedPitId == 13) ) ) // 5 and 13 are the array index of players house pit
                houseIndex = true; //if the last stone is added in the player's house, then they get one more chance

            requestedPitId++;

        }
        game.setPits(allPits);
        if(!houseIndex)
            game.setPlayersTurn(game.getPlayersTurn() == Player.PLAYER_A ? Player.PLAYER_B  : Player.PLAYER_A);

        repository.save(game);

        return game;
    }

}
