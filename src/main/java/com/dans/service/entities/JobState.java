package com.dans.service.entities;

public enum JobState {
    AVAILABLE, UNAVAILABLE, HIDDEN, OFFERED, ACCEPTED;

    public static JobState getState(Job job, User user) {
        if (job.getAcceptedService() != null) {
            if (user.equals(job.getAcceptedService())) {
                return ACCEPTED;
            } else {
                return UNAVAILABLE;
            }
        } else {
            if (job.getOffers().stream().filter(offer -> user.equals(offer.getUser())).findAny().isPresent()) {
                return OFFERED;
            } else if (job.getHiddenForUsers().contains(user)) {
                return HIDDEN;
            }
        }
        return AVAILABLE;
    }
}
