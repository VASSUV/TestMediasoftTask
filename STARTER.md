# 🚀 STARTER.md

Инструкция по развёртыванию Helm-чарта для проекта **warehouse-app** с использованием Minikube.

---

## 🔧 1. Запуск Minikube

```bash # Включаем кластер Minikube
   minikube start
```
```bash # Включаем кластер Minikube в Windows
   minikube start --driver=hyperv --hyperv-virtual-switch="minikube-switch"
```

---

## 🛠 2. Установка nginx-ingress (если ещё не установлен)

```bash перезапустить WebHook подсистему
   kubectl delete -A ValidatingWebhookConfiguration ingress-nginx-admission
```
```bash перезапустить WebHook подсистему
   minikube -p minikube docker-env --shell=powershell | Invoke-Expression
```
```bash добавления плагин ingress
   minikube addons enable ingress
```
```bash удаление плагин ingress
   minikube addons disable ingress
```

Проверь, что ingress работает:

```bash
   kubectl get pods -n ingress-nginx
   kubectl get svc -n ingress-nginx
```

---

## 3. Сборка образа

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
    docker build -t warehouse-app:v17 .
```

---

## 📦 4. Установка Helm-чарта

```bash # Установка чарта в namespace warehouse-ns
   helm install warehouse ./deployment/helm/ --namespace warehouse-ns --create-namespace
```

```bash # Установка чарта в namespace warehouse-ns
   helm install kafka-kraft ./deployment/kafka/
```
```bash # Установка kafkaUi
   helm install kafka-ui kafka-ui/kafka-ui -f ./deployment/kafka/kafka-ui-values.yaml
```
```bash # Запуск KafkaUi
   minikube service kafka-ui  
```


Если чарт уже установлен:

```bash
   helm upgrade warehouse ./deployment/helm/ --namespace warehouse-ns
```

---

## 🔍 5. Проверка состояния ресурсов

### 🔧 Проверка всех компонентов

```bash
   kubectl get pods -n warehouse-ns
```
```bash
   kubectl get all -n warehouse-ns
```

### 🔧 Проверка PostgreSQL StatefulSet

```bash
   kubectl get statefulsets -n warehouse-ns
   kubectl get pvc -n warehouse-ns
```

---

## 🌐 6. Проверка Ingress

### Узнать IP Ingress-контроллера:

```bash
   minikube ip
```

### Пример запроса:

```bash
   curl http://$(minikube ip)/actuator/health
```

или:

```bash
   curl http://minikube/test/hello
```

_(если hostname и path соответствуют `values.yaml`)_

---

## 🖥 6.1. Особенности запуска на macOS (minikube tunnel)

На macOS и некоторых Linux-системах `minikube` не выдаёт внешний IP-адрес для сервисов типа LoadBalancer. Чтобы корректно работал Ingress, необходимо вручную активировать туннель.

### 🔧 Шаги:

1. Преобразуем `ingress-nginx-controller` в LoadBalancer:

```bash
   kubectl patch svc ingress-nginx-controller -n ingress-nginx -p '{"spec": {"type": "LoadBalancer"}}'
```

2. Запускаем туннель (в отдельном терминале):

```bash
   sudo minikube tunnel
```

> ⚠️ Требует `sudo`, так как minikube создаёт сетевой маршрут через localhost.

3. Убедитесь, что IP теперь `localhost`:

```bash
   kubectl get svc -n ingress-nginx ingress-nginx-controller
```

Вы должны увидеть:

```
NAME                       TYPE           CLUSTER-IP     EXTERNAL-IP   PORT(S)
ingress-nginx-controller   LoadBalancer   10.x.x.x       127.0.0.1     80:xxxxx/TCP, 443:xxxxx/TCP
```

4. После запуска туннеля можно обращаться к Ingress через `localhost`:

```bash
   curl http://localhost/actuator/health
```

Или открыть в браузере:

```
http://localhost/
```

---

## 🧹 7. Удаление чарта и ресурсов

```bash
   helm uninstall warehouse -n warehouse-ns
   kubectl delete namespace warehouse-ns
```

```bash
   helm uninstall kafka-kraft
   helm uninstall kafka-ui
```

---

## 🧪 8. Отладка

```bash
   kubectl describe pod warehouse-warehouse-app-8c788cdb9-2mnh7 -n warehouse-ns
```
```bash
   kubectl logs warehouse-warehouse-app-79c97c5c4-g8khr -n warehouse-ns
```

---

## 🧪 9. Проброс IP для 

```bash для ingress
    kubectl port-forward -n ingress-nginx svc/ingress-nginx-controller 8090:80
```
```bash для сваггер
   kubectl port-forward -n warehouse-ns pod/$(kubectl get pods -n warehouse-ns --no-headers | sed -n '2p' | awk '{print $1}')  8084:8080 
```

## 10. скрипты


```bash
   helm uninstall warehouse -n warehouse-ns
   kubectl delete namespace warehouse-ns
   
```
```bash
   ./gradlew build -x test
```
```bash
   eval $(minikube docker-env)
   docker build -t warehouse-app:v52 .
   
   helm install warehouse ./deployment/helm/ --namespace warehouse-ns --create-namespace

   kubectl get pods -n warehouse-ns
```
```bash лог второго пода в namespace
   kubectl logs -f $(kubectl get pods -n warehouse-ns --no-headers | sed -n '2p' | awk '{print $1}') -n warehouse-ns
```
```bash лог третьего пода в namespace
   kubectl logs -f $(kubectl get pods -n warehouse-ns --no-headers | sed -n '3p' | awk '{print $1}') -n warehouse-ns
```

```bash describe второго пода в namespace
   kubectl describe pod $(kubectl get pods -n warehouse-ns --no-headers | sed -n '2p' | awk '{print $1}') -n warehouse-ns
```
```bash describe третьего пода в namespace
   kubectl describe pod  $(kubectl get pods -n warehouse-ns --no-headers | sed -n '3p' | awk '{print $1}') -n warehouse-ns
```
