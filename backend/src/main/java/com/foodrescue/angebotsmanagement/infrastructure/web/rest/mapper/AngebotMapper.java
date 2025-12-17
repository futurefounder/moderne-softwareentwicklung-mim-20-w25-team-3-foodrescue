package com.foodrescue.angebotsmanagement.infrastructure.web.rest.mapper;

import com.foodrescue.abholungsmanagement.domain.model.AbholZeitfenster;
import com.foodrescue.angebotsmanagement.domain.model.Angebot;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AngebotMapper {

    // ---------- Response DTOs ----------

    public record AngebotResponse(
            String id,
            String anbieterId,
            String titel,
            String beschreibung,
            Set<String> tags,
            String status,
            ZeitfensterDto zeitfenster
    ) {}

    public record ZeitfensterDto(String von, String bis) {}

    public record CreateAngebotResponse(String id) {}

    // ---------- Request DTOs ----------

    public static class ErstelleAngebotRequest {
        private String anbieterId;
        private String titel;
        private String beschreibung;
        private Set<String> tags;
        private Zeitfenster zeitfenster;

        public String getAnbieterId() { return anbieterId; }
        public void setAnbieterId(String anbieterId) { this.anbieterId = anbieterId; }

        public String getTitel() { return titel; }
        public void setTitel(String titel) { this.titel = titel; }

        public String getBeschreibung() { return beschreibung; }
        public void setBeschreibung(String beschreibung) { this.beschreibung = beschreibung; }

        public Set<String> getTags() { return tags; }
        public void setTags(Set<String> tags) { this.tags = tags; }

        public Zeitfenster getZeitfenster() { return zeitfenster; }
        public void setZeitfenster(Zeitfenster zeitfenster) { this.zeitfenster = zeitfenster; }
    }

    public static class Zeitfenster {
        private String von;
        private String bis;

        public String getVon() { return von; }
        public void setVon(String von) { this.von = von; }

        public String getBis() { return bis; }
        public void setBis(String bis) { this.bis = bis; }
    }

    // ---------- Mapping Domain -> DTO ----------

    public AngebotResponse toResponse(Angebot angebot) {
        AbholZeitfenster z = angebot.getZeitfenster();

        String von = extractZeitfensterPart(z, "getVon", "von");
        String bis = extractZeitfensterPart(z, "getBis", "bis");

        return new AngebotResponse(
                angebot.getId(),
                angebot.getAnbieterId(),
                angebot.getTitel(),
                angebot.getBeschreibung(),
                angebot.getTags(),
                angebot.getStatus().name(),
                new ZeitfensterDto(von, bis)
        );
    }

    public List<AngebotResponse> toResponseList(List<Angebot> angebote) {
        return angebote.stream().map(this::toResponse).collect(Collectors.toList());
    }

    // ---------- Helpers (robust für record/klassisch) ----------

    private String extractZeitfensterPart(AbholZeitfenster z, String getterName, String recordAccessorName) {
        if (z == null) return null;

        Object value = tryInvoke(z, getterName);
        if (value == null) value = tryInvoke(z, recordAccessorName);

        return value != null ? value.toString() : null;
    }

    private Object tryInvoke(Object target, String methodName) {
        try {
            Method m = target.getClass().getMethod(methodName);
            return m.invoke(target);
        } catch (Exception ignored) {
            return null;
        }
    }
}

