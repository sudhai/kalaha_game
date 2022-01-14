package com.test.kalahagame.gameController;

import com.test.kalahagame.dataModels.KalahaGame;
import com.test.kalahagame.exception.KalahaException;
import com.test.kalahagame.exception.ResourceNotFoundException;
import com.test.kalahagame.gameService.KalahaService;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class KalahaGameController {

    @Autowired
    KalahaService service;

    @PostMapping(value = "/createGame")
    public ResponseEntity<Integer> createGame(){
        log.info("Create game method controller called");
        Integer gameId = service.createGame();
        log.info("Created game with id "+gameId);

        return ResponseEntity.ok(gameId);

    }

    @GetMapping(value = "/getGameStatus/{id}")
    public ResponseEntity<KalahaGame> getGame(@ApiParam(required = true) @PathVariable(value = "id") Integer gameId) throws ResourceNotFoundException {
        log.info("Get game called.");
        return ResponseEntity.ok(service.getGame(gameId));
    }

    @PutMapping(value = "/playGame/{gameId}/{pitId}")
    public ResponseEntity<KalahaGame> playGame(@ApiParam(required = true) @PathVariable(value = "gameId")Integer gameId,
                                               @ApiParam(value = "Pit id or index should be between 1-6 or 8-13",required = true)
                                               @PathVariable(value = "pitId")Integer pitId) throws Exception {

        log.info("playGame service is called for gameId "+gameId +" and pit id "+pitId);
        if(pitId < 1 || pitId.equals(7) || pitId.equals(14)){
            throw new KalahaException("Invalid pit index. Pit id or index should be between 1-6 or 8-13");
        }
        KalahaGame game = service.playGame(gameId,pitId);

        return ResponseEntity.ok(game);
    }


}
