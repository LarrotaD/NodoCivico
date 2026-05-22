// GET /reports
[
{
"id": 1,
"title": "Hueco en la vía",
"description": "Gran hueco frente al parque principal",
"category": "Infraestructura",
"priority": "ALTA",
"location": "Calle 45 con Carrera 27",
"status": "PENDIENTE",
"created_at": "2026-05-07T10:00:00"
}
]

// POST /reports
{
"title": "...",
"description": "...",
"category": "...",
"priority": "...",
"location": "..."
}

// GET /categories
[
{ "id": 1, "name": "Infraestructura" },
{ "id": 2, "name": "Seguridad" },
{ "id": 3, "name": "Servicios públicos" }
]

// POST /users/login
{ "name": "Juan", "email": "juan@barrio.com" }