package com.dans.service.repositories;

import com.dans.service.entities.ChatMessage;
import com.dans.service.entities.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "chat", path = "chat")
public interface ChatRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findAllByJob(Job job);

    List<ChatMessage> findAllByJobIn(List<Job> jobs);
}
