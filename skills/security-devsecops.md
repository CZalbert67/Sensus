# Skill: Seguridad y DevSecOps (Sensus Mobile)

## Cuándo usar
Al manejar tokens, persistencia, permisos, red o moderación.

## Reglas
- Tokens JWT en EncryptedSharedPreferences + Android Keystore.
- DB local cifrada con SQLCipher (AES-256); nunca texto plano.
- Permisos en tiempo de ejecución (cámara, galería, notificaciones Android 13+).
- TLS 1.3 para todo tráfico; certificate pinning recomendado.
- Moderación previa al guardado: OpenAI `omni-moderation-latest` (texto), Sightengine (imágenes).
- Logout invalida token en backend (Redis) y destruye estado local.
- Eliminación de cuenta purga datos remotos y locales.

## Checklist
- [ ] Sin secretos hardcodeados
- [ ] Almacenamiento cifrado
- [ ] Permisos solicitados en runtime
- [ ] Moderación integrada (si aplica)
- [ ] Logout/eliminación implementados (si aplica)
