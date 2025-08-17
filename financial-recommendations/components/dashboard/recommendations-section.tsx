"use client"

import { useState, useEffect } from "react"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import { Alert, AlertDescription } from "@/components/ui/alert"
import { TrendingUp, Target, Lightbulb, CheckCircle, XCircle, RefreshCw } from "lucide-react"
import { recommendationApi } from "@/lib/api"

interface Recommendation {
  id: number
  recommendationType: string
  title: string
  description: string
  reasoning: string
  expectedReturn?: number
  confidenceScore: number
  priority: number
  riskAssessment: string
  timeHorizon: number
  status: string
}

const getTypeIcon = (type: string) => {
  switch (type) {
    case "BUY":
      return <TrendingUp className="h-4 w-4" />
    case "SELL":
      return <TrendingUp className="h-4 w-4 rotate-180" />
    case "REBALANCE":
      return <Target className="h-4 w-4" />
    default:
      return <Lightbulb className="h-4 w-4" />
  }
}

const getTypeColor = (type: string) => {
  switch (type) {
    case "BUY":
      return "bg-accent text-accent-foreground"
    case "SELL":
      return "bg-destructive text-destructive-foreground"
    case "REBALANCE":
      return "bg-primary text-primary-foreground"
    default:
      return "bg-secondary text-secondary-foreground"
  }
}

const getPriorityColor = (priority: number) => {
  switch (priority) {
    case 1:
      return "bg-destructive text-destructive-foreground"
    case 2:
      return "bg-primary text-primary-foreground"
    case 3:
      return "bg-secondary text-secondary-foreground"
    default:
      return "bg-muted text-muted-foreground"
  }
}

const getPriorityLabel = (priority: number) => {
  switch (priority) {
    case 1:
      return "HIGH"
    case 2:
      return "MEDIUM"
    case 3:
      return "LOW"
    default:
      return "NORMAL"
  }
}

