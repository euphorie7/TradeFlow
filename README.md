# TradeFlow

Spring Framework
│
├── Spring Core
│   └── IoC / Beans / Injection de dépendances
│
├── Spring MVC
│   └── Web
│       └── DispatcherServlet
│           └── Servlet API (Jakarta)
│
├── Spring Data
│   └── accès aux données
│
├── Spring Security
│   └── authentification / autorisation
│
└── plein d'autres fonctionnalités


Java / Jakarta
      ↑
Spring Framework
├── Core (DI, Beans)
├── MVC
├── Security
├── Data
└── ...
      ↑
Spring Boot
→ auto-configuration
→ starters
→ serveur embarqué (Tomcat)
→ application directement exécutable
→ application.properties
→ Actuator...

==================================================================

import jakarta.persistence.Entity;

@Entity
public class User {
}

 -> @Entity vient de Jakarta Persistence (JPA) :
    ------------------------------------------
    Cette classe est une entité persistante que JPA/Hibernate doit mapper vers un  table de BDD.

==================================================================

   TU ÉCRIS
   │
   ├── User.java (@Entity)
   │
   └── UserRepository.java (interface)
             │
             ▼
      Spring Data JPA
      génère l'implémentation
      du Repository
             │
             ▼
          Hibernate
      traduit les opérations
      JPA en SQL
             │
             ▼
         PostgreSQL


====================================================================

exemple de repo brut 

@Repository
public class UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public User findByEmail(String email) {

        return entityManager
            .createQuery(
                "SELECT u FROM User u WHERE u.email = :email",
                User.class
            )
            .setParameter("email", email)
            .getSingleResult();
    }
}



->'EntityManager, c'est l'API JPA de bas niveau qui permet de manipuler directement les entités :

entityManager.persist(user);     // INSERT
entityManager.find(User.class, 1L); // SELECT
entityManager.remove(user);      // DELETE



===================================================================================

UserNotFoundException extends RuntimeException

-> on la laisse remonter jusqu'à Spring MVC, qui la transforme en 404 grâce à @ResponseStatus (Unchecked exception).

methodB()
   ↓ throw
methodA()         ← ne catch pas
   ↓
main()            ← ne catch pas
   ↓
JVM
   ↓
Programme terminé + stack trace



==================================================================================

User Service
├── API / Swagger → localhost:8081
└── Actuator      → localhost:9001

http://localhost:3000/swagger
http://localhost:3000/api-docs
http://localhost:3001/actuator/health

==================================================================================
===================================================================================
Spring Boot au démarrage : 
--------------------------

                SPRING BOOT
                    │
                    ▼
            ApplicationContext
                    │
          component scanning
                    │
      ┌─────────────┼─────────────┐
      ▼             ▼             ▼
 @Controller     @Service     @Component
      │             │             │
      ▼             ▼             ▼
    Bean           Bean          Bean


au runtime : 
------------

Client
  │
  │ HTTP
  ▼
┌─────────────────────┐
│       TOMCAT        │
│ Servlet Container   │
│                     │
│ comprend HTTP       │
│ écoute port 8080    │
│ crée request/resp.  │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│ DispatcherServlet   │
│                     │
│ comprend Spring MVC │
│ trouve Controller   │
└──────────┬──────────┘
           │
           ▼
     UserController



==========================================================================
dans le modèle Spring MVC classique avec Tomcat , une requête est traitée par un thread pendant son traitement



              TOMCAT
                │
           Thread Pool
      ┌─────────┼─────────┐
      ▼         ▼         ▼
   Thread 1  Thread 2  Thread 3
      │         │         │
 Request A   Request B   Request C
      │         │         │
   Filters   Filters   Filters
      │         │         │
 Dispatcher Dispatcher Dispatcher
      │         │         │
 Controller Controller Controller
      │         │         │
  Service     Service    Service



               Thread Pool Tomcat
               -------------------
      T1   T2   T3   T4 ... T200
      ↑
      │
nouvelle requête
      │
Tomcat prend un thread disponible


Thread-42
   │
   ├── Filter
   ├── Spring Security
   ├── DispatcherServlet
   ├── Interceptor
   ├── Controller
   ├── Service
   ├── Repository
   └── réponse


servlet jakarta ~ express  : un objet Java capable de recevoir une requête HTTP et de produire une réponse HTTP, mecanisme de jakartaEE anciennement J2E (JEE).


une servlet c'est quoi  ? 

Navigateur                         Java
   │
   │ GET /hello
   ▼
┌──────────┐
│  TOMCAT  │
└────┬─────┘
     │
     │ "Qui traite /hello ?"
     ▼
