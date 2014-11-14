package com.tistory.nomimic.openapi.api.v1;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
/**
 * Created by Lucas,Lee on 14. 11. 14..
 */
@RunWith(MockitoJUnitRunner.class)
public class EncoderAPITest {

    MockMvc mockMvc;

    @InjectMocks
    EncoderAPI encoderAPI;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(encoderAPI).build();
    }

    @Test
    public void testEncodeIs5xxServerError() throws Exception {
        String url = "/v1/encoder";

        mockMvc.perform(post(url))
                .andDo(print())
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void testEncodeIsSuccessful() throws Exception {
        String url = "/v1/encoder";

        mockMvc.perform(post(url).param("msg","HELLO OPEN API"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

}
