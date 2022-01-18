package com.test.kalahagame.gameController;

import com.test.kalahagame.dataModels.KalahaGame;
import com.test.kalahagame.exception.KalahaBadRequestException;
import com.test.kalahagame.exception.ResourceNotFoundException;
import com.test.kalahagame.gameService.KalahaGameService;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/games")
public class KalahaGameController {

    @Autowired
    KalahaGameService service;

    @PostMapping
    public ResponseEntity<KalahaGame> createGame(){
        log.info("Create game method controller called");
        KalahaGame game = service.createGame();
        log.info("Created game with id "+game.getGameId());

        return ResponseEntity.ok(game);

    }

    @GetMapping(value = "/{id}" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<KalahaGame> getGame(@ApiParam(required = true) @PathVariable(value = "id") Integer gameId) {
        log.info("Get game called.");
        return ResponseEntity.ok(service.getGame(gameId));
    }

    @PostMapping(value = "/{gameId}/pits/{pitId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<KalahaGame> playGame(@ApiParam(required = true) @PathVariable(value = "gameId")Integer gameId,
                                               @ApiParam(value = "Pit id or index should be between 1-6 or 8-13",required = true)
                                               @PathVariable(value = "pitId")Integer pitId)  {

        log.info("playGame controller for gameId "+gameId +" and pit id "+pitId);
        if(pitId < 1 || pitId.equals(7) || pitId > 13){
            throw new KalahaBadRequestException("Invalid pit index. Pit id or index should be between 1-6 or 8-13");
        }
        KalahaGame game = service.playGame(gameId,pitId);

        return ResponseEntity.ok(game);
    }


}
