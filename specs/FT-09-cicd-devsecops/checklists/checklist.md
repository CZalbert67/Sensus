# CHECKLIST — FT-09 CI/CD y Prácticas DevSecOps

> Gate de cierre. Sin todos los items en `[x]` no hay pase a Producción.

## Definición de Listo (Definition of Ready - DoR)

- [x] Requisitos `RF-09-01` a `RF-09-15` formalizados en `spec.md` con las 7 secciones obligatorias.
- [x] Alineación estricta con `constitution.md` (§7.3 y §8).
- [x] Plan de arquitectura por capas documentado en `plan.md`.
- [x] Tareas atómicas trazables registradas en `tasks.md`.

## Flujos de CI/CD Implementados

- [x] `.github/workflows/ci.yml` configurado con orden constitucional de ejecución.
- [x] Validación de wrapper Gradle obligatoria antes de cualquier compilación.
- [x] Compuertas SDD (`check_sdd`, `check_layers`, `check_endpoints`, `check_secrets`, `check_coverage`) ejecutadas de forma bloqueante.
- [x] Tablero visual emitido en `$GITHUB_STEP_SUMMARY` al finalizar cada corrida de CI.

## Suite de Seguridad DevSecOps (14 Herramientas)

- [x] `.github/workflows/security-scanners.yml` creado y activo en PRs, push a `main` y cron semanal.
- [x] SAST semántico: CodeQL (Java/Kotlin) integrado y activo.
- [x] SAST de reglas móviles: Semgrep (`p/mobile`, `p/owasp-top-ten`) con subida de SARIF.
- [x] Auditoría de AndroidManifest y componentes con MobSFscan.
- [x] Escaneo estricto de secretos con TruffleHog OSS y Gitleaks.
- [x] Análisis de dependencias (SCA) con OSV-Scanner, Trivy y OWASP Dependency-Check.
- [x] Calidad de código Kotlin con Android SDK Lint, Detekt y Ktlint.
- [x] Integración condicional para SonarCloud y Snyk preparada.

## Despliegue Continuo a Google Play Console (CD)

- [x] `.github/workflows/release-playstore.yml` configurado con disparo manual o tags `v*.*.*`.
- [x] `pre-flight-gates` bloqueante: si CUALQUIER prueba o escáner falla, el despliegue se cancela.
- [x] Verificación de entorno: si los secretos de Play Console faltan, se valida compilación Release y se pausa la publicación de forma limpia.
- [x] Keystore y Service Account inyectados exclusivamente en memoria volátil de runner efímero.
- [x] Destrucción higiénica de claves garantizada en bloque `always()`.
- [x] Soporte para pistas `internal`, `alpha`, `beta`, `production` y rollout gradual (`user_fraction`).

## Documentación y Gobernanza

- [x] Guía técnica completa en `docs/cicd-pipeline.md`.
- [x] Matriz de herramientas y tokens en `docs/security-scanners.md`.
- [x] Plantilla de secretos actualizada en `keys.properties.example`.
- [x] Sección `## 🚀 Apartado de CI/CD y Despliegue Continuo` añadida a `README.md`.
- [x] Registro de auditoría y evidencia documentado en `Memoria.md`.
- [x] Registro en `specs/README.md`.

## Definición de Terminado (Definition of Done - DoD)

- [x] Todos los gates de `python scripts/check_*.py` en PASS.
- [x] Workflows validados y sin errores sintácticos.
- [x] Código y configuraciones integradas y desplegadas en la rama `main`.
