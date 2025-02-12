package com.agorapulse.micronaut.aws.kinesis.worker

import com.amazonaws.services.kinesis.clientlibrary.lib.worker.KinesisClientLibConfiguration
import groovy.transform.CompileStatic
import io.micronaut.context.annotation.EachProperty
import io.micronaut.context.annotation.Parameter
import io.micronaut.context.annotation.Requires
import io.micronaut.context.annotation.Value

/**
 * Kinesis listener configuration for given configuration key.
 */
@CompileStatic
@EachProperty('aws.kinesis.listeners')
@Requires(classes = KinesisClientLibConfiguration)
class NamedKinesisClientConfiguration extends KinesisClientConfiguration {

    final String name

    NamedKinesisClientConfiguration(
        @Parameter String name,
        @Value('${aws.kinesis.application.name:}') String applicationName,
        @Value('${aws.kinesis.worker.id:}') String workerId
    ) {
        super(applicationName, workerId)
        this.name = name
    }

}
