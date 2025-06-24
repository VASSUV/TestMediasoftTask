

```bash для бд
   kubectl port-forward -n warehouse-db-ns pod/$(kubectl get pods -n warehouse-db-ns --no-headers | sed -n '2p' | awk '{print $1}') 5432:5432 
```
```bash для ingress
    kubectl port-forward -n ingress-nginx svc/ingress-nginx-controller 8090:80
```
```bash для сваггер
   kubectl port-forward -n warehouse-ns pod/$(kubectl get pods -n warehouse-ns --no-headers | sed -n '2p' | awk '{print $1}')  8084:8080 
```
```bash для minio
   kubectl port-forward svc/minio-console 9001:9001 -n minio
```
[Чтобы перейти в браузер, кликни сюда](http://localhost:9001)

```bash для examad сервиса
   kubectl port-forward svc/excamad-service 8882:8080 -n warehouse-ns
```
```bash для пода приложения
   kubectl port-forward -n warehouse-ns pod/$(kubectl get pods -n warehouse-ns --no-headers | sed -n '1p' | awk '{print $1}') 8080:8080 
```
