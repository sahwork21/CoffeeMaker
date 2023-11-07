package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.ncsu.csc.CoffeeMaker.TestConfig;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
class CustomerTest {

    /**
     * Test the class v
     */
    @Test
    public void testClassChecker () {
        fail( "Not yet implemented" );
    }

    /**
     * Test that the getters and setters work properly
     */
    @Test
    public void testGettersAndSetters () {

    }

    /**
     * Test that the service classes work
     */

}
