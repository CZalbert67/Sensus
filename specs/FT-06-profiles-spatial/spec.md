# SPEC — FT-06 Perfiles y Recálculo Espacial

> Rector: `.specify/memory/constitution.md` §2, §4, §5, §6 y §10. Plantilla: `.specify/templates/spec-template.md`.
> Historias: **SE-006, SE-007**.

## 1. Contexto y objetivo

`ui/buyer/BuyerProfileScreen.kt` y `ui/merchant/MerchantProfileScreen.kt` muestran datos simulados y no persisten cambios. El marketplace depende de coordenadas correctas para el filtro por radio (FT-07).

**Objetivo:** editar el perfil de Consumidor (`PUT /api/users/profile`) y de Comercio (`PUT /api/merchants/profile`), persistiendo cambios y recalculando coordenadas espaciales (`GEOGRAPHY`) cuando cambie la direccion.

**Alcance:** edicion de perfil de ambos roles, validacion de campos, actualizacion optimista con rollback, recalculo de `location` en backend y cache local.
**Fuera de alcance:** modo invitado (FT-07), borrado de cuenta (FT-08), carga de imagenes.

## 2. Requisitos funcionales

| ID | Requisito |
|---|---|
| RF-06-01 | `PUT /api/users/profile` recibe `displayName`, `phone`, `address`, `latitude`, `longitude` y devuelve el perfil actualizado. |
| RF-06-02 | `PUT /api/merchants/profile` recibe ademas `businessName`, `category`, `schedule` y devuelve el perfil actualizado. |
| RF-06-03 | Si cambia la direccion, el backend recalcula `location` como `GEOGRAPHY(POINT, 4326)`; el cliente no calcula el punto final. |
| RF-06-04 | El cliente envia `latitude`/`longitude` solo como sugerencia; la respuesta del backend es la fuente de verdad. |
| RF-06-05 | Los cambios se reflejan de inmediato en la UI y se revierten si el backend falla. |
| RF-06-06 | El perfil actualizado se cachea en la DB cifrada (FT-02) para uso offline. |
| RF-06-07 | Cambiar de rol no borra el perfil del otro rol. |
| RF-06-08 | Los campos se validan en `domain/usecase` antes de enviar. |

### Historias

| ID | Historia | Criterio |
|---|---|---|
| SE-006 | Como Consumidor quiero editar mi perfil y mi direccion. | El perfil se guarda y el radio de busqueda usa la nueva ubicacion. |
| SE-007 | Como Comercio quiero editar mi perfil y mi ubicacion. | El comercio aparece en el radio correcto tras el recalculo. |

## 3. Requisitos no funcionales

- **Seguridad:** solo el dueno edita su perfil; el backend valida el `userId` del token.
- **Consistencia:** escritura optimista con rollback y mensaje de error.
- **Precision:** `GEOGRAPHY` con SRID 4326; sin truncar decimales en el cliente.
- **Rendimiento:** actualizacion <1 s en red normal; sin recargar toda la lista.
- **Accesibilidad:** labels persistentes, errores anunciados.

## 4. Arquitectura y contratos internos

```
presentation/profile/
├── buyer/{BuyerProfileScreen,BuyerProfileViewModel}.kt
└── merchant/{MerchantProfileScreen,MerchantProfileViewModel}.kt

domain/profile/
├── model/{UserProfile,MerchantProfile,GeoPoint}.kt
├── repository/ProfileRepository.kt
└── usecase/{UpdateUserProfileUseCase,UpdateMerchantProfileUseCase,ValidateProfileUseCase}.kt

data/profile/
├── remote/ProfileApi.kt
├── dto/{UserProfileDto,MerchantProfileDto}.kt
├── mapper/ProfileMapper.kt
└── repository/ProfileRepositoryImpl.kt
```

Contrato:

```kotlin
data class GeoPoint(val latitude: Double, val longitude: Double)

interface ProfileRepository {
    suspend fun updateUserProfile(profile: UserProfile): Result<UserProfile>
    suspend fun updateMerchantProfile(profile: MerchantProfile): Result<MerchantProfile>
}
```

## 5. Criterios de aceptacion

- **Given** un Consumidor autenticado **When** guarda su perfil **Then** el backend responde con el perfil actualizado y la UI lo muestra.
- **Given** una direccion nueva **When** se guarda **Then** `location` se recalcula en backend y el radio usa el nuevo punto.
- **Given** un fallo de red **When** se guarda **Then** la UI revierte el cambio y muestra error.
- **Given** un Comercio **When** edita su horario **Then** el cambio persiste sin afectar su rol de Consumidor.
- **Given** la app sin red **When** se abre el perfil **Then** se muestra el ultimo perfil cacheado.

## 6. Plan de pruebas

- **Unitarias:** `ValidateProfileUseCase`, mappers y use cases con `MockK`.
- **ViewModel:** Turbine para escritura optimista y rollback.
- **UI:** Compose Test Rule para edicion y error.
- **Contrato:** fixtures de `PUT /api/users/profile` y `PUT /api/merchants/profile`.
- **Espacial:** verificar que el cliente envia coordenadas y que respeta la respuesta del backend.
- **Cobertura:** Kover ≥45% (propuesto).

## 7. Riesgos, limites y deuda

- El recalculo `GEOGRAPHY` depende de PostGIS/backend; el cliente no puede simularlo con precision.
- La carga de imagenes de perfil queda fuera de alcance.
- La escritura optimista puede generar conflictos si el backend normaliza direcciones; se documenta el rollback.
- Depende de FT-02 (cache cifrada) y FT-04 (sesion y rol).

## Aprobación

| Estado | Revisor | Fecha | Evidencia |
|---|---|---|---|
| Borrador | — | — | Pendiente de ejecucion de FT-06. |
