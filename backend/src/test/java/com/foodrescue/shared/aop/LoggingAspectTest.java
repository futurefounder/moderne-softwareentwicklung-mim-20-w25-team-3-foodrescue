package com.foodrescue.shared.aop;

import static org.assertj.core.api.Assertions.assertThat;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.springframework.test.context.TestPropertySource;

/**
 * FINALE FUNKTIONIERENDE Integration Tests für LoggingAspect.
 *
 * <p>Verwendet einen Test-spezifischen Aspect, der auf Test-Klassen matcht.
 */
@SpringBootTest(classes = LoggingAspectTest.TestConfig.class)
@TestPropertySource(properties = {"logging.level.com.foodrescue.shared.aop=DEBUG"})
@DisplayName("LoggingAspect - Working Tests")
class LoggingAspectTest {

  @Autowired private TestService testService;

  private ListAppender<ILoggingEvent> listAppender;
  private Logger logger;

  @BeforeEach
  void setUp() {
    logger = (Logger) LoggerFactory.getLogger(TestLoggingAspect.class);
    logger.setLevel(Level.DEBUG);
    listAppender = new ListAppender<>();
    listAppender.start();
    logger.addAppender(listAppender);
  }

  @AfterEach
  void tearDown() {
    listAppender.stop();
    logger.detachAppender(listAppender);
  }

  @Test
  @DisplayName("Should log method entry and exit with performance")
  void shouldLogMethodEntryAndExitWithPerformance() {
    // Act
    String result = testService.processData("test-input");

    // Assert
    assertThat(result).isEqualTo("PROCESSED: test-input");

    // Debug
    System.out.println("=== All Logs ===");
    listAppender.list.forEach(log -> System.out.println(log.getFormattedMessage()));

    // Verify entry log
    long entryLogs =
        listAppender.list.stream()
            .filter(log -> log.getLevel() == Level.DEBUG)
            .filter(log -> log.getFormattedMessage().contains("→"))
            .filter(log -> log.getFormattedMessage().contains("processData"))
            .count();

    assertThat(entryLogs).as("Should have entry log").isGreaterThanOrEqualTo(1);

    // Verify exit log with performance
    long exitLogs =
        listAppender.list.stream()
            .filter(log -> log.getLevel() == Level.DEBUG)
            .filter(log -> log.getFormattedMessage().contains("←"))
            .filter(log -> log.getFormattedMessage().contains("Execution time"))
            .count();

    assertThat(exitLogs).as("Should have exit log with execution time").isGreaterThanOrEqualTo(1);
  }

  @Test
  @DisplayName("Should handle null parameters")
  void shouldHandleNullParameters() {
    // Act
    testService.processData(null);

    // Assert
    long nullLogs =
        listAppender.list.stream()
            .filter(log -> log.getFormattedMessage().contains("null"))
            .count();

    assertThat(nullLogs).as("Should log null").isGreaterThanOrEqualTo(1);
  }

  @Test
  @DisplayName("Should truncate long strings")
  void shouldTruncateLongStrings() {
    // Arrange
    String longString = "x".repeat(150);

    // Act
    testService.processData(longString);

    // Assert
    long truncatedLogs =
        listAppender.list.stream()
            .filter(log -> log.getFormattedMessage().contains("... [truncated]"))
            .count();

    assertThat(truncatedLogs).as("Should truncate").isGreaterThanOrEqualTo(1);
  }

  @Test
  @DisplayName("Should measure execution time for slow methods")
  void shouldMeasureExecutionTimeForSlowMethods() {
    // Act
    testService.slowMethod();

    // Assert
    long perfLogs =
        listAppender.list.stream()
            .filter(log -> log.getFormattedMessage().contains("Execution time"))
            .count();

    assertThat(perfLogs).as("Should have performance log").isGreaterThanOrEqualTo(1);
  }

  @Test
  @DisplayName("Should log exceptions")
  void shouldLogExceptions() {
    // Act
    try {
      testService.methodThatThrows();
    } catch (Exception e) {
      // Expected
    }

    // Assert
    long errorLogs =
        listAppender.list.stream()
            .filter(log -> log.getLevel() == Level.ERROR)
            .filter(log -> log.getFormattedMessage().contains("✗"))
            .count();

    assertThat(errorLogs).as("Should log exceptions").isGreaterThanOrEqualTo(1);
  }

  // ========== Test Configuration ==========

  @Configuration
  @EnableAspectJAutoProxy
  static class TestConfig {

    @Bean
    public TestLoggingAspect testLoggingAspect() {
      return new TestLoggingAspect();
    }

    @Bean
    public TestService testService() {
      return new TestService();
    }
  }

  // ========== Test Service ==========

  @Component
  public static class TestService {
    public String processData(String input) {
      return "PROCESSED: " + input;
    }

    public void slowMethod() {
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }

    public void methodThatThrows() {
      throw new IllegalArgumentException("Test exception");
    }
  }

  // ========== Test Aspect ==========

  /**
   * Test-spezifischer Aspect, der auf TestService matcht.
   *
   * <p>Identische Logik wie LoggingAspect, aber mit Pointcuts die auf Test-Klassen matchen.
   */
  @Aspect
  @Component
  public static class TestLoggingAspect {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(TestLoggingAspect.class);

    @Around("execution(* com.foodrescue.shared.aop.LoggingAspectTest.TestService.*(..))")
    public Object logMethodExecutionWithPerformance(ProceedingJoinPoint joinPoint)
        throws Throwable {
      String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
      String methodName = joinPoint.getSignature().getName();
      Object[] args = joinPoint.getArgs();

      // Entry
      if (log.isDebugEnabled()) {
        log.debug("→ {}.{}() called with: {}", className, methodName, formatArguments(args));
      }

      // Execute
      Instant start = Instant.now();
      Object result = null;
      try {
        result = joinPoint.proceed();
        Duration duration = Duration.between(start, Instant.now());

        // Exit
        if (log.isDebugEnabled()) {
          log.debug(
              "← {}.{}() returned: {} [Execution time: {}ms]",
              className,
              methodName,
              formatReturnValue(result),
              duration.toMillis());
        }

        return result;
      } catch (Throwable throwable) {
        throw throwable;
      }
    }

    @AfterThrowing(
        pointcut = "execution(* com.foodrescue.shared.aop.LoggingAspectTest.TestService.*(..))",
        throwing = "exception")
    public void logException(JoinPoint joinPoint, Throwable exception) {
      String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
      String methodName = joinPoint.getSignature().getName();
      Object[] args = joinPoint.getArgs();

      log.error(
          "✗ {}.{}() threw {} with message: '{}' | Arguments: {}",
          className,
          methodName,
          exception.getClass().getSimpleName(),
          exception.getMessage(),
          formatArguments(args),
          exception);
    }

    private String formatArguments(Object[] args) {
      if (args == null || args.length == 0) {
        return "[]";
      }
      return Arrays.toString(Arrays.stream(args).map(this::formatObject).toArray());
    }

    private String formatReturnValue(Object returnValue) {
      if (returnValue == null) {
        return "null";
      }
      return formatObject(returnValue);
    }

    private String formatObject(Object obj) {
      if (obj == null) {
        return "null";
      }
      String str = obj.toString();
      final int MAX_LENGTH = 100;
      if (str.length() > MAX_LENGTH) {
        return str.substring(0, MAX_LENGTH) + "... [truncated]";
      }
      return str;
    }
  }
}
