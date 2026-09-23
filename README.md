# 🔐 SecureDocs — Sistema de Gestión de Documentos Empresariales

> Laboratorio 06 — Seguridad en Cloud Computing  
> Curso: Desarrollo de Soluciones en la Nube | TECSUP 2026

---

## 📋 Descripción

SecureDocs es una aplicación web que implementa un sistema de autorización de doble capa para la gestión segura de documentos empresariales, combinando:

- **RBAC** (Role-Based Access Control) — control de acceso basado en roles
- **ABAC** (Attribute-Based Access Control) — control de acceso basado en atributos

---

## 🏗️ Arquitectura del Sistema

```
┌─────────────────┐         ┌──────────────────────────────────────────────────┐
│                 │  HTTP   │              Spring Boot 4.1.1                    │
│  React + Vite   │ ──────► │                                                  │
│  (puerto 5173)  │         │  ┌───────────┐  ┌──────────┐  ┌───────────────┐ │
│                 │         │  │   Auth    │  │   RBAC   │  │     ABAC      │ │
└─────────────────┘         │  │  (JWT)    │  │ Service  │  │Policy Engine  │ │
                             │  └───────────┘  └──────────┘  └───────────────┘ │
                             │  ┌───────────┐  ┌──────────┐  ┌───────────────┐ │
                             │  │ Document  │  │  User    │  │   Auditoría   │ │
                             │  │ Service   │  │ Service  │  │   Service     │ │
                             │  └───────────┘  └──────────┘  └───────────────┘ │
                             └──────────────────────┬───────────────────────────┘
                                                    │
                                                    ▼
                                         ┌──────────────────┐
                                         │   MySQL 8.x      │
                                         │  (XAMPP)         │
                                         └──────────────────┘
```

---

## 🛠️ Stack Tecnológico

| Capa | Tecnología |
|---|---|
| Backend | Spring Boot 4.1.1 (Java 21) |
| Frontend | React 18 + Vite |
| Base de datos | MySQL 8 (XAMPP) |
| Autenticación | JWT (jjwt 0.12.6) |
| Seguridad | Spring Security 7.1.1 |
| ORM | Hibernate 7 / Spring Data JPA |

---

## 📁 Estructura del Proyecto

```
securedocs/
├── src/main/java/com/gllanos/securedocs/
│   ├── auth/               ← Login, JWT, AuthController
│   ├── abac/               ← Motor ABAC + 8 políticas
│   │   └── policies/       ← Una clase por política
│   ├── rbac/               ← Motor RBAC centralizado
│   ├── config/             ← SecurityConfig, CorsConfig, DataInitializer
│   ├── controller/         ← DocumentoController, UsuarioController, AuditoriaController
│   ├── model/              ← Entidades JPA
│   ├── repository/         ← Repositorios Spring Data
│   ├── security/           ← JwtFilter
│   └── service/            ← DocumentoService, UsuarioService, AuditoriaService
├── src/main/resources/
│   └── application.properties
└── frontend/
    └── src/
        ├── api/            ← axios.js
        ├── context/        ← AuthContext
        ├── pages/          ← Login, Dashboard, Documentos, Usuarios, Auditoria
        └── components/     ← Layout
```

---

## 👥 Roles del Sistema

| Rol | Descripción |
|---|---|
| ADMINISTRADOR | Acceso total al sistema |
| GERENTE | Gestión y aprobación de documentos |
| SUPERVISOR | Creación, consulta, modificación y aprobación |
| EMPLEADO | Creación, consulta y modificación |
| AUDITOR | Solo consulta y auditoría |
| INVITADO | Solo consulta de documentos públicos nivel ≤ 1 |

---

## 🔒 Matriz RBAC

| Permiso | ADMIN | GERENTE | SUPERVISOR | EMPLEADO | AUDITOR | INVITADO |
|---|:---:|:---:|:---:|:---:|:---:|:---:|
| CREAR_DOCUMENTO | ✅ | ✅ | ✅ | ✅ | ❌ | ❌ |
| CONSULTAR_DOCUMENTO | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| MODIFICAR_DOCUMENTO | ✅ | ✅ | ✅ | ✅ | ❌ | ❌ |
| ELIMINAR_DOCUMENTO | ✅ | ✅ | ❌ | ❌ | ❌ | ❌ |
| APROBAR_DOCUMENTO | ✅ | ✅ | ✅ | ❌ | ❌ | ❌ |
| VER_AUDITORIA | ✅ | ✅ | ❌ | ❌ | ✅ | ❌ |
| GESTIONAR_USUARIOS | ✅ | ❌ | ❌ | ❌ | ❌ | ❌ |
| ASIGNAR_ROLES | ✅ | ❌ | ❌ | ❌ | ❌ | ❌ |

---

## 🛡️ Políticas ABAC

| # | Política | Descripción |
|---|---|---|
| 1 | DEPARTAMENTO | Usuario y documento deben ser del mismo departamento |
| 2 | NIVEL_SEGURIDAD | Nivel del usuario ≥ nivel de confidencialidad del documento |
| 3 | PROPIEDAD | Solo el propietario puede modificar (salvo ADMIN/GERENTE) |
| 4 | HORARIO | Documentos nivel ≥ 4 solo accesibles entre 08:00 y 18:00 |
| 5 | PAIS | País del usuario debe coincidir con país del documento |
| 6 | DISPOSITIVO | Documentos nivel ≥ 4 solo desde dispositivo CORPORATIVO |
| 7 | ESTADO_USUARIO | El usuario debe estar en estado ACTIVO |
| 8 | INVITADO | Invitados: EXTERNO + nivel ≤ 1 + documento PUBLICADO |

