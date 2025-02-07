package com.agorapulse.micronaut.aws.ses;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.SendRawEmailResult;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for sending emails with Java.
 */
public class SendEmailTest {

    @Rule public TemporaryFolder tmp = new TemporaryFolder();

    private AmazonSimpleEmailService simpleEmailService = mock(AmazonSimpleEmailService.class);

    private SimpleEmailService service = new DefaultSimpleEmailService(simpleEmailService);

    @Test
    public void testSendEmail() throws IOException {
        when(simpleEmailService.sendRawEmail(Mockito.any()))
            .thenReturn(new SendRawEmailResult().withMessageId("foobar"));

        File file = tmp.newFile("test.pdf");
        Files.write(file.toPath(), Collections.singletonList("not a real PDF"));
        String filepath = file.getCanonicalPath();

        EmailDeliveryStatus status = service.send(e ->                                  // <1>
            e.subject("Hi Paul")                                                        // <2>
                .from("subscribe@groovycalamari.com")                                   // <3>
                .to("me@sergiodelamo.com")                                              // <4>
                .htmlBody("<p>This is an example body</p>")                             // <5>
                .attachment(a ->                                                        // <6>
                    a.filepath(filepath)                                                // <7>
                        .filename("test.pdf")                                           // <8>
                        .mimeType("application/pdf")                                    // <9>
                        .description("An example pdf")                                  // <10>
                )
        );

        Assert.assertEquals(EmailDeliveryStatus.STATUS_DELIVERED, status);
    }
}
