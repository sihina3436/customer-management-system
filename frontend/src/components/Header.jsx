import React from 'react'
import { Link } from 'react-router-dom'

//  header component (Navigation bar)
//  displayed at the top of the application
//  provides navigation links to different pages

export default function Header() {
  return (
    <header className="bg-indigo-600 text-white">
       {/* header container with background color */}
      <div className="container flex items-center justify-between py-4">
        {/* application title ( navigate to home page) */}
        <Link to="/" className="text-xl font-semibold">Customer Management</Link>
        {/* Navigation menu */}
        <nav className="space-x-4">
          {/* Navigation links */}
          <Link to="/customers" className="hover:underline">Customers</Link>
          <Link to="/customers/new" className="hover:underline">New Customer</Link>
          <Link to="/bulk-upload" className="hover:underline">Bulk Upload</Link>
        </nav>
      </div>
    </header>
  )
}