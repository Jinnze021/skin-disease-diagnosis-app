package com.project.demo.query;

import com.project.demo.constant.FindConfig;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class QuerySpecParser {

    private static final Pattern ORDER_PATTERN = Pattern.compile(
            "^([A-Za-z][A-Za-z0-9_]*)\\s*(?:(asc|desc))?$", Pattern.CASE_INSENSITIVE);
    private static final Set<String> CONFIG_KEYS = new HashSet<>(Arrays.asList(
            FindConfig.PAGE, FindConfig.SIZE, FindConfig.LIKE, FindConfig.ORDER_BY,
            FindConfig.FIELD, FindConfig.GROUP_BY
    ));

    private final EntityFieldRegistry fieldRegistry;

    public QuerySpecParser(EntityFieldRegistry fieldRegistry) {
        this.fieldRegistry = fieldRegistry;
    }

    public QuerySpec parse(Class<?> entityType, HttpServletRequest request) {
        if (request.getParameterMap().containsKey(FindConfig.SQLWHERE)) {
            throw new InvalidQueryException("不支持原始 SQL 查询条件");
        }

        Map<String, String> filters = new LinkedHashMap<>();
        Map<String, String> minimums = new LinkedHashMap<>();
        Map<String, String> maximums = new LinkedHashMap<>();

        for (Map.Entry<String, String[]> parameter : request.getParameterMap().entrySet()) {
            String name = parameter.getKey();
            if (CONFIG_KEYS.contains(name)) {
                continue;
            }
            String value = request.getParameter(name);
            if (!StringUtils.hasText(value)) {
                continue;
            }
            if (name.endsWith(FindConfig.MIN_SUFFIX)) {
                String column = fieldRegistry.resolve(entityType,
                        name.substring(0, name.length() - FindConfig.MIN_SUFFIX.length()));
                minimums.put(column, value);
            } else if (name.endsWith(FindConfig.MAX_SUFFIX)) {
                String column = fieldRegistry.resolve(entityType,
                        name.substring(0, name.length() - FindConfig.MAX_SUFFIX.length()));
                maximums.put(column, value);
            } else {
                filters.put(fieldRegistry.resolve(entityType, name), value);
            }
        }

        boolean like = parseLike(request.getParameter(FindConfig.LIKE));
        List<String> fields = parseFields(entityType, request.getParameter(FindConfig.FIELD));
        List<QuerySpec.Order> orders = parseOrders(entityType, request.getParameter(FindConfig.ORDER_BY));
        String groupBy = parseOptionalField(entityType, request.getParameter(FindConfig.GROUP_BY));
        int page = parseInt(request.getParameter(FindConfig.PAGE), 1, 1, Integer.MAX_VALUE, "page");
        int size = parseInt(request.getParameter(FindConfig.SIZE), 20, 1, 100, "size");
        return new QuerySpec(filters, minimums, maximums, like, fields, orders, groupBy, page, size);
    }

    private boolean parseLike(String value) {
        if (!StringUtils.hasText(value) || "false".equalsIgnoreCase(value)) {
            return false;
        }
        if ("true".equalsIgnoreCase(value) || "1".equals(value) || "0".equals(value)) {
            return true;
        }
        throw new InvalidQueryException("like 必须为 true 或 false");
    }

    private List<String> parseFields(Class<?> entityType, String value) {
        List<String> fields = new ArrayList<>();
        if (!StringUtils.hasText(value)) {
            return fields;
        }
        for (String field : value.split(",")) {
            fields.add(fieldRegistry.resolve(entityType, field));
        }
        return fields;
    }

    private List<QuerySpec.Order> parseOrders(Class<?> entityType, String value) {
        List<QuerySpec.Order> orders = new ArrayList<>();
        if (!StringUtils.hasText(value)) {
            return orders;
        }
        for (String clause : value.split(",")) {
            String normalized = clause.trim().replace("`", "");
            Matcher matcher = ORDER_PATTERN.matcher(normalized);
            if (!matcher.matches()) {
                throw new InvalidQueryException("排序参数格式不正确");
            }
            String column = fieldRegistry.resolve(entityType, matcher.group(1));
            QuerySpec.Direction direction = "desc".equalsIgnoreCase(matcher.group(2))
                    ? QuerySpec.Direction.DESC : QuerySpec.Direction.ASC;
            orders.add(new QuerySpec.Order(column, direction));
        }
        return orders;
    }

    private String parseOptionalField(Class<?> entityType, String value) {
        return StringUtils.hasText(value) ? fieldRegistry.resolve(entityType, value) : null;
    }

    private int parseInt(String value, int defaultValue, int minimum, int maximum, String name) {
        if (!StringUtils.hasText(value)) {
            return defaultValue;
        }
        try {
            int parsed = Integer.parseInt(value);
            if (parsed < minimum || parsed > maximum) {
                throw new InvalidQueryException(name + " 超出允许范围");
            }
            return parsed;
        } catch (NumberFormatException exception) {
            throw new InvalidQueryException(name + " 必须为整数");
        }
    }
}
