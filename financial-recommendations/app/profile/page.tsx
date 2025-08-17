import { ProfileForm } from "@/components/profile/profile-form"
import { DashboardHeader } from "@/components/dashboard/dashboard-header"
import { ProtectedRoute } from "@/components/auth/protected-route"

export default function ProfilePage() {
  return (
    <ProtectedRoute>
      <div className="min-h-screen bg-background">
        <DashboardHeader />
        <main className="container mx-auto px-4 py-8">
          <div className="max-w-2xl mx-auto">
            <div className="mb-8">
              <h1 className="text-3xl font-bold text-foreground">Investment Profile</h1>
              <p className="text-muted-foreground mt-2">
                Complete your profile to receive personalized investment recommendations
              </p>
            </div>
            <ProfileForm />
          </div>
        </main>
      </div>
    </ProtectedRoute>
  )
}
