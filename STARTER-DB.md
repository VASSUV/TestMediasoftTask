# 🚀 STARTER-DB.md

Инструкция по развёртыванию Helm-чарта для проекта **warehouse-db** с использованием Minikube.

---

## 📦 1. Установка Helm-чарта

```bash # Установка чарта в namespace warehouse-ns
   helm install warehouse-db ./deployment/helm-db/ --namespace warehouse-db-ns --create-namespace
```

Если чарт уже установлен:

```bash
   helm upgrade warehouse-db ./deployment/helm-db/ --namespace warehouse-db-ns
```

---

## 🔍 2. Проверка состояния ресурсов

### 🔧 Проверка всех компонентов

```bash
   kubectl get pods -n warehouse-db-ns
```
```bash
   kubectl get all -n warehouse-db-ns
```

### 🔧 Проверка PostgreSQL StatefulSet

```bash
   kubectl get statefulsets -n warehouse-db-ns
   kubectl get pvc -n warehouse-db-ns
```

---

## 🧹 3. Удаление чарта и ресурсов

```bash
   helm uninstall warehouse-db -n warehouse-db-ns
   kubectl delete namespace warehouse-db-ns
```

---

## 🧪 4. Отладка
warehouse-db-ns
```bash
   kubectl describe pod $(kubectl get pods -n warehouse-db-ns --no-headers | sed -n '2p' | awk '{print $1}') -n warehouse-db-ns
```
```bash
   kubectl logs -f $(kubectl get pods -n warehouse-db-ns --no-headers | sed -n '2p' | awk '{print $1}') -n warehouse-db-ns
```

---

## 🧪 5. Проброс IP для 

```bash для бд
   kubectl port-forward -n warehouse-db-ns pod/$(kubectl get pods -n warehouse-db-ns --no-headers | sed -n '2p' | awk '{print $1}') 5432:5432 
```