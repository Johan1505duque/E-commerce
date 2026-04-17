import { useEffect, useState } from 'react'
import {
  getUsuarios,
  getUsuarioById,
  createUsuario,
  deleteUsuario,
} from '../../services/users'
import './UserManagement.css'

const initialForm = {
  nombre: '',
  apellido: '',
  correoElectronico: '',
  password: '',
  rol: 'CLIENTE',
}

function UserManagement() {
  const [usuarios, setUsuarios] = useState([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const [formData, setFormData] = useState(initialForm)
  const [searchId, setSearchId] = useState('')
  const [usuarioBuscado, setUsuarioBuscado] = useState(null)

  const loadUsuarios = async () => {
    setLoading(true)
    setError('')
    try {
      const response = await getUsuarios()
      setUsuarios(response.data ?? [])
    } catch (err) {
      setError('No se pudieron cargar los usuarios. Por favor intenta nuevamente más tarde.')
      console.error(err)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    loadUsuarios()
  }, [])

  const handleInputChange = (event) => {
    const { name, value } = event.target
    setFormData((prev) => ({ ...prev, [name]: value }))
  }

  const handleCreate = async (event) => {
    event.preventDefault()
    setError('')
    try {
      await createUsuario(formData)
      setFormData(initialForm)
      await loadUsuarios()
    } catch (err) {
      setError('No se pudo crear el usuario. Verifica los datos y vuelve a intentarlo.')
      console.error(err)
    }
  }

  const handleDelete = async (id) => {
    setError('')
    try {
      await deleteUsuario(id)
      await loadUsuarios()
    } catch (err) {
      setError('No se pudo desactivar el usuario. Intenta de nuevo más tarde.')
      console.error(err)
    }
  }

  const handleSearchById = async (event) => {
    event.preventDefault()
    if (!searchId) return
    setError('')
    try {
      const response = await getUsuarioById(searchId)
      setUsuarioBuscado(response.data ?? null)
    } catch (err) {
      setError('No se encontró el usuario o la consulta falló.')
      setUsuarioBuscado(null)
      console.error(err)
    }
  }

  return (
    <div className="user-management">
      <h1>Gestión de usuarios</h1>

      {error && <div className="user-error">{error}</div>}

      <section className="user-section">
        <h2>Lista de usuarios activos</h2>
        <button onClick={loadUsuarios} disabled={loading}>
          {loading ? 'Cargando...' : 'Refrescar lista'}
        </button>

        <div className="user-list">
          {usuarios.length === 0 ? (
            <p>No hay usuarios activos registrados.</p>
          ) : (
            usuarios.map((usuario) => (
              <div className="user-card" key={usuario.id}>
                <div>
                  <strong>{usuario.nombre} {usuario.apellido}</strong>
                  <p>{usuario.correoElectronico}</p>
                  <p>Rol: {usuario.rol}</p>
                </div>
                <button className="delete-button" onClick={() => handleDelete(usuario.id)}>
                  Desactivar
                </button>
              </div>
            ))
          )}
        </div>
      </section>

      <section className="user-section">
        <h2>Crear usuario</h2>
        <form className="user-form" onSubmit={handleCreate}>
          <label>
            Nombre
            <input name="nombre" value={formData.nombre} onChange={handleInputChange} required />
          </label>
          <label>
            Apellido
            <input name="apellido" value={formData.apellido} onChange={handleInputChange} required />
          </label>
          <label>
            Correo electrónico
            <input name="correoElectronico" value={formData.correoElectronico} onChange={handleInputChange} required />
          </label>
          <label>
            Contraseña
            <input type="password" name="password" value={formData.password} onChange={handleInputChange} required />
          </label>
          <label>
            Rol
            <select name="rol" value={formData.rol} onChange={handleInputChange}>
              <option value="CLIENTE">CLIENTE</option>
              <option value="ADMIN">ADMIN</option>
            </select>
          </label>
          <button type="submit">Crear usuario</button>
        </form>
      </section>

      <section className="user-section">
        <h2>Buscar usuario por ID</h2>
        <form className="user-search" onSubmit={handleSearchById}>
          <input
            placeholder="ID de usuario"
            value={searchId}
            onChange={(event) => setSearchId(event.target.value)}
          />
          <button type="submit">Buscar</button>
        </form>

        {usuarioBuscado && (
          <div className="user-card found">
            <strong>{usuarioBuscado.nombre} {usuarioBuscado.apellido}</strong>
            <p>{usuarioBuscado.correoElectronico}</p>
            <p>Rol: {usuarioBuscado.rol}</p>
          </div>
        )}
      </section>
    </div>
  )
}

export default UserManagement
