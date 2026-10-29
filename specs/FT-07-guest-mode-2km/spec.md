# SPEC — FT-07 Modo Invitado y Filtro de 2 km

> Rector: `.specify/memory/constitution.md` §2, §4, §5 y §10. Plantilla: `.specify/templates/spec-template.md`.
> Historia: **SE-008**.

## 1. Contexto y objetivo

`ui/auth/OnboardingLocationScreen.kt` permite continuar como invitado, pero `BuyerMapScreen`, `BuyerHomeScreen` y `RadiusSelectorModal` usan radios simulados de 0.5 a 5.0 km sin restriccion por sesion. Un invitado podria ver ofertas fuera de su zona o acceder a funciones que requieren cuenta.

**Objetivo:** definir el modo invitado con **filtro estricto de 2000 metros** y bloqueo de favoritos, comunidad y publicacion de ofertas.

**Alcance:** entrada como invitado, filtro de radio fijo, bloqueo de acciones autenticadas, invitacion a iniciar sesion y cache local de ofertas cercanas.
**Fuera de alcance:** login (FT-04), perfiles (FT-06), borrado de cuenta (FT-08).

## 2. Requisitos funcionales

| ID | Requisito |
|---|---|
| RF-07-01 | Desde la bienvenida, `Continuar como invitado` navega a `BuyerMapScreen` sin crear sesion. |
| RF-07-02 | En modo invitado el radio es **exactamente 2000 m**; `RadiusSelectorModal` no permite cambiarlo. |
| RF-07-03 | El repositorio aplica el filtro `distance <= 2000` en la consulta, no solo en la UI. |
| RF-07-04 | Favoritos, Comunidad y Publicar oferta estan bloqueados con dialogo `Inicia sesion para continuar`. |
| RF-07-05 | `BuyerHomeScreen` muestra solo ofertas dentro de 2000 m. |
| RF-07-06 | El invitado no puede acceder a `MerchantDashboard` ni a rutas de comercio. |
| RF-07-07 | Al iniciar sesion, el radio vuelve al valor por defecto del rol. |
| RF-07-08 | El modo invitado no persiste tokens ni datos de cuenta. |
| RF-07-09 | `GET /api/offers/nearby` recibe `center` y `radiusMeters`; en invitado el backend exige `radiusMeters=2000`. |

### Historia

| ID | Historia | Criterio |
|---|---|---|
| SE-008 | Como visitante quiero explorar ofertas cercanas sin registrarme. | Ve solo ofertas a ≤2000 m y las acciones que requieren cuenta le piden iniciar sesion. |

## 3. Requisitos no funcionales

- **Privacidad:** el invitado no envia `userId` ni token; solo coordenadas aproximadas.
- **Consistencia:** el filtro se aplica en repositorio y en backend; la UI no es autoridad.
- **UX:** el bloqueo es explicito, no silencioso; un solo dialogo reutilizable.
- **Rendimiento:** la consulta de cercania usa indice espacial; sin traer todo el catalogo.
- **Design System:** morado de marca; sin amarillos ni emojis.

## 4. Arquitectura y contratos internos

```
presentation/guest/
├── GuestMapScreen.kt
├── GuestHomeScreen.kt
└── GuestModeViewModel.kt

domain/guest/
├── model/GuestSession.kt
├── repository/GuestOfferRepository.kt
└── usecase/{EnterGuestModeUseCase,GetNearbyOffersUseCase}.kt

data/guest/
├── remote/GuestOfferApi.kt
└── repository/GuestOfferRepositoryImpl.kt

presentation/common/
└── RadiusSelectorModal.kt        # bloqueado en invitado
```

Contrato:

```kotlin
data class GuestSession(val center: GeoPoint, val radiusMeters: Int = 2000)

interface GuestOfferRepository {
    suspend fun nearby(center: GeoPoint, radiusMeters: Int): Result<List<Offer>>
}
```

Regla dura: `radiusMeters` se valida en el caso de uso; si el modo es invitado y el valor no es `2000`, se normaliza a `2000`.

## 5. Criterios de aceptacion

- **Given** un invitado en el mapa **When** abre el selector de radio **Then** ve 2 km fijo y no puede cambiarlo.
- **Given** ofertas a 2.5 km **When** el invitado carga el feed **Then** no aparecen.
- **Given** un invitado **When** pulsa Favoritos, Comunidad o Publicar **Then** se muestra `Inicia sesion para continuar`.
- **Given** un invitado **When** intenta abrir `MerchantDashboard` **Then** se le redirige a login.
- **Given** un invitado que inicia sesion **When** entra **Then** el radio vuelve al valor del rol.

## 6. Plan de pruebas

- **Unitarias:** `EnterGuestModeUseCase`, `GetNearbyOffersUseCase` (normalizacion de radio).
- **ViewModel:** Turbine sobre estado de invitado y bloqueo.
- **UI:** Compose Test Rule para selector fijo, dialogo de bloqueo y redireccion.
- **Integracion:** repositorio con `MockWebServer` devolviendo ofertas dentro y fuera de 2000 m.
- **Seguridad:** verificar que no se envia token en modo invitado.
- **Cobertura:** Kover ≥45% (propuesto).

## 7. Riesgos, limites y deuda

- El backend debe aplicar el mismo filtro; el cliente no es autoridad.
- La ubicacion del invitado puede ser imprecisa; se usa la ultima conocida con consentimiento.
- El modo invitado no accede a historial ni apartados; se difiere a una feature posterior.
- Depende de FT-06 (`GeoPoint`) y FT-01 (red).

## Aprobación

| Estado | Revisor | Fecha | Evidencia |
|---|---|---|---|
| Borrador | — | — | Pendiente de ejecucion de FT-07. |
