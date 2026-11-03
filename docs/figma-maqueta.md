# Maqueta Oficial Figma & Integración MCP — Sensus Mobile

> Documento canónico de enlace visual y diseño para el proyecto **Sensus Mobile**.
> Rector: `constitution.md` §9 y `Memoria.md` §5.

---

## 1. Coordenadas de la Maqueta Oficial

| Propiedad | Valor |
|---|---|
| **Proyecto Figma** | **SENSUS** |
| **File Key** | `iwlrDS420Bsbgtn5GeOhNn` |
| **URL del Diseño** | [https://www.figma.com/design/iwlrDS420Bsbgtn5GeOhNn/SENSUS](https://www.figma.com/design/iwlrDS420Bsbgtn5GeOhNn/SENSUS) |
| **Página Principal** | `Page 1` |
| **Total de Pantallas / Frames** | **109 pantallas maestras** |

---

## 2. Configuración MCP (Model Context Protocol)

El repositorio cuenta con configuración local y plantillas para conectar los asistentes de IA directamente con Figma vía MCP:

### A. Servidores Soportados en `mcp_config.json`
1. **`Figma` (Stdio / Local Node CLI):**
   - Utiliza `figma-developer-mcp` vía stdio con token de acceso personal de Figma (`FIGMA_API_KEY`).
   - Permite consultar metadatos del árbol de nodos, variables de diseño, autolayout y descargar imágenes.
2. **`FigmaRemote` (Streamable HTTP):**
   - Endpoint oficial de Figma en la nube: `https://mcp.figma.com/mcp`.
   - Autenticación transparente mediante OAuth browser flow.
3. **`FigmaDesktop` (Dev Mode Local):**
   - Endpoint del cliente de escritorio de Figma: `http://127.0.0.1:3845/mcp` (activable en preferencias de Figma Desktop -> *Enable Dev Mode MCP Server*).

### B. Herramientas MCP Disponibles
- `get_figma_data`: Recupera la jerarquía de nodos, propiedades de layout, dimensiones y estilos de cualquier nodo o pantalla por su ID o File Key (`iwlrDS420Bsbgtn5GeOhNn`).
- `download_figma_images`: Descarga componentes gráficos, iconos y vistas previas directamente a la estructura de assets del proyecto móvil.

---

## 3. Resumen de Flujos Maestros (109 Pantallas)

1. **Onboarding & Autenticación:**
   - Splash animado (caída de bolsa, gato sentado, cascabel).
   - Login, Registro Consumidor y Comercio, Términos y Condiciones.
   - Recuperación de contraseña en 3 pasos (envío de código, ingreso de OTP, nueva contraseña).
2. **Flujo Consumidor / Comprador:**
   - `04b · Inicio · Vista de mapa`: Mapa interactivo con gatito y halo pulsante morado.
   - Selector de radio interactivo (0.5 km a 5.0 km).
   - Feed de ofertas con botón "Apartar" morado.
   - Detalle de oferta, reserva con código QR, ruta GPS.
   - Perfil de usuario, historial y soporte.
3. **Flujo Comercio / Vendedor:**
   - Panel de vendedor (con y sin ofertas).
   - Formulario de publicación rápida de nueva oferta.
   - Registro de datos del negocio y fotografía de fachada.
   - Ventas, canjes y perfil del comercio.
4. **Modo Invitado (Guest Mode):**
   - Pantallas `G1` a `G6` navegables sin sesión en un radio restringido de 2 km, con aviso modal para iniciar sesión al apartar.
5. **Menú Flotante Insignia (FAM):**
   - Botón anclado de gatito sentado con menú desplegable en arcoíris lateral mediante animación física Spring.

---

## 4. Higiene y DevSecOps

- **Protección de Credenciales:** `mcp_config.json`, `.agent/mcp_config.json` y `.cursor/mcp.json` están listados en `.gitignore`. Nunca se commitean tokens personales de Figma.
- **Plantilla para el Equipo:** Se versiona `mcp_config.example.json` como molde estándar para que cualquier desarrollador configure sus credenciales locales.
