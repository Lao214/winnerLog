package com.example.winnerLogBackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@Schema(description = "批量导入多场比赛AI预测请求")
public class BatchImportPredictionRequest {

    @Valid
    @NotEmpty(message = "导入比赛列表不能为空")
    @Schema(description = "比赛预测导入列表")
    private List<ImportPredictionRequest> matches;

    public BatchImportPredictionRequest() {}

    public BatchImportPredictionRequest(List<ImportPredictionRequest> matches) {
        this.matches = matches;
    }

    public static BatchImportPredictionRequestBuilder builder() {
        return new BatchImportPredictionRequestBuilder();
    }

    public static class BatchImportPredictionRequestBuilder {
        private List<ImportPredictionRequest> matches;

        public BatchImportPredictionRequestBuilder matches(List<ImportPredictionRequest> matches) {
            this.matches = matches;
            return this;
        }

        public BatchImportPredictionRequest build() {
            return new BatchImportPredictionRequest(matches);
        }
    }

    public List<ImportPredictionRequest> getMatches() { return matches; }
    public void setMatches(List<ImportPredictionRequest> matches) { this.matches = matches; }
}
