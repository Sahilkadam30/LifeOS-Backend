package com.life.service.chat;

import java.util.List;

import org.springframework.stereotype.Service;

import com.life.entity.studytracker.SkillProgress;
import com.life.repository.studytracker.SkillProgressRepository;

@Service
public class AIStudyMentorService {

    private final SkillProgressRepository skillRepository;
    
    public AIStudyMentorService(SkillProgressRepository skillRepository) {
    	this.skillRepository=skillRepository;
    }

    public String buildStudyContext(Long userId) {

        List<SkillProgress> skills =
                skillRepository.findByUserId(userId);

        StringBuilder sb = new StringBuilder();

        sb.append("Skill Progress:\n");

        for(SkillProgress skill : skills) {

            sb.append(
                    skill.getSkillName()
                    + " Progress="
                    + skill.getProgressPercentage()
                    + "%\n"
            );
        }

        return sb.toString();
    }
}
