package org.mots.service;

import org.mots.model.UserMots;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMotsRepository extends org.mots.service.JpaRepository<UserMots, Long> {
    List<UserMots> findByUserId(String userId); // Метод для получения UserMots по userId
}
