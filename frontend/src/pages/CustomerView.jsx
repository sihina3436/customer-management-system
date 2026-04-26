import React, { useEffect, useState } from 'react'
import { useParams, Link } from 'react-router-dom'
import api from '../api/axios'


// component to view a single customer
export default function CustomerView() {
  // get customer ID from URL
  const { id } = useParams()
  // state to store customer data
  const [customer, setCustomer] = useState(null)

  // load customer when component mounts or ID changes
  useEffect(() => {
    load()
  }, [id])

  // fetch customer details from backend
  async function load() {
    try {
      const res = await api.get(`/customers/${id}`)
      setCustomer(res.data)
    } catch (err) {
      console.error(err)
      alert('Failed to load customer')
    }
  }
  // Show loading until data is available
  if (!customer) return <div>Loading...</div>

  return (
    <div>
      {/* Customer header */}
      <div className="flex items-center justify-between mb-4">
        <h1 className="text-xl font-semibold">Customer: {customer.name}</h1>
        <div>
          {/* Link to edit page */}
          <Link to={`/customers/${id}/edit`} className="px-3 py-1 bg-green-600 text-white rounded">Edit</Link>
        </div>
      </div>

    {/* Main content */}
      <div className="grid grid-cols-2 gap-4">
        {/* Customer basic details */}
        <div className="p-4 border rounded">
          <h2 className="font-semibold mb-2">Details</h2>
          <p><strong>ID:</strong> {customer.id}</p>
          <p><strong>Name:</strong> {customer.name}</p>
          <p><strong>Date of Birth:</strong> {customer.dateOfBirth}</p>
          <p><strong>NIC:</strong> {customer.nic}</p>
        </div>
        {/* Phone numbers */}
        <div className="p-4 border rounded">
          <h2 className="font-semibold mb-2">Phones</h2>
          <ul>
            {(customer.phoneNumbers || []).map(p => <li key={p.id}>{p.phone}</li>)}
          </ul>
        </div>
          {/* Addresses */}
        <div className="p-4 border rounded col-span-2">
          <h2 className="font-semibold mb-2">Addresses</h2>
          <ul>
            {(customer.addresses || []).map(a =>
              <li key={a.id} className="mb-2">
                {a.addressLine1} {a.addressLine2 && `, ${a.addressLine2}`} {a.city ? `, ${a.city.name}` : ''} {a.country ? `, ${a.country.name}` : ''}
              </li>
            )}
          </ul>
        </div>
      </div>
    </div>
  )
}