package com.life.service.traveltrack;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.life.entity.User;
import com.life.entity.traveltrack.TravelComment;
import com.life.entity.traveltrack.TravelImage;
import com.life.entity.traveltrack.TravelLike;
import com.life.entity.traveltrack.TravelPost;
import com.life.repository.UserRepository;
import com.life.repository.traveltrack.TravelCommentRepository;
import com.life.repository.traveltrack.TravelLikeRepository;
import com.life.repository.traveltrack.TravelPostRepository;

@Service
public class TravelService {

	@Autowired
    private TravelPostRepository repo;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TravelLikeRepository likeRepo;
	
	@Autowired
	private TravelCommentRepository commentRepo;

	public TravelPost createPost(String placeName, String caption, User user, List<String> imageUrls) {

	    TravelPost post = new TravelPost();
	    post.setPlaceName(placeName);
	    post.setCaption(caption);
	    post.setUser(user);
	    post.setCreatedAt(LocalDateTime.now());

	    List<TravelImage> images = imageUrls.stream().map(url -> {
	        TravelImage img = new TravelImage();
	        img.setImageUrl(url);
	        img.setTravelPost(post);
	        return img;
	    }).toList();

	    post.setImages(images);

	    return repo.save(post);
	}
	
	public long likePost(Long postId, User user) {

	    if (likeRepo.existsByUser_IdAndTravelPost_Id(user.getId(), postId)) {
	        return likeRepo.countByTravelPost_Id(postId); // already liked
	    }

	    TravelPost post = repo.findById(postId).orElseThrow();

	    TravelLike like = new TravelLike();
	    like.setUser(user);
	    like.setTravelPost(post);

	    likeRepo.save(like);

	    return likeRepo.countByTravelPost_Id(postId);
	}
	
	public TravelComment addComment(Long postId, String text, User user) {

	    TravelPost post = repo.findById(postId).orElseThrow();

	    TravelComment comment = new TravelComment();
	    comment.setText(text);
	    comment.setUser(user);
	    comment.setTravelPost(post);
	    comment.setCreatedAt(LocalDateTime.now());

	    return commentRepo.save(comment);
	}
	
	public void deletePost(Long postId, User user) {

	    TravelPost post = repo.findById(postId).orElseThrow();

	    if (!post.getUser().getId().equals(user.getId())) {
	        throw new RuntimeException("Not authorized");
	    }

	    repo.delete(post);
	}
	
	public User getUserByUsername(String username) {
	    return userRepository.findByUsername(username)
	            .orElseThrow(() -> new RuntimeException("User not found"));
	}

    public List<TravelPost> getUserPosts(Long userId) {
        return repo.findByUserIdOrderByCreatedAtDesc(userId);
    }
    
//    private final String UPLOAD_DIR = "uploads/";
}
