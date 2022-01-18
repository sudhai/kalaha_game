package com.test.kalahagame.controller;

import com.test.kalahagame.dataModels.KalahaGame;
import com.test.kalahagame.exception.KalahaBadRequestException;
import com.test.kalahagame.exception.ResourceNotFoundException;
import com.test.kalahagame.gameController.KalahaGameController;
import com.test.kalahagame.gameService.KalahaGameService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class KalahaGameControllerTest {

    @InjectMocks
    private KalahaGameController controller;

    @Mock
    private KalahaGameService service;

    private Integer pitId = 1;

    private KalahaGame game;

    @Before
    public void init(){
        game = new KalahaGame();
    }

    @Test
    public void testCreateGame(){

        when(service.createGame()).thenReturn(game);
        ResponseEntity<KalahaGame> response = controller.createGame();

        verify(service).createGame();
        assertEquals(game,response.getBody());
    }

    @Test
    public void testGetGame() {
        when(service.getGame(1)).thenReturn(game);
        ResponseEntity<KalahaGame> response = controller.getGame(1);

        verify(service).getGame(1);
        assertEquals(game,response.getBody());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetGameInvalidGameId(){
        when(service.getGame(7)).thenThrow(ResourceNotFoundException.class);
        controller.getGame(7);

        verify(service).getGame(7);
    }

    @Test
    public void testPlayGame() {
        when(service.playGame(anyInt(),pitId)).thenReturn(game);
        ResponseEntity<KalahaGame> response = controller.playGame(anyInt(),pitId);

        verify(service).playGame(anyInt(),pitId);
        assertEquals(game,response.getBody());
    }
}
