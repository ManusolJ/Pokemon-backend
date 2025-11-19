package com.pokemon.services.service;

import com.pokemon.utils.enums.SearchOperation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchCriteria {
    private String key;
    private Object value;
    private Object valueTo;
    private SearchOperation operation;
}
