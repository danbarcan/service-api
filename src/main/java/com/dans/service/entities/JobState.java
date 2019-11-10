package com.dans.service.entities;

public enum JobState {
    AVAILABLE, UNAVAILABLE, HIDDEN, OFFERED, ACCEPTED, COMPLETED;

    public static JobState getState(Job job, User user) {
        if (job.getAcceptedService() != null) {
            if (job.getReview() != null) {
                return COMPLETED;
            } else if (user.equals(job.getAcceptedService())) {
                return ACCEPTED;
            } else {
                return UNAVAILABLE;
            }
        } else {
            if (job.getOffers() != null && job.getOffers().stream().anyMatch(offer -> user.equals(offer.getUser()))) {
                return OFFERED;
            } else if (job.getHiddenForUsers() != null && job.getHiddenForUsers().contains(user)) {
                return HIDDEN;
            }
        }
        return AVAILABLE;
    }
}
