/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.daisyflemming.restful.spring.boot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UIDServiceTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getInvalidId() throws Exception {
        this.mockMvc.perform(get("/id/invalidInput")).andDo(print()).andExpect(status().is4xxClientError());

    }

    @Test
    public void getValidId() throws Exception {
        this.mockMvc.perform(get("/id/1000")).andDo(print()).andExpect(status().isOk())
                    .andExpect(content().string("{\"uid\":\"RPCBBB\",\"input\":1000}"));
        this.mockMvc.perform(get("/id/1000/length/6")).andDo(print()).andExpect(status().isOk())
                    .andExpect(content().string("{\"uid\":\"RPCBBB\",\"input\":1000}"));
        this.mockMvc.perform(get("/id/1000/length/5")).andDo(print()).andExpect(status().isOk())
                    .andExpect(content().string("{\"uid\":\"RPCBB\",\"input\":1000}"));
        this.mockMvc.perform(get("/id/1000/length/4")).andDo(print()).andExpect(status().isOk())
                    .andExpect(content().string("{\"uid\":\"RPCB\",\"input\":1000}"));
    }

    @Test
    public void getOutOfRangeValue() throws Exception {
        this.mockMvc.perform(get("/id/1000000")).andDo(print()).andExpect(status().isOk())
                    .andExpect(content().string("{\"uid\":\"C10BCB\",\"input\":1000000}"));
        this.mockMvc.perform(get("/id/1000000/length/6")).andDo(print()).andExpect(status().isOk())
                    .andExpect(content().string("{\"uid\":\"C10BCB\",\"input\":1000000}"));
        this.mockMvc.perform(get("/id/1000000/length/5")).andDo(print()).andExpect(status().isOk())
                    .andExpect(content().string("{\"uid\":\"C10BC\",\"input\":1000000}"));
        this.mockMvc.perform(get("/id/1000000/length/4")).andDo(print()).andExpect(status().is5xxServerError());
    }
}
