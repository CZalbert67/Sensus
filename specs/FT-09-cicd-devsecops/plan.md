# PLAN — FT-09 CI/CD y Prácticas DevSecOps

> Derivado de `spec.md`. Rector: `.specify/memory/constitution.md` §7, §8 y §10.

## 1. Diseño por Capas del Ecosistema CI/CD

El diseño del pipeline implementa una arquitectura por capas desacoplada que separa la verificación rápida de PRs, la auditoría profunda de seguridad y el despliegue a producción:

### Capa 1: Validación de Wrapper e Integridad
- Validación del Gradle Wrapper (`gradle/wrapper-validation-action@v2`). Evita ataques de envenenamiento en la cadena de suministro (*supply chain attacks*) bloqueando binarios wrapper manipulados.

### Capa 2: Compuertas SDD y Reglas de Arquitectura
- Ejecución determinista de los 5 scripts Python en runner aislado:
  - `check_sdd.py`: Verifica las 7 secciones de cada spec y la existencia de los 4 artefactos canónicos.
  - `check_layers.py`: Garantiza que `domain/` no importe Android ni librerías de red, y que `presentation/` no acceda a `data/`.
  - `check_endpoints.py`: Cruza contratos de specs contra `docs/endpoints-internos.md`.
  - `check_secrets.py`: Detección temprana de credenciales hardcodeadas.
  - `check_coverage.py`: Asegura que el código nuevo respete el umbral constitucional del 80%.

### Capa 3: DevSecOps (14 Escáneres de Seguridad y Calidad)
- **SAST Semántico:** GitHub CodeQL rastreando flujos de datos y tainted inputs.
- **SAST de Reglas Móviles:** Semgrep ejecutando packs `p/mobile`, `p/owasp-top-ten` y `p/default`.
- **Auditoría de Manifiesto y Componentes:** MobSFscan detectando configuraciones inseguras de red, exportación de actividades y permisos excesivos.
- **Secret Scanning Avanzado:** Gitleaks en commits y TruffleHog OSS con verificación de entropía.
- **SCA de Dependencias:** Google OSV-Scanner, Aqua Security Trivy y OWASP Dependency-Check cruzando el grafo de dependencias contra bases de datos CVE.
- **Linters de Código Kotlin:** Android SDK Lint oficial, Detekt con `detekt.yml` y Ktlint.
- **Plataformas Cloud:** Integración condicional con SonarCloud y Snyk.

### Capa 4: Despliegue Continuo Condicionado a Google Play
- **Compuerta Pre-Lanzamiento (Quality Gate):** Bloqueo terminante si falla cualquier verificación previa.
- **Detección de Entorno:** Si `KEYSTORE_BASE64` o `PLAY_STORE_SERVICE_ACCOUNT_JSON` faltan, valida compilación Release y pausa el envío de forma limpia sin generar errores en CI.
- **Firma Efímera:** Keystore inyectado solo en memoria volátil de runner y purgado en bloque `always()`.
- **Desofuscación:** Subida automática de `mapping.txt` a Google Play Console.

### Capa 5: Observabilidad y Step Summary
- Emisión de tablero visual Markdown en `$GITHUB_STEP_SUMMARY` en cada flujo (`ci.yml` y `release-playstore.yml`).

---

## 2. UI y Navegación

- No aplica. Esta feature es 100% de infraestructura DevSecOps y automatización de despliegue.

---

## 3. Inyección de Dependencias (DI)

- No aplica. Los componentes operan a nivel de runners de GitHub Actions, scripts de verificación y Gradle.

---

## 4. Gates de Verificación

- `python scripts/check_sdd.py` -> PASS.
- `python scripts/check_layers.py` -> PASS.
- `python scripts/check_endpoints.py` -> PASS.
- `python scripts/check_secrets.py` -> PASS.
- `actionlint` o validación YAML de workflows en `.github/workflows/` -> PASS.
- Build de Release verificado: `./gradlew compileReleaseKotlin` -> PASS.

---

## 5. Orden de Implementación

1. **Fase A (Workflows CI):** Configurar `ci.yml` con validación de wrapper, compuertas SDD, CodeQL, Lint, tests y `cicd-summary`.
2. **Fase B (Suite de Seguridad):** Implementar `security-scanners.yml` con las 14 herramientas y consolidación SARIF.
3. **Fase C (CD a Google Play):** Implementar `release-playstore.yml` con Quality Gate bloqueante y verificación de credenciales de producción.
4. **Fase D (Documentación y Tableros):** Redactar `docs/cicd-pipeline.md`, `docs/security-scanners.md`, `keys.properties.example` y sección en `README.md`.
5. **Fase E (Cierre SDD):** Registrar la feature en `specs/README.md` y actualizar `Memoria.md`.
