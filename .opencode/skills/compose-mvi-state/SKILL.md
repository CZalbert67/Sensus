---
name: compose-mvi-state
description: Patron Compose + MVI con ViewState StateFlow, ViewIntent y ViewEffect SharedFlow para pantallas stateless. Usar al crear una pantalla o refactorizar estado.
---

## Overview

Estado unidireccional testeable, sin logica de negocio en Composables. Nombres canonicos de Sensus: `ViewState`, `ViewIntent`, `ViewEffect` (no `UiState`/`UiEvent`).

## Contratos base

```kotlin
interface ViewState
interface ViewIntent
interface ViewEffect

abstract class MviViewModel<S : ViewState, I : ViewIntent, E : ViewEffect>(
    initialState: S,
) : ViewModel() {
    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<E>(extraBufferCapacity = 1)
    val effect: SharedFlow<E> = _effect.asSharedFlow()

    protected fun setState(reducer: S.() -> S) { _state.update(reducer) }
    protected suspend fun emitEffect(effect: E) { _effect.emit(effect) }

    abstract fun onIntent(intent: I)
}
```

## Patron Screen

- `*Screen(state, onIntent, onEffect, ...)` stateless; la Route recolecta con `collectAsStateWithLifecycle()`.
- `LaunchedEffect` solo para `ViewEffect` (navegacion, dialogos, snackbar).
- Previews obligatorias por cada estado relevante (cargando, vacio, error, contenido).
- Sin `try/catch` en el Composable; sin acceso directo a repositorios.
- Mapeo de errores: `NotFound -> UI vacia`, `Forbidden -> login`, `Conflict -> mensaje`, validacion -> mensaje, red -> snackbar.

## Navegacion

- Solo rutas `@Serializable` en `navigation/Routes.kt`, `NavHost` centralizado.
- Argumentos complejos via `SavedStateHandle`.
- Arranque: leer `sessionFlow.first()` para decidir la ruta inicial sin parpadeo (ver `auth-session-management`).

## Cuando usar

- Nueva pantalla, refactor de estado, revision de UI, navegacion con sesion.
