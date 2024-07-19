package com.regalo_libre.auth.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "authuser", schema = "oauth")
public class OAuthUser {
    @Id
    @JsonProperty("sub")
    private Long id;
    private String name;
    private String nickname;
    private String picture;
    @JsonProperty("updated_at")
    private String updatedAt;

    public static class OAuthUserBuilder {
        public OAuthUserBuilder fullStringId(String fullString) {
            try {
                String[] parts = fullString.split("\\|");
                if (parts.length > 0) {
                    this.id = Long.parseLong(parts[parts.length - 1]);
                } else {
                    throw new IllegalArgumentException("String does not contain expected format");
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Last part is not a valid number", e);
            }
            return this;
        }
    }
}
