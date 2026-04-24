package ism.dakar.edumanage.security.api.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import ism.dakar.edumanage.security.datas.enums.StatutEnum;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccessDto implements Serializable {

    public AccessDto(boolean active, String code, String description, boolean visible, StatutEnum statut) {
        this.actif = active;
        this.code = code;
        this.description = description;
        this.visible = visible;
        this.statut = statut;
    }

    public AccessDto() {
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate createAt;

    private boolean actif;

    private StatutEnum statut;

    private String code;

    private String description;

    private boolean visible;
}
