package com.test.kalahagame.dataModels;

import lombok.Getter;

@Getter
public enum Player {
    PLAYER_A(1),
    PLAYER_B(2);

    private Integer id;

    Player(Integer id){
        this.id = id;
    }

}
