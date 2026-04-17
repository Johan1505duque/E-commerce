import { apiClient } from './apiClient'

const USER_PATH = '/usuarios'

/**
 * Lista todos los usuarios activos.
 * Devuelve la respuesta completa del backend para que el componente decida qué hacer.
 */
export const getUsuarios = async () => {
  const response = await apiClient.get(USER_PATH)
  return response.data
}

/**
 * Busca un usuario por su ID.
 * @param {number|string} id
 */
export const getUsuarioById = async (id) => {
  const response = await apiClient.get(`${USER_PATH}/${id}`)
  return response.data
}

/**
 * Crea un nuevo usuario.
 * @param {Object} usuario
 */
export const createUsuario = async (usuario) => {
  const response = await apiClient.post(USER_PATH, usuario)
  return response.data
}

/**
 * Desactiva un usuario por su ID.
 * @param {number|string} id
 */
export const deleteUsuario = async (id) => {
  const response = await apiClient.delete(`${USER_PATH}/${id}`)
  return response.data
}
