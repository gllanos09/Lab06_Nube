import { createContext, useContext, useState } from 'react'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
    const [user, setUser] = useState(() => {
        const token = localStorage.getItem('token')
        const correo = localStorage.getItem('correo')
        const rol = localStorage.getItem('rol')
        return token ? { token, correo, rol } : null
    })

    const login = (data) => {
        localStorage.setItem('token', data.token)
        localStorage.setItem('correo', data.correo)
        localStorage.setItem('rol', data.rol)
        setUser(data)
    }

    const logout = () => {
        localStorage.clear()
        setUser(null)
    }

    return (
        <AuthContext.Provider value={{ user, login, logout }}>
            {children}
        </AuthContext.Provider>
    )
}

export const useAuth = () => useContext(AuthContext)