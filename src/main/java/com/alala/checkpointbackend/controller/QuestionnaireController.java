package com.alala.checkpointbackend.controller;

import com.alala.checkpointbackend.enums.StatusCode;
import com.alala.checkpointbackend.model.BaseResponse;
import com.alala.checkpointbackend.model.QuestionnaireRequest;
import com.alala.checkpointbackend.service.QuestionnaireService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "questionnaire", description = "問卷相關API")
@RestController
@RequestMapping(value = "/questionnaire")
@RequiredArgsConstructor
public class QuestionnaireController {
    private final QuestionnaireService questionnaireService;

    @Operation(description = "提交問卷")
    @PostMapping(value = "/submit")
    public BaseResponse<String> submit(@RequestBody QuestionnaireRequest request) {
        return new BaseResponse<>(StatusCode.SUCCESS.getCode(), questionnaireService.submit(request));
    }
}
