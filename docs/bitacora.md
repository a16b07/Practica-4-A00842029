# Bitacora Sabores en Red

## Ejercicio 0
1. GET /restaurants -> Lista principal
2. GET /restaurants/3 -> Pantalla de Detalle
3. GET /reviews?restaurantId=3 -> Detalle (las reseñas)
4. POST /reviews -> Pantalla para escribir reseña
5. GET /me/reviews -> Pantalla de Mis reseñas
6. DELETE /reviews/12 -> Se usa en mis reseñas para borrar

## Ejercicio A2
Se usan los dos porque el del servidor ayuda a que la lista cargue mas rapido sin pedir todas las reseñas. Pero el de la app sirve para que cuando publique una reseña nueva el promedio cambie al momento sin tener que recargar todo del server otra vez.

## Ejercicio B1
Los tres estados son cargando, exito y error. No es lo mismo cargando que lista vacia porque cargando es que apenas viene la info y lista vacia es que si llego pero no hay nada. El error es cuando falla el wifi o el server.

## Ejercicio C2
Si quito la validacion de la app el server me regresa un error 422 porque alla tambien se valida. La app no truena porque usamos el try catch y muestra el mensaje que manda la api de que el comentario es muy corto.

---
Se utilizó la IA (Gemini) para analizar el código completo de forma más rápida.
