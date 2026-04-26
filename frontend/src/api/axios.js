import axios from 'axios'

// base url from environment variable or default to backend url
const baseURL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8081/api'

const api = axios.create({
  baseURL, // base url for all API requests
  timeout: 120000,  // Request timeout 2 minutes
})

export default api