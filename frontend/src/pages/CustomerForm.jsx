import React, { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import api from '../api/axios'

// form component for creating and editing customers
export default function CustomerForm() {
  // if id param is present, then edit mode
  const { id } = useParams()
  const navigate = useNavigate()
  const editMode = Boolean(id)
  // form state
  const [form, setForm] = useState({
    name: '',
    dateOfBirth: '',
    nic: '',
    phoneNumbers: [],
    addresses: []
  })
  // master data for dropdowns
  const [countries, setCountries] = useState([])
  const [cities, setCities] = useState([])

  // fetch master data and customer data if in edit mode
  useEffect(() => {
    fetchMasters()
    if (editMode) fetchCustomer()
  }, [])

  // fetch countries and cities for dropdowns
  async function fetchMasters() {
    try {
      const [cRes, cityRes] = await Promise.all([api.get('/master/countries'), api.get('/master/cities')])
      setCountries(cRes.data || [])
      setCities(cityRes.data || [])
      console.log('Fetched countries and cities', cRes.data, cityRes.data)
    } catch (err) {
      console.error(err)
    }
  }

  // fetch customer data and populate form
  async function fetchCustomer() {
    try {
      const res = await api.get(`/customers/${id}`)
      setForm({
        name: res.data.name || '',
        dateOfBirth: res.data.dateOfBirth || '',
        nic: res.data.nic || '',
        phoneNumbers: (res.data.phoneNumbers || []).map(p => ({ phone: p.phone })),
        addresses: (res.data.addresses || []).map(a => ({
          addressLine1: a.addressLine1 || '',
          addressLine2: a.addressLine2 || '',
          cityId: a.cityId || a.city?.id || '',
          countryId: a.countryId || a.country?.id || ''
        }))
      })
    } catch (err) {
      console.error(err)
      alert('Failed to load customer')
    }
  }

  // helper functions to update form state
  function updateField(field, value) {
    setForm(prev => ({ ...prev, [field]: value }))
  }

  // phone numbers are stored as array of objects with a "phone" property to match backend structure
  function updatePhone(index, value) {
    setForm(prev => {
      const phones = [...prev.phoneNumbers]
      phones[index] = { phone: value }
      return { ...prev, phoneNumbers: phones }
    })
  }

  // add a new empty phone number to the form
  function addPhone() {
    setForm(prev => ({ ...prev, phoneNumbers: [...prev.phoneNumbers, { phone: '' }] }))
  }

  // remove a phone number at a specific index
  function removePhone(i) {
    setForm(prev => ({ ...prev, phoneNumbers: prev.phoneNumbers.filter((_, idx) => idx !== i) }))
  }

  // addresses are stored as array of objects with addressLine1, addressLine2, cityId and country
  function addAddress() {
    setForm(prev => ({ ...prev, addresses: [...prev.addresses, { addressLine1: '', addressLine2: '', cityId: '', countryId: '' }] }))
  }

  // update a specific field of an address at a specific index
  function updateAddress(i, field, value) {
    setForm(prev => {
      const addresses = [...prev.addresses]
      addresses[i] = { ...addresses[i], [field]: value }
      return { ...prev, addresses }
    })
  }

  // remove an address at a specific index
  function removeAddress(i) {
    setForm(prev => ({ ...prev, addresses: prev.addresses.filter((_, idx) => idx !== i) }))
  }

  // submit form data to backend, either create or update based on edit mode
  async function submit(e) {
    e.preventDefault()
    try {
      if (!form.name || !form.dateOfBirth || !form.nic) {
        alert('Name, Date of Birth and NIC are required')
        return
      }
      if (editMode) {
        await api.put(`/customers/${id}`, form)
        alert('Customer updated')
      } else {
        await api.post('/customers', form)
        alert('Customer created')
      }
      navigate('/customers')
    } catch (err) {
      console.error(err)
      alert('Failed to save customer: ' + (err.response?.data?.message || err.message))
    }
  }

  return (
    <div>
      {/* Page title */}
      <h1 className="text-xl font-semibold mb-4">{editMode ? 'Edit Customer' : 'Create Customer'}</h1>
      {/* Form */}
      <form onSubmit={submit} className="space-y-4">
        <div>
          <label className="block mb-1">Name *</label>
          <input value={form.name} onChange={e => updateField('name', e.target.value)} className="w-full border px-3 py-2 rounded" />
        </div>

        <div>
          <label className="block mb-1">Date of Birth (yyyy-MM-dd) *</label>
          <input value={form.dateOfBirth} onChange={e => updateField('dateOfBirth', e.target.value)} className="w-48 border px-3 py-2 rounded" placeholder="1990-01-01" />
        </div>

        <div>
          <label className="block mb-1">NIC *</label>
          <input value={form.nic} onChange={e => updateField('nic', e.target.value)} className="w-64 border px-3 py-2 rounded" />
        </div>


        <div>
          <label className="block mb-2">Phone Numbers</label>
          {form.phoneNumbers.map((p, i) => (
            <div key={i} className="flex items-center mb-2 space-x-2">
              <input value={p.phone} onChange={e => updatePhone(i, e.target.value)} className="flex-1 border px-3 py-2 rounded" />
              <button type="button" onClick={() => removePhone(i)} className="px-3 py-1 bg-red-500 text-white rounded">Remove</button>
            </div>
          ))}
          <button type="button" onClick={addPhone} className="px-3 py-1 bg-indigo-600 text-white rounded">Add Phone</button>
        </div>

        
        <div>
          <label className="block mb-2">Addresses</label>
          {form.addresses.map((a, i) => (
            <div key={i} className="mb-3 p-3 border rounded">
              <div className="mb-2">
                <label className="block text-sm mb-1">Address Line 1</label>
                <input value={a.addressLine1} onChange={e => updateAddress(i, 'addressLine1', e.target.value)} className="w-full border px-3 py-2 rounded" />
              </div>
              <div className="mb-2">
                <label className="block text-sm mb-1">Address Line 2</label>
                <input value={a.addressLine2} onChange={e => updateAddress(i, 'addressLine2', e.target.value)} className="w-full border px-3 py-2 rounded" />
              </div>
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm mb-1">Country</label>
                  <select value={a.countryId} onChange={e => updateAddress(i, 'countryId', e.target.value)} className="w-full border px-3 py-2 rounded">
                    <option value="">Select</option>
                    {countries.map(c => <option value={c.id} key={c.id}>{c.name}</option>)}
                  </select>
                </div>
                <div>
                  <label className="block text-sm mb-1">City</label>
                  <select value={a.cityId} onChange={e => updateAddress(i, 'cityId', e.target.value)} className="w-full border px-3 py-2 rounded">
                    <option value="">Select</option>
                    {cities.map(c => <option value={c.id} key={c.id}>{c.name} ({c.country?.name || c.countryId})</option>)}
                  </select>
                </div>
              </div>
              <div className="mt-2">
                <button type="button" onClick={() => removeAddress(i)} className="px-3 py-1 bg-red-500 text-white rounded">Remove Address</button>
              </div>
            </div>
          ))}
          <button type="button" onClick={addAddress} className="px-3 py-1 bg-indigo-600 text-white rounded">Add Address</button>
        </div>

        <div>
          <button type="submit" className="px-4 py-2 bg-green-600 text-white rounded">{editMode ? 'Update' : 'Create'}</button>
        </div>
      </form>
    </div>
  )
}