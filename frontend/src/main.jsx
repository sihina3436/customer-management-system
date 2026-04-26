import React from 'react'
import { createRoot } from 'react-dom/client'
import { BrowserRouter, Routes, Route } from 'react-router-dom'
import './index.css'

// Import main layout and pages
import App from './App'
import Home from './pages/Home'
import CustomersList from './pages/CustomersList'
import CustomerForm from './pages/CustomerForm'
import CustomerView from './pages/CustomerView'
import BulkUpload from './pages/BulkUpload'

// Render React app into root element
createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    {/* Enable routing */}
    <BrowserRouter>
    {/* Define all routes */}
      <Routes>
        {/* Default route */}
        <Route path="/" element={<App />}>
          {/* Home route */}
          <Route index element={<Home />} />
          {/* Customers routes */}
          <Route path="customers" element={<CustomersList />} />
          {/* Create new customer */}
          <Route path="customers/new" element={<CustomerForm />} />
          {/* Edit customer */}
          <Route path="customers/:id/edit" element={<CustomerForm />} />
          {/* View customer */}
          <Route path="customers/:id" element={<CustomerView />} />
          {/* Bulk upload */}
          <Route path="bulk-upload" element={<BulkUpload />} />
        </Route>
      </Routes>
    </BrowserRouter>
  </React.StrictMode>
)