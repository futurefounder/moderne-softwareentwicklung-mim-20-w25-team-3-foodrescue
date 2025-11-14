package com.foodrescue.shared.domain;

public enum DomainError {
  ANGEBOT_BEREITS_VEROEFFENTLICHT("Angebot ist bereits veröffentlicht oder aktiv"),
  ANGEBOT_NICHT_VERFUEGBAR("Angebot ist nicht verfügbar"),
  RESERVIERUNG_NICHT_AKTIV("Reservierung ist nicht aktiv"),
  ABHOLCODE_FALSCH("Falscher Abholcode"),
  ZEITFENSTER_UNGUELTIG("Ungültiges Zeitfenster"),
  ENTITAET_UNVOLLSTAENDIG("Entität unvollständig"),
  ID_UNGUELTIG("Ungültige ID"),
  STATUSWECHSEL_UNGUELTIG("Statuswechsel nicht erlaubt");

  private final String defaultMessage;

  DomainError(String msg) {
    this.defaultMessage = msg;
  }

  public String defaultMessage() {
    return defaultMessage;
  }
}
