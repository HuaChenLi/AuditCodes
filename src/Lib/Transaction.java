package src.Lib;

import java.time.LocalDate;

public class Transaction implements Comparable<Transaction>{
    LocalDate date;
    double Amount;
    String description;

    public LocalDate getDate() {
        return date;
    }

    public double getAmount() {
        return Amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isIncome() {
        return getAmount() >= 0;
    }

    @Override
    public int compareTo(Transaction t) {
        return getDate().compareTo(t.getDate());
    }
}
