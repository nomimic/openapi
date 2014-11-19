package com.tistory.nomimic.openapi.api.v1;

import com.tistory.nomimic.openapi.aspect.SLACheck;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Lucas,Lee on 14. 11. 14..
 */
@RestController
@RequestMapping("/v1/encoder")
public class EncoderAPI {

    /**
     * Encoding to HEX format
     *
     * @param message The string to be encoded in HEX format
     * @return encoded String
     * @throws Exception
     */
    @SLACheck //서비스 이용한계 체크하도록 하는 커스텀 어노테이션
    @RequestMapping(method = RequestMethod.POST,produces = {"application/json; charset=UTF-8"})
    @ResponseBody
    public String encodeHexString(@RequestParam("msg") String message) throws Exception {
        byte[] msgBytes = message.getBytes("UTF8");
        String result = "";

        for(byte value : msgBytes){
            result += String.format("%02X",value);
        }
        return result;
    }

    /**
     * 익센셥이 발생시, 해당 에러 메세지를 HttpBody로 전송하는 함수
     *
     * @param e Exception Object
     * @return
     * @throws Exception
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String onException(Exception e) throws Exception {
        return e.getMessage();
    }
}
