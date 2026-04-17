import axios from 'axios'

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
      console.error('API error:', error.response.status, error.response.data)
    } else {
      console.error('Network error:', error.message)
    }
    return Promise.reject(error)
  },
)
