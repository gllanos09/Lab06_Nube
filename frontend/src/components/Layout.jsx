import { Outlet, Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export default function Layout() {
    const { user, logout } = useAuth()
    const navigate = useNavigate()

    const handleLogout = () => {
        logout()
        navigate('/login')
    }

    return (
        <div style={{ display: 'flex', minHeight: '100vh' }}>
            <aside style={{
                width: '220px', background: '#1e293b', color: 'white',
                padding: '20px', display: 'flex', flexDirection: 'column', gap: '12px'
            }}>
                <h2 style={{ marginBottom: '20px', color: '#38bdf8' }}>SecureDocs</h2>
                <Link to="/" style={linkStyle}>🏠 Dashboard</Link>
                <Link to="/documentos" style={linkStyle}>📄 Documentos</Link>
                {user?.rol === 'ADMINISTRADOR' && (
                    <Link to="/usuarios" style={linkStyle}>👥 Usuarios</Link>
                )}
                {['ADMINISTRADOR', 'GERENTE', 'AUDITOR'].includes(user?.rol) && (
                    <Link to="/auditoria" style={linkStyle}>🔍 Auditoría</Link>
                )}
                <div style={{ marginTop: 'auto' }}>
                    <p style={{ fontSize: '12px', color: '#94a3b8' }}>{user?.correo}</p>
                    <p style={{ fontSize: '11px', color: '#64748b' }}>{user?.rol}</p>
                    <button onClick={handleLogout} style={btnStyle}>Cerrar sesión</button>
                </div>
            </aside>
            <main style={{ flex: 1, padding: '30px', background: '#f1f5f9' }}>
                <Outlet />
            </main>
        </div>
    )
}

const linkStyle = {
    color: '#cbd5e1', textDecoration: 'none', padding: '8px 12px',
    borderRadius: '6px', display: 'block'
}

const btnStyle = {
    marginTop: '10px', width: '100%', padding: '8px',
    background: '#ef4444', color: 'white', border: 'none',
    borderRadius: '6px', cursor: 'pointer'
}