package com.test.kalahagame.dataModels;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
@Entity
@Table
public class KalahaGame implements Serializable {

    private static final AtomicInteger count = new AtomicInteger(0);

    @Id
    @Column
    private Integer id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "gameId")
    private List<GamePit> pits;

    private Player playersTurn;

//    @JsonIgnore
//    private int currentPitIndex;

    public KalahaGame(){
        this(6);
    }

    public KalahaGame(int stones){
        this.id = count.incrementAndGet();
        List<GamePit> pit = new ArrayList<>();
        pit.add(new GamePit(1,stones));
        pit.add(new GamePit(2,stones));
        pit.add(new GamePit(3,stones));
        pit.add(new GamePit(4,stones));
        pit.add(new GamePit(5,stones));
        pit.add(new GamePit(6,stones));
        pit.add(new GamePit(7,0));//player A house pit
        pit.add(new GamePit(8,stones));
        pit.add(new GamePit(9,stones));
        pit.add(new GamePit(10,stones));
        pit.add(new GamePit(11,stones));
        pit.add(new GamePit(12,stones));
        pit.add(new GamePit(13,stones));
        pit.add(new GamePit(14,0));// player B house pit
        this.pits = pit;
    }

    public GamePit getPitDetailsById(Integer pitId){
        try {
            return this.pits.get(pitId - 1);
        }catch (Exception e){
            //throw new exception
            throw new RuntimeException("Invalid Pit ID");
        }
    }

}