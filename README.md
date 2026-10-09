# Getting Started

### You need some Kafka container
Use docker-compose file:   
to start: docker compose up -d   
to stop: docker compose down

Kafka UI will be here: http://localhost:18080/ui/clusters/course/all-topics?fts=false

### In PowerShell call POST request to add order:
```
Invoke-RestMethod -Uri "http://localhost:8080/api/order" -Method Post -Body '{"email":"my@mail.com", "productName":"Iphone 18 Pro Max Burgundy", "quantity":4}' -ContentType "application/json"
```

In Inventory repository we have next Items:
```
inventory.put("Iphone 18 Pro", 5);
inventory.put("Iphone 18 Pro Max Burgundy", 10);
inventory.put("Mac Pro", 6);
```

Commit 1 - learn.  
Commit 2 - Use PostgreSQL and idempotent producers
Commit 3 - idempotent consumer (inventory-service) - create table with saving ID - primary key (UNIQUE),
orderId for example. - Это защитит от дупликации обработки входящих сообщений (пришел заказ), чтобы защититься 
от падения сервиса и НЕ передачи в Кафку используем Outbox-Pattern (Event-Driven Architecture).