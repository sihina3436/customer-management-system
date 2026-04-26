import React from 'react'
import { Link } from 'react-router-dom'


//  Home page component
export default function Home() {
  return (
    <div>
      {/* Page title */}
      <h1 className="text-2xl font-semibold mb-4">Welcome</h1>
      {/* Description */}
      <p className="mb-6">Use the navigation links to manage customers or upload CSV files for bulk import.</p>
      {/* Navigation buttons */}
      <div className="flex space-x-4">
        <Link to="/customers" className="bg-indigo-600 text-white px-4 py-2 rounded">View Customers</Link>
        <Link to="/customers/new" className="bg-green-600 text-white px-4 py-2 rounded">Create Customer</Link>
        <Link to="/bulk-upload" className="bg-gray-800 text-white px-4 py-2 rounded">Bulk Upload</Link>
      </div>
    </div>
  )
}