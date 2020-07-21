package com.xkcoding.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiaxiaopeng
 * 2020-07-20 20:29
 **/
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@JsonInclude(Include.ALWAYS)
public class Result<T> {

  private Integer code;
  private String message;
  private T result;

  public static Result succes(String result) {
    return Result.builder()
      .code(0)
      .message("SUCCESS")
      .result(result)
      .build();
  }

}
