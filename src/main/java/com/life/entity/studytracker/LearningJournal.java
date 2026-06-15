package com.life.entity.studytracker;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="learning_journal")
public class LearningJournal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String title;

    @Column(length = 5000)
    private String WhatILearned;

    private LocalDate JournalDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getWhatILearned() {
		return WhatILearned;
	}

	public void setWhatILearned(String whatILearned) {
		WhatILearned = whatILearned;
	}

	public LocalDate getJournalDate() {
		return JournalDate;
	}

	public void setJournalDate(LocalDate journalDate) {
		JournalDate = journalDate;
	}
}
