package com.dans.service.services;

import com.dans.service.entities.ChatMessage;
import com.dans.service.entities.Job;
import com.dans.service.entities.User;
import com.dans.service.payloads.ChatMessagePayload;
import com.dans.service.repositories.ChatRepository;
import com.dans.service.repositories.JobRepository;
import com.dans.service.repositories.UserRepository;
import com.dans.service.security.UserPrincipal;
import com.dans.service.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatService {
    private UserRepository userRepository;
    private JobRepository jobRepository;
    private ChatRepository chatRepository;

    @Autowired
    public ChatService(final UserRepository userRepository, final ChatRepository chatRepository, final JobRepository jobRepository) {
        this.userRepository = userRepository;
        this.chatRepository = chatRepository;
        this.jobRepository = jobRepository;
    }

    public ResponseEntity<Map<Long, Long>> getUnreadMessagesByJob() {
        UserPrincipal userPrincipal = AppUtils.getCurrentUserDetails();
        if (userPrincipal == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Optional<User> userOptional = userRepository.findByUsername(userPrincipal.getUsername());
        if (!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Map<Long, List<ChatMessage>> messagesByJob = getMessagesByJob(userOptional.get());

        Map<Long, Long> unreadMessagesByJob = messagesByJob.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, o -> o.getValue().stream()
                        .filter(chatMessage -> !chatMessage.getFromUser().equals(userOptional.get()) && !chatMessage.getRead()).count()));

        return ResponseEntity.status(HttpStatus.OK).body(unreadMessagesByJob);
    }

    public ResponseEntity<Map<Long, List<ChatMessage>>> getMessagesGroupByJob() {
        UserPrincipal userPrincipal = AppUtils.getCurrentUserDetails();
        if (userPrincipal == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Optional<User> userOptional = userRepository.findByUsername(userPrincipal.getUsername());
        if (!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(getMessagesByJob(userOptional.get()));
    }

    public ResponseEntity<List<Job>> getAllJobsWithMessages() {
        UserPrincipal userPrincipal = AppUtils.getCurrentUserDetails();
        if (userPrincipal == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Optional<User> userOptional = userRepository.findByUsername(userPrincipal.getUsername());
        if (!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        User fromUser = userOptional.get();
        List<Job> jobs = jobRepository.findAllByUserOrAcceptedServiceOrderByTimestampDesc(fromUser, fromUser);

        return ResponseEntity.status(HttpStatus.OK).body(jobs);
    }

    public ResponseEntity<List<ChatMessage>> getMessagesByJobId(Long jobId) {
        UserPrincipal userPrincipal = AppUtils.getCurrentUserDetails();
        if (userPrincipal == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Optional<User> userOptional = userRepository.findByUsername(userPrincipal.getUsername());
        if (!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        User fromUser = userOptional.get();

        Optional<Job> jobOptional = jobRepository.findById(jobId);
        if (!jobOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Job job = jobOptional.get();
        if (!job.getUser().getId().equals(fromUser.getId()) && !job.getAcceptedService().getId().equals(fromUser.getId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<ChatMessage> chatMessages = chatRepository.findAllByJob(job);
        chatMessages.stream().filter(chatMessage -> !chatMessage.getFromUser().equals(fromUser)).forEach(chatMessage -> chatMessage.setRead(true));
        chatRepository.saveAll(chatMessages);

        return ResponseEntity.status(HttpStatus.OK).body(chatMessages);
    }

    public ResponseEntity<List<ChatMessage>> postMessage(ChatMessagePayload chatMessagePayload) {
        UserPrincipal userPrincipal = AppUtils.getCurrentUserDetails();
        if (userPrincipal == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Optional<User> userOptional = userRepository.findByUsername(userPrincipal.getUsername());
        if (!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        User fromUser = userOptional.get();

        Optional<Job> jobOptional = jobRepository.findById(chatMessagePayload.getJobId());
        if (!jobOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Job job = jobOptional.get();

        if (!job.getUser().getId().equals(fromUser.getId()) && !job.getAcceptedService().getId().equals(fromUser.getId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        ChatMessage message = ChatMessage.builder()
                .message(chatMessagePayload.getMessage())
                .fromUser(fromUser)
                .job(job)
                .timestamp(Timestamp.from(Instant.now()))
                .read(false)
                .build();

        chatRepository.save(message);

        List<ChatMessage> chatMessages = chatRepository.findAllByJob(job);
        chatMessages.stream().filter(chatMessage -> !chatMessage.getFromUser().equals(fromUser)).forEach(chatMessage -> chatMessage.setRead(true));
        chatRepository.saveAll(chatMessages);

        return ResponseEntity.status(HttpStatus.OK).body(chatMessages);
    }

    private Map<Long, List<ChatMessage>> getMessagesByJob(User user) {
        List<Job> jobs = jobRepository.findAllByUserOrAcceptedServiceOrderByTimestampDesc(user, user);

        List<ChatMessage> chatMessages = chatRepository.findAllByJobIn(jobs);
        return chatMessages.stream().collect(Collectors.groupingBy(message -> message.getJob().getId()));
    }
}
