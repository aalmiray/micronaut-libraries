package com.agorapulse.micronaut.aws.kinesis.worker;

import com.agorapulse.micronaut.aws.kinesis.annotation.KinesisListener;
import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.KinesisClientLibConfiguration;
import com.amazonaws.services.kinesis.model.Record;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.event.ApplicationEventPublisher;
import io.micronaut.core.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.function.BiConsumer;

@Singleton
@Requires(
    classes = KinesisClientLibConfiguration.class,
    property = "aws.kinesis"
)
public class DefaultKinesisWorkerFactory implements KinesisWorkerFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(KinesisListener.class);
    private static final KinesisWorker NOOP = new KinesisWorker() {
        @Override
        public void start() {
            // do nothing
        }

        @Override
        public void shutdown() {
            // do nothing
        }

        @Override
        public void addConsumer(BiConsumer<String, Record> next) {
            // do nothing
        }
    };

    private final ExecutorService executorService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final Optional<AmazonDynamoDB> amazonDynamoDB;
    private final Optional<AmazonKinesis> kinesis;
    private final Optional<AmazonCloudWatch> cloudWatch;

    public DefaultKinesisWorkerFactory(ExecutorService executorService, ApplicationEventPublisher applicationEventPublisher, Optional<AmazonDynamoDB> amazonDynamoDB, Optional<AmazonKinesis> kinesis, Optional<AmazonCloudWatch> cloudWatch) {
        this.executorService = executorService;
        this.applicationEventPublisher = applicationEventPublisher;
        this.amazonDynamoDB = amazonDynamoDB;
        this.kinesis = kinesis;
        this.cloudWatch = cloudWatch;
    }

    @Override
    public KinesisWorker create(KinesisClientLibConfiguration kinesisConfiguration) {
        if (kinesisConfiguration == null) {
            return NOOP;
        }

        if (StringUtils.isEmpty(kinesisConfiguration.getApplicationName())) {
            LOGGER.error("Cannot setup listener Kinesis listener, application name is missing. Please, set 'aws.kinesis.application.name' property");
            return NOOP;
        }

        return new DefaultKinesisWorker(kinesisConfiguration, applicationEventPublisher, amazonDynamoDB, kinesis, cloudWatch);
    }
}
