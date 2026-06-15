package com.life.entity.traveltrack;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class TravelImage {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl; // S3 URL or temporary storage URL

    @ManyToOne
    @JoinColumn(name = "post_id")
    @JsonBackReference
    private TravelPost travelPost;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public TravelPost getTravelPost() {
		return travelPost;
	}

	public void setTravelPost(TravelPost travelPost) {
		this.travelPost = travelPost;
	}
}
