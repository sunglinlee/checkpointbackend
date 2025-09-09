package com.alala.checkpointbackend.controller;

import com.alala.checkpointbackend.enums.StatusCode;
import com.alala.checkpointbackend.model.BaseResponse;
import com.alala.checkpointbackend.service.QuestionnaireService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "snapshot", description = "快照相關API")
@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
public class SnapshotController {
    private final QuestionnaireService questionnaireService;

    @Operation(description = "查詢全部問卷")
    @GetMapping(value = "/snapshots")
    public BaseResponse<String> snapshots(@RequestParam("email") String email) throws JsonProcessingException {
        return new BaseResponse<>(StatusCode.SUCCESS.getCode(), questionnaireService.query(email));
    }
}
