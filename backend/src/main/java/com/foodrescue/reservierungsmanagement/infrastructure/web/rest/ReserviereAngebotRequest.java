package com.foodrescue.reservierungsmanagement.infrastructure.web.rest;

import java.util.UUID;

public class ReserviereAngebotRequest {
    private String angebotId;
    private UUID abholerId;

    public ReserviereAngebotRequest() {}

    public String getAngebotId() {
        return angebotId;
    }

    public void setAngebotId(String angebotId) {
        this.angebotId = angebotId;
    }

    public UUID getAbholerId() {
        return abholerId;
    }

    public void setAbholerId(UUID abholerId) {
        this.abholerId = abholerId;
    }
}
