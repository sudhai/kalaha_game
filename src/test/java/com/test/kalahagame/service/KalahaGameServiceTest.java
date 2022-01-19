package com.test.kalahagame.service;

import com.test.kalahagame.dataModels.KalahaGame;
import com.test.kalahagame.exception.GameEndException;
import com.test.kalahagame.exception.KalahaBadRequestException;
import com.test.kalahagame.exception.ResourceNotFoundException;
import com.test.kalahagame.gameService.KalahaGameServiceImpl;
import com.test.kalahagame.repository.KalahaGameRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static com.test.kalahagame.constants.GameConstants.DEFAULT_STONES;
import static com.test.kalahagame.dataModels.Player.PLAYER_A;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class KalahaGameServiceTest {

    @InjectMocks
    private KalahaGameServiceImpl service;

    @Mock
    private KalahaGameRepository repository;

    @Test
    public void testCreateGame(){
        KalahaGame game = new KalahaGame(DEFAULT_STONES);
        game.setPlayersTurn(PLAYER_A);
        game.setGameStatus("Game is In progress");
        game.setGameId(2);

        when(repository.save(game)).thenReturn(game);
        service.createGame();
        verify(repository).save(game);
    }
    @Test
    public void testGetGame() {
        KalahaGame game = new KalahaGame(DEFAULT_STONES);
        game.setPlayersTurn(PLAYER_A);
        game.setGameStatus("Game is In progress");

        when(repository.findById(1)).thenReturn(Optional.of(game));
        service.getGame(1);

        verify(repository).findById(1);
    }

    @Test
    public void testPlayGame()  {
        int gameId = 1;
        KalahaGame game = new KalahaGame(DEFAULT_STONES);
        game.setPlayersTurn(PLAYER_A);
        game.setGameStatus("Game is In progress");

        when(repository.findById(gameId)).thenReturn(Optional.ofNullable(game));
        when(repository.save(game)).thenReturn(game);
        service.playGame(gameId,1);

        verify(repository).findById(gameId);
        verify(repository).save(game);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testPlayGameInvalidId() {
        int gameId = 10;
        KalahaGame game = new KalahaGame(DEFAULT_STONES);

        when(repository.findById(gameId)).thenThrow(ResourceNotFoundException.class);
        service.playGame(gameId,1);

        verify(repository).findById(gameId);
        verify(repository , never()).save(game);
    }
    @Test(expected = KalahaBadRequestException.class)
    public void testPlayGameOppositePitId()  {
        int gameId = 1;
        KalahaGame game = new KalahaGame(DEFAULT_STONES);
        game.setPlayersTurn(PLAYER_A);

        when(repository.findById(gameId)).thenThrow(KalahaBadRequestException.class);
        service.playGame(gameId,8);

        verify(repository).findById(gameId);
        verify(repository , never()).save(game);
    }


    @Test(expected = GameEndException.class)
    public void testPlayGameMethodGameEndException()  {
        int gameId = 1;
        KalahaGame game = new KalahaGame(DEFAULT_STONES);
        game.setPlayersTurn(PLAYER_A);

        when(repository.findById(gameId)).thenThrow(GameEndException.class);
        service.playGame(gameId,2);

        verify(repository).findById(gameId);
        verify(repository , never()).save(game);
    }
}
