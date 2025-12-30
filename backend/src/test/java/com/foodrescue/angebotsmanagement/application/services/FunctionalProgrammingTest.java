package com.foodrescue.angebotsmanagement.application.services;

import static com.foodrescue.angebotsmanagement.domain.functional.AngebotPredicates.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import com.foodrescue.abholungsmanagement.domain.model.AbholZeitfenster;
import com.foodrescue.angebotsmanagement.domain.model.Angebot;
import com.foodrescue.angebotsmanagement.domain.valueobjects.AngebotsId;
import com.foodrescue.angebotsmanagement.infrastructure.persistence.FunctionalAngebotRepository;
import com.foodrescue.userverwaltung.domain.valueobjects.UserId;
import java.time.LocalDateTime;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

/**
 * Tests für funktionale Programmierkonzepte in Übung 7.
 *
 * <p>Testet:
 *
 * <ul>
 *   <li>Composable Predicates
 *   <li>Functional Repository Operations
 *   <li>Collection Processing mit Streams
 * </ul>
 */
@DisplayName("Funktionale Programmierkonzepte - Tests")
class FunctionalProgrammingTest {

  private FunctionalAngebotRepository repository;
  private AngebotCollectionService collectionService;

  @BeforeEach
  void setUp() {
    ApplicationEventPublisher mockPublisher = mock(ApplicationEventPublisher.class);
    repository = new FunctionalAngebotRepository(mockPublisher);
    collectionService = new AngebotCollectionService(repository);

    // Testdaten erstellen
    erstelleTestdaten();
  }

  private void erstelleTestdaten() {
    // Angebot 1: Verfügbar, Bio, Heute
    Angebot angebot1 =
        Angebot.erstelle(
            AngebotsId.of("1"),
            new UserId(UUID.randomUUID()),
            "Bio-Brot",
            "Frisches Brot",
            new HashSet<>(Arrays.asList("Bio", "Brot")),
            new AbholZeitfenster(LocalDateTime.now(), LocalDateTime.now().plusHours(2)));
    angebot1.veroeffentlichen();

    // Angebot 2: Verfügbar, Vegan, Morgen
    Angebot angebot2 =
        Angebot.erstelle(
            AngebotsId.of("2"),
            new UserId(UUID.randomUUID()),
            "Vegane Suppe",
            "Gemüsesuppe",
            new HashSet<>(Arrays.asList("Vegan", "Suppe")),
            new AbholZeitfenster(
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(3)));
    angebot2.veroeffentlichen();

    // Angebot 3: Entwurf, Bio
    Angebot angebot3 =
        Angebot.erstelle(
            AngebotsId.of("3"),
            new UserId(UUID.randomUUID()),
            "Bio-Käse",
            "Käse",
            new HashSet<>(Arrays.asList("Bio", "Käse")),
            new AbholZeitfenster(LocalDateTime.now(), LocalDateTime.now().plusHours(1)));
    // Nicht veröffentlicht -> bleibt ENTWURF

    repository.speichern(angebot1);
    repository.speichern(angebot2);
    repository.speichern(angebot3);
  }

  // ========== Tests für Composable Predicates ==========

  @Test
  @DisplayName("Predicate: istVerfuegbar() filtert nur verfügbare Angebote")
  void testIstVerfuegbar() {
    List<Angebot> verfuegbare = repository.finde(istVerfuegbar());

    assertThat(verfuegbare).hasSize(2);
    assertThat(verfuegbare).allMatch(a -> a.getStatus() == Angebot.Status.VERFUEGBAR);
  }

  @Test
  @DisplayName("Predicate: hatTag() filtert nach spezifischem Tag")
  void testHatTag() {
    List<Angebot> bioAngebote = repository.finde(hatTag("Bio"));

    assertThat(bioAngebote).hasSize(2); // Angebot 1 und 3
    assertThat(bioAngebote).allMatch(a -> a.getTags().contains("Bio"));
  }

  @Test
  @DisplayName("Predicate Composition: AND-Verknüpfung funktioniert")
  void testPredicateAnd() {
    // Verfügbar UND hat Tag "Bio"
    List<Angebot> result = repository.finde(istVerfuegbar().and(hatTag("Bio")));

    assertThat(result).hasSize(1); // Nur Angebot 1
    assertThat(result.get(0).getTitel()).isEqualTo("Bio-Brot");
  }

  @Test
  @DisplayName("Predicate Composition: OR-Verknüpfung funktioniert")
  void testPredicateOr() {
    // Hat Tag "Vegan" ODER "Käse"
    List<Angebot> result = repository.finde(hatTag("Vegan").or(hatTag("Käse")));

    assertThat(result).hasSize(2); // Angebot 2 und 3
  }

  @Test
  @DisplayName("Predicate Composition: Komplexe Kombination")
  void testKomplexePredicate() {
    // (Verfügbar UND Bio) ODER Vegan
    List<Angebot> result = repository.finde(istVerfuegbar().and(hatTag("Bio")).or(hatTag("Vegan")));

    assertThat(result).hasSize(2); // Angebot 1 und 2
  }

  @Test
  @DisplayName("Predicate: negate() kehrt Bedingung um")
  void testPredicateNegate() {
    List<Angebot> nichtVerfuegbar = repository.finde(istVerfuegbar().negate());

    assertThat(nichtVerfuegbar).hasSize(1); // Nur Angebot 3
    assertThat(nichtVerfuegbar.get(0).getStatus()).isNotEqualTo(Angebot.Status.VERFUEGBAR);
  }

