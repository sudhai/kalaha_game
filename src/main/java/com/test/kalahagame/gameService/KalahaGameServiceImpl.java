package com.test.kalahagame.gameService;

import com.test.kalahagame.dataModels.KalahaPit;
import com.test.kalahagame.dataModels.KalahaGame;
import com.test.kalahagame.exception.GameEndException;
import com.test.kalahagame.exception.KalahaBadRequestException;
import com.test.kalahagame.exception.ResourceNotFoundException;
import com.test.kalahagame.repository.KalahaGameRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.test.kalahagame.constants.GameConstants.*;
import static com.test.kalahagame.dataModels.Player.PLAYER_A;
import static com.test.kalahagame.dataModels.Player.PLAYER_B;

@Service
@Slf4j
public class KalahaGameServiceImpl implements KalahaGameService {

    @Autowired
    KalahaGameRepository repository;

    private final String statusGame = "Game is In progress";

    @Override
    public KalahaGame createGame() {

        log.info("create game service called");
        KalahaGame game = new KalahaGame(DEFAULT_STONES);//Default is 6 stones per pit
        game.setPlayersTurn(PLAYER_A);
        game.setGameStatus(statusGame);
        repository.save(game);
        log.info("create game service call ended");
        return game;
    }

    @Override
    public KalahaGame getGame(Integer gameId) {
        log.info("get game service called");
        Optional<KalahaGame> result = repository.findById(gameId);
        if(result.isEmpty())
            throw new ResourceNotFoundException("Game id is not present");
        log.info("get game service call ended");
        return result.get();
    }

    @Override
    public KalahaGame playGame(Integer gameId, Integer pitId) {

        log.info("play game service call started");
        KalahaGame game = getGame(gameId);

        if(game.getGameStatus().equals(statusGame)) {
            /*
              If Player has selected other player's pit, then return
            */
            if ((game.getPlayersTurn() == PLAYER_A && pitId > 6)
                    || (game.getPlayersTurn() == PLAYER_B && pitId < 8)) {
                throw new KalahaBadRequestException("You have to select your own pit. It is " + game.getPlayersTurn() + " turn");
            }

            //Get Pit details - stones in the pit
            KalahaPit selectedPit = game.getPitDetailsById(pitId);

            Integer stonesInPit = selectedPit.getStones();

            if (stonesInPit == 0) {
                throw new KalahaBadRequestException("No stones in the pid. Please select different Pid.");
            }


            List<KalahaPit> allPits = game.getPits();
            allPits.get(pitId - 1).clear();

            boolean houseIndex = false;


            for (int i = 1; i <= stonesInPit; i++) {

                if (i == stonesInPit) {
                    boolean isNextPidIdSelf = false;
                    int nextPitId = pitId + 1;
                    if ((game.getPlayersTurn() == PLAYER_A && nextPitId < 7)
                            || (game.getPlayersTurn() == PLAYER_B && (nextPitId > 7 && nextPitId < 14)))
                        isNextPidIdSelf = true;

                    if (isNextPidIdSelf) {
                        KalahaPit nextPit = game.getPitDetailsById(nextPitId);
                        if (nextPit.isEmpty()) {
                            int oppositePitIndex = TOTAL_PITS - nextPitId;
                            int stonesInOppPit = game.getPitDetailsById(oppositePitIndex).getStones();
                            int housePitIndex = game.getPlayersTurn() == PLAYER_A ? PLAYER_A_HOUSE_PIT : PLAYER_B_HOUSE_PIT;
                            allPits.get(housePitIndex).addStones(stonesInOppPit + 1);
                            allPits.get(oppositePitIndex -1).clear();
                            break;
                        }
                    }
                }

            /*
              If the requested pit index is the last pid index of array then start from first pid

              to prevent array index out of bound exception
             */
                if (pitId == TOTAL_PITS) {
                    pitId = 0;
                }

            /*
              don't add stone in the opposite player's house pit
             */
                if ((pitId == PLAYER_A_HOUSE_PIT && game.getPlayersTurn() != PLAYER_A) || (pitId == PLAYER_B_HOUSE_PIT && game.getPlayersTurn() != PLAYER_B)) {
                    i = i-1;
                    pitId++;
                    continue;
                }

             /*
                    if the last stone is added in the player's house, then they get one more chance

              */
                if (i == stonesInPit && ((game.getPlayersTurn() == PLAYER_A && pitId == PLAYER_A_HOUSE_PIT)
                        || (game.getPlayersTurn() == PLAYER_B && pitId == PLAYER_B_HOUSE_PIT))) {
                    houseIndex = true;
                }

                allPits.get(pitId).addStones(1);

                pitId++;

            }
            game.setPits(allPits);
            if (!houseIndex)
                game.setPlayersTurn(game.getPlayersTurn() == PLAYER_A ? PLAYER_B : PLAYER_A);

            if(checkGameOver(game)){
                checkWinner(game);
            }

            repository.save(game);
        }else{
            throw new GameEndException(game.getGameId()+" : This game is not in progress. Game status : "+game.getGameStatus());
        }
        log.info("play game service called ended");

        return game;
    }

    /**
     * Sets winner for the game by comparing the total number of stones in each player's house pit
     * @param game
     */

    private void checkWinner(KalahaGame game) {

        Integer playerAStones = game.getPits().get(PLAYER_A_HOUSE_PIT).getStones();
        Integer playerBStones = game.getPits().get(PLAYER_B_HOUSE_PIT).getStones();

        switch (playerAStones.compareTo(playerBStones)) {
            case 0:
                game.setGameStatus("game is DRAW");
                break;
            case -1:
                game.setGameStatus("Player B wins");
                break;
            case 1:
                game.setGameStatus("Player A wins");
                break;
        }


    }

    /**
     * To check if the game is over
     * @param game
     * @return true if any Player's pits are empty and other player's remaining stones are added to the house pit
     */

    private boolean checkGameOver(KalahaGame game){
        if(game.checkPlayersNonHousePitsEmpty(PLAYER_A.getId()) || game.checkPlayersNonHousePitsEmpty(PLAYER_A.getId())){

            game.addAllStonesToHouseIndex(PLAYER_A.getId());
            game.addAllStonesToHouseIndex(PLAYER_B.getId());
            game.getPits().stream().filter(x -> x.getPitId() != 7 && x.getPitId() != 14).forEach(KalahaPit::clear);
            return true;
        }
        return false;
    }
}