export function RecommendationsSection() {
  const [recommendations, setRecommendations] = useState<Recommendation[]>([])
  const [isLoading, setIsLoading] = useState(true)
  const [isGenerating, setIsGenerating] = useState(false)
  const [error, setError] = useState("")

  const loadRecommendations = async () => {
    try {
      setIsLoading(true)
      const data = await recommendationApi.getUserRecommendations()
      setRecommendations(data)
      setError("")
    } catch (err) {
      setError("Failed to load recommendations")
      console.error("Error loading recommendations:", err)
    } finally {
      setIsLoading(false)
    }
  }

  const generateRecommendations = async () => {
    try {
      setIsGenerating(true)
      setError("")
      const data = await recommendationApi.generateRecommendations()
      setRecommendations(data)
    } catch (err: any) {
      setError(err.message || "Failed to generate recommendations. Please complete your profile first.")
      console.error("Error generating recommendations:", err)
    } finally {
      setIsGenerating(false)
    }
  }

  const handleAcceptRecommendation = async (id: number) => {
    try {
      await recommendationApi.acceptRecommendation(id)
      await loadRecommendations() // Reload to get updated status
    } catch (err) {
      console.error("Error accepting recommendation:", err)
    }
  }

  const handleRejectRecommendation = async (id: number) => {
    try {
      await recommendationApi.rejectRecommendation(id)
      await loadRecommendations() // Reload to get updated status
    } catch (err) {
      console.error("Error rejecting recommendation:", err)
    }
  }

  useEffect(() => {
    loadRecommendations()
  }, [])

  if (isLoading) {
    return (
      <Card className="bg-card border-border">
        <CardContent className="flex items-center justify-center py-8">
          <div className="text-center">
            <RefreshCw className="h-8 w-8 animate-spin text-primary mx-auto mb-2" />
            <p className="text-muted-foreground">Loading recommendations...</p>
          </div>
        </CardContent>
      </Card>
    )
  }

  return (
    <Card className="bg-card border-border">
      <CardHeader>
        <div className="flex items-center justify-between">
          <CardTitle className="text-xl font-bold text-card-foreground flex items-center space-x-2">
            <Lightbulb className="h-6 w-6 text-primary" />
            <span>AI Recommendations</span>
          </CardTitle>
          <Button
            onClick={generateRecommendations}
            disabled={isGenerating}
            className="bg-primary text-primary-foreground hover:bg-primary/90"
          >
            {isGenerating ? (
              <>
                <RefreshCw className="h-4 w-4 mr-2 animate-spin" />
                Generating...
              </>
            ) : (
              "Generate New"
            )}
          </Button>
        </div>
      </CardHeader>
      <CardContent className="space-y-4">
        {error && (
          <Alert variant="destructive">
            <AlertDescription>{error}</AlertDescription>
          </Alert>
        )}

        {recommendations.length === 0 ? (
          <div className="text-center py-8">
            <Lightbulb className="h-12 w-12 text-muted-foreground mx-auto mb-4" />
            <h3 className="text-lg font-medium text-card-foreground mb-2">No Recommendations Yet</h3>
            <p className="text-muted-foreground mb-4">
              Complete your investment profile to receive personalized recommendations.
            </p>
            <Button
              onClick={generateRecommendations}
              disabled={isGenerating}
              className="bg-primary text-primary-foreground hover:bg-primary/90"
            >
              Generate Recommendations
            </Button>
          </div>
        ) : (
          recommendations.map((rec) => (
            <Card key={rec.id} className="bg-muted/50 border-border">
              <CardContent className="p-6">
                <div className="flex items-start justify-between mb-4">
                  <div className="flex items-center space-x-3">
                    <Badge className={getTypeColor(rec.recommendationType)}>
                      {getTypeIcon(rec.recommendationType)}
                      <span className="ml-1">{rec.recommendationType}</span>
                    </Badge>
                    <Badge variant="outline" className={getPriorityColor(rec.priority)}>
                      {getPriorityLabel(rec.priority)}
                    </Badge>
                    <div className="flex items-center space-x-1">
                      <span className="text-sm text-muted-foreground">Confidence:</span>
                      <span className="text-sm font-medium text-card-foreground">
                        {Math.round(rec.confidenceScore * 100)}%
                      </span>
                    </div>
                  </div>
                  {rec.status === "PENDING" && (
                    <div className="flex space-x-2">
                      <Button
                        size="sm"
                        className="bg-primary text-primary-foreground hover:bg-primary/90"
                        onClick={() => handleAcceptRecommendation(rec.id)}
                      >
                        <CheckCircle className="h-4 w-4 mr-1" />
                        Accept
                      </Button>
                      <Button
                        variant="outline"
                        size="sm"
                        className="border-border text-muted-foreground hover:text-destructive bg-transparent"
                        onClick={() => handleRejectRecommendation(rec.id)}
                      >
                        <XCircle className="h-4 w-4 mr-1" />
                        Reject
                      </Button>
                    </div>
                  )}
                  {rec.status !== "PENDING" && (
                    <Badge variant={rec.status === "ACCEPTED" ? "default" : "secondary"}>{rec.status}</Badge>
                  )}
                </div>

                <h3 className="text-lg font-semibold text-card-foreground mb-2">{rec.title}</h3>
                <p className="text-muted-foreground mb-3">{rec.description}</p>

                <div className="bg-card rounded-lg p-4 mb-4">
                  <h4 className="text-sm font-medium text-card-foreground mb-2">Reasoning:</h4>
                  <p className="text-sm text-muted-foreground">{rec.reasoning}</p>
                </div>

                <div className="grid grid-cols-2 md:grid-cols-3 gap-4 text-sm">
                  {rec.expectedReturn && (
                    <div>
                      <span className="text-muted-foreground">Expected Return:</span>
                      <p className="font-medium text-accent">{rec.expectedReturn}%</p>
                    </div>
                  )}
                  <div>
                    <span className="text-muted-foreground">Risk Assessment:</span>
                    <p className="font-medium text-card-foreground">{rec.riskAssessment}</p>
                  </div>
                  <div>
                    <span className="text-muted-foreground">Time Horizon:</span>
                    <p className="font-medium text-card-foreground">{Math.round(rec.timeHorizon / 30)} months</p>
                  </div>
                </div>
              </CardContent>
            </Card>
          ))
        )}
      </CardContent>
    </Card>
  )
}
