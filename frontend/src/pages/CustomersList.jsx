import React, { useEffect, useState } from 'react'
import api from '../api/axios'
import CustomerTable from '../components/CustomerTable'


 // Page to display list of customers 

export default function CustomersList() {
  // State variables
  const [customers, setCustomers] = useState([])
  const [page, setPage] = useState(0)
  const [size] = useState(25)
  const [total, setTotal] = useState(0)
  const [loading, setLoading] = useState(false)

  // Load data when page changes
  useEffect(() => {
    load()
  }, [page])

  // Fetch customers from backend
  async function load() {
    setLoading(true)
    try {
      const res = await api.get('/customers', { params: { page, size, sort: 'id' } })
      // backend returns Page<CustomerDto>
      setCustomers(res.data.content || [])
      setTotal(res.data.totalElements || 0)
    } catch (err) {
      console.error(err)
      alert('Failed to load customers')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div>
      {/* Header section */}
      <div className="flex items-center justify-between mb-4">
        <h1 className="text-xl font-semibold">Customers</h1>
        <div>
          {/* Refresh button */}
          <button onClick={() => setPage(0)} className="mr-2 bg-gray-200 px-3 py-1 rounded">Refresh</button>
        </div>
      </div>
      {/* Loading state */}
      {loading ? (
        <div>Loading...</div>
      ) : (
        <>
        {/* Table component */}
          <CustomerTable customers={customers} />
          {/* Pagination section */}
          <div className="mt-4 flex items-center justify-between">
            <div>Showing {customers.length} of {total}</div>
            {/* Pagination controls */}
            <div className="space-x-2">
              <button onClick={() => setPage(p => Math.max(0, p - 1))} className="px-3 py-1 bg-gray-100 rounded">Prev</button>
              <span>Page {page + 1}</span>
              <button onClick={() => setPage(p => p + 1)} className="px-3 py-1 bg-gray-100 rounded">Next</button>
            </div>
          </div>
        </>
      )}
    </div>
  )
}