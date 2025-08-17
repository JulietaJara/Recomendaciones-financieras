"use client"

import type React from "react"

import { useState, useEffect } from "react"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Checkbox } from "@/components/ui/checkbox"
import { Alert, AlertDescription } from "@/components/ui/alert"
import { User, DollarSign, Target, Clock } from "lucide-react"
import { userApi } from "@/lib/api"
import { useRouter } from "next/navigation"

export function ProfileForm() {
  const [formData, setFormData] = useState({
    riskTolerance: "",
    investmentExperience: "",
    investmentHorizon: "",
    monthlyIncome: "",
    monthlyExpenses: "",
    currentSavings: "",
    age: "",
    employmentStatus: "",
    investmentGoals: [] as string[],
  })
  const [isLoading, setIsLoading] = useState(false)
  const [error, setError] = useState("")
  const [success, setSuccess] = useState("")
  const router = useRouter()

  useEffect(() => {
    // Load existing profile data
    const loadProfile = async () => {
      try {
        const user = await userApi.getCurrentUser()
        if (user.userProfile) {
          const profile = user.userProfile
          setFormData({
            riskTolerance: profile.riskTolerance || "",
            investmentExperience: profile.investmentExperience || "",
            investmentHorizon: profile.investmentHorizon?.toString() || "",
            monthlyIncome: profile.monthlyIncome?.toString() || "",
            monthlyExpenses: profile.monthlyExpenses?.toString() || "",
            currentSavings: profile.currentSavings?.toString() || "",
            age: profile.age?.toString() || "",
            employmentStatus: profile.employmentStatus || "",
            investmentGoals: profile.investmentGoals || [],
          })
        }
      } catch (err) {
        console.error("Failed to load profile:", err)
      }
    }

    loadProfile()
  }, [])

  const investmentGoalOptions = [
    { id: "RETIREMENT", label: "Retirement Planning" },
    { id: "WEALTH_BUILDING", label: "Wealth Building" },
    { id: "EMERGENCY_FUND", label: "Emergency Fund" },
    { id: "HOME_PURCHASE", label: "Home Purchase" },
    { id: "EDUCATION", label: "Education Funding" },
    { id: "EARLY_RETIREMENT", label: "Early Retirement" },
  ]

  const handleGoalChange = (goalId: string, checked: boolean) => {
    setFormData((prev) => ({
      ...prev,
      investmentGoals: checked ? [...prev.investmentGoals, goalId] : prev.investmentGoals.filter((g) => g !== goalId),
    }))
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError("")
    setSuccess("")
    setIsLoading(true)

    try {
      const profileData = {
        riskTolerance: formData.riskTolerance,
        investmentExperience: formData.investmentExperience,
        investmentHorizon: Number.parseInt(formData.investmentHorizon),
        monthlyIncome: formData.monthlyIncome ? Number.parseFloat(formData.monthlyIncome) : null,
        monthlyExpenses: formData.monthlyExpenses ? Number.parseFloat(formData.monthlyExpenses) : null,
        currentSavings: formData.currentSavings ? Number.parseFloat(formData.currentSavings) : null,
        age: formData.age ? Number.parseInt(formData.age) : null,
        employmentStatus: formData.employmentStatus,
        investmentGoals: formData.investmentGoals,
      }

      await userApi.updateProfile(profileData)
      setSuccess("Profile updated successfully!")

      // Redirect to dashboard after successful update
      setTimeout(() => {
        router.push("/")
      }, 2000)
    } catch (err) {
      setError("Failed to update profile. Please try again.")
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <form onSubmit={handleSubmit} className="space-y-6">
      {error && (
        <Alert variant="destructive">
          <AlertDescription>{error}</AlertDescription>
        </Alert>
      )}

      {success && (
        <Alert className="border-accent bg-accent/10">
          <AlertDescription className="text-accent-foreground">{success}</AlertDescription>
        </Alert>
      )}

      <Card className="bg-card border-border">
        <CardHeader>
          <CardTitle className="flex items-center space-x-2">
            <User className="h-5 w-5 text-primary" />
            <span>Personal Information</span>
          </CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="space-y-2">
              <Label htmlFor="age">Age</Label>
              <Input
                id="age"
                type="number"
                placeholder="Enter your age"
                value={formData.age}
                onChange={(e) => setFormData((prev) => ({ ...prev, age: e.target.value }))}
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="employment">Employment Status</Label>
              <Select
                value={formData.employmentStatus}
                onValueChange={(value) => setFormData((prev) => ({ ...prev, employmentStatus: value }))}
              >
                <SelectTrigger>
                  <SelectValue placeholder="Select employment status" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="EMPLOYED">Employed</SelectItem>
                  <SelectItem value="SELF_EMPLOYED">Self-Employed</SelectItem>
                  <SelectItem value="UNEMPLOYED">Unemployed</SelectItem>
                  <SelectItem value="RETIRED">Retired</SelectItem>
                  <SelectItem value="STUDENT">Student</SelectItem>
                </SelectContent>
              </Select>
            </div>
          </div>
        </CardContent>
      </Card>

      <Card className="bg-card border-border">
        <CardHeader>
          <CardTitle className="flex items-center space-x-2">
            <DollarSign className="h-5 w-5 text-primary" />
            <span>Financial Information</span>
          </CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div className="space-y-2">
              <Label htmlFor="income">Monthly Income ($)</Label>
              <Input
                id="income"
                type="number"
                placeholder="5000"
                value={formData.monthlyIncome}
                onChange={(e) => setFormData((prev) => ({ ...prev, monthlyIncome: e.target.value }))}
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="expenses">Monthly Expenses ($)</Label>
              <Input
                id="expenses"
                type="number"
                placeholder="3500"
                value={formData.monthlyExpenses}
                onChange={(e) => setFormData((prev) => ({ ...prev, monthlyExpenses: e.target.value }))}
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="savings">Current Savings ($)</Label>
              <Input
                id="savings"
                type="number"
                placeholder="25000"
                value={formData.currentSavings}
                onChange={(e) => setFormData((prev) => ({ ...prev, currentSavings: e.target.value }))}
              />
            </div>
          </div>
        </CardContent>
      </Card>

      <Card className="bg-card border-border">
        <CardHeader>
          <CardTitle className="flex items-center space-x-2">
            <Target className="h-5 w-5 text-primary" />
            <span>Investment Profile</span>
          </CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="space-y-2">
              <Label htmlFor="risk">Risk Tolerance</Label>
              <Select
                value={formData.riskTolerance}
                onValueChange={(value) => setFormData((prev) => ({ ...prev, riskTolerance: value }))}
              >
                <SelectTrigger>
                  <SelectValue placeholder="Select risk tolerance" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="CONSERVATIVE">Conservative - Prefer stability</SelectItem>
                  <SelectItem value="MODERATE">Moderate - Balanced approach</SelectItem>
                  <SelectItem value="AGGRESSIVE">Aggressive - Seek high returns</SelectItem>
                </SelectContent>
              </Select>
            </div>
            <div className="space-y-2">
              <Label htmlFor="experience">Investment Experience</Label>
              <Select
                value={formData.investmentExperience}
                onValueChange={(value) => setFormData((prev) => ({ ...prev, investmentExperience: value }))}
              >
                <SelectTrigger>
                  <SelectValue placeholder="Select experience level" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="BEGINNER">Beginner - New to investing</SelectItem>
                  <SelectItem value="INTERMEDIATE">Intermediate - Some experience</SelectItem>
                  <SelectItem value="ADVANCED">Advanced - Experienced investor</SelectItem>
                </SelectContent>
              </Select>
            </div>
          </div>

          <div className="space-y-2">
            <Label htmlFor="horizon">Investment Horizon (years)</Label>
            <Select
              value={formData.investmentHorizon}
              onValueChange={(value) => setFormData((prev) => ({ ...prev, investmentHorizon: value }))}
            >
              <SelectTrigger>
                <SelectValue placeholder="Select investment timeline" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="1">1-2 years</SelectItem>
                <SelectItem value="5">3-5 years</SelectItem>
                <SelectItem value="10">6-10 years</SelectItem>
                <SelectItem value="15">11-15 years</SelectItem>
                <SelectItem value="20">16-20 years</SelectItem>
                <SelectItem value="25">20+ years</SelectItem>
              </SelectContent>
            </Select>
          </div>
        </CardContent>
      </Card>

      <Card className="bg-card border-border">
        <CardHeader>
          <CardTitle className="flex items-center space-x-2">
            <Clock className="h-5 w-5 text-primary" />
            <span>Investment Goals</span>
          </CardTitle>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            {investmentGoalOptions.map((goal) => (
              <div key={goal.id} className="flex items-center space-x-2">
                <Checkbox
                  id={goal.id}
                  checked={formData.investmentGoals.includes(goal.id)}
                  onCheckedChange={(checked) => handleGoalChange(goal.id, checked as boolean)}
                />
                <Label htmlFor={goal.id} className="text-sm font-normal">
                  {goal.label}
                </Label>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>

      <div className="flex justify-end space-x-4">
        <Button
          variant="outline"
          type="button"
          className="border-border text-muted-foreground hover:text-primary bg-transparent"
          onClick={() => router.push("/")}
        >
          Cancel
        </Button>
        <Button type="submit" className="bg-primary text-primary-foreground hover:bg-primary/90" disabled={isLoading}>
          {isLoading ? "Saving..." : "Save Profile"}
        </Button>
      </div>
    </form>
  )
}
