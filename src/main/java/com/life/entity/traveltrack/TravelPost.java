package com.life.entity.traveltrack;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.life.entity.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class TravelPost {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String placeName;

    @Column(length = 2000)
    private String caption;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "travelPost", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<TravelImage> images;

    @OneToMany(mappedBy = "travelPost", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<TravelLike> likes;

    @OneToMany(mappedBy = "travelPost", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<TravelComment> comments;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<TravelImage> getImages() {
		return images;
	}

	public void setImages(List<TravelImage> images) {
		this.images = images;
	}

	public List<TravelLike> getLikes() {
		return likes;
	}

	public void setLikes(List<TravelLike> likes) {
		this.likes = likes;
	}

	public List<TravelComment> getComments() {
		return comments;
	}

	public void setComments(List<TravelComment> comments) {
		this.comments = comments;
	}
}
