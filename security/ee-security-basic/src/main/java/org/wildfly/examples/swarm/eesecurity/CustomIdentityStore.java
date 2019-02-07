package org.wildfly.examples.swarm.eesecurity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;

/**
 * Custom identity store with couple of sample users.
 */
@ApplicationScoped
public class CustomIdentityStore implements IdentityStore {

    private Map<String, String> users = new HashMap<>();

    public CustomIdentityStore() {
        users.put("admin", "admin");
        users.put("user", "user");
    }

    public CredentialValidationResult validate(UsernamePasswordCredential userCredential) {
        String caller = userCredential.getCaller();
        if (users.containsKey(caller)) {
            if (userCredential.compareTo(caller, users.get(caller))) {
                return new CredentialValidationResult(caller,
                        // everybody belongs to "user" group, additionally add group with the same name as username
                        new HashSet<>(Arrays.asList(caller, "user")));
            }
        }
        return CredentialValidationResult.INVALID_RESULT;
    }
}