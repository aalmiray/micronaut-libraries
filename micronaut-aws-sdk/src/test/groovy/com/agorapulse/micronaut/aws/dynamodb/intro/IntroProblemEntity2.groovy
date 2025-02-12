package com.agorapulse.micronaut.aws.dynamodb.intro

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable
import groovy.transform.Canonical
import groovy.transform.CompileStatic

/**
 * Testing entity.
 */
@Canonical
@CompileStatic
@DynamoDBTable(tableName = 'IntroProblemEntity2')
class IntroProblemEntity2 {

    @DynamoDBHashKey
    String hashKey

}
