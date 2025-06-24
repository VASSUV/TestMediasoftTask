# 🚀 STARTER-EXCAMAD.md

Инструкция по развёртыванию Helm-чарта для проекта **EXCAMAD** с использованием Minikube.
 
---

## 📦 1. Установка Helm-чарта

```bash # Установка чарта в namespace warehouse-ns
   helm install excamad ./deployment/excamad/ -n excamad-ns --create-namespace
```

```bash
   helm upgrade excamad ./deployment/excamad/ -n excamad-ns
```

```bash
   kubectl apply -f ./deployment/excamad/templates/deployment-excamad.yaml 
```
```bash
   kubectl apply -f ./deployment/excamad/templates/service-excamad.yaml
```

---

## 🧹 2. Удаление чарта и ресурсов

```bash
   helm uninstall excamad
```

---

## 🧪 3. Проброс IP для

```bash для examad сервиса
   kubectl port-forward svc/excamad-service 29999:9098 -n warehouse-ns
```
```bash для пода приложения
   kubectl port-forward -n warehouse-ns pod/$(kubectl get pods -n warehouse-ns --no-headers | sed -n '1p' | awk '{print $1}') 8080:8080 
```