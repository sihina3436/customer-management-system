import React from 'react'
import { Outlet } from 'react-router-dom'
import Header from './components/Header'

export default function App() {
  return (
    <div className="min-h-screen bg-gray-50">
      <Header />
      <main className="container bg-white rounded shadow p-6 mt-6">
        <Outlet />
      </main>
    </div>
  )
}