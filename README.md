# Roadmap Java & Spring Boot

> Objectif : maîtriser Java/Spring Boot pour développer des backends robustes,
> sécurisés et distribués, en appliquant les concepts dans le projet **TradeFlow**.

---

## 1. Java moderne

- [x] Classes, interfaces, héritage, polymorphisme
- [x] Exceptions checked / unchecked
- [x] Collections (`List`, `Set`, `Map`)
- [x] Génériques
- [x] `Optional`
- [x] Streams et lambdas
- [x] Records / Enums

---

## 2. Fondamentaux de Spring

### Spring Framework
- [x] IoC — Inversion of Control
- [x] Dependency Injection
- [x] Beans
- [x] `ApplicationContext`
- [x] Component scanning
- [x] `@Component`
- [x] `@Service`
- [x] `@Repository`
- [x] `@Configuration` / `@Bean`

### Spring Boot
- [x] Auto-configuration
- [x] Starters
- [x] `application.properties`
- [x] Serveur Tomcat embarqué
- [x] Profils et configuration
- [x] Actuator

---

## 3. Spring MVC & API REST

- [x] Servlet API / Jakarta
- [x] Tomcat et Thread Pool
- [x] `DispatcherServlet`
- [x] `HandlerMapping`
- [x] `HandlerAdapter`
- [x] Controllers REST
- [x] `@RequestMapping`
- [x] `@GetMapping`, `@PostMapping`, etc.
- [x] DTO
- [x] Validation
- [x] Sérialisation JSON
- [x] Gestion des exceptions
- [x] `@ControllerAdvice`
- [x] Filters
- [x] `HandlerInterceptor`

### Cycle d'une requête

HTTP Request
→ Tomcat
→ Filter Chain
→ DispatcherServlet
→ HandlerMapping
→ Interceptor
→ Controller
→ Service
→ Repository
→ Response

---

## 4. Persistance — Spring Data JPA

- [x] JPA / Jakarta Persistence
- [x] Hibernate
- [x] `@Entity`
- [x] `@Id`
- [x] `@GeneratedValue`
- [x] `JpaRepository`
- [x] Requêtes dérivées
- [x] `EntityManager`
- [x] Relations JPA
  - [x] `@OneToMany`
  - [x] `@ManyToOne`
  - [ ] `@OneToOne`
  - [ ] `@ManyToMany`
- [x] PostgreSQL
- [x] Contraintes / index
- [x] Transactions avec `@Transactional`
- [ ] Problème N+1
- [ ] JPQL
- [ ] Pagination

---

## 5. Spring Security

- [x] `SecurityFilterChain`
- [x] Authentification
- [x] Autorisation
- [x] JWT
- [x] Claims
- [x] BCrypt / `PasswordEncoder`
- [x] `Authentication`
- [x] `SecurityContext`
- [x] `@AuthenticationPrincipal`
- [x] 401 vs 403
- [x] `AuthenticationEntryPoint`
- [x] `AccessDeniedHandler`
- [ ] Autorisation par rôles
- [x] `@PreAuthorize`
- [x] OAuth2 / OpenID Connect

---


## 7. Architecture microservices

### Architecture
- [x] Découpage par domaine
- [x] Base de données par microservice
- [x] Communication REST
- [x] Communication événementielle
- [ ] API Gateway
- [ ] Service discovery
- [ ] Configuration distribuée

### TradeFlow

User Service
→ authentification / utilisateurs

Order Service
→ gestion et exécution des ordres

Portfolio Service
→ portefeuilles / positions

---

## 8. Apache Kafka

- [x] Broker
- [x] Controller / KRaft
- [x] Topics
- [x] Partitions
- [x] Producer
- [x] Consumer
- [x] Consumer Groups
- [x] Offsets
- [x] Sérialisation JSON
- [x] `KafkaTemplate`
- [x] `@KafkaListener`
- [x] Clé de partitionnement
- [x] Retries
- [ ] ErrorHandlingDeserializer
- [ ] Dead Letter Topic (DLT)
- [ ] Idempotence des consumers
- [x] Outbox Pattern
- [ ] Garanties at-most-once / at-least-once / exactly-once

### Flux TradeFlow

Order Service
    │
    │ OrderExecutedEvent
    ▼
Apache Kafka
    │
    ▼
Portfolio Service
    │
    ├── mise à jour du cash
    └── mise à jour des positions

---

## 9. Docker & infrastructure

- [x] Docker
- [x] Docker Compose
- [x] PostgreSQL conteneurisé
- [x] Kafka conteneurisé
- [x] Portainer
- [x] pgAdmin
- [ ] Dockeriser chaque microservice
- [ ] Réseau Docker interne
- [ ] Health checks
- [ ] Variables d'environnement / secrets

---

## 10. Observabilité

- [x] Spring Boot Actuator
- [ ] Logs structurés
- [ ] Micrometer
- [ ] Prometheus
- [ ] Grafana
- [ ] Distributed tracing
- [ ] OpenTelemetry

---

## 11. CI/CD

- [x] Git
- [x] Maven
- [x] Docker
- [x] Jenkins
- [ ] Pipeline TradeFlow
- [ ] Build automatique
- [ ] Tests automatiques
- [ ] Build des images Docker
- [ ] Déploiement automatisé

---

## 12. Concepts avancés Spring

- [x] Événements Spring
- [x] `ApplicationEventPublisher`
- [x] `@TransactionalEventListener`
- [x] `AFTER_COMMIT`
- [ ] Spring AOP
- [ ] Proxy Spring
- [ ] Fonctionnement interne de `@Transactional`
- [ ] Gestion avancée des transactions
- [ ] Scheduling
- [ ] Cache

---

# Progression recommandée

Java
↓
Spring Core / IoC / DI
↓
Spring Boot
↓
Spring MVC / REST
↓
Spring Data JPA
↓
Spring Security
↓
Tests
↓
Microservices
↓
Kafka
↓
Fiabilité distribuée
↓
Docker / Infrastructure
↓
Observabilité
↓
CI/CD