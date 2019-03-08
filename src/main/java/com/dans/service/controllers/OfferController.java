package com.dans.service.controllers;

import com.dans.service.entities.Offer;
import com.dans.service.payloads.ApiResponse;
import com.dans.service.payloads.OfferPayload;
import com.dans.service.services.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class OfferController {

    private OfferService offerService;

    @Autowired
    public OfferController(final OfferService offerService) {
        this.offerService = offerService;
    }

    @PostMapping("/services/offer")
    @PreAuthorize("hasRole('ROLE_SERVICE')")
    public ResponseEntity<ApiResponse> saveOffer(@Valid @RequestBody OfferPayload offerPayload) {
        return offerService.saveOffer(offerPayload);
    }

    @PostMapping("/services/updateOffer")
    @PreAuthorize("hasRole('ROLE_SERVICE')")
    public ResponseEntity<ApiResponse> updateOffer(@Valid @RequestBody OfferPayload offerPayload) {
        return offerService.updateOffer(offerPayload);
    }

    @GetMapping("/services/deleteOffer")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse> deleteOffer(@RequestParam Long offerId) {
        return offerService.deleteOffer(offerId);
    }

    @GetMapping("/users/acceptOffer")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse> acceptOffer(@RequestParam Long offerId) {
        return offerService.acceptOffer(offerId);
    }

    @GetMapping("/users/allOffers")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<Offer>> getAllJobs() {
        return offerService.getAllOffers();
    }

    @GetMapping("/users/offers")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<Offer>> getAllJobs(@RequestParam Long userId) {
        return offerService.getAllOffersByUser(userId);
    }
}
