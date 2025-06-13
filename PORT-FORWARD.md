

```bash для бд
   kubectl port-forward -n warehouse-ns pod/$(kubectl get pods -n warehouse-ns --no-headers | sed -n '1p' | awk '{print $1}') 5432:5432 
```
```bash для ingress
    kubectl port-forward -n ingress-nginx svc/ingress-nginx-controller 8090:80
```
```bash для сваггер
   kubectl port-forward -n warehouse-ns pod/$(kubectl get pods -n warehouse-ns --no-headers | sed -n '2p' | awk '{print $1}')  8084:8080 
```
