# Skill: Jetpack Compose UI (Sensus Mobile)

## Cuándo usar
Al crear o modificar pantallas Compose.

## Reglas
- Funciones `@Composable` en PascalCase.
- Estado hoisted (stateless cuando sea posible).
- Previews con `@Preview(showBackground = true)`.
- Usar design tokens de `theme/` (colores, tipografía).
- Sin emojis; iconos vectoriales.
- Sin amarillos en UI interna.
- `contentDescription` obligatorio en iconos interactivos.
- Padding inferior de 96.dp en feeds con FAM.

## Componentes reutilizables
- `CategoryIcons.kt` → `getCategoryIcon(category)`.
- `RadiusSelectorBottomSheet` → selector de radio.

## Checklist
- [ ] Preview agregado
- [ ] Design tokens usados
- [ ] Accesibilidad (contentDescription)
- [ ] Sin hardcode de colores
- [ ] Compilación verificada
