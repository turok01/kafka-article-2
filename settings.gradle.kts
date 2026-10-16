
rootProject.name = "kafka-article-2"

includeBuild("build-logic")

include(":notification-service")
include(":inventory-service")
include(":analitycs-service")
include(":order-service")
include(":kafka-dto-contracts")
