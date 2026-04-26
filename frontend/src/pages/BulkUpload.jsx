import React, { useState } from 'react'
import api from '../api/axios'

// component for bulk uploading customers
// supports CSV and Excel (.xlsx) files

export default function BulkUpload() {
  // state variables
  const [file, setFile] = useState(null)
  const [progress, setProgress] = useState(0)
  const [processing, setProcessing] = useState(false)
  const [result, setResult] = useState(null)

  // handle form submit
  async function submit(e) {
    e.preventDefault()
    if (!file) {
      alert('Select a file first')
      return
    }
    const fd = new FormData()
    fd.append('file', file)

    setProcessing(true)
    setProgress(0)
    setResult(null)

    try {
      // Send file to backend
      const res = await api.post('/customers/bulk-upload', fd, {
        headers: { 'Content-Type': 'multipart/form-data' },
        onUploadProgress: (evt) => {
          if (evt.total) {
            setProgress(Math.round((evt.loaded * 100) / evt.total))
          }
        }
      })
      // Save result
      setResult(res.data)
      // Show alert with processed count
      alert('Upload processed: ' + (res.data.processed || 0) + ' rows')
    } catch (err) {
      // Log error and show alert
      console.error(err)
      alert('Upload failed: ' + (err.response?.data?.message || err.message))
    } finally {
      // Reset processing state
      setProcessing(false)
    }
  }

  return (
    <div>
      {/* Page title  */}
      <h1 className="text-xl font-semibold mb-4">Bulk Upload Customers (CSV or XLSX)</h1>
        {/* File input  */}
        <form onSubmit={submit} className="space-y-3">
          <input
            type="file"
            accept=".csv,.xlsx"
            onChange={e => setFile(e.target.files[0])}
            className="block w-2/6 text-sm border border-gray-300 rounded px-3 py-2"
          />
          {/* Submit button */}
          <button
            disabled={processing}
            type="submit"
            className="px-4 py-2 bg-indigo-600 text-white rounded disabled:bg-gray-400"
          >
            {processing ? 'Uploading...' : 'Upload'}
          </button>
        </form>

      {/* Progress bar */}
      {processing && (
        <div className="mt-4">
          <div className="w-full bg-gray-200 h-4 rounded">
            <div className="bg-indigo-600 h-4 rounded" style={{ width: `${progress}%` }} />
          </div>
          <div className="mt-2">{progress}%</div>
        </div>
      )}
      {/* Result */}
      {result && (
        <div className="mt-4 p-4 border rounded">
          <div><strong>Processed:</strong> {result.processed}</div>
          <div><strong>Message:</strong> {result.message}</div>
        </div>
      )}
      {/* Notes */}
      <div className="mt-6 text-sm text-gray-600">
        Note: For large files prefer CSV encoded as UTF-8 and use curl for upload to avoid browser timeouts.
      </div>
    </div>
  )
}