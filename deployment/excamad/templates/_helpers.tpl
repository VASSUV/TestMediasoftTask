{{- define "excamad.name" -}}
excamad
{{- end -}}

{{- define "excamad.fullname" -}}
{{ .Release.Name }}-{{ include "excamad.name" . }}
{{- end -}}
