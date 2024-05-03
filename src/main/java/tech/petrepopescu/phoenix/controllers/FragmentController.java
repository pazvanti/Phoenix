package tech.petrepopescu.phoenix.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tech.petrepopescu.phoenix.format.Format;
import tech.petrepopescu.phoenix.format.PhoenixResponse;
import tech.petrepopescu.phoenix.format.Result;
import tech.petrepopescu.phoenix.special.PhoenixSpecialElementsUtil;
import tech.petrepopescu.phoenix.spring.config.PhoenixConfiguration;
import tech.petrepopescu.phoenix.views.View;

import java.util.List;

@Controller
public class FragmentController {
    private final PhoenixConfiguration configuration;
    private final PhoenixSpecialElementsUtil specialElementsUtil;

    public FragmentController(PhoenixConfiguration configuration, PhoenixSpecialElementsUtil specialElementsUtil) {
        this.configuration = configuration;
        this.specialElementsUtil = specialElementsUtil;
    }

    @GetMapping("/phoenix/fragment")
    public Result getFragment(@RequestParam(name = "name", required = false, defaultValue = "") String fragmentName,
                              @RequestParam(value = "param", required = false)List<String> params) {
        if (params == null) {
            params = List.of();
        }

        if (!configuration.isFragmentRetrieveEnabled()) {
            return PhoenixResponse.notFound();
        }

        Format format = View.ofNullableWithStringArgs(fragmentName, params);
        if (format == null) {
            return PhoenixResponse.notFound();
        }

        return PhoenixResponse.ok(format.getContent(specialElementsUtil));
    }
}
