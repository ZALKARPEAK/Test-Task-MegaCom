package com.career.testtaskmegacom.api;

import com.career.testtaskmegacom.service.ExternalService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/additional-task/external")
@RequiredArgsConstructor
public class ExternalApi {

    private final ExternalService externalService;

    @GetMapping
    @Operation(
            summary = "Выполнение внешнего запроса",
            description = "Выполняет HTTP GET запрос к внешнему API и возвращает ответ в виде строки."
    )
    public String getTaskById() {
        return externalService.fetchAndLogExternalData();
    }
}