import { apiClient } from '../apiClient'

const USER_PATH = '/usuarios'

export const getUsuarios = async () => {
  const response = await apiClient.get(USER_PATH)
  return response.data
}

export const getUsuarioById = async (id) => {
  const response = await apiClient.get(`${USER_PATH}/${id}`)
  return response.data
}

export const createUsuario = async (usuario) => {
  const response = await apiClient.post(USER_PATH, usuario)
  return response.data
}

export const deleteUsuario = async (id) => {
  const response = await apiClient.delete(`${USER_PATH}/${id}`)
  return response.data
}
