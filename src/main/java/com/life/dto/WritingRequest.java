package com.life.dto;

import com.life.entity.notes.WritingType;

public class WritingRequest {

	private String title;

    private String content;

    private WritingType type;

    private String cardColor;

    private boolean favorite;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public WritingType getType() {
		return type;
	}

	public void setType(WritingType type) {
		this.type = type;
	}

	public String getCardColor() {
		return cardColor;
	}

	public void setCardColor(String cardColor) {
		this.cardColor = cardColor;
	}

	public boolean isFavorite() {
		return favorite;
	}

	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}
}
