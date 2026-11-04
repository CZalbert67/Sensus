# Suite de Escáneres de Seguridad y Calidad de Código (CI/CD)

Este documento describe las **14 herramientas de escaneo y análisis estático de código** configuradas en el pipeline de Integración Continua (CI/CD) de la aplicación móvil Sensus (`.github/workflows/security-scanners.yml` y `.github/workflows/ci.yml`), conforme a la `constitution.md` (§7.3 y §8).

---

## Matriz de las 14 Herramientas

| # | Herramienta | Tipo | Objetivo y Alcance | Requiere Clave |
|---|---|---|---|---|
| **1** | **Sensus SDD Gates** | Estructura & Políticas | Valida arquitectura limpia, contratos, ausencia de secretos y estructura SDD (`check_*.py`). | No (nativo) |
| **2** | **Gitleaks** | Secret Scanner | Detección rápida de secretos, tokens y credenciales en commits y PRs. | No |
| **3** | **TruffleHog OSS** | Secret Scanner | Escaneo profundo con análisis de entropía y verificación en vivo de credenciales expuestas. | No |
| **4** | **Semgrep SAST** | SAST | Reglas de seguridad OWASP Top 10 y Mobile Security para código Kotlin y Java. | No |
| **5** | **MobSFscan** | Mobile SAST | Auditoría estática de Android: AndroidManifest.xml, permisos, componentes exportados, WebViews. | No |
| **6** | **Android SDK Lint** | Linter Oficial Android | Correctitud, compatibilidad de APIs Android, rendimiento, accesibilidad y buenas prácticas. | No |
| **7** | **Detekt** | Kotlin SAST / Code Smells | Detección de complejidad ciclomática, malas prácticas y code smells según `detekt.yml`. | No |
| **8** | **Ktlint** | Formato & Estilo | Verificación de estilo canónico de código Kotlin y cumplimiento de formato. | No |
| **9** | **Google OSV-Scanner** | SCA / Vulnerabilidades | Escaneo de dependencias contra la base de datos de vulnerabilidades Open Source de Google. | No |
| **10** | **Aqua Security Trivy** | Misconfig & Vulnerabilidades | Auditoría de configuraciones de seguridad, secretos y dependencias en el repositorio. | No |
| **11** | **OWASP Dependency-Check** | SCA / CVEs | Análisis formal de dependencias Gradle contra la base de datos nacional de vulnerabilidades (NVD). | No |
| **12** | **SonarCloud** | SAST & Quality Gate | Análisis continuo de calidad, deuda técnica, cobertura, duplicación y vulnerabilidades. | **Sí (`SONAR_TOKEN`)** |
| **13** | **Snyk Security** | SCA & SAST | Identificación de vulnerabilidades en dependencias Gradle con recomendaciones de parches. | **Sí (`SNYK_TOKEN`)** |
| **14** | **GitHub CodeQL** | SAST Semántico | Análisis semántico profundo de flujos de datos en Java/Kotlin (inyecciones, crypto insegura). | No (nativo GitHub) |

---

## Configuración de Secretos en GitHub Actions

Las primeras **11 herramientas y CodeQL** corren de forma 100% autónoma y no requieren claves privadas ni suscripciones.

Para activar las herramientas Cloud opcionales (**SonarCloud** y **Snyk**), configura las siguientes variables en tu repositorio de GitHub (**Settings > Secrets and variables > Actions > Repository secrets**):

### 1. SonarCloud
* **`SONAR_TOKEN`**: Token de análisis generado en [SonarCloud](https://sonarcloud.io) en `My Account > Security > Generate Token`.
* *(Opcional)* Si tu proyecto pertenece a una organización, puedes configurar `SONAR_ORGANIZATION` y `SONAR_PROJECT_KEY`.

### 2. Snyk Security
* **`SNYK_TOKEN`**: Token de API obtenido en [Snyk.io](https://app.snyk.io) en `Account Settings > API Token`.

> [!NOTE]
> El workflow `.github/workflows/security-scanners.yml` está configurado para ejecutarse automáticamente en cada Pull Request y Push a `main`. Las tareas de SonarCloud y Snyk se evalúan de forma condicional: si no has configurado sus tokens, el pipeline continuará sin fallar ejecutando las 12 herramientas restantes.
