import { useAuth } from '../context/AuthContext'

export default function Dashboard() {
    const { user } = useAuth()

    return (
        <div>
            <h1 style={{ color: '#1e293b', marginBottom: '8px' }}>
                Bienvenido, {user?.correo}
            </h1>
            <p style={{ color: '#64748b', marginBottom: '32px' }}>
                Rol: <strong>{user?.rol}</strong>
            </p>
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)', gap: '20px' }}>
                {[
                    { icon: '📄', titulo: 'Documentos', desc: 'Gestiona expedientes y archivos' },
                    { icon: '🔒', titulo: 'Control RBAC', desc: 'Permisos basados en roles' },
                    { icon: '🛡️', titulo: 'Políticas ABAC', desc: 'Autorización por atributos' },
                ].map((card, i) => (
                    <div key={i} style={{
                        background: 'white', padding: '24px', borderRadius: '12px',
                        boxShadow: '0 1px 4px rgba(0,0,0,0.08)'
                    }}>
                        <div style={{ fontSize: '32px', marginBottom: '12px' }}>{card.icon}</div>
                        <h3 style={{ color: '#1e293b', marginBottom: '8px' }}>{card.titulo}</h3>
                        <p style={{ color: '#64748b', fontSize: '14px' }}>{card.desc}</p>
                    </div>
                ))}
            </div>
        </div>
    )
}