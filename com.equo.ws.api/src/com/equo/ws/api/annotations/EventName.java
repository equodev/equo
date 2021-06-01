package com.equo.ws.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Annotation to define the name of the event to be listened to in the annotated
 * class/method.
 * </p>
 * <p>
 * If a class implementing {@link com.equo.ws.api.actions.IActionHandler} is
 * annotated, then the
 * {@link com.equo.ws.api.actions.IActionHandler#call(Object) call} method will
 * be executed when this event is sent.
 * </p>
 * <p>
 * If a method of a class implementing
 * {@link com.equo.ws.api.actions.IActionHandler} is annotated, then that method
 * will be executed when this event is sent.
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface EventName {
  /** Gets the event name. */
  String value();
}
