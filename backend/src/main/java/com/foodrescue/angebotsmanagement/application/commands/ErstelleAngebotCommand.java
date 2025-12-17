package com.foodrescue.angebotsmanagement.application.commands;

import java.util.Set;

public class ErstelleAngebotCommand {

    private String anbieterId;     // UUID als String
    private String titel;
    private String beschreibung;
    private Set<String> tags;
    private String von;            // ISO-String "2025-12-14T10:00"
    private String bis;

    public ErstelleAngebotCommand() {}

    public String getAnbieterId() { return anbieterId; }
    public void setAnbieterId(String anbieterId) { this.anbieterId = anbieterId; }

    public String getTitel() { return titel; }
    public void setTitel(String titel) { this.titel = titel; }

    public String getBeschreibung() { return beschreibung; }
    public void setBeschreibung(String beschreibung) { this.beschreibung = beschreibung; }

    public Set<String> getTags() { return tags; }
    public void setTags(Set<String> tags) { this.tags = tags; }

    public String getVon() { return von; }
    public void setVon(String von) { this.von = von; }

    public String getBis() { return bis; }
    public void setBis(String bis) { this.bis = bis; }
}
