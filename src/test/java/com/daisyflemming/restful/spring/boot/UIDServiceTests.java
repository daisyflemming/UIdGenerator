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
    public void resetToZero() throws Exception {
        this.mockMvc.perform(get("/reset")).andDo(print()).andExpect(status().isOk())
                    .andExpect(content().string("Input is reset to 0"));
        this.mockMvc.perform(get("/getNextId")).andDo(print()).andExpect(status().isOk())
                    .andExpect(content().string("{\"uId\":\"BBBBBB\",\"input\":0}"));
        this.mockMvc.perform(get("/getNextId")).andDo(print()).andExpect(status().isOk())
                    .andExpect(content().string("{\"uId\":\"CBBBBB\",\"input\":1}"));
        this.mockMvc.perform(get("/getNextId")).andDo(print()).andExpect(status().isOk())
                    .andExpect(content().string("{\"uId\":\"DBBBBB\",\"input\":2}"));
    }

    @Test
    public void resetTo1000() throws Exception {
        this.mockMvc.perform(get("/reset/10000")).andDo(print()).andExpect(status().isOk())
                    .andExpect(content().string("Input is reset to 10000"));
        this.mockMvc.perform(get("/getNextId")).andDo(print()).andExpect(status().isOk())
                    .andExpect(content().string("{\"uId\":\"GLRBBB\",\"input\":10000}"));
        this.mockMvc.perform(get("/getNextId")).andDo(print()).andExpect(status().isOk())
                    .andExpect(content().string("{\"uId\":\"HLRBBB\",\"input\":10001}"));
        this.mockMvc.perform(get("/getNextId")).andDo(print()).andExpect(status().isOk())
                    .andExpect(content().string("{\"uId\":\"JLRBBB\",\"input\":10002}"));

    }

    @Test
    public void resetToMax() throws Exception {
        this.mockMvc.perform(get("/reset/1269789695")).andDo(print()).andExpect(status().isOk())
                    .andExpect(content().string("Input is reset to 1269789695"));
        this.mockMvc.perform(get("/getNextId")).andDo(print()).andExpect(status().isOk())
                    .andExpect(content().string("{\"uId\":\"Z99999\",\"input\":1269789695}"));
    }

    @Test
    public void getInvalidCharLength() throws Exception {
    }

    @Test
    public void resetToVeryLargeNumber() throws Exception {
    }

    @Test
    public void getIdWithInvalidLength() throws Exception {
    }
}
