import { DashboardHeader } from "@/components/dashboard/dashboard-header"
import { PortfolioOverview } from "@/components/dashboard/portfolio-overview"
import { RecommendationsSection } from "@/components/dashboard/recommendations-section"
import { RiskAnalysis } from "@/components/dashboard/risk-analysis"
import { RecentTransactions } from "@/components/dashboard/recent-transactions"
import { ProtectedRoute } from "@/components/auth/protected-route"
import '@/styles/globals.css';

export default function DashboardPage() {
  return (
    <ProtectedRoute>
      <div className="min-h-screen bg-background">
        <DashboardHeader />
        <main className="container mx-auto px-4 py-8 space-y-8">
          <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
            <div className="lg:col-span-2 space-y-8">
              <PortfolioOverview />
              <RecommendationsSection />
            </div>
            <div className="space-y-8">
              <RiskAnalysis />
              <RecentTransactions />
            </div>
          </div>
        </main>
      </div>
    </ProtectedRoute>
  )
}
