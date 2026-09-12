package com.project.demo.service.base;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.demo.dao.base.BaseMapper;
import com.project.demo.query.InvalidQueryException;
import com.project.demo.query.QuerySpec;
import com.project.demo.query.QuerySpecParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BaseService<E> {

    @Autowired
    protected BaseMapper<E> baseMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private QuerySpecParser querySpecParser;

    private final Class<E> entityClass = resolveEntityClass();

    public Class<E> getEntityClass() {
        return entityClass;
    }

    public QuerySpec parse(HttpServletRequest request) {
        return querySpecParser.parse(entityClass, request);
    }

    public QuerySpec scopeToAuthenticatedUser(QuerySpec spec, Integer userId) {
        String ownerColumn = readOwnerColumn();
        if (ownerColumn == null || userId == null) {
            return spec;
        }
        return spec.withForcedFilter(ownerColumn, String.valueOf(userId));
    }

    public QuerySpec scopeMutationToAuthenticatedUser(QuerySpec spec, Integer userId) {
        String ownerColumn = mutationOwnerColumn();
        if (ownerColumn == null || userId == null) {
            return spec;
        }
        return spec.withForcedFilter(ownerColumn, String.valueOf(userId));
    }

    public void scopeBodyToAuthenticatedUser(Map<String, Object> body, Integer userId) {
        String ownerColumn = mutationOwnerColumn();
        if (ownerColumn != null && userId != null) {
            body.put(ownerColumn, userId);
        }
    }

    public QueryWrapper<E> buildWhere(QuerySpec spec) {
        QueryWrapper<E> wrapper = buildConditions(spec);
        if (!spec.getFields().isEmpty()) {
            wrapper.select(spec.getFields().toArray(new String[0]));
        }
        if (spec.getGroupBy() != null) {
            wrapper.groupBy(spec.getGroupBy());
        }
        for (QuerySpec.Order order : spec.getOrders()) {
            if (order.getDirection() == QuerySpec.Direction.DESC) {
                wrapper.orderByDesc(order.getColumn());
            } else {
                wrapper.orderByAsc(order.getColumn());
            }
        }
        return wrapper;
    }

    public Map<String, Object> selectToPage(QuerySpec spec) {
        IPage<E> page = baseMapper.selectPage(
                new Page<>(spec.getPage(), spec.getSize()), buildWhere(spec));
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("list", page.getRecords());
        result.put("count", page.getTotal());
        return result;
    }

    public E selectOne(QuerySpec spec) {
        QueryWrapper<E> wrapper = buildWhere(spec);
        wrapper.last("LIMIT 1");
        List<E> results = baseMapper.selectList(wrapper);
        return results.isEmpty() ? null : results.get(0);
    }

    public Map<String, Object> selectGroupCount(QuerySpec spec) {
        if (spec.getGroupBy() == null) {
            throw new InvalidQueryException("groupby 不能为空");
        }
        QueryWrapper<E> wrapper = buildConditions(spec);
        wrapper.select(spec.getGroupBy(), "COUNT(*) AS aggregate_value")
                .groupBy(spec.getGroupBy());
        List<Map<String, Object>> maps = baseMapper.selectMaps(wrapper);
        List<Map<String, Object>> list = new ArrayList<>();
        for (Map<String, Object> row : maps) {
            Map<String, Object> normalized = new LinkedHashMap<>(row);
            Object count = valueIgnoreCase(row, "aggregate_value");
            Object group = valueIgnoreCase(row, spec.getGroupBy());
            normalized.put("count", count);
            normalized.put("0", count);
            normalized.put("1", group);
            list.add(normalized);
        }
        return Collections.<String, Object>singletonMap("list", list);
    }

    public Map<String, Object> selectBarGroup(QuerySpec spec) {
        if (spec.getGroupBy() == null || spec.getFields().isEmpty()) {
            throw new InvalidQueryException("bar_group 需要 groupby 和 field");
        }
        List<String> selections = new ArrayList<>();
        selections.add(spec.getGroupBy());
        for (int index = 0; index < spec.getFields().size(); index++) {
            selections.add("SUM(" + spec.getFields().get(index) + ") AS value_" + index);
        }
        QueryWrapper<E> wrapper = buildConditions(spec);
        wrapper.select(selections.toArray(new String[0])).groupBy(spec.getGroupBy());
        List<Map<String, Object>> rows = baseMapper.selectMaps(wrapper);
        List<List<Object>> result = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            List<Object> values = new ArrayList<>();
            values.add(valueIgnoreCase(row, spec.getGroupBy()));
            for (int index = 0; index < spec.getFields().size(); index++) {
                values.add(valueIgnoreCase(row, "value_" + index));
            }
            result.add(values);
        }
        return Collections.<String, Object>singletonMap("list", result);
    }

    public Object count(QuerySpec spec) {
        return baseMapper.selectCount(buildConditions(spec));
    }

    public Object sum(QuerySpec spec) {
        return aggregate(spec, "SUM");
    }

    public Object avg(QuerySpec spec) {
        return aggregate(spec, "AVG");
    }

    @Transactional
    public int insert(Map<String, Object> body) {
        return baseMapper.insert(toEntity(body));
    }

    @Transactional
    public int save(E entity) {
        return baseMapper.insert(entity);
    }

    @Transactional
    public int update(QuerySpec spec, Map<String, Object> body) {
        requireNarrowMutation(spec);
        return baseMapper.update(toEntity(body), buildConditions(spec));
    }

    @Transactional
    public int delete(QuerySpec spec) {
        requireNarrowMutation(spec);
        return baseMapper.delete(buildConditions(spec));
    }

    public Map<String, Object> readBody(BufferedReader reader) throws IOException {
        return objectMapper.readValue(reader, new TypeReference<Map<String, Object>>() { });
    }

    private QueryWrapper<E> buildConditions(QuerySpec spec) {
        QueryWrapper<E> wrapper = new QueryWrapper<>();
        spec.getFilters().forEach((column, value) -> {
            if (spec.isLike()) {
                wrapper.like(column, value);
            } else {
                wrapper.eq(column, value);
            }
        });
        spec.getMinimums().forEach(wrapper::ge);
        spec.getMaximums().forEach(wrapper::le);
        return wrapper;
    }

    private Object aggregate(QuerySpec spec, String operation) {
        if (spec.getFields().size() != 1) {
            throw new InvalidQueryException("聚合查询必须指定一个 field");
        }
        QueryWrapper<E> wrapper = buildConditions(spec);
        String expression = operation + "(" + spec.getFields().get(0) + ") AS aggregate_value";
        if (spec.getGroupBy() == null) {
            wrapper.select(expression);
        } else {
            wrapper.select(spec.getGroupBy(), expression).groupBy(spec.getGroupBy());
        }
        List<Map<String, Object>> rows = baseMapper.selectMaps(wrapper);
        if (spec.getGroupBy() != null) {
            return rows;
        }
        return rows.isEmpty() ? 0 : valueIgnoreCase(rows.get(0), "aggregate_value");
    }

    private Object valueIgnoreCase(Map<String, Object> row, String key) {
        for (Map.Entry<String, Object> entry : row.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(key)) {
                return entry.getValue();
            }
        }
        return null;
    }

    private void requireNarrowMutation(QuerySpec spec) {
        if (!spec.hasFilters()) {
            throw new InvalidQueryException("更新或删除操作必须指定有效筛选条件");
        }
    }

    private E toEntity(Map<String, Object> body) {
        if (body == null || body.isEmpty()) {
            throw new InvalidQueryException("请求体不能为空");
        }
        return objectMapper.convertValue(body, entityClass);
    }

    private String readOwnerColumn() {
        String entityName = entityClass.getSimpleName();
        if ("RegisteredUser".equals(entityName)) {
            return "user_id";
        }
        if ("SkinDetection".equals(entityName) || "OnlineConsultation".equals(entityName)) {
            return "registered_user";
        }
        return null;
    }

    private String mutationOwnerColumn() {
        String readOwner = readOwnerColumn();
        if (readOwner != null) {
            return readOwner;
        }
        String entityName = entityClass.getSimpleName();
        if ("Comment".equals(entityName) || "Praise".equals(entityName)
                || "Collect".equals(entityName) || "Hits".equals(entityName)) {
            return "user_id";
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private Class<E> resolveEntityClass() {
        Class<?> current = getClass();
        while (current != null) {
            Type generic = current.getGenericSuperclass();
            if (generic instanceof ParameterizedType) {
                ParameterizedType parameterized = (ParameterizedType) generic;
                if (parameterized.getRawType() instanceof Class
                        && BaseService.class.isAssignableFrom((Class<?>) parameterized.getRawType())) {
                    Type type = parameterized.getActualTypeArguments()[0];
                    if (type instanceof Class) {
                        return (Class<E>) type;
                    }
                }
            }
            current = current.getSuperclass();
        }
        throw new IllegalStateException("Cannot resolve BaseService entity type");
    }
}
