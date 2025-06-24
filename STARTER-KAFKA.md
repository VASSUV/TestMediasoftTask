# 🚀 STARTER-KAFKA.md

Инструкция по развёртыванию Helm-чарта для проекта **kafka** с использованием Minikube.
 
---

## 📦 1. Установка Helm-чарта

```bash # Установка чарта в namespace warehouse-ns
   helm install kafka-kraft ./deployment/kafka/
```
```bash # Установка kafkaUi
   helm install kafka-ui kafka-ui/kafka-ui -f ./deployment/kafka/kafka-ui-values.yaml
```
```bash # Запуск KafkaUi
   minikube service kafka-ui  
```

---

## 🧹 2. Удаление чарта и ресурсов

```bash
   helm uninstall kafka-kraft
   helm uninstall kafka-ui
```