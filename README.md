# 🛒 Aplicación de Búsqueda y Visualización de Productos

Esta aplicación Android permite a los usuarios autenticarse, buscar productos por categorías o palabras clave, recibir sugerencias inteligentes, y visualizar detalles completos de los productos. Además, ofrece una pantalla inicial con los productos más vendidos.

---

## 👥 Usuarios de Prueba

Para acceder a la aplicación, puedes utilizar uno de los siguientes usuarios de prueba registrados en **Firebase Authentication**:

| Correo electrónico           | Contraseña |
|------------------------------|------------|
| prueba@gmail.com.com         | 123456     |

---

### 🧩 Módulos Funcionales

| Módulo                        | Descripción                                                                                                                                             |
|------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------|
| 🔐 Autenticación Firebase    | El usuario debe iniciar sesión mediante Firebase Authentication para acceder a la aplicación. Validación de credenciales por correo/contraseña.         |
| 🔍 Búsqueda de productos     | Permite buscar productos por palabras clave o categorías. La búsqueda activa sugerencias inteligentes en tiempo real.                                     |
| 🧠 Sugerencias inteligentes  | Al escribir en el buscador, se despliega una lista de productos sugeridos que se actualiza dinámicamente desde el backend.                                |
| 🏷️ Listado por categorías     | Lista horizontal de categorías que, al seleccionarse, filtran productos automáticamente según el dominio correspondiente.                                 |
| 💥 Productos más vendidos     | Pantalla inicial muestra los productos más vendidos, integrada como fragmento independiente. Se oculta automáticamente tras realizar una búsqueda exitosa. |
| 📦 Detalle del producto       | Al seleccionar un producto, se navega a una vista con información detallada: nombre, descripción, características, entre otros.                         |
| 🧪 Manejo de errores          | Sistema de UI reactiva para mostrar errores personalizados si no se encuentran resultados o si ocurre un error en las solicitudes.                       |

---

### 🏗️ Stack Tecnológico y Dependencias

| Categoría                | Tecnologías / Librerías                                                                                           |
|--------------------------|------------------------------------------------------------------------------------------------------------------|
| Lenguaje / Corrutinas    | Kotlin + Kotlin Coroutines                                                                                        |
| Arquitectura             | Clean Architecture + MVVM — ViewModel → UseCase → Repository → DataSource                                       |
| Inyección de dependencias| Hilt (dagger-hilt-android, hilt-viewmodel, kapt)                                                                  |
| UI / Layout              | XML Layouts, Fragmentos, RecyclerView, LiveData, StateFlow, Navigation Component                                  |
| Red                      | Retrofit + OkHttp3 + Gson                                                                                         |
| Firebase                 | Firebase Authentication + Firestore + Firebase BoM                                                               |
| Sugerencias              | Custom Popup con RecyclerView para mostrar sugerencias dinámicas                                                 |
| Pruebas                  | JUnit5, Truth, Mockito-Kotlin, Coroutines Test                                                                    |

---

### 📌 Notas
- Esta app sigue principios de Clean Architecture y SOLID para garantizar un código mantenible y escalable.
- El flujo de navegación es gestionado mediante el Navigation Component, promoviendo una arquitectura modular y desacoplada.

---

## ✅ Requisitos Previos

- Android Studio Flamingo o superior  
- Java 11 o superior  
- Un emulador o dispositivo físico con Android 8.0 (API 24) o superior

---
