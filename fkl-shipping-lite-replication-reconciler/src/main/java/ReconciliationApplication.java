import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class ReconciliationApplication extends Application<ReconciliationConfiguration> {
    public ReconciliationApplication() {
    }

    public void initialize(Bootstrap<ReconciliationConfiguration> bootstrap) {
        RedisBundle<ReconciliationConfiguration> redisBundle = new RedisBundle<ReconciliationConfiguration>(){
            @Override
            public RedisConfiguration getRedisConfiguration(ReconciliationConfiguration configuration) {
                return configuration.getRedisConfiguration();
            }
        };
        bootstrap.addBundle(redisBundle);
    }

    public static void main(String[] args) throws Exception {
        (new ReconciliationApplication()).run(args);
    }

    public void run(final ReconciliationConfiguration conf, Environment environment) throws Exception {
        Injector baseInjector = Guice.createInjector(new AbstractModule() {
            protected void configure() {
                this.bind(ReconciliationConfiguration.class).toInstance(conf);
                this.bind(ReconciliationResource.class);
                this.bind(ReconcilerDataStore.class).to(RedisDataStore.class);
            }
        });
        environment.jersey().register(baseInjector.getInstance(ReconciliationResource.class));
    }
}