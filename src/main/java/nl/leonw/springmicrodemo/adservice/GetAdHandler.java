package nl.leonw.springmicrodemo.adservice;

import brave.Tracer;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetAdHandler {
    private final Tracer tracer;
    private static final int MAX_ADS_TO_SERVE = 2;
    private final Random random = new Random();

    private final Map<String, List<DomainAd>> ads = createAdsMap();


    public List<DomainAd> getAds(List<String> contextKeys) {
        if (contextKeys.isEmpty()) {
            tracer.currentSpan().annotate("Empty set of context keys");
            return getRandomAds();
        } else {
            tracer.currentSpan().annotate("Context keys: " + contextKeys);
            return getContextAds(contextKeys);
        }

    }

    List<DomainAd> getContextAds(List<String> contextKeys) {
        return contextKeys.stream()
                .flatMap(key -> getAdsByCategory(key).stream())
                .collect(Collectors.toList());
    }

    List<DomainAd> getAdsByCategory(String contextKey) {
        return ads.getOrDefault(contextKey, Collections.emptyList());
    }

    List<DomainAd> getRandomAds() {
        var allAds = ads.values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        var ads = new ArrayList<DomainAd>();
        for (int i = 0; i < MAX_ADS_TO_SERVE; i++) {
            ads.add(allAds.get(random.nextInt(allAds.size())));
        }
        return ads;
    }

    private Map<String, List<DomainAd>> createAdsMap() {
        DomainAd camera =
                DomainAd.newBuilder()
                        .setRedirectUrl("/product/2ZYFJ3GM2N")
                        .setText("Film camera for sale. 50% off.")
                        .build();
        DomainAd lens =
                DomainAd.newBuilder()
                        .setRedirectUrl("/product/66VCHSJNUP")
                        .setText("Vintage camera lens for sale. 20% off.")
                        .build();
        DomainAd recordPlayer =
                DomainAd.newBuilder()
                        .setRedirectUrl("/product/0PUK6V6EV0")
                        .setText("Vintage record player for sale. 30% off.")
                        .build();
        DomainAd bike =
                DomainAd.newBuilder()
                        .setRedirectUrl("/product/9SIQT8TOJO")
                        .setText("City Bike for sale. 10% off.")
                        .build();
        DomainAd baristaKit =
                DomainAd.newBuilder()
                        .setRedirectUrl("/product/1YMWWN1N4O")
                        .setText("Home Barista kitchen kit for sale. Buy one, get second kit for free")
                        .build();
        DomainAd airPlant =
                DomainAd.newBuilder()
                        .setRedirectUrl("/product/6E92ZMYYFZ")
                        .setText("Air plants for sale. Buy two, get third one for free")
                        .build();
        DomainAd terrarium =
                DomainAd.newBuilder()
                        .setRedirectUrl("/product/L9ECAV7KIM")
                        .setText("Terrarium for sale. Buy one, get second one for free")
                        .build();
        return Map.of(
                "photography", List.of(camera, lens),
                "vintage", List.of(camera, lens, recordPlayer),
                "cycling", List.of(bike),
                "cookware", List.of(baristaKit),
                "gardening", List.of(airPlant, terrarium)
        );
    }
}

@Data
@Builder(builderMethodName = "newBuilder", setterPrefix = "set")
class DomainAd {
    // url to redirect to when an ad is clicked.
    private String redirectUrl;

    // short advertisement text to display.
    private String text;
}
