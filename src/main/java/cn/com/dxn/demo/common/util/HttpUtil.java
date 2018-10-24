package cn.com.dxn.demo.common.util;

import cn.com.dxn.demo.common.system.ResponseEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;

/**
 * @author richard
 */
@Component
public class HttpUtil {

    public void writeExceptionResponse(HttpServletResponse response, int errorCode, String errorMessage) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(errorCode);
        ResponseEntity responseEntity = new ResponseEntity(errorCode, Collections.singletonList(errorMessage), null);
        PrintWriter out = response.getWriter();
        out.print(new ObjectMapper().writeValueAsString(responseEntity));
        out.flush();
        out.close();
    }

    public void writeSuccessResponse(HttpServletResponse response, Object data) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=utf-8");
        ResponseEntity responseEntity = ResponseEntity.ok(data);
        PrintWriter out = response.getWriter();
        out.print(new ObjectMapper().writeValueAsString(responseEntity));
        out.flush();
        out.close();
    }

}
