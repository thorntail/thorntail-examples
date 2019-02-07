# JAX-RS endpoint and a servlet secured via Java EE 8 Security API

This example serves a servlet and a JAX-RS endpoint secured via Java EE 8 Security API. 

> Please raise any issues found with this example in our JIRA:
> https://issues.jboss.org/browse/THORN

## Servlet Configuration

Servlet uses the `@BasicAuthenticationMechanismDefinition` annotation to indicate that basic HTTP authentication 
mechanism should be used.

```java
@BasicAuthenticationMechanismDefinition
@WebServlet("/servlet")
@DeclareRoles({"user", "admin"})
@ServletSecurity(@HttpConstraint( rolesAllowed = {"user", "admin"}))
public class SecuredServlet extends HttpServlet {
    // ...
}
```

The identities are defined in a custom identity store - an application scoped bean implementing the
`javax.security.enterprise.identitystore.IdentityStore` interface.

```java
@ApplicationScoped
public class CustomIdentityStore implements IdentityStore {
    public CredentialValidationResult validate(UsernamePasswordCredential userCredential) {
        // ...
    }
}
```

## Run

* Either build and execute an Uber-JAR:

  ```mvn package && java -jar ./target/example-ee-security-basic-thorntail.jar```
  
* or use the `thorntail:run` maven goal:

  ```mvn thorntail:run```

## Use

1. Visit http://localhost:8080/whoami to see the JAX-RS endpoint. It should report that you are not logged in:
  
   ```json
   {"logged in":false}
   ```

2. Visit http://localhost:8080/servlet to visit the secured servlet. Basic HTTP authentication should be prompted. Enter
  "user" as both username and password. You should see the servlet greeting you with a message "Hello user".
  
3. Go back to http://localhost:8080/whoami, which should report details about the authenticated principal:

   ```json
   {"logged in":true,"caller":"user","has role admin":false,"has role user":true}
   ```