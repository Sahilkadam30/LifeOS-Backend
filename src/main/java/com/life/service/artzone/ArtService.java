package com.life.service.artzone;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.life.entity.artzone.ArtPost;
import com.life.entity.artzone.Comment;
import com.life.entity.artzone.PostLike;
import com.life.repository.artzone.ArtPostRepository;
import com.life.repository.artzone.CommentRepository;
import com.life.repository.artzone.PostLikeRepository;

@Service
public class ArtService {

	@Autowired
    private ArtPostRepository repo;

    @Autowired
    private CommentRepository commentRepo;
    
    @Autowired
    private PostLikeRepository likeRepo;

    public ArtPost createPost(String username, MultipartFile file, String caption) throws Exception {
        ArtPost post = new ArtPost();
        post.setUsername(username);
        post.setCaption(caption);
        post.setImage(file.getBytes());
        post.setCreatedAt(LocalDateTime.now());
        post.setLikes(0);
        return repo.save(post);
    }

    public List<ArtPost> getPosts(String username) {
        return repo.findByUsernameOrderByCreatedAtDesc(username);
    }

    public void likePost(Long postId) {
        ArtPost post = repo.findById(postId).orElseThrow();
        post.setLikes(post.getLikes() + 1);
        repo.save(post);
    }

    public Comment addComment(Long postId, String username, String text) {
        Comment c = new Comment();
        c.setPostId(postId);
        c.setUsername(username);
        c.setText(text);
        c.setCreatedAt(LocalDateTime.now());
        return commentRepo.save(c);
    }

    public List<Comment> getComments(Long postId) {
        return commentRepo.findByPostId(postId);
    }
    
    public int likePost(Long postId, String username) {
        if (!likeRepo.existsByPostIdAndUsername(postId, username)) {
            PostLike like = new PostLike();
            like.setPostId(postId);
            like.setUsername(username);
            likeRepo.save(like);
        }
        return (int) likeRepo.countByPostId(postId);
    }
}
