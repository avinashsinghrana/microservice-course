# Keycloak Configuration for Microservices

## Architecture

```text
Client
   |
   | JWT Token
   v
API Gateway (8084)
   |
   +--> Auth Service (8086)
   |
   +--> User Service (8081)
   |
Keycloak (8080)
```

---

# 1. Create Realm

1. Open Keycloak Admin Console.
2. Create a new realm.

```text
Realm Name: oauth-test
```

---

# 2. Create Master Service Client

This client is used by `auth-service` to dynamically create and manage Keycloak clients.

### Client Configuration

```text
Client ID: oauth-test-service-client
Name: Master Service Client
```

Enable:

```text
✓ Client Authentication
✓ Standard Flow
✓ Direct Access Grants
✓ Service Accounts Roles
```

### Redirect URLs

```text
http://localhost:8084/login/oauth2/code/keycloak
```

### Web Origins

```text
http://localhost:8084
```

Save the client.

---

# 3. Assign Realm Management Roles

Navigate:

```text
Clients
  -> oauth-test-service-client
  -> Service Account Roles
```

Assign roles from:

```text
realm-management
```

Required roles:

```text
manage-clients
view-clients
```

This enables dynamic client registration.

---

# 4. Create Realm Roles

Create the following realm roles:

```text
admin
user
```

---

# 5. Obtain Client Secret

Navigate:

```text
Clients
  -> oauth-test-service-client
  -> Credentials
```

Copy:

```text
Client Secret
```

---

# 6. API Gateway Configuration

## application.properties

```properties
server.port=8084
spring.application.name=api-gateway

eureka.client.service-url.defaultZone=http://localhost:8761/eureka/

spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8080/realms/oauth-test

spring.cloud.gateway.server.webmvc.routes[0].id=USER-SERVICE
spring.cloud.gateway.server.webmvc.routes[0].uri=lb://USER-SERVICE
spring.cloud.gateway.server.webmvc.routes[0].predicates[0]=Path=/user-service/**
spring.cloud.gateway.server.webmvc.routes[0].filters[0]=StripPrefix=1

spring.cloud.gateway.server.webmvc.routes[1].id=AUTH-SERVICE
spring.cloud.gateway.server.webmvc.routes[1].uri=lb://AUTH-SERVICE
spring.cloud.gateway.server.webmvc.routes[1].predicates[0]=Path=/auth-service/**
spring.cloud.gateway.server.webmvc.routes[1].filters[0]=StripPrefix=1
```

## Security Configuration

```java
@Configuration
@EnableWebSecurity
public class GatewaySecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth-service/**").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(
                oauth2 -> oauth2.jwt(Customizer.withDefaults())
            );

        return http.build();
    }
}
```

### Gateway Responsibilities

* Validate JWT signature
* Validate token expiration
* Validate issuer
* Reject invalid tokens
* Forward authenticated requests

---

# 7. Auth Service Configuration

## application.properties

```properties
server.port=8086
spring.application.name=auth-service

eureka.client.service-url.defaultZone=http://localhost:8761/eureka/

keycloak.server-url=http://localhost:8080
keycloak.realm=oauth-test
keycloak.client-id=oauth-test-service-client
keycloak.client-secret=<CLIENT_SECRET>
```

## Responsibilities

* Dynamic client registration
* Client management
* User management
* Role assignment

---

# 8. Dynamic Client Registration

Example:

```java
Keycloak keycloakAdmin = KeycloakBuilder.builder()
        .serverUrl(serverUrl)
        .realm(realm)
        .grantType("client_credentials")
        .clientId(masterClientId)
        .clientSecret(masterClientSecret)
        .build();
```

New clients are created with:

```text
serviceAccountsEnabled=true
publicClient=false
protocol=openid-connect
```

---

# 9. User Service Configuration

## application.properties

```properties
server.port=8081
spring.application.name=user-service

spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8080/realms/oauth-test

spring.security.strategy=MODE_INHERITABLETHREADLOCAL
```

## Authorization Rules

```java
.authorizeHttpRequests(auth -> auth
    .requestMatchers(HttpMethod.DELETE, "/users/**")
        .hasRole("admin")
    .requestMatchers("/users/admin/**")
        .hasRole("admin")
    .anyRequest()
        .authenticated()
)
```

---

# 10. Keycloak Role Mapping

Convert Keycloak roles into Spring Security authorities.

Example:

```text
admin -> ROLE_admin
user  -> ROLE_user
```

Used by:

```java
JwtAuthenticationConverter
```

---

# 11. Logging User Information Using MDC

JWT claims can be added to MDC for traceability.

Claims:

```text
preferred_username
azp
client_id
```

Log example:

```text
2026-05-31 10:00:00 INFO
[User: john]
[Client: mobile-app]
User fetched successfully
```

---

# 12. Testing

## Obtain Access Token

```bash
POST /realms/oauth-test/protocol/openid-connect/token
```

Grant Type:

```text
client_credentials
```

## Call Protected API

```bash
curl \
-H "Authorization: Bearer <TOKEN>" \
http://localhost:8084/user-service/users/1
```

Expected:

```text
200 OK
```

Invalid token:

```text
401 Unauthorized
```

Missing role:

```text
403 Forbidden
```

---

# Production Recommendations

* Use HTTPS everywhere.
* Store client secrets in Vault or Secrets Manager.
* Use short-lived access tokens.
* Enable refresh tokens for user login flows.
* Enable distributed tracing.
* Enable audit logging.
* Avoid exposing Keycloak directly to public traffic.
* Keep JWT validation at API Gateway and downstream services.
* Use role-based authorization at service level.

```
```
