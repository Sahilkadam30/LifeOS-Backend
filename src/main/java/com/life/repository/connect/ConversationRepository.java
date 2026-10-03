package com.life.repository.connect;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.life.entity.User;
import com.life.entity.connect.Conversation;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long>{

	 Optional<Conversation> findByUserOneAndUserTwo(
	            User userOne,
	            User userTwo
	    );

	    List<Conversation> findByUserOneOrUserTwoOrderByUpdatedAtDesc(
	            User userOne,
	            User userTwo
	    );
}
