package ru.practicum.item.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.practicum.item.model.ItemInfoWithUrlState;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    private final ItemRepository repo;

    private final RestTemplate restTemplate;

    public ItemRepositoryCustomImpl(@Lazy ItemRepository repo) {
        this.repo = repo;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public Collection<ItemInfoWithUrlState> checkItemsUrls(long userId) {
        return repo.findAllByUserId(userId)
                .stream()
                .map(i -> new ItemInfoWithUrlState(i, checkUrl(i.getUrl())))
                .collect(Collectors.toList());
    }

    private HttpStatus checkUrl(String url) {
        HttpStatus status;
        try {
            RequestEntity<byte[]> request = new RequestEntity<>(HttpMethod.GET, new URI(url));
            ResponseEntity<byte[]> response = restTemplate.exchange(request, byte[].class);
            status = response.getStatusCode();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return status;
    }
}
