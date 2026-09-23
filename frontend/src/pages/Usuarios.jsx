import { useState, useEffect } from 'react'
import api from '../api/axios'

export default function Usuarios() {
    const [usuarios, setUsuarios] = useState([])
    const [error, setError] = useState('')

    useEffect(() => { cargar() }, [])

    const cargar = async () => {
        try {
            const res = await api.get('/usuarios')
            setUsuarios(res.data)
        } catch (e) {
            setError('No tiene permiso para ver usuarios')
        }
    }

    const toggleEstado = async (u) => {
        try {
            if (u.estado === 'ACTIVO') {
                await api.patch(`/usuarios/${u.id}/desactivar`)
            } else {
                await api.patch(`/usuarios/${u.id}/activar`)
            }
            cargar()
        } catch (e) {
            setError('Error al cambiar estado')
        }
    }

    return (
        <div>
            <h1 style={{ color: '#1e293b', marginBottom: '24px' }}>👥 Usuarios</h1>
            {error && (
                <div style={{ background: '#fee2e2', color: '#b91c1c', padding: '10px', borderRadius: '6px', marginBottom: '16px' }}>
                    {error}
                </div>
            )}
            <div style={{ background: 'white', borderRadius: '12px', overflow: 'hidden', boxShadow: '0 1px 4px rgba(0,0,0,0.06)' }}>
                <table style={{ width: '100%', borderCollapse: 'collapse' }}>
                    <thead>
                    <tr style={{ background: '#f8fafc' }}>
                        {['Nombre', 'Correo', 'Rol', 'Departamento', 'Nivel', 'Estado', 'Acciones'].map(h => (
                            <th key={h} style={{ padding: '12px 16px', textAlign: 'left', fontSize: '13px', color: '#64748b', fontWeight: '600' }}>{h}</th>
                        ))}
                    </tr>
                    </thead>
                    <tbody>
                    {usuarios.map(u => (
                        <tr key={u.id} style={{ borderTop: '1px solid #f1f5f9' }}>
                            <td style={tdStyle}>{u.nombre}</td>
                            <td style={tdStyle}>{u.correo}</td>
                            <td style={tdStyle}>{u.rol?.nombre}</td>
                            <td style={tdStyle}>{u.departamento?.nombre || '-'}</td>
                            <td style={tdStyle}>{u.nivelSeguridad}</td>
                            <td style={tdStyle}>
                  <span style={{
                      padding: '3px 10px', borderRadius: '20px', fontSize: '12px', fontWeight: '600',
                      background: u.estado === 'ACTIVO' ? '#dcfce7' : '#fee2e2',
                      color: u.estado === 'ACTIVO' ? '#166534' : '#b91c1c'
                  }}>{u.estado}</span>
                            </td>
                            <td style={tdStyle}>
                                <button onClick={() => toggleEstado(u)} style={{
                                    padding: '6px 12px', fontSize: '12px', border: 'none', borderRadius: '6px', cursor: 'pointer',
                                    background: u.estado === 'ACTIVO' ? '#fef3c7' : '#dcfce7',
                                    color: u.estado === 'ACTIVO' ? '#92400e' : '#166534'
                                }}>
                                    {u.estado === 'ACTIVO' ? 'Desactivar' : 'Activar'}
                                </button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </div>
    )
}

const tdStyle = { padding: '12px 16px', fontSize: '14px', color: '#1e293b' }