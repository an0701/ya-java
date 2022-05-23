package com.yac.service.util;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author xiaoya
 * @qq 278729535
 * @link https://github.com/an0701/ya-java
 * @date : 2022/5/17 15:14
 * @since 0.1.0
 */
public class ResponseUtil {
    /**
     * 返回错误信息
     */
    public static boolean errResponse(HttpServletResponse response, Integer code, String msg) {
        JSONObject res = new JSONObject();
        PrintWriter out;
        res.put("Success", "false");
        res.put("Code", code);
        res.put("Msg", msg);
        assert false;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            response.setStatus(2222);
            return Boolean.FALSE;
        }
        out.append(res.toString());
        return Boolean.FALSE;
    }
}
