package com.agorapulse.micronaut.http.examples.planets

import com.agorapulse.dru.Dru
import com.agorapulse.dru.dynamodb.persistence.DynamoDB
import com.agorapulse.gru.Gru
import com.agorapulse.gru.agp.ApiGatewayProxy
import com.amazonaws.services.dynamodbv2.datamodeling.IDynamoDBMapper
import io.micronaut.context.ApplicationContext
import org.junit.Rule
import spock.lang.Specification

/**
 * Test for planet controller.
 */
class PlanetControllerSpec extends Specification {

    @Rule private final Gru gru = Gru.equip(ApiGatewayProxy.steal(this) {               // <1>
        map '/planet/{star}' to MicronautHandler                                        // <2>
        map '/planet/{star}/{name}' to MicronautHandler
    })

    @Rule private final Dru dru = Dru.steal(this)

    void setup() {
        MicronautHandler.reset()                                                        // <3>
        MicronautHandler.applicationContext.with { ApplicationContext ctx ->
            ctx.registerSingleton(IDynamoDBMapper, DynamoDB.createMapper(dru))          // <4>
        }
        dru.add(new Planet(star: 'sun', name: 'mercury'))
        dru.add(new Planet(star: 'sun', name: 'venus'))
        dru.add(new Planet(star: 'sun', name: 'earth'))
        dru.add(new Planet(star: 'sun', name: 'mars'))
    }

    void 'get planet'() {                                                               // <5>
        expect:
            gru.test {
                get('/planet/sun/earth')
                expect {
                    json 'earth.json'
                }
            }
    }

    void 'get planet which does not exist'() {
        expect:
            gru.test {
                get('/planet/sun/vulcan')
                expect {
                    status NOT_FOUND
                }
            }
    }

    void 'list planets by existing star'() {
        expect:
            gru.test {
                get('/planet/sun')
                expect {
                    json 'planetsOfSun.json'
                }
            }
    }

    void 'add planet'() {
        when:
            gru.test {
                post '/planet/sun/jupiter'
                expect {
                    status CREATED
                    json 'jupiter.json'
                }
            }
        then:
            gru.verify()
            dru.findAllByType(Planet).size() == 5
    }

    void 'delete planet'() {
        given:
            dru.add(new Planet(star: 'sun', name: 'pluto'))
        expect:
            dru.findAllByType(Planet).size() == 5
            gru.test {
                delete '/planet/sun/pluto'
                expect {
                    status NO_CONTENT
                    json 'pluto.json'
                }
            }
            dru.findAllByType(Planet).size() == 4
    }

}
