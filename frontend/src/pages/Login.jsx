import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import api from '../api/axios'

export default function Login() {
    const [form, setForm] = useState({ correo: '', password: '' })
    const [error, setError] = useState('')
    const { login } = useAuth()
    const navigate = useNavigate()

    const handleSubmit = async (e) => {
        e.preventDefault()
        setError('')
        try {
            const res = await api.post('/auth/login', form)
            login(res.data)
            navigate('/')
        } catch (err) {
            setError(err.response?.data || 'Error al iniciar sesión')
        }
    }

    return (
        <div style={{
            minHeight: '100vh', display: 'flex',
            alignItems: 'center', justifyContent: 'center',
            background: '#0f172a'
        }}>
            <div style={{
                background: 'white', padding: '40px', borderRadius: '12px',
                width: '360px', boxShadow: '0 4px 24px rgba(0,0,0,0.3)'
            }}>
                <h1 style={{ textAlign: 'center', color: '#1e293b', marginBottom: '8px' }}>
                    🔐 SecureDocs
                </h1>
                <p style={{ textAlign: 'center', color: '#64748b', marginBottom: '24px' }}>
                    Sistema de Gestión de Expedientes
                </p>
                {error && (
                    <div style={{
                        background: '#fee2e2', color: '#b91c1c', padding: '10px',
                        borderRadius: '6px', marginBottom: '16px', fontSize: '14px'
                    }}>{error}</div>
                )}
                <form onSubmit={handleSubmit}>
                    <div style={{ marginBottom: '16px' }}>
                        <label style={labelStyle}>Correo</label>
                        <input
                            type="email"
                            value={form.correo}
                            onChange={e => setForm({ ...form, correo: e.target.value })}
                            style={inputStyle}
                            placeholder="correo@empresa.com"
                            required
                        />
                    </div>
                    <div style={{ marginBottom: '24px' }}>
                        <label style={labelStyle}>Contraseña</label>
                        <input
                            type="password"
                            value={form.password}
                            onChange={e => setForm({ ...form, password: e.target.value })}
                            style={inputStyle}
                            placeholder="••••••••"
                            required
                        />
                    </div>
                    <button type="submit" style={{
                        width: '100%', padding: '12px', background: '#3b82f6',
                        color: 'white', border: 'none', borderRadius: '8px',
                        fontSize: '16px', cursor: 'pointer', fontWeight: 'bold'
                    }}>
                        Ingresar
                    </button>
                </form>
            </div>
        </div>
    )
}

const labelStyle = {
    display: 'block', marginBottom: '6px',
    fontSize: '14px', fontWeight: '600', color: '#374151'
}

const inputStyle = {
    width: '100%', padding: '10px 12px', border: '1px solid #d1d5db',
    borderRadius: '6px', fontSize: '14px', boxSizing: 'border-box'
}