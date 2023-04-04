package com.exception.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exception.demo.entity.TutorialEntity;

public interface TutorialRepo extends JpaRepository<TutorialEntity, Integer> {

}
