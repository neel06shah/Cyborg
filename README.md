# Tally Access using LAN

## About
With this application you can connect to your tally on a local network. All you need is IP Address of the Machine where tally is running and ODBC Port of tally. You can view reports like 
* Stock Sumary
* DayBook
* Profit & Loss
* Balance Sheet
* Ledger Vouchers
* Sales Register
* Purchase Register
* Payments & Receipts
* Payables & Receivables

## How to use
**Tally**
You need to enable tally to act as an odbc server and set the port to 9000 which is default
```
GatewayOfTally->F12 Configuration->Advanced Configuration->Set tallyerp9 acting as 'Server' and enable ODBC Server to 'Yes' then set Port to '9000' which is default
```

**Windows**
Use the following command to get IP address from your PC
```
$ipconfig
```









