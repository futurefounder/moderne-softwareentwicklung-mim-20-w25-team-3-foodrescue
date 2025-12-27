package com.foodrescue.abholungsmanagement.application.commands;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.foodrescue.abholungsmanagement.domain.model.Abholcode;
import com.foodrescue.reservierungsmanagement.domain.valueobjects.ReservierungsId;
import org.junit.jupiter.api.Test;

class BestaetigeAbholungCommandTest {

  @Test
  void getters_returnConstructorArguments() {
    ReservierungsId id = mock(ReservierungsId.class);
    Abholcode code = Abholcode.of("AB12");

    BestaetigeAbholungCommand cmd = new BestaetigeAbholungCommand(id, code);

    assertSame(id, cmd.getReservierungsId());
    assertSame(code, cmd.getCode());
  }
}
