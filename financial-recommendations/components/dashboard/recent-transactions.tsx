import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Button } from "@/components/ui/button"
import { ArrowUpRight, ArrowDownRight, History } from "lucide-react"

const transactions = [
  {
    id: 1,
    type: "BUY",
    symbol: "SPY",
    name: "SPDR S&P 500 ETF",
    quantity: 25,
    price: 450.0,
    total: 11250.0,
    date: "2024-01-15",
    status: "COMPLETED",
  },
  {
    id: 2,
    type: "BUY",
    symbol: "AAPL",
    name: "Apple Inc.",
    quantity: 30,
    price: 185.0,
    total: 5550.0,
    date: "2024-01-14",
    status: "COMPLETED",
  },
  {
    id: 3,
    type: "SELL",
    symbol: "TSLA",
    name: "Tesla Inc.",
    quantity: 20,
    price: 260.0,
    total: 5200.0,
    date: "2024-01-13",
    status: "COMPLETED",
  },
  {
    id: 4,
    type: "BUY",
    symbol: "VTI",
    name: "Vanguard Total Stock Market ETF",
    quantity: 15,
    price: 225.0,
    total: 3375.0,
    date: "2024-01-12",
    status: "PENDING",
  },
]

export function RecentTransactions() {
  return (
    <Card className="bg-card border-border">
      <CardHeader>
        <div className="flex items-center justify-between">
          <CardTitle className="text-lg font-bold text-card-foreground flex items-center space-x-2">
            <History className="h-5 w-5 text-primary" />
            <span>Recent Transactions</span>
          </CardTitle>
          <Button
            variant="outline"
            size="sm"
            className="border-border text-muted-foreground hover:text-primary bg-transparent"
          >
            View All
          </Button>
        </div>
      </CardHeader>
      <CardContent className="space-y-4">
        {transactions.map((transaction) => (
          <div key={transaction.id} className="flex items-center justify-between p-3 bg-muted/50 rounded-lg">
            <div className="flex items-center space-x-3">
              <div
                className={`p-2 rounded-full ${
                  transaction.type === "BUY" ? "bg-accent/20 text-accent" : "bg-destructive/20 text-destructive"
                }`}
              >
                {transaction.type === "BUY" ? (
                  <ArrowUpRight className="h-4 w-4" />
                ) : (
                  <ArrowDownRight className="h-4 w-4" />
                )}
              </div>
              <div>
                <p className="font-medium text-card-foreground">{transaction.symbol}</p>
                <p className="text-sm text-muted-foreground">{transaction.name}</p>
              </div>
            </div>

            <div className="text-right">
              <p className="font-medium text-card-foreground">
                {transaction.type === "BUY" ? "-" : "+"}${transaction.total.toLocaleString()}
              </p>
              <div className="flex items-center space-x-2">
                <p className="text-sm text-muted-foreground">{transaction.quantity} shares</p>
                <Badge variant={transaction.status === "COMPLETED" ? "default" : "secondary"} className="text-xs">
                  {transaction.status}
                </Badge>
              </div>
            </div>
          </div>
        ))}
      </CardContent>
    </Card>
  )
}
