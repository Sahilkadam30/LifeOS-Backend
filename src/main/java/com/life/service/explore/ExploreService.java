package com.life.service.explore;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.life.dto.explore.ExploreCommentDTO;
import com.life.dto.explore.ExploreFeedItemDTO;
import com.life.entity.User;
import com.life.entity.artzone.ArtPost;
import com.life.entity.artzone.Comment;
import com.life.entity.artzone.PostLike;
import com.life.entity.notes.ThoughtComment;
import com.life.entity.notes.ThoughtLike;
import com.life.entity.notes.Writing;
import com.life.entity.notes.WritingType;
import com.life.repository.UserRepository;
import com.life.repository.artzone.ArtPostRepository;
import com.life.repository.artzone.CommentRepository;
import com.life.repository.artzone.PostLikeRepository;
import com.life.repository.notes.ThoughtCommentRepository;
import com.life.repository.notes.ThoughtLikeRepository;
import com.life.repository.notes.WritingRepository;

@Service
public class ExploreService {

    private final ArtPostRepository artPostRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;

    private final WritingRepository writingRepository;
    private final ThoughtLikeRepository thoughtLikeRepository;
    private final ThoughtCommentRepository thoughtCommentRepository;

    private final UserRepository userRepository;

    public ExploreService(
            ArtPostRepository artPostRepository,
            PostLikeRepository postLikeRepository,
            CommentRepository commentRepository,
            WritingRepository writingRepository,
            ThoughtLikeRepository thoughtLikeRepository,
            ThoughtCommentRepository thoughtCommentRepository,
            UserRepository userRepository
    ) {
        this.artPostRepository = artPostRepository;
        this.postLikeRepository = postLikeRepository;
        this.commentRepository = commentRepository;
        this.writingRepository = writingRepository;
        this.thoughtLikeRepository = thoughtLikeRepository;
        this.thoughtCommentRepository = thoughtCommentRepository;
        this.userRepository = userRepository;
    }

    public List<ExploreFeedItemDTO> getFeed(String currentUsername, String filterType) {
        List<ExploreFeedItemDTO> feed = new ArrayList<>();
        String type = (filterType == null || filterType.isBlank()) ? "ALL" : filterType.toUpperCase();

        // 1. Fetch Art posts (public only)
        if ("ALL".equals(type) || "ART".equals(type)) {
            List<ArtPost> artPosts = artPostRepository.findByIsPublicTrueOrderByCreatedAtDesc();
            for (ArtPost art : artPosts) {
                ExploreFeedItemDTO item = new ExploreFeedItemDTO();
                item.setId(art.getId());
                item.setItemType("ART");
                item.setTitle(art.getCaption() != null && !art.getCaption().isBlank() ? art.getCaption() : "Artwork");
                item.setContent(art.getCaption());
                if (art.getImage() != null) {
                    item.setImageBase64(Base64.getEncoder().encodeToString(art.getImage()));
                }
                item.setUsername(art.getUsername());
                item.setAuthorFullName(resolveFullName(art.getUsername()));
                item.setCreatedAt(art.getCreatedAt());

                long likes = postLikeRepository.countByPostId(art.getId());
                item.setLikesCount(likes);
                item.setLiked(currentUsername != null && postLikeRepository.existsByPostIdAndUsername(art.getId(), currentUsername));

                List<Comment> artComments = commentRepository.findByPostId(art.getId());
                List<ExploreCommentDTO> commentDTOs = artComments.stream()
                        .map(c -> new ExploreCommentDTO(c.getId(), c.getUsername(), c.getText(), c.getCreatedAt()))
                        .collect(Collectors.toList());
                item.setComments(commentDTOs);
                item.setCommentsCount(commentDTOs.size());

                feed.add(item);
            }
        }

        // 2. Fetch Thought posts (Writing of type POST)
        if ("ALL".equals(type) || "POST".equals(type)) {
            List<Writing> thoughtPosts = writingRepository.findByTypeOrderByCreatedAtDesc(WritingType.POST);
            for (Writing w : thoughtPosts) {
                ExploreFeedItemDTO item = new ExploreFeedItemDTO();
                item.setId(w.getId());
                item.setItemType("POST");
                item.setTitle(w.getTitle() != null ? w.getTitle() : "Thought Post");
                item.setContent(w.getContent());
                item.setCardColor(w.getCardColor() != null ? w.getCardColor() : "#2563EB");

                String authorUsername = w.getUsername();
                if (authorUsername == null && w.getUserId() != null) {
                    authorUsername = userRepository.findById(w.getUserId())
                            .map(User::getUsername)
                            .orElse("User");
                }
                item.setUsername(authorUsername != null ? authorUsername : "Anonymous");
                item.setAuthorFullName(resolveFullName(authorUsername));
                item.setCreatedAt(w.getCreatedAt());

                long likes = thoughtLikeRepository.countByWritingId(w.getId());
                item.setLikesCount(likes);
                item.setLiked(currentUsername != null && thoughtLikeRepository.existsByWritingIdAndUsername(w.getId(), currentUsername));

                List<ThoughtComment> comments = thoughtCommentRepository.findByWritingIdOrderByCreatedAtAsc(w.getId());
                List<ExploreCommentDTO> commentDTOs = comments.stream()
                        .map(c -> new ExploreCommentDTO(c.getId(), c.getUsername(), c.getText(), c.getCreatedAt()))
                        .collect(Collectors.toList());
                item.setComments(commentDTOs);
                item.setCommentsCount(commentDTOs.size());

                feed.add(item);
            }
        }

        // Sort descending by createdAt
        feed.sort(Comparator.comparing(
                ExploreFeedItemDTO::getCreatedAt,
                Comparator.nullsLast(Comparator.reverseOrder())
        ));

        return feed;
    }

