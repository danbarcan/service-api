package com.dans.service.services;

import com.dans.service.entities.Job;
import com.dans.service.entities.Offer;
import com.dans.service.entities.User;
import com.dans.service.messaging.Publisher;
import com.dans.service.messaging.entities.Message;
import com.dans.service.messaging.entities.MessageType;
import com.dans.service.payloads.ApiResponse;
import com.dans.service.payloads.OfferPayload;
import com.dans.service.repositories.JobRepository;
import com.dans.service.repositories.OfferRepository;
import com.dans.service.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OfferService {

    private OfferRepository offerRepository;
    private JobRepository jobRepository;
    private UserRepository userRepository;
    private Publisher publisher;

    @Autowired
    public OfferService(final OfferRepository offerRepository, final JobRepository jobRepository, final UserRepository userRepository, final Publisher publisher) {
        this.offerRepository = offerRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.publisher = publisher;
    }

    public ResponseEntity<List<Offer>> saveOffer(OfferPayload offerPayload) {
        Optional<User> service = userRepository.findById(offerPayload.getServiceId());

        if (!service.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Optional<Job> job = jobRepository.findById(offerPayload.getJobId());
        if (!job.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Offer offer = Offer.createOfferFromPayload(offerPayload, service.get(), job.get());

        offerRepository.save(offer);

        publisher.produceMsg(createNewOfferMessage(offer));

        return getAllOffers();
    }

    public ResponseEntity<List<Offer>> updateOffer(OfferPayload offerPayload) {
        Optional<Offer> offerOptional = offerRepository.findById(offerPayload.getId());
        if (!offerOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Offer offer = offerOptional.get();
        offer.updateFieldsWithPayloadData(offerPayload);

        offerRepository.save(offer);

        return getAllOffers();
    }

    public ResponseEntity<ApiResponse> acceptOffer(Long offerId) {
        Optional<Offer> offerOptional = offerRepository.findById(offerId);
        if (!offerOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, "Offer not found"));
        }

        Offer offer = offerOptional.get();
        offer.setAccepted(true);
        offer.getJob().setAcceptedService(offer.getUser());

        offerRepository.save(offer);

        List<Offer> allOffersForJob = offerRepository.findAllByJob(offer.getJob());
        allOffersForJob.remove(offer);

        offerRepository.deleteAll(allOffersForJob);

        publisher.produceMsg(createAcceptedOfferMessage(offer));

        return ResponseEntity.ok(new ApiResponse(true, "Offer successfully accepted"));
    }

    public ResponseEntity<List<Offer>> deleteOffer(Long offerId) {
        Optional<Offer> offerOptional = offerRepository.findById(offerId);
        if (!offerOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Offer offer = offerOptional.get();
        offerRepository.delete(offer);

        return getAllOffers();
    }

    public ResponseEntity<List<Offer>> getAllOffers() {
        return ResponseEntity.ok(offerRepository.findAll());
    }

    public ResponseEntity<List<Offer>> getAllOffersByUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);

        if (!user.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.ok(offerRepository.findAllByUser(user.get()));
    }

    private Message createNewOfferMessage(Offer offer) {
        return Message.builder()
                .messageType(MessageType.NEW_OFFER)
                .offer(offer)
                .job(offer.getJob())
                .build();
    }

    private Message createAcceptedOfferMessage(Offer offer) {
        return Message.builder()
                .messageType(MessageType.ACCEPTED_OFFER)
                .offer(offer)
                .job(offer.getJob())
                .build();
    }
}
