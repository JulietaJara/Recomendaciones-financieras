"use client"

import { useState, useEffect } from "react"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { TrendingUp, DollarSign, LucidePieChart } from "lucide-react"
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
  PieChart as RechartsPieChart,
  Pie,
  Cell,
} from "recharts"
import { portfolioApi } from "@/lib/api"

const performanceData = [
  { month: "Jan", value: 45000 },
  { month: "Feb", value: 47500 },
  { month: "Mar", value: 46800 },
  { month: "Apr", value: 49200 },
  { month: "May", value: 52100 },
  { month: "Jun", value: 54300 },
]

const allocationData = [
  { name: "Stocks", value: 60, color: "hsl(var(--chart-1))" },
  { name: "Bonds", value: 25, color: "hsl(var(--chart-2))" },
  { name: "International", value: 15, color: "hsl(var(--chart-3))" },
]

export function PortfolioOverview() {
  const [portfolios, setPortfolios] = useState([])
  const [isLoading, setIsLoading] = useState(true)

  useEffect(() => {
    const loadPortfolios = async () => {
      try {
        const data = await portfolioApi.getUserPortfolios()
        setPortfolios(data)
      } catch (err) {
        console.error("Error loading portfolios:", err)
      } finally {
        setIsLoading(false)
      }
    }

    loadPortfolios()
  }, [])

  return (
    <Card className="bg-card border-border">
      <CardHeader>
        <div className="flex items-center justify-between">
          <CardTitle className="text-xl font-bold text-card-foreground">Portfolio Overview</CardTitle>
          <Button
            variant="outline"
            size="sm"
            className="border-border text-muted-foreground hover:text-primary bg-transparent"
          >
            View Details
          </Button>
        </div>
      </CardHeader>
      <CardContent className="space-y-6">
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div className="space-y-2">
            <div className="flex items-center space-x-2">
              <DollarSign className="h-5 w-5 text-primary" />
              <span className="text-sm text-muted-foreground">Total Value</span>
            </div>
            <p className="text-2xl font-bold text-card-foreground">$54,300</p>
            <div className="flex items-center space-x-1">
              <TrendingUp className="h-4 w-4 text-accent" />
              <span className="text-sm text-accent">+8.2% this month</span>
            </div>
          </div>

          <div className="space-y-2">
            <div className="flex items-center space-x-2">
              <TrendingUp className="h-5 w-5 text-primary" />
              <span className="text-sm text-muted-foreground">Total Gain/Loss</span>
            </div>
            <p className="text-2xl font-bold text-accent">+$9,300</p>
            <div className="flex items-center space-x-1">
              <TrendingUp className="h-4 w-4 text-accent" />
              <span className="text-sm text-accent">+20.7% overall</span>
            </div>
          </div>

          <div className="space-y-2">
            <div className="flex items-center space-x-2">
              <LucidePieChart className="h-5 w-5 text-primary" />
              <span className="text-sm text-muted-foreground">Risk Level</span>
            </div>
            <p className="text-2xl font-bold text-card-foreground">Moderate</p>
            <div className="flex items-center space-x-1">
              <span className="text-sm text-muted-foreground">Aligned with profile</span>
            </div>
          </div>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          <div>
            <h3 className="text-lg font-semibold text-card-foreground mb-4">Performance (6 months)</h3>
            <div className="h-64">
              <ResponsiveContainer width="100%" height="100%">
                <LineChart data={performanceData}>
                  <CartesianGrid strokeDasharray="3 3" stroke="hsl(var(--border))" />
                  <XAxis dataKey="month" stroke="hsl(var(--muted-foreground))" />
                  <YAxis stroke="hsl(var(--muted-foreground))" />
                  <Tooltip
                    contentStyle={{
                      backgroundColor: "hsl(var(--card))",
                      border: "1px solid hsl(var(--border))",
                      borderRadius: "8px",
                    }}
                  />
                  <Line
                    type="monotone"
                    dataKey="value"
                    stroke="hsl(var(--primary))"
                    strokeWidth={3}
                    dot={{ fill: "hsl(var(--primary))", strokeWidth: 2, r: 4 }}
                  />
                </LineChart>
              </ResponsiveContainer>
            </div>
          </div>

          <div>
            <h3 className="text-lg font-semibold text-card-foreground mb-4">Asset Allocation</h3>
            <div className="h-64">
              <ResponsiveContainer width="100%" height="100%">
                <RechartsPieChart>
                  <Pie
                    data={allocationData}
                    cx="50%"
                    cy="50%"
                    innerRadius={60}
                    outerRadius={100}
                    paddingAngle={5}
                    dataKey="value"
                  >
                    {allocationData.map((entry, index) => (
                      <Cell key={`cell-${index}`} fill={entry.color} />
                    ))}
                  </Pie>
                  <Tooltip
                    contentStyle={{
                      backgroundColor: "hsl(var(--card))",
                      border: "1px solid hsl(var(--border))",
                      borderRadius: "8px",
                    }}
                  />
                </RechartsPieChart>
              </ResponsiveContainer>
            </div>
            <div className="flex justify-center space-x-4 mt-4">
              {allocationData.map((item, index) => (
                <div key={index} className="flex items-center space-x-2">
                  <div className="w-3 h-3 rounded-full" style={{ backgroundColor: item.color }} />
                  <span className="text-sm text-muted-foreground">
                    {item.name} ({item.value}%)
                  </span>
                </div>
              ))}
            </div>
          </div>
        </div>
      </CardContent>
    </Card>
  )
}
