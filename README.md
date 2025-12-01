# LevelUp 

Aplicación Android de catálogo de productos gamer/electrónica, con login local y consumo de una API externa en Supabase para productos y categorías.

---

## 1. Nombre del proyecto

**LevelUp – Tienda Gamer**

---

## 2. Integrantes

-Bernardo Cameron
-Scarlet Lopez
-Daniel Vargas


---

## 3. Funcionalidades

### Autenticación y usuarios

- **Registro y login local** usando **Room**.
- La app tiene 3 comportamientos según el correo con el que se registre/logee el usuario:
  -  **Usuario normal**: cualquier correo (ej: `usuario@gmail.com`)  
    - Puede ver el catálogo, filtrar por categoría y ver detalle de productos.
  -  **Usuario Duoc UC**: correos que terminan en `@duocuc.cl`  
    - Ve todos los productos con un **20% de descuento** en la pantalla de detalle.
  -  **Usuario administrador**: correos que terminan en `@levelup.com`  
    - Acceso a un **panel de administración** para:
      - Crear productos
      - Editar productos
      - Eliminar productos
      - Subir imagen desde el teléfono (galería) a Supabase Storage

> La base de datos local **solo se usa para usuarios (login/registro)**.  
> Los productos y categorías vienen desde **Supabase (API externa)**.

---

### Catálogo y navegación

- **Catálogo de productos** consumido desde Supabase (tabla `producto`).
- **Listado de categorías** (tabla `categoria`) para filtrar productos.
- Home con:
  - Productos destacados (`destacado = true`)
  - Productos recomendados (no destacados, filtrables por categoría)
- **Drawer de navegación** con:
  - Categorías
  - Productos destacados (atajos a detalle)
  - Acceso a administración (solo si el usuario es `@levelup.com`)
  - Logout

---

### Panel de administración

Solo visible para usuarios con correo `@levelup.com`.

- **Listado de productos** consumido desde Supabase.
- **Agregar producto:**
  - Formulario con:
    - Nombre
    - Descripción
    - Precio
    - Categoría (ID)
    - Destacado (checkbox)
    - Código de barras / QR (campo de texto)
    - Imagen:
      - URL opcional **o**
      - Selección desde galería → se sube a **Supabase Storage** (bucket `imgs`).
  - Al guardar:
    - Si hay imagen seleccionada:
      1. Se sube la imagen a `Storage`.
      2. Se obtiene la URL pública de la imagen.
      3. Se guarda el producto en la tabla `producto` con esa URL.
    - Si no hay imagen:
      - Se guarda usando una imagen por defecto o la URL indicada.
    - Luego se refresca la lista de productos en el panel de admin.
- **Editar producto:**
  - Se cargan los datos del producto y se permite modificar campos.
  - Se envía un `UPDATE` a la API de Supabase.
- **Eliminar producto:**
  - Se hace `DELETE` del producto en Supabase.
  - Se refresca automáticamente la lista en pantalla.

---

### Detalle de producto

- Imagen del producto:
  - Usa la URL de Supabase cuando existe.
  - Si no, usa un drawable por defecto (`logo_lvlup`).
- Muestra:
  - Nombre
  - Descripción
  - Precio
  - Precio con descuento (si es usuario `@duocuc.cl`):
    - Precio normal tachado
    - Precio Duoc UC con 20% de descuento
- Botón **“Comprar ahora”** (funcionalidad demostrativa, no realiza pago real).

---

## 4. Endpoints utilizados (API externa y microservicio)

La API externa utilizada es **Supabase** (PostgREST + Storage).  
El proyecto asume un `SUPABASE_URL` del tipo:

`https://<PROJECT>.supabase.co`

y un `SUPABASE_ANON_KEY` configurado en el código.

---

### 4.1. Endpoints REST – Productos

**Base:**  
`https://<PROJECT>.supabase.co/rest/v1/`

**Tabla `producto`:**

- **Obtener todos los productos**

  `GET /rest/v1/producto?select=*`

- **Obtener producto por ID**
`GET /rest/v1/producto?id=eq.<ID>&select=*`

- **Guardar producto**

`POST /rest/v1/producto
Headers:
  apikey: <SUPABASE_ANON_KEY>
  Authorization: Bearer <SUPABASE_ANON_KEY>
  Prefer: return=representation`

`Body (JSON):
{
  "nombre": "Monitor 24''",
  "descripcion": "Monitor LED Full HD 24 pulgadas",
  "precio": 119990.0,
  "categoriaId": 3,
  "destacado": true,
  "foto": "https://<PROJECT>.supabase.co/storage/v1/object/public/imgs/product_123.jpg"
}`

- **Actualizar producto**

`PATCH /rest/v1/producto?id=eq.<ID>
  Headers:
  apikey: <SUPABASE_ANON_KEY>
  Authorization: Bearer <SUPABASE_ANON_KEY>`

`Body (JSON, solo campos a actualizar):
{
  "precio": 109990.0,
  "destacado": false
}`

- **Eliminar producto **

`DELETE /rest/v1/producto?id=eq.<ID>
Headers:
  apikey: <SUPABASE_ANON_KEY>
  Authorization: Bearer <SUPABASE_ANON_KEY>`

## 4.2. Endpoints REST – Categorías

Tabla categoria:

**Obtener todas las categorías**

`GET /rest/v1/categoria?select=*
Headers:
  apikey: <SUPABASE_ANON_KEY>
  Authorization: Bearer <SUPABASE_ANON_KEY>`

Las categorías se consumen al iniciar el MainViewModel y se usan en el Drawer y en los filtros del home.

##4.3. Supabase Storage – Imágenes

Bucket utilizado: imgs

Subir imagen de producto
(la app lo hace vía OkHttp/cliente HTTP desde Android)

`POST /storage/v1/object/imgs/product_<TIMESTAMP>.jpg
Headers:
  apikey: <SUPABASE_ANON_KEY>
  Authorization: Bearer <SUPABASE_ANON_KEY>
  Content-Type: image/jpeg`

`Body:
  <bytes del archivo de imagen>`
  
Acceso público a la imagen
  `https://<PROJECT>.supabase.co/storage/v1/object/public/imgs/product_<TIMESTAMP>.jpg`

La app genera nombres del tipo:
`product_<timestamp>.jpg`

##5. Pasos para ejecutar
##-5.1. Requisitos
    -Android Studio (Koala / Iguana o superior).
    -JDK 17.
    -Dispositivo físico o emulador con Android 8.0+ (API 26+).
    -Cuenta y proyecto en Supabase con:
    -Tablas producto y categoria.
    -Bucket de Storage llamado imgs.
