/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.wildfly.swarm.examples.transactions;

import org.wildfly.swarm.container.Container;
import org.wildfly.swarm.transactions.TransactionsFraction;
import org.wildfly.swarm.msc.ServiceDeployment;
import org.wildfly.swarm.jaxrs.JaxRsDeployment;

/**
 * @author nmcl
 */

public class Main
{
    public static void main (String[] args) throws Exception 
    {
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

	JaxRsDeployment appDeployment = new JaxRsDeployment();
        appDeployment.addResource(MyResource.class);

        container.deploy(appDeployment);
    }
}
