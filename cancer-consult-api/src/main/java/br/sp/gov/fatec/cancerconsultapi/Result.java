package br.sp.gov.fatec.cancerconsultapi;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@AllArgsConstructor
public @Data class Result implements Serializable {
    private String label;
    private String probability;
    
}
