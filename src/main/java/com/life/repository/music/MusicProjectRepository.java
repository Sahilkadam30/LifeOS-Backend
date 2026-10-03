package com.life.repository.music;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.life.entity.User;
import com.life.entity.music.MusicIdeaStatus;
import com.life.entity.music.MusicProject;

@Repository
public interface MusicProjectRepository extends JpaRepository<MusicProject, Long> {

    List<MusicProject> findByUserOrderByIdDesc(User user);

    Optional<MusicProject> findByIdAndUser(Long id, User user);

    long countByUser(User user);

    long countByUserAndStatus(User user, MusicIdeaStatus status);
}
