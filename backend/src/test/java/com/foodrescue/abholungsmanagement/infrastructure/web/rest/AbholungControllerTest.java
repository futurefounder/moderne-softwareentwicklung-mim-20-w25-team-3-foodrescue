package com.foodrescue.abholungsmanagement.infrastructure.web.rest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.foodrescue.abholungsmanagement.application.commands.BestaetigeAbholungCommand;
import com.foodrescue.abholungsmanagement.application.services.AbholungApplicationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class AbholungControllerTest {

  @Mock AbholungApplicationService service;

  @InjectMocks AbholungController controller;

  @Test
  void bestaetigeAbholung_delegatesToService_andReturns200() {
    BestaetigeAbholungCommand cmd = mock(BestaetigeAbholungCommand.class);

    ResponseEntity<Void> response = controller.bestaetigeAbholung(cmd);

    verify(service).bestaetigeAbholung(cmd);
    assertEquals(200, response.getStatusCodeValue());
    assertNull(response.getBody());
  }
}
