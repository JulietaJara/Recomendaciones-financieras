"use client"

import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Progress } from "@/components/ui/progress"
import { AlertTriangle, Shield, TrendingUp } from "lucide-react"
import { RadialBarChart, RadialBar, ResponsiveContainer } from "recharts"

const riskData = [{ name: "Current Risk", value: 65, fill: "hsl(var(--primary))" }]

const riskBreakdown = [
  { name: "Market Risk", value: 40, color: "hsl(var(--chart-1))" },
  { name: "Sector Risk", value: 25, color: "hsl(var(--chart-2))" },
  { name: "Currency Risk", value: 20, color: "hsl(var(--chart-3))" },
  { name: "Liquidity Risk", value: 15, color: "hsl(var(--chart-4))" },
]

export function RiskAnalysis() {
  return (
    <Card className="bg-card border-border">
      <CardHeader>
        <CardTitle className="text-lg font-bold text-card-foreground flex items-center space-x-2">
          <Shield className="h-5 w-5 text-primary" />
          <span>Risk Analysis</span>
        </CardTitle>
      </CardHeader>
      <CardContent className="space-y-6">
        <div className="text-center">
          <div className="h-32 mb-4">
            <ResponsiveContainer width="100%" height="100%">
              <RadialBarChart cx="50%" cy="50%" innerRadius="60%" outerRadius="90%" data={riskData}>
                <RadialBar dataKey="value" cornerRadius={10} fill="hsl(var(--primary))" />
              </RadialBarChart>
            </ResponsiveContainer>
          </div>
          <div className="space-y-2">
            <p className="text-2xl font-bold text-card-foreground">65%</p>
            <p className="text-sm text-muted-foreground">Current Risk Level</p>
            <div className="flex items-center justify-center space-x-1">
              <AlertTriangle className="h-4 w-4 text-primary" />
              <span className="text-sm text-primary">Moderate Risk</span>
            </div>
          </div>
        </div>

        <div className="space-y-4">
          <h4 className="text-sm font-medium text-card-foreground">Risk Breakdown</h4>
          {riskBreakdown.map((risk, index) => (
            <div key={index} className="space-y-2">
              <div className="flex justify-between text-sm">
                <span className="text-muted-foreground">{risk.name}</span>
                <span className="text-card-foreground font-medium">{risk.value}%</span>
              </div>
              <Progress value={risk.value} className="h-2" />
            </div>
          ))}
        </div>

        <div className="bg-muted/50 rounded-lg p-4">
          <div className="flex items-center space-x-2 mb-2">
            <TrendingUp className="h-4 w-4 text-accent" />
            <span className="text-sm font-medium text-card-foreground">Risk Assessment</span>
          </div>
          <p className="text-sm text-muted-foreground">
            Your portfolio risk level is well-aligned with your moderate risk tolerance. Consider diversifying into
            international markets to reduce concentration risk.
          </p>
        </div>
      </CardContent>
    </Card>
  )
}
