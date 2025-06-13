
```bash
    kubectl create namespace minio
```
```bash
   helm uninstall minio -n minio
```
```bash    
    helm install minio minio/minio --namespace minio --create-namespace --set accessKey=minioadmin --set secretKey=minioadmin --set resources.requests.memory=256Mi --set console.enabled=true --set console.service.type=ClusterIP --set console.service.port=9001
```
```bash
   kubectl port-forward svc/minio-console 9001:9001 -n minio
```
[Чтобы перейти в браузер, кликни сюда](http://localhost:9001)

И получи данные для входа:
```bash логин
   echo "$(kubectl get secret -n minio minio -o jsonpath='{.data.rootUser}' | base64 -d)"
```
```bash пароль
   echo "$(kubectl get secret -n minio minio -o jsonpath='{.data.rootPassword}' | base64 -d)"
```