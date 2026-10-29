---
name: android-testing-generic
description: Testing Android con JUnit5, MockK, Turbine y Compose Test Rule, con cobertura Kover. Usar al crear un UseCase o ViewModel, ante un gate rojo o al revisar cobertura.
---

## Overview

Un test por caso de uso y por ViewModel. Fakes en `test/shared/`, fixtures sin datos sensibles.

## Unit `app/src/test/`

- JUnit5 + MockK + `runTest` + Turbine para `StateFlow`/`SharedFlow`.
- Casos minimos: exito, validacion, error de red, sesion expirada, estado offline.
- Reutiliza `test/shared/TestDataFactory`; no dupliques helpers.
- Nombres `metodo_escenario_resultadoEsperado`.

## UI `app/src/androidTest/`

- Compose Test Rule para los flujos criticos que declare cada spec (p. ej. autenticacion y operaciones sensibles).
- Un test de navegacion por ruta nueva.
- Sin dependencia de red real: usa fakes o servidor mock.

## Cobertura

- `testDebugUnitTest` verde siempre.
- `koverVerify` con umbral >= 45% cuando el plugin este cableado (umbral propuesto, pendiente de ratificacion del equipo).
- Reporta cobertura real; no maquilles con tests vacios.

## Deuda conocida

`MainScreenViewModelTest` y `MainScreenTest` estan stale y no cubren el marketplace actual. Su reescritura se planifica en la fase de pruebas.

## Cuando usar

- Nuevo UseCase o ViewModel, gate rojo, revision de cobertura.
