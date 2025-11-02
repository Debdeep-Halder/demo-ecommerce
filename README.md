User Management (usermgmt)
-------------------------------
User Management service for the demo-ecommerce project. It handles user registration, authentication, authorization (roles/permissions), and basic profile operations. 
Designed as a Spring Boot service that can run standalone or as part of the full e-commerce stack.

Utilized:
1. Custom Authentication
2. JWT Authentication and Authorization involving Grants.
3. Authorized certain APIs for Admin Usage
4. Involved Redis to hold the user counts based on JWT Tokens.
5. Custom created Micrometer Gauges to create App metrics.
6. Involved Prometheus for repesenting custom metrics as TSDB.

Other Microservices Upcoming...
