# 🚀 STARTER-APP.md

Инструкция по развёртыванию Helm-чарта для проекта **warehouse-app** с использованием Minikube.

---

## 1. Сборка образа

```bash
   ./gradlew build -x test
```
```bash linux, unix
     eval $(minikube docker-env)
```
```bash windows
     minikube -p minikube docker-env --shell=powershell | Invoke-Expression
```
```bash
   sed -i '' '/^[[:space:]]*image:/,/^[^[:space:]]/ s/^[[:space:]]*tag: .*/  tag: v136/' ./deployment/helm-app/values.yaml
```
```bash
    docker build -t warehouse-app:v136 .
```

---

## 📦 2. Установка Helm-чарта

```bash # Установка чарта в namespace warehouse-ns
   helm install warehouse ./deployment/helm-app/ --namespace warehouse-ns --create-namespace
```

Если чарт уже установлен:

```bash
   helm upgrade warehouse ./deployment/helm-app/ --namespace warehouse-ns
```

---

## 🧹 3. Удаление чарта и ресурсов

```bash
   helm uninstall warehouse -n warehouse-ns
```
```bash
   kubectl delete namespace warehouse-ns
```

---

## 🔍 4. Проверка состояния ресурсов

### 🔧 Проверка всех компонентов

```bash
   kubectl get pods -n warehouse-ns
```
```bash
   kubectl get all -n warehouse-ns
```

---

## 🧪 5. Отладка

```bash лог первого пода в namespace
   kubectl logs -f $(kubectl get pods -n warehouse-ns --no-headers | sed -n '1p' | awk '{print $1}') -n warehouse-ns
```
```bash лог второго пода в namespace
   kubectl logs -f $(kubectl get pods -n warehouse-ns --no-headers | sed -n '2p' | awk '{print $1}') -n warehouse-ns
```
```bash describe первого пода в namespace
   kubectl describe pod $(kubectl get pods -n warehouse-ns --no-headers | sed -n '1p' | awk '{print $1}') -n warehouse-ns
```
```bash describe второго пода в namespace
   kubectl describe pod  $(kubectl get pods -n warehouse-ns --no-headers | sed -n '2p' | awk '{print $1}') -n warehouse-ns
```

---

## 🧪 6. Проброс IP для 

```bash для excamad
   kubectl port-forward -n warehouse-ns pod/$(kubectl get pods -n warehouse-ns --no-headers | sed -n '1p' | awk '{print $1}') 8888:8080 
```
```bash для app
   kubectl port-forward -n warehouse-ns pod/$(kubectl get pods -n warehouse-ns --no-headers | sed -n '2p' | awk '{print $1}') 8080:8080 
```

## 7. скрипты


```bash
   helm uninstall warehouse -n warehouse-ns
   kubectl delete namespace warehouse-ns
   
```
```bash
   ./gradlew build -x test
```
```bash
   eval $(minikube docker-env)
   sed -i '' '/^[[:space:]]*image:/,/^[^[:space:]]/ s/^[[:space:]]*tag: .*/  tag: v144/' ./deployment/helm-app/values.yaml
   docker build -t warehouse-app:v144 .
```
```bash
   helm install warehouse ./deployment/helm-app/ --namespace warehouse-ns --create-namespace
   kubectl get pods -n warehouse-ns
```
```bash
   helm upgrade warehouse ./deployment/helm-app/ --namespace warehouse-ns
   kubectl get pods -n warehouse-ns
```


```bash
   kubectl run pg-client --rm -it --restart=Never --image=postgres:15 --namespace=warehouse-ns --env="PGPASSWORD=password" --command -- psql -h postgresql-warehouse-postgres-service.warehouse-db-ns.svc.cluster.local -U admin -d warehouse_db
```

 
```bash
   kubectl exec -it $(kubectl get pods -n warehouse-ns --no-headers | sed -n '1p' | awk '{print $1}') -n warehouse-ns -- curl http://warehouse-warehouse-app:8080/api/products
```
```bash
   kubectl exec -it $(kubectl get pods -n warehouse-ns --no-headers | sed -n '1p' | awk '{print $1}') -n warehouse-ns -- curl http://warehouse-warehouse-app:9090/actuator/health
```
```bash
   kubectl exec -it $(kubectl get pods -n warehouse-ns --no-headers | sed -n '1p' | awk '{print $1}') -n warehouse-ns -- curl http://warehouse-warehouse-app:8080/engine-rest/engine
```


```bash
   kubectl exec -it $(kubectl get pods -n warehouse-ns --no-headers | sed -n '2p' | awk '{print $1}') -n warehouse-ns -- curl http://localhost:8080/api/products
```
```bash
   kubectl exec -it $(kubectl get pods -n warehouse-ns --no-headers | sed -n '2p' | awk '{print $1}') -n warehouse-ns -- curl http://localhost:9090/actuator/health
```
```bash
   kubectl exec -it $(kubectl get pods -n warehouse-ns --no-headers | sed -n '2p' | awk '{print $1}') -n warehouse-ns -- curl http://localhost:8080/engine-rest/engine
```


```bash
   kubectl exec -it svc/warehouse-warehouse-app -n warehouse-ns -- curl http://localhost:8080/api/products
```
```bash
   kubectl exec -it svc/warehouse-warehouse-app -n warehouse-ns -- curl http://localhost:9090/actuator/health
```
```bash
   kubectl exec -it svc/warehouse-warehouse-app -n warehouse-ns -- curl http://localhost:8080/engine-rest/engine
```


```bash
   curl http://minikube:8090/api/products
```
```bash
   curl http://minikube:8090/actuator/health
```
```bash
   curl http://minikube:8090/engine-rest/engine
```

