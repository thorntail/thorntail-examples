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

package org.wildfly.swarm.examples.stm;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.arjuna.ats.arjuna.common.Uid;
import com.arjuna.ats.arjuna.AtomicAction;
import org.jboss.stm.Container;

/**
 * @author nmcl
 */

@Path("/")
public class MyResource {

    @GET
    @Produces("text/plain")
    public String init() throws Exception {
        return "Active";
    }

    @Path("stm")
    @GET
    @Produces("text/plain")
    public String stm() throws Exception  // dummy method name for now
    {
	/*
	 * STM states are identified by Uids in the ObjectStore. This is an example.
	 */

	Container<Sample> theContainer = new Container<Sample>("Demo", Container.TYPE.RECOVERABLE, Container.MODEL.EXCLUSIVE);
	Sample obj1 = theContainer.create(new SampleLockable(10));
	String str = "Object name: "+theContainer.getIdentifier(obj1)+"\n";

	str += "Transactional object value initially "+obj1.value()+"\n";
	
	for (int i = 0; i < 10; i++) {
	    AtomicAction A = new AtomicAction();

	    A.begin();

	    obj1.increment();

	    str += "Transactional object value incremented to " + obj1.value() + "\n";

	    A.commit();
	}

	return str;
    }

    @Path("stmPersistent")
    @GET
    @Produces("text/plain")
    public String stmPersistent() throws Exception  // dummy method name for now
    {
	/*
	 * STM states are identified by Uids in the ObjectStore. This is an example.
	 */

	Container<Sample> theContainer = new Container<Sample>("Demo", Container.TYPE.PERSISTENT, Container.MODEL.EXCLUSIVE);
	Sample obj1 = ((MyResource._theUid == null) ? theContainer.create(new SampleLockable(10)) : theContainer.clone(new SampleLockable(-1), MyResource._theUid));
	String str = "Object name: "+theContainer.getIdentifier(obj1)+"\n";

	AtomicAction B = new AtomicAction();

	B.begin();
	
	str += "Transactional object value initially "+obj1.value()+"\n";
	
	B.commit();
	
	if (MyResource._theUid == null)
	    MyResource._theUid = theContainer.getIdentifier(obj1);
	
	for (int i = 0; i < 10; i++) {
	    AtomicAction A = new AtomicAction();

	    A.begin();

	    obj1.increment();

	    str += "Transactional value incremented to " + obj1.value() + "\n";

	    A.commit();
	}

	return str;
    }    

    private static Uid _theUid = null;
}
