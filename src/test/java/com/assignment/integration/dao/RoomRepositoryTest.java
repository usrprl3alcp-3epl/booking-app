package com.assignment.integration.dao;

import com.assignment.dao.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

public class RoomRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private RoomRepository roomRepository;

    @Override
    public JpaRepository getRepository() {
        return roomRepository;
    }
}