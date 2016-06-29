package org.wildfly.swarm.examples.management.console;

import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.management.ManagementFraction;

public class Main {

	public static void main(String[] args) throws Exception {
		Swarm swarm = new Swarm();
		swarm.fraction(
				ManagementFraction.createDefaultFraction()
						.httpInterfaceManagementInterface((iface) -> {
							iface.allowedOrigin("http://localhost:8080");
							iface.securityRealm("ManagementRealm");
						})
						.securityRealm("ManagementRealm", (realm) -> {
							realm.inMemoryAuthentication( (authn)->{
								authn.add( "bob", "tacos!", true );
							});
							realm.inMemoryAuthorization( (authz)->{
								authz.add( "bob", "admin" );
							});
						})
		);
		swarm.start();
		swarm.deploy();
	}
}
