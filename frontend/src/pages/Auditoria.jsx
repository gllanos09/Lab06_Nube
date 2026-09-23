import { useState, useEffect } from 'react'
import api from '../api/axios'

export default function Auditoria() {
    const [registros, setRegistros] = useState([])
    const [error, setError] = useState('')

    useEffect(() => {
        api.get('/auditoria')
            .then(res => setRegistros(res.data))
            .catch(() => setError('No tiene permiso para ver la auditoría'))
    }, [])

    return (
        <div>
            <h1 style={{ color: '#1e293b', marginBottom: '24px' }}>🔍 Registro de Auditoría</h1>
            {error && (
                <div style={{ background: '#fee2e2', color: '#b91c1c', padding: '10px', borderRadius: '6px', marginBottom: '16px' }}>
                    {error}
                </div>
            )}
            <div style={{ background: 'white', borderRadius: '12px', overflow: 'hidden', boxShadow: '0 1px 4px rgba(0,0,0,0.06)' }}>
                <table style={{ width: '100%', borderCollapse: 'collapse' }}>
                    <thead>
                    <tr style={{ background: '#f8fafc' }}>
                        {['Usuario', 'Recurso', 'Acción', 'Fecha', 'Resultado', 'Motivo'].map(h => (
                            <th key={h} style={{ padding: '12px 16px', textAlign: 'left', fontSize: '13px', color: '#64748b', fontWeight: '600' }}>{h}</th>
                        ))}
                    </tr>
                    </thead>
                    <tbody>
                    {registros.map(r => (
                        <tr key={r.id} style={{ borderTop: '1px solid #f1f5f9' }}>
                            <td style={tdStyle}>{r.usuario}</td>
                            <td style={tdStyle}>{r.recurso}</td>
                            <td style={tdStyle}>{r.accion}</td>
                            <td style={tdStyle}>{new Date(r.fecha).toLocaleString()}</td>
                            <td style={tdStyle}>
                  <span style={{
                      padding: '3px 10px', borderRadius: '20px', fontSize: '12px', fontWeight: '600',
                      background: r.resultado === 'PERMITIDO' ? '#dcfce7' : '#fee2e2',
                      color: r.resultado === 'PERMITIDO' ? '#166534' : '#b91c1c'
                  }}>{r.resultado}</span>
                            </td>
                            <td style={{ ...tdStyle, fontSize: '12px', color: '#64748b' }}>{r.motivo}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </div>
    )
}

const tdStyle = { padding: '12px 16px', fontSize: '14px', color: '#1e293b' }