    private String resolveFullName(String username) {
        if (username == null || username.isBlank()) return null;
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String fName = user.getFirstName() != null ? user.getFirstName() : "";
            String lName = user.getLastName() != null ? user.getLastName() : "";
            String full = (fName + " " + lName).trim();
            return full.isEmpty() ? username : full;
        }
        return username;
    }

    // ================= THOUGHT POST LIKE / COMMENT =================

    @Transactional
    public int toggleThoughtLike(Long writingId, String username) {
        Optional<ThoughtLike> existing = thoughtLikeRepository.findByWritingIdAndUsername(writingId, username);
        if (existing.isPresent()) {
            thoughtLikeRepository.delete(existing.get());
        } else {
            ThoughtLike like = new ThoughtLike(writingId, username);
            thoughtLikeRepository.save(like);
        }
        return (int) thoughtLikeRepository.countByWritingId(writingId);
    }

    @Transactional
    public ExploreCommentDTO addThoughtComment(Long writingId, String username, String text) {
        ThoughtComment comment = new ThoughtComment(writingId, username, text);
        ThoughtComment saved = thoughtCommentRepository.save(comment);
        return new ExploreCommentDTO(saved.getId(), saved.getUsername(), saved.getText(), saved.getCreatedAt());
    }

    public List<ExploreCommentDTO> getThoughtComments(Long writingId) {
        return thoughtCommentRepository.findByWritingIdOrderByCreatedAtAsc(writingId).stream()
                .map(c -> new ExploreCommentDTO(c.getId(), c.getUsername(), c.getText(), c.getCreatedAt()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteThoughtPost(Long writingId, String username) {
        Writing writing = writingRepository.findById(writingId)
                .orElseThrow(() -> new RuntimeException("Writing post not found"));

        boolean isAuthor = (writing.getUsername() != null && writing.getUsername().equalsIgnoreCase(username));
        if (!isAuthor && writing.getUserId() != null) {
            Optional<User> userOpt = userRepository.findByUsername(username);
            if (userOpt.isPresent() && userOpt.get().getId().equals(writing.getUserId())) {
                isAuthor = true;
            }
        }

        if (!isAuthor) {
            throw new RuntimeException("Unauthorized: you can only delete your own posts.");
        }

        thoughtCommentRepository.deleteByWritingId(writingId);
        writingRepository.delete(writing);
    }

    // ================= ART POST LIKE / COMMENT =================

    @Transactional
    public int toggleArtLike(Long postId, String username) {
        Optional<PostLike> existing = postLikeRepository.findByPostIdAndUsername(postId, username);
        if (existing.isPresent()) {
            postLikeRepository.delete(existing.get());
        } else {
            PostLike like = new PostLike();
            like.setPostId(postId);
            like.setUsername(username);
            postLikeRepository.save(like);
        }
        return (int) postLikeRepository.countByPostId(postId);
    }

    @Transactional
    public ExploreCommentDTO addArtComment(Long postId, String username, String text) {
        Comment c = new Comment();
        c.setPostId(postId);
        c.setUsername(username);
        c.setText(text);
        c.setCreatedAt(LocalDateTime.now());
        Comment saved = commentRepository.save(c);
        return new ExploreCommentDTO(saved.getId(), saved.getUsername(), saved.getText(), saved.getCreatedAt());
    }

    public List<ExploreCommentDTO> getArtComments(Long postId) {
        return commentRepository.findByPostId(postId).stream()
                .map(c -> new ExploreCommentDTO(c.getId(), c.getUsername(), c.getText(), c.getCreatedAt()))
                .collect(Collectors.toList());
    }
}
