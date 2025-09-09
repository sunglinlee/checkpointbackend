package com.alala.checkpointbackend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Questionnaire {
    String email;
    String qa;
    String createTime;
    String scheduleTime;
    String moodAndTags;
}