**Regla de evaluación:**
```
ACCESO = RBAC(rol, permiso) AND ABAC(usuario, documento, acción, entorno)
```

---

## 🗄️ Modelo de Base de Datos

```
usuarios          roles             permisos
─────────         ──────            ────────
id                id                id
nombre            nombre (enum)     nombre (enum)
correo
password          rol_permisos
rol_id ──────────► rol_id
departamento_id   permiso_id ──────►
nivel_seguridad
pais              departamentos
tipo_contrato     ─────────────
estado            id
                  nombre

documentos        auditoria         politicas
──────────        ─────────         ─────────
id                id                id
titulo            usuario           nombre
descripcion       recurso           descripcion
propietario_id    accion            activa
departamento_id   fecha
nivel_conf.       resultado
estado            motivo
pais              direccion_ip
fecha_creacion    dispositivo
                  ubicacion
```

---

## 🚀 Instalación y Ejecución

### Requisitos
- Java 21
- Maven
- MySQL (XAMPP)
- Node.js 18+

### Backend

```bash
# 1. Clona el repositorio
git clone https://github.com/gllanos09/Lab06_Nube.git

# 2. Inicia MySQL en XAMPP

# 3. Configura application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/securedocs?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=

# 4. Ejecuta el proyecto
./mvnw spring-boot:run
```

Al iniciar por primera vez se crean automáticamente:
- Roles y permisos
- Matriz RBAC completa
- Usuario administrador: `admin@securedocs.com` / `admin123`

### Frontend

```bash
cd frontend
npm install
npm run dev
```

Accede en: http://localhost:5173

---

## 🔌 API REST

| Método | Endpoint | Descripción | Autenticación |
|---|---|---|---|
| POST | `/auth/login` | Iniciar sesión | ❌ |
| GET | `/usuarios` | Listar usuarios | ✅ ADMIN |
| POST | `/usuarios` | Crear usuario | ✅ ADMIN |
| PUT | `/usuarios/{id}` | Modificar usuario | ✅ ADMIN |
| PATCH | `/usuarios/{id}/activar` | Activar usuario | ✅ ADMIN |
| PATCH | `/usuarios/{id}/desactivar` | Desactivar usuario | ✅ ADMIN |
| GET | `/documentos` | Listar documentos | ✅ |
| GET | `/documentos/{id}` | Obtener documento | ✅ |
| POST | `/documentos` | Crear documento | ✅ |
| PUT | `/documentos/{id}` | Modificar documento | ✅ |
| DELETE | `/documentos/{id}` | Eliminar documento | ✅ |
| POST | `/documentos/{id}/aprobar` | Aprobar documento | ✅ |
| GET | `/auditoria` | Ver registro de auditoría | ✅ ADMIN/GERENTE/AUDITOR |

---

## 🧪 Casos de Prueba

### Casos de Acceso Permitido

| # | Usuario | Acción | Documento | Resultado esperado |
|---|---|---|---|---|
| 1 | EMPLEADO (SISTEMAS) | Consultar | Nivel 1 - SISTEMAS | ✅ PERMITIDO |
| 2 | SUPERVISOR (SISTEMAS) | Aprobar | Nivel 2 - SISTEMAS | ✅ PERMITIDO |
| 3 | ADMINISTRADOR | Eliminar | Cualquier documento | ✅ PERMITIDO |
| 4 | AUDITOR | Ver auditoría | — | ✅ PERMITIDO |
| 5 | INVITADO (EXTERNO) | Consultar | Nivel 1 - PUBLICADO | ✅ PERMITIDO |
| 6 | ADMIN | Consultar nivel 4 | Horario laboral + dispositivo corporativo | ✅ PERMITIDO |

### Casos de Acceso Denegado

| # | Usuario | Acción | Documento | Motivo denegación |
|---|---|---|---|---|
| 7 | EMPLEADO (SISTEMAS) | Consultar | Nivel 1 - FINANZAS | ❌ Política DEPARTAMENTO |
| 8 | EMPLEADO | Eliminar | Cualquiera | ❌ Política RBAC |
| 9 | INVITADO | Consultar | Nivel 3 | ❌ Política NIVEL_SEGURIDAD |
| 10 | EMPLEADO | Consultar nivel 4 | Fuera de horario (23:00) | ❌ Política HORARIO |
| 11 | USUARIO INACTIVO | Cualquier acción | Cualquiera | ❌ Política ESTADO_USUARIO |
| 12 | EMPLEADO | Consultar nivel 4 | Dispositivo PERSONAL | ❌ Política DISPOSITIVO |

---

## 📊 Auditoría

Cada intento de acceso genera un registro automático con:

```json
{
  "usuario": "correo@empresa.com",
  "recurso": "documento-1",
  "accion": "CONSULTAR_DOCUMENTO",
  "fecha": "2026-09-23T10:00:00",
  "resultado": "PERMITIDO | DENEGADO",
  "motivo": "Acceso autorizado | Acceso denegado por política: DEPARTAMENTO",
  "direccionIp": "192.168.1.1",
  "dispositivo": "CORPORATIVO",
  "ubicacion": "PERU"
}
```

---

## 👤 Autor

**Gabriel Llanos**  
Estudiante — Diseño y Desarrollo de Software  
TECSUP Lima, 2026
