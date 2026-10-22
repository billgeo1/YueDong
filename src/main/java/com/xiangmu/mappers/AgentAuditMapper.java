package com.xiangmu.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.xiangmu.agent.record.AgentConfirmationRecord;
import com.xiangmu.agent.record.AgentSessionRecord;
import com.xiangmu.agent.record.AgentToolCallRecord;

@Mapper
public interface AgentAuditMapper {
    int upsertSession(AgentSessionRecord record);

    int insertToolCall(AgentToolCallRecord record);

    AgentToolCallRecord findCallByKey(@Param("idempotencyKey") String idempotencyKey);

    int insertConfirmation(AgentConfirmationRecord record);

    AgentConfirmationRecord findConfirmationByToken(@Param("token") String token);

    int claimConfirmation(@Param("token") String token,
                          @Param("actorType") String actorType,
                          @Param("actorId") int actorId);

    int finishConfirmation(@Param("token") String token,
                           @Param("status") String status,
                           @Param("toolCallId") Long toolCallId,
                           @Param("resultSummary") String resultSummary);
}
