package ism.dakar.edumanage.api.modeles;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import ism.dakar.edumanage.security.api.models.AbstractDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuditLogResponseDto extends AbstractDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String userEmail;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String userName;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String action;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String cible;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String details;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String adresseIp;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime loggedAt;
}
