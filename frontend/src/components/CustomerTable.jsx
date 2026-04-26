import React from 'react'
import { Link } from 'react-router-dom'

// Component to display customer list in a table
// Receives "customers" as props

export default function CustomerTable({ customers }) {
  return (
    <div className="overflow-x-auto">
      {/* Table container with horizontal scroll */}
      <table className="min-w-full bg-white border">
        {/* Table Header */}
        <thead className="bg-gray-100">
          <tr>
            <th className="px-4 py-2 text-left">ID</th>
            <th className="px-4 py-2 text-left">Name</th>
            <th className="px-4 py-2 text-left">DOB</th>
            <th className="px-4 py-2 text-left">NIC</th>
            <th className="px-4 py-2 text-left">Actions</th>
          </tr>
        </thead>
        <tbody>
          {/* Table Body */}
          {customers.map(c => (
            // Loop through customer list
            <tr key={c.id} className="border-t">
              <td className="px-4 py-2">{c.id}</td>
              <td className="px-4 py-2">{c.name}</td>
              <td className="px-4 py-2">{c.dateOfBirth}</td>
              <td className="px-4 py-2">{c.nic}</td>
              {/* Action buttons */}
              <td className="px-4 py-2">
                <Link to={`/customers/${c.id}`} className="text-indigo-600 hover:underline mr-3">View</Link>
                <Link to={`/customers/${c.id}/edit`} className="text-green-600 hover:underline">Edit</Link>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}