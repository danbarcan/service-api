package com.dans.service.services;

import com.dans.service.entities.Job;
import com.dans.service.entities.Offer;
import com.dans.service.entities.User;
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

    @Autowired
    public OfferService(final OfferRepository offerRepository, final JobRepository jobRepository, final UserRepository userRepository) {
        this.offerRepository = offerRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<ApiResponse> saveOffer(OfferPayload offerPayload) {
        Optional<User> service = userRepository.findById(offerPayload.getServiceId());

        if (!service.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "UNAUTHORIZED"));
        }

        Optional<Job> job = jobRepository.findById(offerPayload.getJobId());
        if (!job.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, "Job not found"));
        }

        Offer offer = Offer.createOfferFromPayload(offerPayload, service.get(), job.get());

        offerRepository.save(offer);

        return ResponseEntity.ok(new ApiResponse(true, "Offer successfully saved"));
    }

    public ResponseEntity<ApiResponse> updateOffer(OfferPayload offerPayload) {
        Optional<Offer> offerOptional = offerRepository.findById(offerPayload.getId());
        if (!offerOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, "Offer not found"));
        }

        Offer offer = offerOptional.get();
        offer.updateFieldsWithPayloadData(offerPayload);

        offerRepository.save(offer);

        return ResponseEntity.ok(new ApiResponse(true, "Offer successfully updated"));
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

        return ResponseEntity.ok(new ApiResponse(true, "Offer successfully accepted"));
    }

    public ResponseEntity<ApiResponse> deleteOffer(Long offerId) {
        Optional<Offer> offerOptional = offerRepository.findById(offerId);
        if (!offerOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, "Offer not found"));
        }

        Offer offer = offerOptional.get();
        offerRepository.delete(offer);

        return ResponseEntity.ok(new ApiResponse(true, "Offer successfully deleted"));
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
}
