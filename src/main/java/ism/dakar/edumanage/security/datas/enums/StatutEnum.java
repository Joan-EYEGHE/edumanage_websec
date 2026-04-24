package ism.dakar.edumanage.security.datas.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.text.MessageFormat;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;


@Getter
public enum StatutEnum {
    ACTIF("Actif"),
    EXPIRER("Expirer"),
    ARCHIVER("Archiver");

    private String description;


    StatutEnum(String description) {
        this.description = description;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static StatutEnum fromValue(Object enu) {
        if (enu instanceof Map) {
            Map<String, Object> mapEnu = (Map<String, Object>) enu;
            if (mapEnu.containsKey("name")) {
                return StatutEnum.valueOf(mapEnu.get("name").toString());
            }
        }
        if (enu instanceof String) {
            return StatutEnum.valueOf(enu.toString());
        }
        throw new IllegalArgumentException(MessageFormat.format("{0} not found with the value: {1} in [{2}]", StatutEnum.class, enu, values()));
    }

    @JsonValue
    Map<String, Object> getModule() {
        return Map.of(
                "name", name(),
                "description", description
        );
    }

    public static Set<StatutEnum> getAllEnu() {
        return stream(values())
                .collect(Collectors.toSet());
    }
}
