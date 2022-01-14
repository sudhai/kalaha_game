package com.test.kalahagame.repository;

import com.test.kalahagame.dataModels.KalahaGame;
import org.springframework.data.repository.CrudRepository;

public interface GameRepository extends CrudRepository<KalahaGame,Integer> {
}
