package com.xiangmu.controler;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xiangmu.agent.AgentChatRequest;
import com.xiangmu.agent.AgentConfirmRequest;
import com.xiangmu.agent.AgentException;
import com.xiangmu.agent.AgentService;
import com.xiangmu.agent.AgentToolDefinition;
import com.xiangmu.util.JsonResult;

@RestController
public class AgentController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AgentController.class);

    @Autowired
    private AgentService agentService;

    @PostMapping("/agent/chat")
    public JsonResult<Object> chat(@RequestBody AgentChatRequest request, HttpSession session) {
        return success(agentService.chat(request, session));
    }

    @PostMapping("/agent/confirm")
    public JsonResult<Object> confirm(@RequestBody AgentConfirmRequest request, HttpSession session) {
        return success(agentService.confirm(request, session));
    }

    @GetMapping("/agent/tools")
    public JsonResult<List<AgentToolDefinition>> tools(
            @RequestParam(value = "mode", defaultValue = "customer") String mode,
            HttpSession session) {
        JsonResult<List<AgentToolDefinition>> result = new JsonResult<>();
        result.setState(66);
        result.setMessage("ok");
        result.setData(agentService.listTools(mode, session));
        return result;
    }

    @ExceptionHandler(AgentException.class)
    public JsonResult<Object> handleAgentException(AgentException ex) {
        JsonResult<Object> result = new JsonResult<>();
        result.setState(ex.getState());
        result.setMessage(ex.getMessage());
        return result;
    }

    @ExceptionHandler(Exception.class)
    public JsonResult<Object> handleUnexpectedException(Exception ex) {
        LOGGER.error("Unexpected agent error", ex);
        JsonResult<Object> result = new JsonResult<>();
        result.setState(500);
        result.setMessage("智能助手暂时开小差了，请稍后再试；商城原有购物流程不受影响");
        return result;
    }

    private JsonResult<Object> success(Object data) {
        JsonResult<Object> result = new JsonResult<>();
        result.setState(66);
        result.setMessage("ok");
        result.setData(data);
        return result;
    }
}
