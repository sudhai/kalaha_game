package com.test.kalahagame.dataModels;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GamePit implements Serializable {

    @Id
    @Column
    private Integer pitId;

    @Column
    private Integer stones;

    public void clear (){
        this.stones = 0;
    }
    public void addStones (){
        this.stones++;
    }
    @JsonIgnore
    public boolean isEmpty(){
        return this.stones == 0;
    }
}
