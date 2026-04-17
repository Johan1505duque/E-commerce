import axios from 'axios'

// Cliente Axios centralizado para todas las llamadas al backend.
// Usamos baseURL para no repetir la URL completa en cada servicio.
export const apiClient = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json',
  },
})

apiClient.interceptors.response.use(
  response => response,
  error => {
    if (error.response) {
      // Aquí puedes añadir lógica global para manejar errores de API.
      console.error('API error:', error.response.status, error.response.data)
    } else {
      console.error('Network error:', error.message)
    }
    return Promise.reject(error)
  },
)
