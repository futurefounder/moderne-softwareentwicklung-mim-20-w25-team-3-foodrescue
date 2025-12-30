package com.foodrescue.shared.aop;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * AOP Aspect für automatisches Logging
 *
 * <p>Dieser Aspect implementiert Cross-Cutting Concerns für Logging und eliminiert redundante
 * manuelle Log-Statements in Service-Klassen, Controllern und Repositories.
 */
@Aspect
@Component
public class LoggingAspect {

  private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

  // ========== Pointcut Definitions ==========

  /**
   * Pointcut für alle Application Service Methoden. Pattern:
   * com.foodrescue.*.application.services.*.*(..)
   */
  @Pointcut("execution(* com.foodrescue.*.application.services.*.*(..))")
  public void applicationServiceMethods() {}

  /**
   * Pointcut für alle REST Controller Methoden. Pattern:
   * com.foodrescue.*.infrastructure.web.rest.*.*(..)
   */
  @Pointcut("execution(* com.foodrescue.*.infrastructure.web.rest.*.*(..))")
  public void controllerMethods() {}

  /**
   * Pointcut für alle Repository Methoden. Pattern:
   * com.foodrescue.*.infrastructure.repositories.*.*(..) und
   * com.foodrescue.*.infrastructure.persistence.*.*(..)
   */
  @Pointcut(
      "execution(* com.foodrescue.*.infrastructure.repositories.*.*(..)) || "
          + "execution(* com.foodrescue.*.infrastructure.persistence.*.*(..))")
  public void repositoryMethods() {}

  /**
   * Pointcut für alle Event Handler Methoden. Pattern:
   * com.foodrescue.*.infrastructure.eventhandlers.*.*(..)
   */
  @Pointcut("execution(* com.foodrescue.*.infrastructure.eventhandlers.*.*(..))")
  public void eventHandlerMethods() {}

  /** Kombinierter Pointcut für alle zu loggenden Methoden. */
  @Pointcut(
      "applicationServiceMethods() || controllerMethods() || repositoryMethods() || eventHandlerMethods()")
  public void loggableMethods() {}

  // ========== Around Advice (Method Entry/Exit + Performance) ==========

  /**
   * Around Advice für automatisches Method Entry/Exit Logging mit Performance-Messung.
   *
   * <p>Wird für alle Application Service und Repository Methoden angewendet.
   *
   * @param joinPoint Der Join Point (Methoden-Ausführung)
   * @return Das Ergebnis der Methoden-Ausführung
   * @throws Throwable Falls die Methode eine Exception wirft
   */
  @Around("applicationServiceMethods() || repositoryMethods()")
  public Object logMethodExecutionWithPerformance(ProceedingJoinPoint joinPoint) throws Throwable {

    String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
    String methodName = joinPoint.getSignature().getName();
    Object[] args = joinPoint.getArgs();

    // Method Entry Logging
    if (log.isDebugEnabled()) {
      log.debug("→ {}.{}() called with: {}", className, methodName, formatArguments(args));
    }

    // Performance-Messung
    Instant start = Instant.now();
    Object result = null;
    try {
      // Methoden-Ausführung
      result = joinPoint.proceed();

      // Performance berechnen
      Duration duration = Duration.between(start, Instant.now());

      // Method Exit Logging mit Performance
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
      // Exception wird separat im @AfterThrowing Advice behandelt
      throw throwable;
    }
  }

  // ========== Before Advice (Controller Entry Logging) ==========

  /**
   * Before Advice für Controller-Methoden.
   *
   * <p>Loggt eingehende HTTP-Requests mit ihren Parametern.
   *
   * @param joinPoint Der Join Point
   */
  @Before("controllerMethods()")
  public void logControllerEntry(JoinPoint joinPoint) {
    String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
    String methodName = joinPoint.getSignature().getName();
    Object[] args = joinPoint.getArgs();

    log.info("HTTP → {}.{}() | Request: {}", className, methodName, formatArguments(args));
  }

  // ========== AfterReturning Advice (Controller Exit Logging) ==========

  /**
   * AfterReturning Advice für Controller-Methoden.
   *
   * <p>Loggt HTTP-Response nach erfolgreicher Verarbeitung.
   *
   * @param joinPoint Der Join Point
   * @param result Das Rückgabe-Objekt
   */
  @AfterReturning(pointcut = "controllerMethods()", returning = "result")
  public void logControllerExit(JoinPoint joinPoint, Object result) {
    String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
    String methodName = joinPoint.getSignature().getName();

    log.info("HTTP ← {}.{}() | Response: {}", className, methodName, formatReturnValue(result));
  }

  // ========== AfterThrowing Advice (Exception Logging) ==========

  /**
   * AfterThrowing Advice für alle loggbaren Methoden.
   *
   * <p>Loggt Exceptions mit vollständigem Context (Klasse, Methode, Parameter, Exception).
   *
   * @param joinPoint Der Join Point
   * @param exception Die geworfene Exception
   */
  @AfterThrowing(pointcut = "loggableMethods()", throwing = "exception")
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

  // ========== Before Advice für Event Handlers ==========

  /**
   * Before Advice für Event Handler Methoden.
   *
   * <p>Spezielle Logging-Formatierung für Domain Events.
   *
   * @param joinPoint Der Join Point
   */
  @Before("eventHandlerMethods()")
  public void logEventHandling(JoinPoint joinPoint) {
    String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
    String methodName = joinPoint.getSignature().getName();
    Object[] args = joinPoint.getArgs();

    // Versuche Event-Typ zu extrahieren
    String eventInfo = "Unknown Event";
    if (args.length > 0 && args[0] != null) {
      eventInfo = args[0].getClass().getSimpleName();
    }

    log.info("📨 Event Received: {} | Handler: {}.{}()", eventInfo, className, methodName);
  }

  // ========== Helper Methods ==========

  /**
   * Formatiert Methodenparameter für das Logging.
   *
   * <p>Vermeidet zu lange Strings und handhabt null-Werte.
   *
   * @param args Die Methodenparameter
   * @return Formatierter String
   */
  private String formatArguments(Object[] args) {
    if (args == null || args.length == 0) {
      return "[]";
    }

    return Arrays.toString(Arrays.stream(args).map(this::formatObject).toArray());
  }

  /**
   * Formatiert den Return-Value für das Logging.
   *
   * @param returnValue Der Rückgabewert
   * @return Formatierter String
   */
  private String formatReturnValue(Object returnValue) {
    if (returnValue == null) {
      return "null";
    }
    return formatObject(returnValue);
  }

  /**
   * Formatiert ein einzelnes Objekt für das Logging.
   *
   * <p>Kürzt zu lange Strings und gibt sinnvolle Repräsentationen.
   *
   * @param obj Das zu formatierende Objekt
   * @return Formatierter String
   */
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
