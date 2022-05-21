package io.rpg.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class Result<OkT, ErrorT> {
  @Nullable
  private final OkT okValue;

  @Nullable
  private final ErrorT errorValue;

  @NotNull
  private final Type type;

  private Result(@Nullable OkT okValue, @Nullable ErrorT errorValue, @NotNull Type type) {
    this.okValue = okValue;
    this.errorValue = errorValue;
    this.type = type;
  }

  public static <S, E> Result<S, E> ok(@Nullable S okValue) {
    return new Result<>(okValue, null, Type.OK);
  }

  public static <S, E> Result<S, E> ok() {
    return new Result<>(null, null, Type.OK);
  }

  public static <S, E> Result<S, E> err(@Nullable E errorValue) {
    return new Result<>(null, errorValue, Type.ERROR);
  }

  public static <S, E> Result<S, E> err() {
    return new Result<>(null, null, Type.ERROR);
  }

  public boolean isErr() {
    return type == Type.ERROR;
  }

  public boolean isOk() {
    return type == Type.OK;
  }

  @NotNull
  public Type getType() {
    return type;
  }

  /**
   * If the result is OK and the value is not null, then returns the wrapped nullable value, else it throws.
   *
   * @return wrapped value or throws {@link IllegalStateException}.
   */
  @NotNull
  public OkT getOkValue() {
    if (isOk() && okValue != null) {
      return okValue;
    } else {
      throw new IllegalStateException("Attempt to access ok value on error result!");
    }
  }

  /**
   * If the result is ERR and the value is not null, then returns the wrapped nullable, error value, else it throws.
   *
   * @return wrapped error value or throws {@link IllegalStateException}.
   */
  @NotNull
  public ErrorT getErrValue() {
    if (isErr() && errorValue != null) {
      return errorValue;
    } else {
      throw new IllegalStateException("Attempt to access error value on ok result!");
    }
  }

  public boolean isOkValueNull() {
    return okValue == null;
  }

  public boolean isErrValueNull() {
    return errorValue == null;
  }

  @NotNull
  public Optional<OkT> getOkValueOpt() {
    return Optional.ofNullable(okValue != null ? getOkValue() : null);
  }

  @NotNull
  public Optional<ErrorT> getErrValueOpt() {
    return Optional.ofNullable(errorValue != null ? getErrValue() : null);
  }

  /**
   * Describes the result type.
   */
  public enum Type {
    OK, ERROR
  }
}
