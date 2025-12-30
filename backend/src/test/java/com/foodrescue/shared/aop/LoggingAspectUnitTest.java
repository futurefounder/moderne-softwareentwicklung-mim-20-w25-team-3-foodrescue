package com.foodrescue.shared.aop;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit Tests für den LoggingAspect.
 *
 * <p>Fokus: Verifizierung dass die Aspect-Methoden korrekt ausgeführt werden ohne Exceptions zu
 * werfen. Das tatsächliche Logging wird nicht getestet.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("LoggingAspect Unit Tests")
class LoggingAspectUnitTest {

  private LoggingAspect loggingAspect;

  @Mock private ProceedingJoinPoint proceedingJoinPoint;
  @Mock private JoinPoint joinPoint;
  @Mock private Signature signature;

  @BeforeEach
  void setUp() {
    loggingAspect = new LoggingAspect();

    // Lenient stubbing für Signature-Methoden (werden mehrfach aufgerufen)
    lenient().when(signature.getDeclaringType()).thenReturn((Class) TestService.class);
    lenient().when(signature.getName()).thenReturn("testMethod");
    lenient().when(proceedingJoinPoint.getSignature()).thenReturn(signature);
    lenient().when(joinPoint.getSignature()).thenReturn(signature);
  }

  // ========== Around Advice Tests ==========

  @Test
  @DisplayName("Should execute method and return result")
  void shouldExecuteMethodAndReturnResult() throws Throwable {
    // Arrange
    when(proceedingJoinPoint.getArgs()).thenReturn(new Object[] {"param1"});
    when(proceedingJoinPoint.proceed()).thenReturn("result");

    // Act
    Object result = loggingAspect.logMethodExecutionWithPerformance(proceedingJoinPoint);

    // Assert
    assertThat(result).isEqualTo("result");
    verify(proceedingJoinPoint).proceed();
  }

  @Test
  @DisplayName("Should handle null parameters")
  void shouldHandleNullParameters() throws Throwable {
    // Arrange
    when(proceedingJoinPoint.getArgs()).thenReturn(new Object[] {null});
    when(proceedingJoinPoint.proceed()).thenReturn("result");

    // Act
    Object result = loggingAspect.logMethodExecutionWithPerformance(proceedingJoinPoint);

    // Assert
    assertThat(result).isEqualTo("result");
  }

  @Test
  @DisplayName("Should handle empty parameter array")
  void shouldHandleEmptyParameterArray() throws Throwable {
    // Arrange
    when(proceedingJoinPoint.getArgs()).thenReturn(new Object[] {});
    when(proceedingJoinPoint.proceed()).thenReturn("result");

    // Act
    Object result = loggingAspect.logMethodExecutionWithPerformance(proceedingJoinPoint);

    // Assert
    assertThat(result).isEqualTo("result");
  }

  @Test
  @DisplayName("Should propagate exceptions")
  void shouldPropagateExceptions() throws Throwable {
    // Arrange
    when(proceedingJoinPoint.getArgs()).thenReturn(new Object[] {"test"});
    RuntimeException exception = new RuntimeException("Test exception");
    when(proceedingJoinPoint.proceed()).thenThrow(exception);

    // Act & Assert
    assertThatThrownBy(() -> loggingAspect.logMethodExecutionWithPerformance(proceedingJoinPoint))
        .isEqualTo(exception);
  }

  @Test
  @DisplayName("Should handle long strings")
  void shouldHandleLongStrings() throws Throwable {
    // Arrange
    String longString = "A".repeat(150);
    when(proceedingJoinPoint.getArgs()).thenReturn(new Object[] {longString});
    when(proceedingJoinPoint.proceed()).thenReturn("result");

    // Act
    Object result = loggingAspect.logMethodExecutionWithPerformance(proceedingJoinPoint);

    // Assert
    assertThat(result).isEqualTo("result");
  }

  // ========== Before/AfterReturning Advice Tests ==========

  @Test
  @DisplayName("Should execute controller entry advice")
  void shouldExecuteControllerEntryAdvice() {
    // Arrange
    when(joinPoint.getArgs()).thenReturn(new Object[] {});

    // Act - Should not throw
    loggingAspect.logControllerEntry(joinPoint);

    // No assertion needed - just verify no exception
  }

  @Test
  @DisplayName("Should execute controller exit advice")
  void shouldExecuteControllerExitAdvice() {
    // Act - Should not throw
    loggingAspect.logControllerExit(joinPoint, "response");

    // No assertion needed - just verify no exception
  }

  // ========== AfterThrowing Advice Tests ==========

  @Test
  @DisplayName("Should execute exception advice")
  void shouldExecuteExceptionAdvice() {
    // Arrange
    when(joinPoint.getArgs()).thenReturn(new Object[] {"bad-input"});
    Exception exception = new IllegalArgumentException("Invalid");

    // Act - Should not throw
    loggingAspect.logException(joinPoint, exception);

    // No assertion needed - just verify no exception
  }

  // ========== Event Handler Tests ==========

  @Test
  @DisplayName("Should execute event handler advice")
  void shouldExecuteEventHandlerAdvice() {
    // Arrange
    TestEvent event = new TestEvent("data");
    when(joinPoint.getArgs()).thenReturn(new Object[] {event});

    // Act - Should not throw
    loggingAspect.logEventHandling(joinPoint);

    // No assertion needed - just verify no exception
  }

  @Test
  @DisplayName("Should handle null event")
  void shouldHandleNullEvent() {
    // Arrange
    when(joinPoint.getArgs()).thenReturn(new Object[] {null});

    // Act - Should not throw
    loggingAspect.logEventHandling(joinPoint);

    // No assertion needed - just verify no exception
  }

  // ========== Helper Classes ==========

  static class TestService {
    public String testMethod(String input) {
      return "processed";
    }
  }

  static class TestEvent {
    private final String data;

    public TestEvent(String data) {
      this.data = data;
    }

    public String getData() {
      return data;
    }
  }
}
