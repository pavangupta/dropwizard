package com.example.helloworld;

import com.example.helloworld.auth.ExampleAuthenticator;
import com.example.helloworld.cli.RenderCommand;
import com.example.helloworld.core.Template;
import com.example.helloworld.core.User;
import com.example.helloworld.health.TemplateHealthCheck;
import com.example.helloworld.resources.HelloWorldResource;
import com.example.helloworld.resources.ProtectedResource;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.auth.basic.BasicAuthBundle;
import com.yammer.dropwizard.bundles.AssetsBundle;
import com.yammer.dropwizard.config.Environment;

public class HelloWorldService extends Service<HelloWorldConfiguration> {
    public static void main(String[] args) throws Exception {
        new HelloWorldService().run(args);
    }

    private HelloWorldService() {
        super("hello-world");
        addCommand(new RenderCommand());
        addBundle(new AssetsBundle());
        addBundle(new BasicAuthBundle<User>(new ExampleAuthenticator(), "SUPER SECRET STUFF"));
    }

    @Override
    protected void initialize(HelloWorldConfiguration configuration,
                              Environment environment) {
        final Template template = configuration.buildTemplate();

        environment.addHealthCheck(new TemplateHealthCheck(template));
        environment.addResource(new HelloWorldResource(template));
        environment.addResource(new ProtectedResource());
    }

}
