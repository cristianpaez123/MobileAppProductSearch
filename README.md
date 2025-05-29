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
| Firebase                 | Firebase Authentication                                                              |
| Sugerencias              | Custom Popup con RecyclerView para mostrar sugerencias dinámicas                                                 |
| Pruebas                  | JUnit4, Mockito-Kotlin, Coroutines Test                                                                    |

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


## 🛠️ Instrucciones de Instalación

1. Clona el repositorio:
   ```bash
   git clone https://github.com/cristianpaez123/MobileAppProductSearch

   Abre el proyecto en Android Studio

2. Sincroniza las dependencias de Gradle

3. Configura un emulador o conecta un dispositivo físico

4. Haz clic en el botón "Run" para ejecutar la aplicación

---

## 👤 Autor

Nombre: Cristian Paez

GitHub: @cristianpaez123

Correo: cristianpaezguerrero@gmail.com

Celular: +57 300 702 5600

---

| Login                        | Login(dark)                                                                                                                                             |
|------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------|
| <img src="https://github.com/user-attachments/assets/0ccde1de-d131-4904-8eb0-ebe14c0c9fb6" alt="Pantalla Principal" width="200"/>   | <img src="https://github.com/user-attachments/assets/90d3f66d-2dd8-41eb-9683-445b24794a20" alt="Pantalla Principal" width="200"/>         |

| initial view (best-selling products)                        | initial view (best-selling products)(dark)                                                                                                                                             |
|------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------|
| <img src="https://github.com/user-attachments/assets/69676bdf-066c-4699-afa8-4ec414123dc4" alt="Pantalla Principal" width="200"/>   | <img src="https://github.com/user-attachments/assets/eb6534f5-4e12-49b1-b61e-60613bfc88d5" alt="Pantalla Principal" width="200"/>         |


| suggestions                        | suggestions(dark)                                                                                                                                             |
|------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------|
| <img src="https://github.com/user-attachments/assets/fff96ee9-be3f-4b57-98d9-67c9c15970ba" alt="Pantalla Principal" width="200"/>   | <img src="https://github.com/user-attachments/assets/f455d6f5-ca3f-4c68-bb79-175ca6e4f832" alt="Pantalla Principal" width="200"/>         |


| list of products                | list of products(dark)                                                                                           |
|--------------------------|------------------------------------------------------------------------------------------------------------------|
| <img src="https://github.com/user-attachments/assets/0f4f594f-bf5a-40b5-9fe4-671e16bde282" alt="Pantalla Principal" width="200"/>  | <img src="https://github.com/user-attachments/assets/fc6cf348-aaa0-4185-894d-e968df596c23" alt="Pantalla Principal" width="200"/>   |


| product details                        | product details(dark)                                                                                    |
|------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------|
| <img src="https://github.com/user-attachments/assets/15cbadda-73c0-4f31-be1c-9eedf013a86f" alt="Pantalla Principal" width="200"/>   | <img src="https://github.com/user-attachments/assets/8848e087-d589-4692-8a4d-4d6f793c33ea" alt="Pantalla Principal" width="200"/>         |

| error view                       | error view(dark)                                                                                    |
|------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------|
| <img src="https://github.com/user-attachments/assets/5efa7eb6-be26-470a-b52c-74f4bbc6443d" alt="Pantalla Principal" width="200"/>   | <img src="https://github.com/user-attachments/assets/4e0ff4b5-2ac8-4c78-99ae-e64e5f4b4e73" alt="Pantalla Principal" width="200"/>         |

| product not found                       | product not found(dark)                                                                                    |
|------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------|
| <img src="https://github.com/user-attachments/assets/fb5420e5-18cb-4546-aedf-d5a0996159f8" alt="Pantalla Principal" width="200"/>   | <img src="https://github.com/user-attachments/assets/d1b62e93-4f92-487c-8786-3d6c1627e7a4" alt="Pantalla Principal" width="200"/>         |

