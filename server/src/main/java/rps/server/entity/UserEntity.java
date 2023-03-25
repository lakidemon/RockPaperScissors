package rps.server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.CharJdbcType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(unique = true, nullable = false)
  @JdbcType(CharJdbcType.class)
  private UUID uuid;

  @Column(unique = true, nullable = false)
  private String name;

  @Column(nullable = false)
  private String password;

  private OffsetDateTime lastLoginTime;

  @Column(nullable = false)
  @Builder.Default
  private OffsetDateTime registrationTime = OffsetDateTime.now();

  @PrePersist
  void makeUUID() {
    this.uuid = UUID.randomUUID();
  }
}
