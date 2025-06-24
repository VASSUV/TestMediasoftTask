# 🚀 STARTER-MINIKUBE.md

Инструкция по развёртыванию Minikube контейнера с nginx

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

## 🔍 3. Проверка состояния ресурсов

### 🔧 Проверка всех компонентов

```bash
   kubectl get all -A
``` 

---

## 🌐 4. Проверка Ingress

### Узнать IP Ingress-контроллера:

```bash
   minikube ip
```

---

## 🖥 5. Особенности запуска на macOS (minikube tunnel)

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