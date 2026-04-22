import React, { useState, useEffect } from 'react';
import axios from 'axios';
import {
  Container,
  Typography,
  TextField,
  Button,
  Grid,
  Card,
  CardMedia,
  CardContent,
  CardActions,
  Box,
  Paper,
  Divider
} from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import AddIcon from '@mui/icons-material/Add';

const API_URL = 'http://localhost:8080/api/productos';

const ProductManagement = () => {
  const [products, setProducts] = useState([]);
  const [formData, setFormData] = useState({
    nombre: '',
    descripcion: '',
    precio: '',
    imagenUrl: ''
  });

  // 1. Cargar productos al montar el componente
  useEffect(() => {
    fetchProducts();
  }, []);

  const fetchProducts = async () => {
    try {
      const response = await axios.get(API_URL);
      // Accediendo a la estructura response.data.data
      setProducts(response.data.data);
      // Se valida que la respuesta contenga la estructura response.data.data
      if (response.data && response.data.data) {
        setProducts(response.data.data);
      }
    } catch (error) {
      console.error("Error al obtener productos:", error);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  // 2. Formulario para crear producto
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await axios.post(API_URL, formData);
      setFormData({ nombre: '', descripcion: '', precio: '', imagenUrl: '' });
      fetchProducts(); // Recargar lista
    } catch (error) {
      console.error("Error al crear producto:", error);
    }
  };

  // 3. Función para eliminar producto
  const handleDelete = async (id) => {
    if (window.confirm('¿Estás seguro de eliminar este producto?')) {
      try {
        await axios.delete(`${API_URL}/${id}`);
        fetchProducts(); // Recargar lista
      } catch (error) {
        console.error("Error al eliminar producto:", error);
      }
    }
  };

  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      <Typography variant="h4" component="h1" gutterBottom align="center" sx={{ fontWeight: 'bold', mb: 4 }}>
        Administración de Productos
      </Typography>

      {/* Sección Formulario */}
      <Paper elevation={3} sx={{ p: 4, mb: 6, borderRadius: 2 }}>
        <Typography variant="h6" gutterBottom sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
          <AddIcon /> Añadir Nuevo Producto
        </Typography>
        <Box component="form" onSubmit={handleSubmit} sx={{ mt: 2 }}>
          <Grid container spacing={2}>
            <Grid item xs={12} sm={6}>
              <TextField fullWidth label="Nombre del Producto" name="nombre" value={formData.nombre} onChange={handleInputChange} required />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField fullWidth label="Precio" name="precio" type="number" value={formData.precio} onChange={handleInputChange} required />
            </Grid>
            <Grid item xs={12}>
              <TextField fullWidth label="URL de la Imagen" name="imagenUrl" value={formData.imagenUrl} onChange={handleInputChange} required />
            </Grid>
            <Grid item xs={12}>
              <TextField fullWidth label="Descripción" name="descripcion" value={formData.descripcion} onChange={handleInputChange} multiline rows={2} required />
            </Grid>
            <Grid item xs={12}>
              <Button variant="contained" color="primary" type="submit" size="large" fullWidth>
                Registrar Producto
              </Button>
            </Grid>
          </Grid>
        </Box>
      </Paper>

      <Divider sx={{ mb: 4 }} />

      {/* Sección Listado (Cards) */}
      <Grid container spacing={4}>
        {products.map((product) => (
          <Grid item key={product.id} xs={12} sm={6} md={4}>
            <Card sx={{ height: '100%', display: 'flex', flexDirection: 'column', transition: '0.3s', '&:hover': { boxShadow: 6 } }}>
              <CardMedia component="img" height="220" image={product.imagenUrl} alt={product.nombre} sx={{ objectFit: 'cover' }} />
              <CardContent sx={{ flexGrow: 1 }}>
                <Typography gutterBottom variant="h6" component="h2">{product.nombre}</Typography>
                <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>{product.descripcion}</Typography>
                <Typography variant="h5" color="primary.main" sx={{ fontWeight: 'bold' }}>
                  ${Number(product.precio).toLocaleString('es-CO')}
                </Typography>
              </CardContent>
              <CardActions sx={{ justifyContent: 'flex-end', p: 2 }}>
                <Button variant="outlined" color="error" startIcon={<DeleteIcon />} onClick={() => handleDelete(product.id)}>
                  Eliminar
                </Button>
              </CardActions>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Container>
  );
};

export default ProductManagement;