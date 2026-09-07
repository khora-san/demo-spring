package fr.diginamic.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DepartementApiDto(String code, String nom, String codeRegion) {

}



