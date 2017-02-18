package com.congxiaoyao.spot.pojo;

import java.util.ArrayList;
import java.util.List;

public class SpotExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public SpotExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andSpotIdIsNull() {
            addCriterion("spot_id is null");
            return (Criteria) this;
        }

        public Criteria andSpotIdIsNotNull() {
            addCriterion("spot_id is not null");
            return (Criteria) this;
        }

        public Criteria andSpotIdEqualTo(Long value) {
            addCriterion("spot_id =", value, "spotId");
            return (Criteria) this;
        }

        public Criteria andSpotIdNotEqualTo(Long value) {
            addCriterion("spot_id <>", value, "spotId");
            return (Criteria) this;
        }

        public Criteria andSpotIdGreaterThan(Long value) {
            addCriterion("spot_id >", value, "spotId");
            return (Criteria) this;
        }

        public Criteria andSpotIdGreaterThanOrEqualTo(Long value) {
            addCriterion("spot_id >=", value, "spotId");
            return (Criteria) this;
        }

        public Criteria andSpotIdLessThan(Long value) {
            addCriterion("spot_id <", value, "spotId");
            return (Criteria) this;
        }

        public Criteria andSpotIdLessThanOrEqualTo(Long value) {
            addCriterion("spot_id <=", value, "spotId");
            return (Criteria) this;
        }

        public Criteria andSpotIdIn(List<Long> values) {
            addCriterion("spot_id in", values, "spotId");
            return (Criteria) this;
        }

        public Criteria andSpotIdNotIn(List<Long> values) {
            addCriterion("spot_id not in", values, "spotId");
            return (Criteria) this;
        }

        public Criteria andSpotIdBetween(Long value1, Long value2) {
            addCriterion("spot_id between", value1, value2, "spotId");
            return (Criteria) this;
        }

        public Criteria andSpotIdNotBetween(Long value1, Long value2) {
            addCriterion("spot_id not between", value1, value2, "spotId");
            return (Criteria) this;
        }

        public Criteria andSpotNameIsNull() {
            addCriterion("spot_name is null");
            return (Criteria) this;
        }

        public Criteria andSpotNameIsNotNull() {
            addCriterion("spot_name is not null");
            return (Criteria) this;
        }

        public Criteria andSpotNameEqualTo(String value) {
            addCriterion("spot_name =", value, "spotName");
            return (Criteria) this;
        }

        public Criteria andSpotNameNotEqualTo(String value) {
            addCriterion("spot_name <>", value, "spotName");
            return (Criteria) this;
        }

        public Criteria andSpotNameGreaterThan(String value) {
            addCriterion("spot_name >", value, "spotName");
            return (Criteria) this;
        }

        public Criteria andSpotNameGreaterThanOrEqualTo(String value) {
            addCriterion("spot_name >=", value, "spotName");
            return (Criteria) this;
        }

        public Criteria andSpotNameLessThan(String value) {
            addCriterion("spot_name <", value, "spotName");
            return (Criteria) this;
        }

        public Criteria andSpotNameLessThanOrEqualTo(String value) {
            addCriterion("spot_name <=", value, "spotName");
            return (Criteria) this;
        }

        public Criteria andSpotNameLike(String value) {
            addCriterion("spot_name like", value, "spotName");
            return (Criteria) this;
        }

        public Criteria andSpotNameNotLike(String value) {
            addCriterion("spot_name not like", value, "spotName");
            return (Criteria) this;
        }

        public Criteria andSpotNameIn(List<String> values) {
            addCriterion("spot_name in", values, "spotName");
            return (Criteria) this;
        }

        public Criteria andSpotNameNotIn(List<String> values) {
            addCriterion("spot_name not in", values, "spotName");
            return (Criteria) this;
        }

        public Criteria andSpotNameBetween(String value1, String value2) {
            addCriterion("spot_name between", value1, value2, "spotName");
            return (Criteria) this;
        }

        public Criteria andSpotNameNotBetween(String value1, String value2) {
            addCriterion("spot_name not between", value1, value2, "spotName");
            return (Criteria) this;
        }

        public Criteria andLatitudeIsNull() {
            addCriterion("latitude is null");
            return (Criteria) this;
        }

        public Criteria andLatitudeIsNotNull() {
            addCriterion("latitude is not null");
            return (Criteria) this;
        }

        public Criteria andLatitudeEqualTo(Double value) {
            addCriterion("latitude =", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeNotEqualTo(Double value) {
            addCriterion("latitude <>", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeGreaterThan(Double value) {
            addCriterion("latitude >", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeGreaterThanOrEqualTo(Double value) {
            addCriterion("latitude >=", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeLessThan(Double value) {
            addCriterion("latitude <", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeLessThanOrEqualTo(Double value) {
            addCriterion("latitude <=", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeIn(List<Double> values) {
            addCriterion("latitude in", values, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeNotIn(List<Double> values) {
            addCriterion("latitude not in", values, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeBetween(Double value1, Double value2) {
            addCriterion("latitude between", value1, value2, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeNotBetween(Double value1, Double value2) {
            addCriterion("latitude not between", value1, value2, "latitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeIsNull() {
            addCriterion("longitude is null");
            return (Criteria) this;
        }

        public Criteria andLongitudeIsNotNull() {
            addCriterion("longitude is not null");
            return (Criteria) this;
        }

        public Criteria andLongitudeEqualTo(Double value) {
            addCriterion("longitude =", value, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeNotEqualTo(Double value) {
            addCriterion("longitude <>", value, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeGreaterThan(Double value) {
            addCriterion("longitude >", value, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeGreaterThanOrEqualTo(Double value) {
            addCriterion("longitude >=", value, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeLessThan(Double value) {
            addCriterion("longitude <", value, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeLessThanOrEqualTo(Double value) {
            addCriterion("longitude <=", value, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeIn(List<Double> values) {
            addCriterion("longitude in", values, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeNotIn(List<Double> values) {
            addCriterion("longitude not in", values, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeBetween(Double value1, Double value2) {
            addCriterion("longitude between", value1, value2, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeNotBetween(Double value1, Double value2) {
            addCriterion("longitude not between", value1, value2, "longitude");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}