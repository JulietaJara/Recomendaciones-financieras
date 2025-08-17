const API_BASE_URL = "http://localhost:8080/api"

class ApiClient {
  private getAuthHeaders() {
    const token = localStorage.getItem("token")
    return {
      "Content-Type": "application/json",
      ...(token && { Authorization: `Bearer ${token}` }),
    }
  }

  async get(endpoint: string) {
    const response = await fetch(`${API_BASE_URL}${endpoint}`, {
      method: "GET",
      headers: this.getAuthHeaders(),
    })

    if (!response.ok) {
      throw new Error(`API Error: ${response.status}`)
    }

    return response.json()
  }

  async post(endpoint: string, data: any) {
    const response = await fetch(`${API_BASE_URL}${endpoint}`, {
      method: "POST",
      headers: this.getAuthHeaders(),
      body: JSON.stringify(data),
    })

    if (!response.ok) {
      throw new Error(`API Error: ${response.status}`)
    }

    return response.json()
  }

  async put(endpoint: string, data: any) {
    const response = await fetch(`${API_BASE_URL}${endpoint}`, {
      method: "PUT",
      headers: this.getAuthHeaders(),
      body: JSON.stringify(data),
    })

    if (!response.ok) {
      throw new Error(`API Error: ${response.status}`)
    }

    return response.json()
  }
}

export const apiClient = new ApiClient()

// API functions
export const userApi = {
  getCurrentUser: () => apiClient.get("/users/me"),
  updateProfile: (profileData: any) => apiClient.put("/users/profile", profileData),
}

export const portfolioApi = {
  getUserPortfolios: () => apiClient.get("/portfolios"),
  getPortfolio: (id: number) => apiClient.get(`/portfolios/${id}`),
}

export const recommendationApi = {
  getUserRecommendations: () => apiClient.get("/recommendations"),
  getPendingRecommendations: () => apiClient.get("/recommendations/pending"),
  generateRecommendations: () => apiClient.post("/recommendations/generate", {}),
  acceptRecommendation: (id: number) => apiClient.post(`/recommendations/${id}/accept`, {}),
  rejectRecommendation: (id: number) => apiClient.post(`/recommendations/${id}/reject`, {}),
}
