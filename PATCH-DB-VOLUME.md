# Сменить политику удаления у PVC (рекомендуется)

- Шаг 1. Найди свой PVC
```bash
   kubectl get pvc -n warehouse-ns
```

- Шаг 2. Найди связанный PV
```bash
   kubectl get pv
```

Найди PV, связанный с этим PVC по claim:

CLAIM: warehouse-ns/your-pvc-name

- Шаг 3. Измени политику retention

```bash
   kubectl patch pv <имя-pv> -p '{"spec":{"persistentVolumeReclaimPolicy":"Retain"}}'
```

🔁 После этого:

Даже если PVC будет удалён → PV останется

Данные на диске не исчезнут