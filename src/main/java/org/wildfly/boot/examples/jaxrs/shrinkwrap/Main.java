package org.wildfly.boot.examples.jaxrs.shrinkwrap;

import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.wildfly.boot.container.Container;
import org.wildfly.boot.shrinkwrap.ShrinkWrapDeployment;

/**
 * @author Bob McWhirter
 */
public class Main {

    public static void main(String[] args) throws Exception {
        System.err.println("running main");
        Container container = new Container();

        ShrinkWrapDeployment deployment = new ShrinkWrapDeployment("myarchive.jar");

        deployment.getArchive().addClass(MyApplication.class);
        deployment.getArchive().addClass(MyResource.class);
        StringAsset webXml = new StringAsset("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n" +
                "\n" +
                "<web-app version=\"3.0\" xmlns=\"http://java.sun.com/xml/ns/javaee\"\n" +
                "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "         xsi:schemaLocation=\"http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd\">\n" +
                "</web-app>");

        deployment.getArchive().addAsWebInfResource(webXml, "web.xml");

        container.start(deployment);
    }
}
