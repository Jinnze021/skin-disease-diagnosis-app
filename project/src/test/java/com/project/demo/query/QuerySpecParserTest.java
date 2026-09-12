package com.project.demo.query;

import com.project.demo.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class QuerySpecParserTest {

    private QuerySpecParser parser;

    @BeforeEach
    void setUp() {
        parser = new QuerySpecParser(new EntityFieldRegistry());
    }

    @Test
    void rejectsUnknownOrderColumnAndRawSqlParameter() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("orderby", "username desc; drop table user");
        MockHttpServletRequest unsafeOrderRequest = request;
        assertThrows(InvalidQueryException.class, () -> parser.parse(User.class, unsafeOrderRequest));

        request = new MockHttpServletRequest();
        request.setParameter("sqlwhere", "1=1");
        MockHttpServletRequest rawSqlRequest = request;
        assertThrows(InvalidQueryException.class, () -> parser.parse(User.class, rawSqlRequest));
    }

    @Test
    void rejectsUnknownFilterAndOversizedPage() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("not_a_user_field", "value");
        MockHttpServletRequest unknownFieldRequest = request;
        assertThrows(InvalidQueryException.class, () -> parser.parse(User.class, unknownFieldRequest));

        request = new MockHttpServletRequest();
        request.setParameter("size", "101");
        MockHttpServletRequest oversizedRequest = request;
        assertThrows(InvalidQueryException.class, () -> parser.parse(User.class, oversizedRequest));
    }

    @Test
    void acceptsOnlyValidatedFieldsAndDirections() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("username", "demo");
        request.setParameter("orderby", "`state` desc, create_time asc");
        request.setParameter("field", "username,nickname");
        request.setParameter("page", "2");
        request.setParameter("size", "25");

        QuerySpec spec = parser.parse(User.class, request);

        assertEquals("demo", spec.getFilters().get("username"));
        assertEquals(2, spec.getOrders().size());
        assertEquals("state", spec.getOrders().get(0).getColumn());
        assertEquals(QuerySpec.Direction.DESC, spec.getOrders().get(0).getDirection());
        assertEquals(2, spec.getPage());
        assertEquals(25, spec.getSize());
    }
}
