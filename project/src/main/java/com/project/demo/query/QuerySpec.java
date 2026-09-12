package com.project.demo.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class QuerySpec {

    public enum Direction { ASC, DESC }

    public static final class Order {
        private final String column;
        private final Direction direction;

        public Order(String column, Direction direction) {
            this.column = column;
            this.direction = direction;
        }

        public String getColumn() {
            return column;
        }

        public Direction getDirection() {
            return direction;
        }
    }

    private final Map<String, String> filters;
    private final Map<String, String> minimums;
    private final Map<String, String> maximums;
    private final boolean like;
    private final List<String> fields;
    private final List<Order> orders;
    private final String groupBy;
    private final int page;
    private final int size;

    QuerySpec(Map<String, String> filters,
              Map<String, String> minimums,
              Map<String, String> maximums,
              boolean like,
              List<String> fields,
              List<Order> orders,
              String groupBy,
              int page,
              int size) {
        this.filters = immutableMap(filters);
        this.minimums = immutableMap(minimums);
        this.maximums = immutableMap(maximums);
        this.like = like;
        this.fields = Collections.unmodifiableList(new ArrayList<>(fields));
        this.orders = Collections.unmodifiableList(new ArrayList<>(orders));
        this.groupBy = groupBy;
        this.page = page;
        this.size = size;
    }

    private static Map<String, String> immutableMap(Map<String, String> source) {
        return Collections.unmodifiableMap(new LinkedHashMap<>(source));
    }

    public Map<String, String> getFilters() { return filters; }
    public Map<String, String> getMinimums() { return minimums; }
    public Map<String, String> getMaximums() { return maximums; }
    public boolean isLike() { return like; }
    public List<String> getFields() { return fields; }
    public List<Order> getOrders() { return orders; }
    public String getGroupBy() { return groupBy; }
    public int getPage() { return page; }
    public int getSize() { return size; }

    public boolean hasFilters() {
        return !filters.isEmpty() || !minimums.isEmpty() || !maximums.isEmpty();
    }

    public QuerySpec withForcedFilter(String column, String value) {
        Map<String, String> scopedFilters = new LinkedHashMap<>(filters);
        scopedFilters.put(column, value);
        Map<String, String> scopedMinimums = new LinkedHashMap<>(minimums);
        Map<String, String> scopedMaximums = new LinkedHashMap<>(maximums);
        scopedMinimums.remove(column);
        scopedMaximums.remove(column);
        return new QuerySpec(scopedFilters, scopedMinimums, scopedMaximums, like,
                fields, orders, groupBy, page, size);
    }
}
