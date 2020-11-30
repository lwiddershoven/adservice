package nl.leonw.springmicrodemo.adservice;

import brave.Tracer;
import io.swagger.annotations.ApiOperation;
import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AdResource {
    private final Tracer tracer;
    private final GetAdHandler getAdHandler;

    @GetMapping("/ads")
    @ApiOperation("Get ads appropriate for the provided keywords, or return random ads if none are given.")
    public AdResponse getAds(@RequestParam(value = "contextKeys", defaultValue = "") List<String> contextKeys) {
       var ads = getAdHandler
               .getAds(contextKeys)
               .stream()
               .map(Ad::fromDomain)
               .collect(Collectors.toList());

       return AdResponse.builder()
               .ads(ads)
               .build();
    }
}

@Data
class AdRequest {
    // List of important key words from the current page describing the context.
    private List<String> contextKeys;
}

@Data
@Builder
class AdResponse {
    private List<Ad> ads;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class Ad {
    // url to redirect to when an ad is clicked.
    private String redirectUrl;

    // short advertisement text to display.
    private String text;

    public static Ad fromDomain(DomainAd domain) {
        return new Ad(domain.getRedirectUrl(), domain.getText());
    }
}