  // ========== Tests für Collection Processing ==========

  @Test
  @DisplayName("Gruppierung: groupByStatus() gruppiert korrekt")
  void testGroupByStatus() {
    Map<Angebot.Status, List<Angebot>> grouped = collectionService.groupByStatus();

    assertThat(grouped).containsKey(Angebot.Status.VERFUEGBAR);
    assertThat(grouped.get(Angebot.Status.VERFUEGBAR)).hasSize(2);
  }

  @Test
  @DisplayName("Aggregation: berechneStatistiken() liefert korrekte Zahlen")
  void testBerechneStatistiken() {
    var stats = collectionService.berechneStatistiken();

    assertThat(stats.total()).isEqualTo(2); // Nur verfügbare werden gezählt
    assertThat(stats.verfuegbar()).isEqualTo(2);
    assertThat(stats.reserviert()).isEqualTo(0);
  }

  @Test
  @DisplayName("Transformation: extractAllTags() liefert eindeutige Tags")
  void testExtractAllTags() {
    List<String> tags = collectionService.extractAllTags();

    assertThat(tags).containsExactlyInAnyOrder("Bio", "Brot", "Vegan", "Suppe");
    assertThat(tags).doesNotHaveDuplicates();
  }

  @Test
  @DisplayName("Transformation: toSummaryDTOs() mapped korrekt")
  void testToSummaryDTOs() {
    List<AngebotCollectionService.AngebotSummary> summaries = collectionService.toSummaryDTOs();

    assertThat(summaries).hasSize(2); // Nur verfügbare
    assertThat(summaries).allMatch(s -> s.status().equals("VERFUEGBAR"));
  }

  @Test
  @DisplayName("Filtering: findeMitKomplexenKriterien() kombiniert mehrere Filter")
  void testFindeMitKomplexenKriterien() {
    List<Angebot> result =
        collectionService.findeMitKomplexenKriterien(
            List.of("Bio", "Vegan"), // Mindestens eines dieser Tags
            LocalDateTime.now().minusHours(1), // Frühestens
            Optional.empty() // Kein Titel-Filter
            );

    assertThat(result).hasSize(2); // Beide verfügbaren Angebote matchen
  }

  @Test
  @DisplayName("Partitionierung: partitionByVerfuegbarkeit() teilt korrekt")
  void testPartitionByVerfuegbarkeit() {
    Map<Boolean, List<Angebot>> partitioned = collectionService.partitionByVerfuegbarkeit();

    assertThat(partitioned.get(true)).hasSize(2); // Verfügbar
    assertThat(partitioned.get(false)).isEmpty(); // Nicht verfügbar (in findeAlleVerfuegbar())
  }

  // ========== Tests für Repository-Operationen ==========

  @Test
  @DisplayName("Repository: zaehle() gibt korrekte Anzahl zurück")
  void testZaehle() {
    long anzahl = repository.zaehle(hatTag("Bio"));

    assertThat(anzahl).isEqualTo(2);
  }

  @Test
  @DisplayName("Repository: existiert() prüft korrekt")
  void testExistiert() {
    boolean existiert = repository.existiert(hatTag("Vegan"));

    assertThat(existiert).isTrue();
  }

  @Test
  @DisplayName("Repository: findeErstes() liefert Optional")
  void testFindeErstes() {
    Optional<Angebot> erstes = repository.findeErstes(istVerfuegbar());

    assertThat(erstes).isPresent();
    assertThat(erstes.get().getStatus()).isEqualTo(Angebot.Status.VERFUEGBAR);
  }

  @Test
  @DisplayName("Repository: findeUndMappiere() transformiert direkt")
  void testFindeUndMappiere() {
    List<String> titel =
        repository.findeUndMappiere(
            istVerfuegbar(), Angebot::getTitel // Method Reference!
            );

    assertThat(titel).containsExactlyInAnyOrder("Bio-Brot", "Vegane Suppe");
  }

  // ========== Tests für Method References ==========

  @Test
  @DisplayName("Method Reference: eventPublisher::publishEvent wird korrekt verwendet")
  void testMethodReferenceInEventPublishing() {
    // Dieser Test prüft implizit dass Event Publishing ohne Exception läuft
    Angebot neuesAngebot =
        Angebot.erstelle(
            AngebotsId.of("4"),
            new UserId(UUID.randomUUID()),
            "Test",
            "Test",
            new HashSet<>(Arrays.asList("Test")),
            new AbholZeitfenster(LocalDateTime.now(), LocalDateTime.now().plusHours(1)));
    neuesAngebot.veroeffentlichen();

    // Sollte keine Exception werfen (Events werden per Method Reference published)
    Angebot gespeichert = repository.speichern(neuesAngebot);

    assertThat(gespeichert).isNotNull();
    assertThat(gespeichert.getDomainEvents()).isEmpty(); // Events wurden gecleared
  }

  @Test
  @DisplayName("Immutability: Returned Lists sind unmodifiable")
  void testImmutability() {
    List<Angebot> result = repository.finde(istVerfuegbar());

    // Sollte UnsupportedOperationException werfen
    assertThat(result).isInstanceOf(List.class);
    assertThatThrownBy(() -> result.add(null)).isInstanceOf(UnsupportedOperationException.class);

    assertThatThrownBy(() -> result.clear()).isInstanceOf(UnsupportedOperationException.class);
  }
}
