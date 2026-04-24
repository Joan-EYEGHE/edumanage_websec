package ism.dakar.edumanage.security.api.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import ism.dakar.edumanage.security.datas.enums.StatutEnum;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractAccessDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    protected Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    protected LocalDate createAt;

    protected boolean actif;

    protected StatutEnum statut;

    protected List<String> roles = new ArrayList<>();

    protected List<Long> roleIds = new ArrayList<>();

}
