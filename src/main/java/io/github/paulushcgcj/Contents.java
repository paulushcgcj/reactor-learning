package io.github.paulushcgcj;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@With
public class Contents {
  @Builder.Default
  private String id = UUID.randomUUID().toString();
  @Builder.Default
  private LocalDateTime time = LocalDateTime.now();

  private String data;

  public Contents(String data) {
    this();
    this.data = data;
  }
}
