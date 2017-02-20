package com.blacknebula.familySide;

import com.mongodb.BasicDBObject;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.function.Consumer;

import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author hazem
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class ApplicationTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Before
    public void setUp() throws Exception {
        clearMongo();
        initMocks(this);
    }

    /**
     * Remove all data from collections (expect system collections). Faster than
     * a simple dropDatabase.
     */
    private void clearMongo() throws Exception {
        mongoTemplate.getDb().listCollectionNames()
                .forEach((Consumer<? super String>) name -> {
                    if (!name.startsWith("system.")) {
                        mongoTemplate.getDb().getCollection(name)
                                .deleteMany(new BasicDBObject());
                    }
                });
    }
}
