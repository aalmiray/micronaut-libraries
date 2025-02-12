package com.agorapulse.micronaut.aws.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

import java.util.Date;
import java.util.Objects;

@DynamoDBTable(tableName = "entity")
public class DynamoDBEntity {

    public static final String DATE_INDEX = "date";
    public static final String RANGE_INDEX = "rangeIndex";
    public static final String GLOBAL_INDEX = "globalIndex";

    @DynamoDBHashKey
    String parentId;

    @DynamoDBRangeKey
    String id;

    @DynamoDBIndexRangeKey(localSecondaryIndexName = RANGE_INDEX)
    String rangeIndex;

    @DynamoDBIndexRangeKey(localSecondaryIndexName = DATE_INDEX)
    Date date;

    Integer number = 0;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRangeIndex() {
        return rangeIndex;
    }

    public void setRangeIndex(String rangeIndex) {
        this.rangeIndex = rangeIndex;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @DynamoDBIndexHashKey(globalSecondaryIndexName = GLOBAL_INDEX)
    public String getGlobalIndex() {
        return parentId + ":" + id;
    }

    public void setGlobalIndex(String globalIndex) {
        // ignore
    }

    //CHECKSTYLE:OFF
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DynamoDBEntity that = (DynamoDBEntity) o;
        return Objects.equals(parentId, that.parentId) &&
            Objects.equals(id, that.id) &&
            Objects.equals(rangeIndex, that.rangeIndex) &&
            Objects.equals(date, that.date) &&
            Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parentId, id, rangeIndex, date, number);
    }

    @Override
    public String toString() {
        return "DynamoDBEntity{" +
            "parentId='" + parentId + '\'' +
            ", id='" + id + '\'' +
            ", rangeIndex='" + rangeIndex + '\'' +
            ", date=" + date +
            ", number=" + number +
            '}';
    }
    //CHECKSTYLE:ON
}
