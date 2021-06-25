package nextstep.subway.path.ui;

import nextstep.subway.auth.domain.FlexibleAuthPrinciple;
import nextstep.subway.auth.domain.User;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findShortestPath(@FlexibleAuthPrinciple User user,
                                                         @RequestParam("source") Long sourceId,
                                                         @RequestParam("target") Long targetId) {

        return ResponseEntity.ok().body(pathService.findShortestPath(user.getDiscountStrategy(),
                                                                     sourceId, targetId));
    }
}
