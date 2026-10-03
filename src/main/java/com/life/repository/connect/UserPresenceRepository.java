package com.life.repository.connect;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.life.entity.connect.UserPresence;

@Repository
public interface UserPresenceRepository extends JpaRepository<UserPresence, Long> {
	
}
