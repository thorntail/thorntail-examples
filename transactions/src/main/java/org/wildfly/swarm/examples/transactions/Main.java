/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package org.wildfly.swarm.examples.transactions;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.jaxrs.JAXRSArchive;
import org.wildfly.swarm.transactions.TransactionsFraction;

/**
 * @author nmcl
 */

public class Main {
    public static void main(String[] args) throws Exception {
        Container container = new Container();

	/*
     * Use specific TransactionFraction even though it doesn't do
	 * any more than the default one - for now.
	 */

        container.subsystem(new TransactionsFraction(4712, 4713));

        // Start the container

        container.start();

	/*
     * Now register JAX-RS resource class.
	 */

        JAXRSArchive appDeployment = ShrinkWrap.create(JAXRSArchive.class);
        appDeployment.addResource(MyResource.class);

        container.deploy(appDeployment);
    }
}
