"use client"

import { Button } from "@/components/ui/button"
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import { Bell, Settings, TrendingUp } from "lucide-react"
import { useAuth } from "@/lib/auth-context"
import { useRouter } from "next/navigation"

export function DashboardHeader() {
  const { user, logout } = useAuth()
  const router = useRouter()

  const handleLogout = () => {
    logout()
    router.push("/login")
  }

  return (
    <header className="border-b border-border bg-card">
      <div className="container mx-auto px-4 py-4">
        <div className="flex items-center justify-between">
          <div className="flex items-center space-x-4">
            <div className="flex items-center space-x-2">
              <TrendingUp className="h-8 w-8 text-primary" />
              <h1 className="text-2xl font-bold text-foreground">FinanceAI</h1>
            </div>
            <nav className="hidden md:flex items-center space-x-6">
              <Button variant="ghost" className="text-foreground hover:text-primary">
                Dashboard
              </Button>
              <Button variant="ghost" className="text-muted-foreground hover:text-primary">
                Portfolio
              </Button>
              <Button variant="ghost" className="text-muted-foreground hover:text-primary">
                Recommendations
              </Button>
              <Button variant="ghost" className="text-muted-foreground hover:text-primary">
                Risk Analysis
              </Button>
            </nav>
          </div>

          <div className="flex items-center space-x-4">
            <Button variant="ghost" size="icon" className="text-muted-foreground hover:text-primary">
              <Bell className="h-5 w-5" />
            </Button>
            <Button variant="ghost" size="icon" className="text-muted-foreground hover:text-primary">
              <Settings className="h-5 w-5" />
            </Button>
            <div className="flex items-center space-x-2">
              <Avatar>
                <AvatarImage src="/placeholder-user.png" />
                <AvatarFallback className="bg-primary text-primary-foreground">
                  {user?.firstName?.[0]}
                  {user?.lastName?.[0]}
                </AvatarFallback>
              </Avatar>
              <Button variant="ghost" onClick={handleLogout} className="text-muted-foreground hover:text-primary">
                Logout
              </Button>
            </div>
          </div>
        </div>
      </div>
    </header>
  )
}