┌──────────────────┐
│   HelloServlet   │  ← SERVLET
│                  │
│     doGet()      │
└────────┬─────────┘
         │
         ▼
     "Bonjour"
         │
         ▼
      TOMCAT
         │
         ▼
    Navigateur

une Servlet écrite à la main peut mélanger ce qu'on sépare aujourd'hui entre Controller et logique applicative !

historiquement tu pouvais avoir :

Servlet
├── Routing HTTP
├── récupération paramètres
├── logique métier
├── accès DB
└── construction réponse


et donc le cycle de vie d;ne requette est : 
1. Requête HTTP arrive
        ↓
2. TOMCAT la reçoit
        ↓
3. Tomcat prend un THREAD disponible
        ↓
4. Tomcat parse HTTP et prépare :
   - HttpServletRequest
   - HttpServletResponse
        ↓
5. Tomcat détermine la Servlet correspondant à l’URL
   → dans une appli Spring MVC classique :
     DispatcherServlet
        ↓
6. Tomcat exécute les Filters
        ↓
7. La chaîne arrive au DispatcherServlet
        ↓
8. Tomcat appelle :
   DispatcherServlet.service(request, response)
        ↓
9. Spring MVC prend le relais
        ↓
10. HandlerMapping cherche :
    "Quelle méthode Controller correspond ?"
        ↓
11. Trouve par exemple :
    UserController.getUser(42)
        ↓
12. HandlerInterceptor(s)
        ↓
13. HandlerAdapter prépare les paramètres
        ↓
14. Appel du Controller
        ↓
15. Service
        ↓
16. Repository
        ↓
17. Résultat remonte
        ↓
18. Spring MVC sérialise en JSON
        ↓
19. Remplit HttpServletResponse
        ↓
20. Retour à travers les Filters
        ↓
21. Tomcat envoie la réponse HTTP
        ↓
22. Le thread retourne dans le pool

===========================================================================

NESTJS                         SPRING MVC

ExecutionContext               HandlerMethod
      ↓                             ↓
context.getHandler()           getMethod()
      ↓                             ↓
Reflector.get(...)             getMethodAnnotation(...)
      ↓                             ↓
metadata                       annotation


------------------------------------------------------------------------


                 NESTJS                              SPRING MVC
────────────────────────────────────────────────────────────────────

           ExecutionContext                         HandlerMethod
                  │                                      │
      ┌───────────┼───────────┐              ┌───────────┼───────────┐
      ▼           ▼           ▼              ▼           ▼           ▼
   Request      Handler      Class         Request      Method       Bean
      │           │           │              │           │           │
      ▼           ▼           ▼              ▼           ▼           ▼
GET /users/5   getUser() UserController   GET /users/5 getUser() UserController


---------------------------------------------------------------------------

@SetMetadata('roles', ['ADMIN'])         getUsers()
@Get('/users')                               │          
getUsers() {}                                └── metadata
                                                   │
                                                   └── "roles" → ["ADMIN"]


const roles = this.reflector.get(
    'roles',
    context.getHandler()
);

context.getHandler()
        ↓
     getUsers()
        ↓
reflector.get("roles", getUsers)
        ↓
"cherche la metadata dont
 la clé est 'roles'
 sur cette méthode"
        ↓
["ADMIN"]

              MÉMOIRE NODE.JS
        ┌─────────────────────────┐
        │ UserController          │
        │                         │
        │ getUser()               │
        │   metadata: ADMIN       │
        └────────────▲────────────┘
                     │
                     │ référence
                     │
ExecutionContext ─────┘
      │
      ├── getHandler() → getUser()
      │
      └── switchToHttp()
                ↓
             Request


------------------- spring boot ?  ------------------------------


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresRole {
    String[] value();
}

@RequiresRole({"ADMIN"})
@GetMapping("/users")
public List<User> getUsers() {
    ...
}

getUsers()
    │
    └── annotation
           │
           └── RequiresRole
                    │
                    └── value → ["ADMIN"]




@Injectable()
export class RolesGuard implements CanActivate {

    canActivate(context: ExecutionContext): boolean {
        return true;
    }
}


@Component
public class RolesInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
        HttpServletRequest request,
        HttpServletResponse response,
        Object handler
    ) {
        return true;
    }
}



NESTJS                         SPRING MVC

ExecutionContext              HandlerMethod + request
      │                             │
getHandler()                  getMethod()
      │                             │
Reflector.get(...)            getMethodAnnotation(...)
      │                             │
metadata                      annotation








=============================================================================
Transporter des informations (claims) tout en garantissant qu’elles n’ont pas été falsifiées et qu’elles proviennent d’un émetteur de confiance.
============================================================================










