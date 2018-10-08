package com.codeit.team_4.helper;

public class Transaction {
    private String name;
    private String account;
    private String type;
    private String amount;
    private String description;

    public Transaction( String name, String account, String type,String amount, String description )
    {

        this.name = name;
        this.account=account;
        this.type=type;
        this.amount=amount;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getAccount() {
        return account;
    }

    public String getAmount() {
        return amount;
    }

    public String getType()
    {
        return type;
    }

    public String getDescription()
    {
        return description;
    }

}
