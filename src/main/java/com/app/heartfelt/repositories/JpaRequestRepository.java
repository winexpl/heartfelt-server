package com.app.heartfelt.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.app.heartfelt.models.RequestWithUsernameAndNickname;
import com.app.heartfelt.models.entities.Request;

public interface JpaRequestRepository extends JpaRepository<Request, UUID> {

    @Query(nativeQuery = true)
    public List<RequestWithUsernameAndNickname> findAllWithSenderUsernameAndUserUsername();
}
