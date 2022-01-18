package com.test.kalahagame.dataModels;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.test.kalahagame.exception.KalahaBadRequestException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.concurrent.atomic.AtomicInteger;

import static com.test.kalahagame.constants.GameConstants.*;

@Getter
@Setter
@Entity
@Table
public class KalahaGame implements Serializable {

    private static final AtomicInteger count = new AtomicInteger(0);

    @Id
    @Column
    private Integer gameId;

    private Player playersTurn;

    private String gameStatus;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id")
    private List<KalahaPit> pits;

    public KalahaGame(){
        this(DEFAULT_STONES);
    }

    public KalahaGame(int stones){
        this.gameId = count.incrementAndGet();
        List<KalahaPit> pit = new ArrayList<>();
        pit.add(new KalahaPit(1,stones));
        pit.add(new KalahaPit(2,stones));
        pit.add(new KalahaPit(3,stones));
        pit.add(new KalahaPit(4,stones));
        pit.add(new KalahaPit(5,stones));
        pit.add(new KalahaPit(6,stones));
        pit.add(new KalahaPit(7,0));//player A house pit
        pit.add(new KalahaPit(8,stones));
        pit.add(new KalahaPit(9,stones));
        pit.add(new KalahaPit(10,stones));
        pit.add(new KalahaPit(11,stones));
        pit.add(new KalahaPit(12,stones));
        pit.add(new KalahaPit(13,stones));
        pit.add(new KalahaPit(14,0));// player B house pit
        this.pits = pit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KalahaGame game = (KalahaGame) o;
        return Objects.equals(gameId, game.gameId) && playersTurn == game.playersTurn && Objects.equals(gameStatus, game.gameStatus) && Objects.equals(pits, game.pits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameId, playersTurn, gameStatus, pits);
    }

    public KalahaPit getPitDetailsById(Integer pitId){
        try {
            return this.pits.get(pitId - 1);
        }catch (Exception e){
            throw new KalahaBadRequestException("Invalid Pit ID");
        }
    }

    @JsonIgnore
    public boolean checkPlayersNonHousePitsEmpty(int id){

        if(id == Player.PLAYER_A.getId())
            return this.pits.stream().filter(pit -> pit.getPitId() < 7).allMatch(KalahaPit::isEmpty);
        else
            return this.pits.stream().filter(pit -> pit.getPitId() > 7 && pit.getPitId() < 14).allMatch(KalahaPit::isEmpty);
    }

    @JsonIgnore
    public void addAllStonesToHouseIndex(int id){
        if(id == Player.PLAYER_A.getId()) {
           OptionalInt optionalIntSum = this.pits.stream().filter(pit -> pit.getPitId() < 7).mapToInt(KalahaPit::getStones).reduce(Integer::sum);
           int sumOfStones = optionalIntSum.isPresent() ? optionalIntSum.getAsInt() : 0;
           this.pits.get(PLAYER_A_HOUSE_PIT).addStones(sumOfStones);
        }else{
            OptionalInt optionalIntSum = this.pits.stream().filter(pit -> pit.getPitId() > 7 && pit.getPitId() < 14).mapToInt(KalahaPit::getStones).reduce(Integer::sum);
            int sumOfStones = optionalIntSum.isPresent() ? optionalIntSum.getAsInt() : 0;
            this.pits.get(PLAYER_B_HOUSE_PIT).addStones(sumOfStones);
        }
    }

}
