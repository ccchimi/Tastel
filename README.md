# Tastel – App Móvil de Recetas

Repositorio del **Parcial 1 - Aplicaciones Móviles (Da Vinci)**  
Profesor: Sergio Daniel Medina
Integrantes: Franco Martin Schimizzi y Melina Rocio Martins

---

## Descripción

Tastel es una aplicación móvil de recetas pensada para simplificar la vida del usuario a la hora de cocinar.  
La app permite buscar recetas, filtrarlas por categorías, ver un detalle con imagen y descripción, y guardar favoritas.

---

## Funcionalidades principales

- **Pantallas implementadas**
    - **Splash Screen** con logo.
    - **Login** con navegación hacia la app.
    - **Home / Recetas** con buscador, categorías dinámicas (chips) y listado de recetas.
    - **Detalle de receta** al tocar cualquier card.
    - **Drawer lateral** con secciones: Recetas, Comunidad, Listas, Cerrar Sesión.

- **Interactividad**
    - Búsqueda de recetas por nombre.
    - Filtrado por categorías (Pastas, Carnes, Veggie, Postres, Sopas, Arroces, Ensaladas, Pescados, Tapas & Snacks, Sin TACC).
    - Cards de recetas clickeables que llevan a la pantalla de detalle.
    - Botón de logout que regresa a la pantalla de Login.

- **Persistencia**
    - Uso de `SharedPreferences` para mantener favoritos (ejemplo en `RecipeAdapter`).

- **Diseño**
    - Uso combinado de `ConstraintLayout` y `LinearLayout`.
    - Estilos personalizados (`styles.xml`, `colors.xml`).
    - Material Design Components (Chips, Toolbar, NavigationView, CardView).

---

## Tecnologías usadas

- **Lenguaje:** Java (Android)
- **IDE:** Android Studio (Gradle)
- **UI:** Material Design Components
- **Persistencia:** SharedPreferences (para favoritos)
- **Gestión:** GitHub + conventional commits

---

## Estructura de proyecto

app/
├─ java/com.app.tasteit/
│ ├─ MainActivity.java
│ ├─ LoginActivity.java
│ ├─ SplashActivity.java
│ ├─ RecipeDetailActivity.java
│ └─ adapters/RecipeAdapter.java
├─ res/
│ ├─ layout/ (layouts XML)
│ ├─ drawable/ (recursos gráficos)
│ └─ values/ (colors, styles, strings)

---

## Cómo correr el proyecto

1. Clonar el repo:
  
   git clone https://github.com/ccchimi/Tastel.git

2. Abrir en Android Studio.

3. Sincronizar dependencias con Gradle.

4. Ejecutar en un emulador Android (SDK 30+) o dispositivo físico.
