import { useState, useEffect } from 'react'
import api from '../api/axios'

const entornoDefault = {
    hora: new Date().toTimeString().slice(0, 5),
    direccionIp: '192.168.1.1',
    ubicacion: 'PERU',
    dispositivo: 'CORPORATIVO'
}

export default function Documentos() {
    const [documentos, setDocumentos] = useState([])
    const [error, setError] = useState('')
    const [form, setForm] = useState({
        titulo: '', descripcion: '', nivelConfidencialidad: 1,
        estado: 'BORRADOR', pais: 'PERU'
    })
    const [mostrarForm, setMostrarForm] = useState(false)

    useEffect(() => { cargar() }, [])

    const cargar = async () => {
        try {
            const res = await api.get('/documentos', { data: entornoDefault })
            setDocumentos(res.data)
        } catch (e) {
            setError('No se pudieron cargar los documentos')
        }
    }

    const crear = async (e) => {
        e.preventDefault()
        try {
            await api.post('/documentos', { ...form, entorno: entornoDefault })
            setMostrarForm(false)
            setForm({ titulo: '', descripcion: '', nivelConfidencialidad: 1, estado: 'BORRADOR', pais: 'PERU' })
            cargar()
        } catch (e) {
            setError(e.response?.data || 'Error al crear documento')
        }
    }

    const aprobar = async (id) => {
        try {
            await api.post(`/documentos/${id}/aprobar`, entornoDefault)
            cargar()
        } catch (e) {
            setError(e.response?.data || 'No tiene permiso para aprobar')
        }
    }

    const eliminar = async (id) => {
        try {
            await api.delete(`/documentos/${id}`, { data: entornoDefault })
            cargar()
        } catch (e) {
            setError(e.response?.data || 'No tiene permiso para eliminar')
        }
    }

    return (
        <div>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '24px' }}>
                <h1 style={{ color: '#1e293b' }}>📄 Documentos</h1>
                <button onClick={() => setMostrarForm(!mostrarForm)} style={btnPrimary}>
                    {mostrarForm ? 'Cancelar' : '+ Nuevo documento'}
                </button>
            </div>

            {error && (
                <div style={{ background: '#fee2e2', color: '#b91c1c', padding: '10px', borderRadius: '6px', marginBottom: '16px' }}>
                    {error}
                </div>
            )}

            {mostrarForm && (
                <form onSubmit={crear} style={{ background: 'white', padding: '24px', borderRadius: '12px', marginBottom: '24px' }}>
                    <h3 style={{ marginBottom: '16px' }}>Nuevo documento</h3>
                    <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px' }}>
                        <div>
                            <label style={labelStyle}>Título</label>
                            <input style={inputStyle} value={form.titulo}
                                   onChange={e => setForm({ ...form, titulo: e.target.value })} required />
                        </div>
                        <div>
                            <label style={labelStyle}>Nivel confidencialidad (1-5)</label>
                            <input type="number" min="1" max="5" style={inputStyle}
                                   value={form.nivelConfidencialidad}
                                   onChange={e => setForm({ ...form, nivelConfidencialidad: parseInt(e.target.value) })} />
                        </div>
                        <div>
                            <label style={labelStyle}>Estado</label>
                            <select style={inputStyle} value={form.estado}
                                    onChange={e => setForm({ ...form, estado: e.target.value })}>
                                <option value="BORRADOR">BORRADOR</option>
                                <option value="PENDIENTE">PENDIENTE</option>
                                <option value="PUBLICADO">PUBLICADO</option>
                            </select>
                        </div>
                        <div>
                            <label style={labelStyle}>País</label>
                            <input style={inputStyle} value={form.pais}
                                   onChange={e => setForm({ ...form, pais: e.target.value })} />
                        </div>
                        <div style={{ gridColumn: 'span 2' }}>
                            <label style={labelStyle}>Descripción</label>
                            <textarea style={{ ...inputStyle, height: '80px' }} value={form.descripcion}
                                      onChange={e => setForm({ ...form, descripcion: e.target.value })} />
                        </div>
                    </div>
                    <button type="submit" style={{ ...btnPrimary, marginTop: '16px' }}>Guardar</button>
                </form>
            )}

            <div style={{ display: 'grid', gap: '12px' }}>
                {documentos.length === 0 && (
                    <p style={{ color: '#64748b' }}>No hay documentos disponibles.</p>
                )}
                {documentos.map(doc => (
                    <div key={doc.id} style={{
                        background: 'white', padding: '20px', borderRadius: '10px',
                        boxShadow: '0 1px 4px rgba(0,0,0,0.06)',
                        display: 'flex', justifyContent: 'space-between', alignItems: 'center'
                    }}>
                        <div>
                            <h3 style={{ color: '#1e293b', marginBottom: '4px' }}>{doc.titulo}</h3>
                            <p style={{ color: '#64748b', fontSize: '13px' }}>
                                Estado: <strong>{doc.estado}</strong> |
                                Nivel: <strong>{doc.nivelConfidencialidad}</strong> |
                                País: <strong>{doc.pais}</strong>
                            </p>
                        </div>
                        <div style={{ display: 'flex', gap: '8px' }}>
                            {doc.estado === 'PENDIENTE' && (
                                <button onClick={() => aprobar(doc.id)} style={btnSuccess}>Aprobar</button>
                            )}
                            <button onClick={() => eliminar(doc.id)} style={btnDanger}>Eliminar</button>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    )
}

const btnPrimary = { padding: '10px 20px', background: '#3b82f6', color: 'white', border: 'none', borderRadius: '8px', cursor: 'pointer', fontWeight: 'bold' }
const btnSuccess = { padding: '8px 14px', background: '#22c55e', color: 'white', border: 'none', borderRadius: '6px', cursor: 'pointer' }
const btnDanger = { padding: '8px 14px', background: '#ef4444', color: 'white', border: 'none', borderRadius: '6px', cursor: 'pointer' }
const labelStyle = { display: 'block', marginBottom: '4px', fontSize: '13px', fontWeight: '600', color: '#374151' }
const inputStyle = { width: '100%', padding: '8px 10px', border: '1px solid #d1d5db', borderRadius: '6px', fontSize: '14px', boxSizing: 'border-box' }