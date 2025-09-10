package com.alala.checkpointbackend.controller;

import com.alala.checkpointbackend.enums.StatusCode;
import com.alala.checkpointbackend.model.BaseResponse;
import com.alala.checkpointbackend.model.UpdateTitleRequest;
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

    @Operation(description = "查詢全部快照")
    @GetMapping(value = "/snapshots")
    public BaseResponse<String> snapshots(@RequestParam("email") String email) throws JsonProcessingException {
        return new BaseResponse<>(StatusCode.SUCCESS.getCode(), questionnaireService.query(email));
    }

    @Operation(description = "查詢單一快照")
    @GetMapping(value = "/snapshots/{snapshot_id}")
    public BaseResponse<String> singleSnapshots(@PathVariable("snapshot_id") String snapshotId) throws JsonProcessingException {
        return new BaseResponse<>(StatusCode.SUCCESS.getCode(), questionnaireService.querySingle(snapshotId));
    }

    @Operation(description = "刪除快照")
    @DeleteMapping(value = "/snapshots/{snapshot_id}")
    public BaseResponse<String> deleteSnapshots(@PathVariable("snapshot_id") String snapshotId) {
        return new BaseResponse<>(StatusCode.SUCCESS.getCode(), questionnaireService.delete(snapshotId));
    }

    @Operation(description = "更新快照標題")
    @PutMapping(value = "/snapshots/{snapshot_id}")
    public BaseResponse<String> updateSnapshots(@PathVariable("snapshot_id") String snapshotId, @RequestBody UpdateTitleRequest request) throws JsonProcessingException {
        return new BaseResponse<>(StatusCode.SUCCESS.getCode(), questionnaireService.update(snapshotId, request.title()));
    }
}
