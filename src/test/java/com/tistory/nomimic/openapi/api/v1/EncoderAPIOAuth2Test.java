package com.tistory.nomimic.openapi.api.v1;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tistory.nomimic.openapi.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.test.context.support.WithSecurityContextTestExcecutionListener;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Lucas,Lee on 14. 11. 14..
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
@WebAppConfiguration
@TestExecutionListeners(listeners={ServletTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        WithSecurityContextTestExcecutionListener.class})
public class EncoderAPIOAuth2Test {

    MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilters(springSecurityFilterChain)
                .build();
    }

    @Test
    public void testEncodeIsSuccessful() throws Exception {
        String url = "/v1/encoder";
        String clientId = "oauth2-client";
        String clientPassword = "password";

        Base64 base64 = new Base64();

        String basicAuthentication = clientId + ":" + clientPassword;
        String encodedBasicAuthentication =  "Basic " + new String(base64.encode(basicAuthentication.getBytes()));
        MvcResult mvcResult = null;

        url = "/oauth/token";
        mvcResult = mockMvc.perform(post(url)
                    .param("client_id","oauth2-client")
                    .param("grant_type","client_credentials")
                    .header("Authorization",encodedBasicAuthentication)
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        mvcResult.getResponse().getContentAsByteArray();
        ObjectMapper om = new ObjectMapper();
        JsonNode jsonNode = om.readTree(mvcResult.getResponse().getContentAsByteArray());

        String access_token = jsonNode.get("access_token").textValue();
        String token_type = jsonNode.get("token_type").textValue();
        String expires_in = jsonNode.get("expires_in").textValue();
        String scope = jsonNode.get("scope").textValue();

        url = "/v1/encoder";
        mockMvc.perform(post(url)
                        .param("msg", "HELLO OPEN API")
                        .param("access_token", access_token)
                        )
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void testEncodeIs4xxClientError() throws Exception {
        String url = "/v1/encoder";

        mockMvc.perform(post(url))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }
}
