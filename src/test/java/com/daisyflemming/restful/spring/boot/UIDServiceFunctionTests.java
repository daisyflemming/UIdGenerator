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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class UIDServiceFunctionTests {

    private UIDService service = new UIDService();
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void getId() throws Exception {
        UID uid4char = service.getId(java.util.Optional.of(4));
        assertEquals(service.queryId(uid4char.input, 4).input, uid4char.input);
        assertEquals(service.queryId(uid4char.input, 4).uid, uid4char.uid);

        UID uid5char = service.getId(java.util.Optional.of(5));
        assertEquals(service.queryId(uid5char.input, 5).input, uid5char.input);
        assertEquals(service.queryId(uid5char.input, 5).uid, uid5char.uid);

        UID uid6char = service.getId(java.util.Optional.of(6));
        assertEquals(service.queryId(uid6char.input, 6).input, uid6char.input);
        assertEquals(service.queryId(uid6char.input, 6).uid, uid6char.uid);

    }

    @Test
    public void queryId() throws Exception {
        int id = 100;
        UID uid4char = service.queryId(id, 4);
        UID uid5char = service.queryId(id, 5);
        UID uid6char = service.queryId(id);
        assertEquals(id, uid4char.input);
        assertEquals(id, uid5char.input);
        assertEquals(id, uid6char.input);
        assertNotEquals(uid4char, uid5char);
        assertNotEquals(uid4char, uid6char);
        assertNotEquals(uid5char, uid6char);
    }

    @Test
    public void getIdWithInvalidLength() throws Exception {
        exceptionRule.expect(RuntimeException.class);
        exceptionRule.expectMessage(service.LENGTH_OUT_OF_RANGE);
        UID uid7char = service.getId(java.util.Optional.of(7));
    }

    @Test
    public void getAnOutOfRangeNumber() throws Exception {
        exceptionRule.expect(RuntimeException.class);
        exceptionRule.expectMessage(service.INPUT_OUT_OF_RANGE);
        service.queryId(service.getMaxValue(service.maxLength)+1);
    }
}
