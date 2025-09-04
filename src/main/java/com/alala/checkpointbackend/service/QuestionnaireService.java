package com.alala.checkpointbackend.service;

import com.alala.checkpointbackend.dao.QuestionnaireDAO;
import com.alala.checkpointbackend.model.QuestionnaireRequest;
import com.alala.checkpointbackend.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class QuestionnaireService {
    private final QuestionnaireDAO questionnaireDAO;
    private final DateUtil dateUtil;

    public String submit(QuestionnaireRequest request) {

        Timestamp currentTime = dateUtil.getCurrentTimePlus8();
        questionnaireDAO.insert(request, currentTime, dateUtil.calculateTime(request.scheduleTime(), currentTime));

        return "問卷送出成功";
    }
}
