package com.dans.service.controllers;

import com.dans.service.entities.ChatMessage;
import com.dans.service.entities.Job;
import com.dans.service.payloads.ChatMessagePayload;
import com.dans.service.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
public class ChatController {

    private ChatService chatService;

    @Autowired
    public ChatController(final ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/chat/postMessage")
    @PreAuthorize("hasRole('USER') OR hasRole('ADMIN') OR hasRole('SERVICE')")
    public ResponseEntity<List<ChatMessage>> postMessage(@Valid @RequestBody ChatMessagePayload chatMessagePayload) {
        return chatService.postMessage(chatMessagePayload);
    }

    @GetMapping("/chat/getMessagesByJobId")
    @PreAuthorize("hasRole('USER') OR hasRole('ADMIN') OR hasRole('SERVICE')")
    public ResponseEntity<List<ChatMessage>> getMessagesByJobId(@RequestParam(value = "jobId") Long jobId) {
        return chatService.getMessagesByJobId(jobId);
    }

    @GetMapping("/chat/getUnreadMessages")
    @PreAuthorize("hasRole('USER') OR hasRole('ADMIN') OR hasRole('SERVICE')")
    public ResponseEntity<Map<Job, Long>> getUnreadMessagesByJob() {
        return chatService.getUnreadMessagesByJob();
    }

    @GetMapping("/chat/getAllMessages")
    @PreAuthorize("hasRole('USER') OR hasRole('ADMIN') OR hasRole('SERVICE')")
    public ResponseEntity<Map<Job, List<ChatMessage>>> getMessagesGroupByJob() {
        return chatService.getMessagesGroupByJob();
    }

}
