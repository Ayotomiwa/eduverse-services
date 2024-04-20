package dev.captain.searchservice.model;


import dev.captain.searchservice.model.enums.SEARCH_TYPE;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse {
    private SEARCH_TYPE type;
    private Object search;
}
