package com.tistory.nomimic.openapi.api.v1;

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

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String onException(Exception e) throws Exception {
        return e.getMessage();
    }
}
