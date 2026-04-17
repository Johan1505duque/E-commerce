import { useEffect, useState } from 'react'
import {
  getUsuarios,
  getUsuarioById,
  createUsuario,
  deleteUsuario,
} from '../../services/users'
import {
  Alert,
  Box,
  Button,
  Card,
  CardContent,
  CardHeader,
  CircularProgress,
  Divider,
  FormControl,
  Grid,
  InputLabel,
  MenuItem,
  Paper,
  Select,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  TextField,
  Typography,
} from '@mui/material'

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
      setUsuarios(response.data?.data ?? [])
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
      setUsuarioBuscado(response.data?.data ?? null)
    } catch (err) {
      setError('No se encontró el usuario o la consulta falló.')
      setUsuarioBuscado(null)
      console.error(err)
    }
  }

  return (
    <Box sx={{ p: 3 }}>
      <Typography variant="h4" component="h1" gutterBottom>
        Gestión de usuarios
      </Typography>

      {error && (
        <Alert severity="error" sx={{ mb: 3 }}>
          {error}
        </Alert>
      )}

      <Grid container spacing={3}>
        <Grid item xs={12} md={7}>
          <Card>
            <CardHeader title="Usuarios activos" />
            <Divider />
            <CardContent>
              <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 2 }}>
                <Typography variant="subtitle1">Lista de usuarios activos</Typography>
                <Button variant="contained" onClick={loadUsuarios} disabled={loading}>
                  {loading ? <CircularProgress size={20} color="inherit" /> : 'Refrescar'}
                </Button>
              </Box>

              <TableContainer component={Paper} elevation={0}>
                <Table>
                  <TableHead>
                    <TableRow>
                      <TableCell>Nombre</TableCell>
                      <TableCell>Correo</TableCell>
                      <TableCell>Rol</TableCell>
                      <TableCell align="right">Acciones</TableCell>
                    </TableRow>
                  </TableHead>
                  <TableBody>
                    {usuarios.length === 0 ? (
                      <TableRow>
                        <TableCell colSpan={4} align="center">
                          {loading ? 'Cargando usuarios...' : 'No hay usuarios activos registrados.'}
                        </TableCell>
                      </TableRow>
                    ) : (
                      usuarios.map((usuario) => (
                        <TableRow key={usuario.id} hover>
                          <TableCell>
                            {usuario.nombre} {usuario.apellido}
                          </TableCell>
                          <TableCell>{usuario.correoElectronico}</TableCell>
                          <TableCell>{usuario.rol}</TableCell>
                          <TableCell align="right">
                            <Button
                              color="error"
                              variant="outlined"
                              size="small"
                              onClick={() => handleDelete(usuario.id)}
                            >
                              Desactivar
                            </Button>
                          </TableCell>
                        </TableRow>
                      ))
                    )}
                  </TableBody>
                </Table>
              </TableContainer>
            </CardContent>
          </Card>
        </Grid>

        <Grid item xs={12} md={5}>
          <Card>
            <CardHeader title="Crear nuevo usuario" />
            <Divider />
            <CardContent>
              <Box component="form" onSubmit={handleCreate} noValidate>
                <Grid container spacing={2}>
                  <Grid item xs={12} sm={6}>
                    <TextField
                      fullWidth
                      required
                      label="Nombre"
                      name="nombre"
                      value={formData.nombre}
                      onChange={handleInputChange}
                    />
                  </Grid>
                  <Grid item xs={12} sm={6}>
                    <TextField
                      fullWidth
                      required
                      label="Apellido"
                      name="apellido"
                      value={formData.apellido}
                      onChange={handleInputChange}
                    />
                  </Grid>
                  <Grid item xs={12}>
                    <TextField
                      fullWidth
                      required
                      label="Correo electrónico"
                      name="correoElectronico"
                      type="email"
                      value={formData.correoElectronico}
                      onChange={handleInputChange}
                    />
                  </Grid>
                  <Grid item xs={12}>
                    <TextField
                      fullWidth
                      required
                      type="password"
                      label="Contraseña"
                      name="password"
                      value={formData.password}
                      onChange={handleInputChange}
                    />
                  </Grid>
                  <Grid item xs={12}>
                    <FormControl fullWidth>
                      <InputLabel id="rol-label">Rol</InputLabel>
                      <Select
                        labelId="rol-label"
                        label="Rol"
                        name="rol"
                        value={formData.rol}
                        onChange={handleInputChange}
                      >
                        <MenuItem value="CLIENTE">CLIENTE</MenuItem>
                        <MenuItem value="ADMIN">ADMIN</MenuItem>
                      </Select>
                    </FormControl>
                  </Grid>
                  <Grid item xs={12}>
                    <Button fullWidth type="submit" variant="contained" size="large">
                      Crear usuario
                    </Button>
                  </Grid>
                </Grid>
              </Box>
            </CardContent>
          </Card>

          <Card sx={{ mt: 3 }}>
            <CardHeader title="Buscar usuario" />
            <Divider />
            <CardContent>
              <Box component="form" onSubmit={handleSearchById} noValidate sx={{ display: 'flex', gap: 2, flexWrap: 'wrap' }}>
                <TextField
                  label="ID de usuario"
                  value={searchId}
                  onChange={(event) => setSearchId(event.target.value)}
                  sx={{ flex: 1, minWidth: 120 }}
                />
                <Button type="submit" variant="outlined">
                  Buscar
                </Button>
              </Box>

              {usuarioBuscado && (
                <Box sx={{ mt: 3, p: 2, border: '1px solid', borderColor: 'divider', borderRadius: 2 }}>
                  <Typography variant="subtitle1" fontWeight={600}>
                    {usuarioBuscado.nombre} {usuarioBuscado.apellido}
                  </Typography>
                  <Typography>{usuarioBuscado.correoElectronico}</Typography>
                  <Typography>Rol: {usuarioBuscado.rol}</Typography>
                </Box>
              )}
            </CardContent>
          </Card>
        </Grid>
      </Grid>
    </Box>
  )
}

export default UserManagement
