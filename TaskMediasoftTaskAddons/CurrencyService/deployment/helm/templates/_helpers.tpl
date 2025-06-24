{{- define "external-service.name" -}}
currency-service
{{- end }}

{{- define "external-service.fullname" -}}
{{ printf "%s-%s" .Release.Name (include "external-service.name" .) | trunc 63 | trimSuffix "-" }}
{{- end }}
