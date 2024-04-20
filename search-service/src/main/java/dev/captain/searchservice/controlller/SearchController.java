package dev.captain.searchservice.controlller;


import dev.captain.searchservice.model.SearchResponse;
import dev.captain.searchservice.model.enums.SEARCH_TYPE;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/search-service")
@RequiredArgsConstructor
public class SearchController {
    private final RestTemplate restTemplate;


    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String query,
                                    @RequestParam(required = false) Long universityId

    ) {

        if (query == null || query.isEmpty()) {
            return ResponseEntity.badRequest().body("Search query cannot be empty");
        }

        if (universityId == null) {
            return ResponseEntity.badRequest().body("University id is required");
        }

        String additionalParameters = "&universityId=" + universityId;

        List<Object> users = null;
        List<Object> groups = null;
        List<Object> modules = null;

        try {
        users = restTemplate.exchange("http://USER-SERVICE/api/user-service/users/search?query=" + query +
                        additionalParameters,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Object>>() {
                }).getBody();

        groups = restTemplate.exchange("http://GROUP-SERVICE/api/group-service/groups/search?query=" + query +
                        additionalParameters,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Object>>() {
                }).getBody();

        modules = restTemplate.exchange("http://USER-SERVICE/api/user-service/modules/search?query=" + query +
                        additionalParameters,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Object>>() {
                }).getBody();
        } catch (Exception ignored) {

        }
        List<SearchResponse> searchResponse = new ArrayList<>();


        if (users != null) {
            int index = 0;
            for (Object user : users) {
                searchResponse.add(new SearchResponse(SEARCH_TYPE.USER, user));
                index++;
                if (index == 5) {
                    break;
                }
            }
        }

        if (groups != null) {
            int index = 0;
            for (Object group : groups) {
                searchResponse.add(new SearchResponse(SEARCH_TYPE.GROUP, group));
                index++;
                if (index == 5) {
                    break;
                }
            }
        }

        if(modules != null) {
            int index = 0;
            for (Object module : modules) {
                searchResponse.add(new SearchResponse(SEARCH_TYPE.MODULE, module));
                index++;
                if (index == 5) {
                    break;
                }
            }
        }
        return ResponseEntity.ok(searchResponse);
    }


}
